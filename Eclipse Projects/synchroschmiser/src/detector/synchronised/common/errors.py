"""
  A specification exception to be thrown when a given argument, method
  or class dose not conform to a explicit specification.
"""
class SpecificationError(Exception):
    pass


class ConfigurationError(Exception):
    def __init__(self, mssg, confFile):
        self.confFile = confFile
        self.message = mssg


class SingletonException(Exception):
    pass

class InfoException(Exception):
    pass

class SynchronisationError(Exception):
    pass
