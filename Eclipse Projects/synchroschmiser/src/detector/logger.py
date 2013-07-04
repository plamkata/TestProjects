""" Logger plugin that supports logging to a file. """
import sys
import os
import errors
import ConfigParser
from base import BaseHandler, Singleton
from utils import *

DEFAULT_LOG_FILE = './detector.log'
CONFIG_FILE = './logger.cfg'
SETINGS_SECTION_NAME = 'settings'

class Logger(BaseHandler, Singleton):

  def __init__(self):
    super(Logger, self).__init__()
    self.logFile = os.path.abspath(DEFAULT_LOG_FILE)
    self.batchSize = 5
    self.buffer = []

    self.configFile = os.path.abspath(CONFIG_FILE)
    if (os.path.exists(self.configFile)):
      cfg = ConfigParser.ConfigParser()
      cfg.read(self.configFile)

      propName = 'logFile'
      if (cfg.has_option(SETINGS_SECTION_NAME, propName)):
        propValue = cfg.get(SETINGS_SECTION_NAME, propName)
        self.logFile = os.path.abspath(propValue)

      propName = 'batchSize'
      if (cfg.has_option(SETINGS_SECTION_NAME, propName)):
        try:
          self.batchSize =  int(cfg.get(SETINGS_SECTION_NAME, propName))
          if self.batchSize < 1:
            raise ValueError()
        except ValueError:
          print 'warn: Property %s in file %s should be int and >= 1. (ignored)' % (propName, conf_file)
          self.batchSize = 5

  #@accepts('self', ChangeResult)
  def __handle__(self, change):
    self.buffer.insert(0, change)
    if len(self.buffer) >= self.batchSize:
      try:
        log = None
        if not os.path.exists(self.logFile):
          log = open(self.logFile, 'w')
        else:
          log = open(self.logFile, 'a')
        while len(self.buffer) > 0:
          log.write(str(self.buffer.pop()))
          log.write('\n')
      except Exception, e:
        raise e
      finally:
        if log != None:
          log.close()
