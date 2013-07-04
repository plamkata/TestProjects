#ifndef _LOGGING_H_
#define _LOGGING_H_

#include "stdafx.h"
#include <vector>

using namespace std;

namespace metaheuristic {

enum log_entry_type
{
    Construction, 
    Improvement, 
    Iteration,
    GRASP,
    PostOpt, 
    Relinking, 
    PoolType, 
    NoType
};

class LogEntry
{
public:
    // constructor
    LogEntry(clock_t time, log_entry_type type, float value);

public:
    // the number of the thread that is logging the event
    IND pid;

    // the time when the log event occured
    clock_t time;

    // type of the log event
    log_entry_type type;

    // objective value at the logged type
    float value;

public:
    // export the entry to a stream
    void Export(ostream &outs, time_t start_time);

    // export the type to a stream
    void Export(ostream &outs, log_entry_type type);

    ~LogEntry();

};

class Log
{
public:
    // default constructor
    Log();
    
private:
    // starting time of the algorithm
    time_t start_time;

    // data of the log
    vector<LogEntry *> *data;

public:
    // add an entry in the log
    void Add(log_entry_type type, float value);
    void Add(clock_t time, log_entry_type type, float value);
    void Add(LogEntry *entry);

    void SetStartTime(time_t start_time);

    // export the log to a stream
    void Export(ostream &outs);
    void Export(ostream &outs, log_entry_type type);
    void Export(ostream &outs, log_entry_type type1, log_entry_type type2);

    ~Log();
};

}

#endif /* _LOGGING_H_ */