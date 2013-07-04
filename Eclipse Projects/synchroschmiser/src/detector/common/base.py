import os
import ConfigParser
from utils import *
from errors import SingletonException

"""
  Name of section in configuration file that specifies
  all directories with their child handlers for the current handler.
"""
DIR_SECTION_NAME = 'directories'


"""
  A singleton metaclass to be used by singletons.
  Most of the handlers should be singletons.
"""
class MetaSingleton(type):
    def __new__(metaclass, strName, tupBases, dict):
        if dict.has_key('__new__'):
            raise SingletonException, 'Can not override __new__ in a Singleton'
        return super(MetaSingleton,metaclass).__new__(metaclass, strName, tupBases, dict)
        
    def __call__(cls, *lstArgs, **dictArgs):
        raise SingletonException, 'Singletons may only be instantiated through getInstance()'

"""
  A singleton Base class. All singletons should laverage
  from the ease of being singletons by extending this class.
  Uses the MetaSingleton to achieve its purpose.
"""
class Singleton(object):
    __metaclass__ = MetaSingleton
    
    def getInstance(cls, *lstArgs):
        """
        Call this to instantiate an instance or retrieve the existing instance.
        If the singleton requires args to be instantiated, include them the first
        time you call getInstance.        
        """
        if cls._isInstantiated():
            if len(lstArgs) != 0:
                raise SingletonException, 'If no supplied args, singleton must already be instantiated, or __init__ must require no args'
        else:
            if cls._getConstructionArgCountNotCountingSelf() > 0 and len(lstArgs) <= 0:
                raise SingletonException, 'If the singleton requires __init__ args, supply them on first instantiation'
            instance = cls.__new__(cls)
            instance.__init__(*lstArgs)
            cls.cInstance = instance
        return cls.cInstance
    getInstance = classmethod(getInstance)
    
    def _isInstantiated(cls):
        return hasattr(cls, 'cInstance')
    _isInstantiated = classmethod(_isInstantiated)  

    def _getConstructionArgCountNotCountingSelf(cls):
        return cls.__init__.im_func.func_code.co_argcount - 1
    _getConstructionArgCountNotCountingSelf = classmethod(_getConstructionArgCountNotCountingSelf)

    def _forgetClassInstanceReferenceForTesting(cls):
        """
        This is designed for convenience in testing -- sometimes you 
        want to get rid of a singleton during test code to see what
        happens when you call getInstance() under a new situation.
        
        To really delete the object, all external references to it
        also need to be deleted.
        """
        try:
            delattr(cls,'cInstance')
        except AttributeError:
            # run up the chain of base classes until we find the one that has the instance
            # and then delete it there
            for baseClass in cls.__bases__: 
                if issubclass(baseClass, Singleton):
                    baseClass._forgetClassInstanceReferenceForTesting()
    _forgetClassInstanceReferenceForTesting = classmethod(_forgetClassInstanceReferenceForTesting)


"""
  A base class for handlers of directory changes. Every handler might have
  child handlers that are specified
"""
class BaseHandler(object):

  def __init__(self):
    super(BaseHandler, self).__init__()    
    """
        Child handlers is a hash of watching paths for keys, that refer
        to a collection of handlers, for values, asigned to
        listen for a specific path. The data is stored in the
        following format {path_to_listen -> [(BaseHandler)*]}.
    """
    self.childHandlers = {}

  """
      Handles the secified filesystem change internally and in case the
      current handler has some child handlers, asigned for the watching path
      of the change, routes the change to them for further handling.
  """
  @accepts('self', ChangeResult)
  def handle(self, change):
    try:
      self.__handle__(change)
    except Exception, e:
      yield e

    if change.has_key('watch_path'):
      path = change['watch_path']
      if self.childHandlers.has_key(path):
        for handler in self.childHandlers[path]:
          for ex in handler.handle(change):
            yield ex

  """
    Override this method in order to handle the specified change.
    Template method pattern.
  """
  #@accepts('self', ChangeResult)
  def __handle__(self, change): pass

  """
    Registerse a child handler for the specified path.
    One handler might be registered for more paths, but
    one path contains a handler only once.
  """
  @accepts('self', str, any)
  def registerHandler(self, path, handler):
    if self.childHandlers.has_key(path):
      if self.childHandlers[path].count(handler) == 0:
        self.childHandlers[path].append(handler)
    else:
      self.childHandlers[path] = [handler]

  def registerHandlers(self, path, handlers):
    for handler in handlers:
      self.registerHandler(path, handler)

  @accepts('self', str, any)
  def removeHandler(self, path, handler):
    if self.childHandlers.has_key(path):
      self.childHandlers[path].remove(handler)

  """
    Register a handler for all present watching paths. If a particular
    watching path is not yet registered with a handler it is skiped
  """
  @accepts('self', any)
  def registerHandlerToAll(self, handler):
    for path in self.childHandlers.keys():
      self.registerHandler(path, handler)

  """
    Register a handler for all present watching paths. If a particular
    watching path is not yet registered with a handler it is skiped
  """
  @accepts('self', any)
  def removeHandlerFromAll(self, handler):
    for path in self.childHandlers.keys():
      self.removeHandler(path, handler)

  """
    This function reads from the given configuration parser the
    contents of [directories] section and registers each directory
    with all specified handler instances. The function is a generator
    that yields the full watch path specified in each section row or yields any
    non fatal exceptions that occured during the execution.
  """
  @accepts('self', type(ConfigParser.ConfigParser()))
  def readHandlers(self, config):
    for (path, handler_files) in config.items(DIR_SECTION_NAME):
      if os.path.exists(path):
        path = os.path.abspath (path)
        hpaths = [os.path.abspath(hpath) for hpath in handler_files.split (', ')]
        handlers = []
        for hpath in hpaths:
            if hpath.endswith('.py') and os.path.exists(hpath) and os.path.isfile(hpath):
                try:
                    handlers.extend(instantiateHandlersFrom(hpath))
                except Exception, e:
                    yield e
            else:
                yield IOError("Handler '%s' is not found. (ignored)" % (hpath))

        self.registerHandlers(path, handlers)
        yield path
      else:
        yield IOError("Path '%s' is not found. (ignored)" % (os.path.abspath(path)))
  
