import sys
import os
from shutil import *

import errors
from base import BaseHandler, Singleton
from utils import *

CONFIG_FILE = 'synchroniser.cfg'
SETINGS_SECTION_NAME = 'settings'
COPY_DIR = './synchronised'

class Synchroniser(BaseHandler, Singleton):

  def __init__(self):
    super(Synchroniser, self).__init__()
    self.initialCopied = False
     
  #@accepts('self', ChangeResult)
  def __handle__(self, change):
      if change.action == ACTIONS[3]:
        self.onUpdate(change)
      elif change.action == ACTIONS[2] or change.action == ACTIONS[4]:
        self.onDelete(change)
      else:
        self.onCreate(change)

  def onUpdate(self, change):
    if not self.initialCopy(change):
      if (os.path.isfile(change.filepath)):
        self.copyFile(change)
      elif (os.path.isdir(change.filepath)):
        root_dir = os.path.basename(change.watch_path)
        to_dir = os.path.join(os.path.abspath(COPY_DIR), root_dir, change.file)
        copystat(change.filepath, to_dir)
        raise errors.InfoException("Update dir %s finished." % (to_dir))
      
  def onDelete(self, change):
    if not self.initialCopy(change):
      root_dir = os.path.basename(change.watch_path)
      rm_path = os.path.join(os.path.abspath(COPY_DIR), root_dir, change.file)
      if os.path.isdir(rm_path):
        rm_path += '\\'
        rmtree(rm_path)
      else:
        print rm_path
        os.remove(rm_path)
      raise errors.InfoException("Remove file finished.")
    
  def onCreate(self, change):
    if not self.initialCopy(change):
      if (os.path.isfile(change.filepath)):
        self.copyFile(change)
      elif (os.path.isdir(change.filepath)):
        root_dir = os.path.basename(change.watch_path)
        to_dir = os.path.join(os.path.abspath(COPY_DIR), root_dir, change.file) + '\\'
        print to_dir
        os.mkdir(to_dir)
        raise errors.InfoException("Creating dir %s finished." % (to_dir))

  def copyFile(self, change):
    root_dir = os.path.basename(change.watch_path)
    copy(change.filepath, os.path.join(COPY_DIR, root_dir, change.file))
    raise errors.InfoException("Copy file %s finished." % (change.filepath))
  
  def initialCopy(self, change):
    if (not self.initialCopied):
      root_dir = os.path.basename(change.watch_path)
      from_dir = os.path.abspath(change.watch_path) + '\\'
      to_dir = os.path.join(os.path.abspath(COPY_DIR), root_dir) + '\\'
      if os.path.exists(to_dir):
        self.initialCopied = True
        raise errors.SynchronisationError("Directory %s already exists in synchronization folder." % (to_dir))
      copytree(from_dir, to_dir)
      self.initialCopied = True
      raise errors.InfoException("Initial copy finished.")
    else:
      return False


  
