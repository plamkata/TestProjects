import sys
import time
import inspect

ACTIONS = {
    1 : 'Created',
    2 : 'Deleated',
    3 : 'Updated',
    4 : 'Renamed to something',
    5 : 'Renamed from something'
}

FILE_TYPES = {
    'file' : 'file',
    'folder' : 'folder',
    'deleted' : '<deleted>'
}

"""
    Information about the changes is stored in the following format:
    {   'time' : time of the change,
        'file_type' : the type of the file from FILE_TYPES,
        'watch_path' : the original path that was watched,
        'file' : relative path for the file changed, 
        'filepath' : full path of the file changed,
        'action' : the action type from ACTIONS
    }
"""
class ChangeResult(dict):    
    def __getattr__(self, attr):
        if attr in self:
            return self[attr]
        else:
            raise AttributeError("exists neither an attribute nor a key called '%s'." % attr)

    def __str__(self):
        str_time = time.strftime('%d.%m.%Y %H:%M:%S', time.localtime(self.time))
        fileType = ''
        if self.file_type != None:
            fileType = self.file_type
        return str_time + '\t' + fileType + '\t' + self.filepath + '\t' + self.action
        
        
def accepts(*types):
    def accepter(f):
        def decorated(*args):
#            for (i, (arg, t)) in enumerate(zip(args, types)):
#                if not isinstance(arg, t):
#                    raise TypeError("Argument #%d of '%s' should have been of type %s" % (i, f.__name__, t.__name__))
#                #TODO: more complex checks: tuple of a type, list of type
            return f(*args)
        return decorated
    return accepter
    

"""
    Finds the path for the module containing
    the given object.
"""
def findPath(obj):
    if isHandler(obj.__class__):
        return obj.filePath
    else:
        mod = inspect.getmodule(obj)
        return inspect.getfile(mod)

"""
    Instantiate all module handlers declared in
    the specifie module, by filepath.
"""
def instantiateHandlersFrom(moduleFile):
    module = importmodule(moduleFile, 'module')
    return [instantiateHandler(cls, moduleFile) for cls in findHandlers(module)]

def instantiateHandler(cls, filePath):
    handler = cls.getInstance()
    handler.filePath = filePath
    return handler

"""
    Find all plugin handlers declared in the specified module
    and instantiate them.
"""
def findHandlers(module):
    return [cls for name, cls in inspect.getmembers(module) if isHandler(cls)]

"""
    Test if a given class conformes to the specification for
    plugin handler (i.e. handler of changes)
"""
def isHandler(cls):
    from base import BaseHandler, Singleton
    return inspect.isclass(cls) and issubclass(cls, BaseHandler) and issubclass(cls, Singleton)


"""
    Imports the specified module from from given filepath
    using the modname as the real name of the module.
"""
def importmodule(filename, modname):
    module = newmodule( modname )
    execfile( filename, module.__dict__, module.__dict__ )
    return module

"""
    Creates a new module with the specified module name
    in the global namespace of null_module.
"""
def newmodule( modname ):
    import null_module
    sys.modules[modname] = sys.modules['null_module']
    sys.modules[modname].__name__ = modname
    del sys.modules['null_module']
    del null_module
    return sys.modules[modname]
    
@accepts(Exception)
def format(exc, level='ERROR'):
    return level + " " + exc.__class__.__name__ + " " + exc.message
    
