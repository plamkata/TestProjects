"""Working example of the ReadDirectoryChanges API which will
 track changes made to a directory. Can either be run from a
 command-line, with a comma-separated list of paths to watch,
 or used as a module, either via the watch_path generator or
 via the Watcher threads, one thread per path.

Examples:
  watch_directory.py c:/temp,r:/images

or:
  import watch_directory
  for file_type, filename, action in watch_directory.watch_path ("c:/temp"):
    print filename, action

or:
  import watch_directory
  import Queue
  file_changes = Queue.Queue ()
  for pathname in ["c:/temp", "r:/goldent/temp"]:
    watch_directory.Watcher (pathname, file_changes)

  while 1:
    file_type, filename, action = file_changes.get ()
    print file_type, filename, action
"""
from __future__ import generators
import os
import sys
import Queue
import time
import threading

import win32file
import win32con

from utils import ACTIONS, FILE_TYPES, ChangeResult

def examine(path_to_watch, result_queue, refreshRate=2):
  for p in path_to_watch:
    Watcher (p, result_queue, refreshRate)

""" Represents a watcher thread for a single specified directory.
    Results are stored in a queue in specific format to imitate
    producer-consumer. All watchers are started as daemons.
"""
class Watcher (threading.Thread):

  def __init__ (self, path_to_watch, results_queue, refreshRate=2, **kwds):
    threading.Thread.__init__ (self, **kwds)
    self.path_to_watch = path_to_watch
    self.results_queue = results_queue
    self.refreshRate = refreshRate
    self.setDaemon (1)
    self.start ()

  def run (self):
      for result in self.watch_path (self.path_to_watch, True, self.refreshRate):
        try:
          self.results_queue.put (result)
        except Exception, e:
          self.results_queue.put (e)
        

  """ Watches the specified path for changes and yields imformation
      about the changes in the following format:
       { 'time' : time of the change,
         'file_type' : the type of the file from FILE_TYPES,
         'watch_path' : the original path that was watched,
         'file' : relative path for the file changed, 
         'filepath' : full path of the file changed,
         'action' : the action type from ACTIONS }
  """
  @staticmethod
  def watch_path (path_to_watch, include_subdirectories=True, refreshRate=2):
    FILE_LIST_DIRECTORY = 0x0001
    hDir = win32file.CreateFile (
      path_to_watch,
      FILE_LIST_DIRECTORY,
      win32con.FILE_SHARE_READ | win32con.FILE_SHARE_WRITE,
      None,
      win32con.OPEN_EXISTING,
      win32con.FILE_FLAG_BACKUP_SEMANTICS,
      None
    )
    while 1:
      results = win32file.ReadDirectoryChangesW (
        hDir,
        1024,
        include_subdirectories,
        win32con.FILE_NOTIFY_CHANGE_FILE_NAME | 
         win32con.FILE_NOTIFY_CHANGE_DIR_NAME |
         win32con.FILE_NOTIFY_CHANGE_ATTRIBUTES |
         win32con.FILE_NOTIFY_CHANGE_SIZE |
         win32con.FILE_NOTIFY_CHANGE_LAST_WRITE |
         win32con.FILE_NOTIFY_CHANGE_SECURITY,
        None,
        None
      )
      for action, file in results:
        full_filename = os.path.join (path_to_watch, file)
        if not os.path.exists (full_filename):
          file_type = FILE_TYPES['deleted']
        elif os.path.isdir (full_filename):
          file_type = FILE_TYPES['folder']
        else:
          file_type = FILE_TYPES['file']
        yield ChangeResult(
                { 'time' : time.time(),
                  'file_type' : file_type,
                  'watch_path' : path_to_watch,
                  'file' : file, 
                  'filepath' : full_filename,
                  'action' : ACTIONS.get (action, "Unknown") })
      time.sleep(refreshRate)


#if __name__ == '__main__':
#  """If run from the command line, use the thread-based
#   routine to watch the current directory (default) or
#   a list of directories specified on the command-line
#   separated by commas, eg

#   watch_directory.py c:/temp,c:/
#  """
#  PATH_TO_WATCH = ["."]
#  try:
#    path_to_watch = sys.argv[1].split (",") or PATH_TO_WATCH
#  except:
#    path_to_watch = PATH_TO_WATCH
#  path_to_watch = [os.path.abspath (p) for p in path_to_watch]

#  print "Watching %s at %s" % (", ".join (path_to_watch), time.asctime ())
#  files_changed = Queue.Queue ()
  
#  examine(path_to_watch, files_changed)

#  while 1:
#    try:
#      result = files_changed.get_nowait ()
#      str_time = time.strftime('%d.%m.%Y %H:%M:%S', time.localtime(result['time'])) 
#      print str_time, result['file_type'], result['filepath'], result['action']
#    except Queue.Empty:
#      pass
    
#    time.sleep (1)

