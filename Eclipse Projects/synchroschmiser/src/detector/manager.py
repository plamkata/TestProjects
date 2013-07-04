import sys
import os
import ConfigParser
import Queue

sys.path.append(os.path.abspath ('./common'))
import errors
from utils import *
from base import BaseHandler, Singleton

CONFIG_FILE = 'manager.cfg'
SETINGS_SECTION_NAME = 'settings'
PLUGINS_DIR = './plugins'

class ChangeManager(BaseHandler, Singleton):

  def __init__(self):
    super(ChangeManager, self).__init__()
    self.pathToWatch = []

  def configure(self):
    try:
      self._readConfiguration()
    except errors.ConfigurationError, e:
      print format(e)

    plugin_path = os.path.abspath (PLUGINS_DIR)
    self.pathToWatch.append(plugin_path)
    self.childHandlers[plugin_path] = []

    """ TODO: load handlers from plugins directory """
    paths = [path for path in os.listdir(plugin_path) if path.endswith('.py')]
    plugins = []
    for ppath in paths:
      orig_path = os.path.join(plugin_path, ppath)
      if os.path.exists(orig_path):
        try:
          plugins.extend(instantiateHandlersFrom(orig_path))
        except Exception, e:
          yield e
      else:
          yield IOError("Handler '%s' is not found. (ignored)" % (orig_path))

      for plugin in plugins:    
        self.registerHandlerToAll(plugin)
      
  def _readConfiguration(self):
    conf = ConfigParser.ConfigParser()
    conf.read(CONFIG_FILE)

    self.refreshRate = 1.0
    propName = 'refreshRate'
    if (conf.has_option(SETINGS_SECTION_NAME, propName)):
      try:
        self.refreshRate =  float(conf.get(SETINGS_SECTION_NAME, propName))
        if self.refreshRate < 1.0:
          raise ValueError()
      except ValueError:
        print 'warn: Property %s in file %s should be float and >= 1.0. (ignored)' % (propName, conf_file)
        self.refreshRate = 1.0

    for path in self.readHandlers(conf):
      if isinstance(path, str):
        self.pathToWatch.append( path )
      elif isinstance(path, IOError):
        print format(path, 'warn')
      elif isinstance(path, Exception):
        print format(path)

  def start(self):
    self.startListening()
    print "Watching %s at %s" % (", ".join(self.pathToWatch), time.asctime())
    self.startHandling()

  def startListening(self):
    try:
      platform_module_path = './platform/change_notifier_' + sys.platform + '.py'
      notifier = importmodule(platform_module_path, 'notifier')
    except IOError:
      raise OSError("Cannot start listening. Platform %s is not supported." % (sys.platform))
    
    self.filesChanged = Queue.Queue ()
    notifier.examine(self.pathToWatch, self.filesChanged, self.refreshRate)

  def startHandling(self):
    while 1:
      try:
        while 1:
          try:
            change = self.filesChanged.get_nowait ()
            if isinstance(change, ChangeResult):
              self.handle(change)
            elif isinstance(change, Exception):
              # change is an error - notify user or handle
              print '[platform] ' + format(change)
          except Queue.Empty:
            break
      except Exception, e:
        print format(e)
      time.sleep(self.refreshRate)

  """
    Override handle to support exception handling.
  """
  @accepts('self', ChangeResult)
  def handle(self, change):
    print change
    for ex in super(ChangeManager, self).handle(change):
      try:
        raise ex
      except errors.InfoException, e:
        print format(e, 'info')
      except Exception, e:
        print format(ex)

  """
    Internally handle autodeployment tasks.
  """
  def __handle__(self, change):
    ## filter only modules and cfg files
    if change.file.endswith('.py') or change.file.endswith('.cfg'):
      if change.watch_path == os.path.abspath(PLUGINS_DIR):
        if change.action == ACTIONS[3]:
          self.onUpdate(change)
        elif change.action == ACTIONS[2] or change.action == ACTIONS[4]:
          self.onDelete(change)
        else:
          self.onCreate(change)

  """
    If it is a .py file find all plugin handlers and regidter them to
    all directories, in case of a .cfg file procceed as onUpdate()
  """
  def onCreate(self, change):
    if change.file.endswith('.py'):
      ## instantiate handlers and register them
      handlers = instantiateHandlersFrom(change.filepath)
      for handler in handlers:
        self.registerHandlerToAll(handler)
      raise errors.InfoException("Successfuly deployed module %s." % (change.file))
    else:
      self.onUpdate(change)

  """
    Update only .cfg plugin configuration files.
  """
  def onUpdate(self, change):
    if change.file.endswith('.cfg'):
      cfgpath = change.filepath.replace('.cfg', '.py')
      if os.path.exists(cfgpath):
        ## read configuration
        cfg = ConfigParser.ConfigParser()
        cfg.read(cfgpath)
        handlers = instantiateHandlersFrom(path)
        for handler in handlers:
          handler.childHandlers.clear()
          handler.readHandlers(cfg)
        raise errors.InfoException("Successfuly updated configuration %s." % (change.file))

  def onDelete(self, change):
    if change.file.endswith('.py'):
      ## instantiate handlers and register them
      for path, handlers in self.childHandlers.items():
        for handler in handlers:
          if change.filepath == findPath(handler):
            self.removeHandler(path, handler)
      cfgpath = change.filepath.replace('.py', '.cfg')
      if os.path.exists(cfgpath):
        os.remove(cfgpath)
      raise errors.InfoException("Successfuly removed module %s." % (change.file))
    elif change.file.endswith('.cfg'):
      path = change.filepath.replace('.cfg', '.py')
      if os.path.exists(path):
        ## clear child handlers
        handlers = instantiateHandlersFrom(path)
        for handler in handlers:
          handler.childHandlers.clear()
      raise errors.InfoException("Successfuly removed configuration %s." % (change.file))

      
if __name__ == "__main__":
  manager = ChangeManager.getInstance()
  try:
    for ex in manager.configure():
      print format(ex, 'warning')
    manager.start()
  except Exception, e:
    print format(e)
    sys.stdout.write('Terminating application')
    for i in range(0, 15):
      sys.stdout.write('.')
      time.sleep(2)

