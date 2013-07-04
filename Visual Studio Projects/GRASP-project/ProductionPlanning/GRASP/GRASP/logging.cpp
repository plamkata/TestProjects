#include "stdafx.h"
#include "logging.h"

namespace metaheuristic {

LogEntry::LogEntry(clock_t time, log_entry_type type, float value)
{
    this->pid = omp_get_thread_num();
    this->time = time;
    this->type = type;
    this->value = value;
}

void LogEntry::Export(std::ostream &outs, time_t start_time)
{
    outs << pid << ", ";
    Export(outs, this->type);
    outs << ", " << this->time - start_time << ", " << this->value << endl;
}

void LogEntry::Export(std::ostream &outs, log_entry_type type)
{
    switch(type)
    {
    case Construction:
        outs << "const";
        break;
    case Improvement:
        outs << "imprv";
        break;
    case Iteration:
        outs << "iter";
        break;
    case GRASP:
        outs << "grasp";
        break;
    case PostOpt:
        outs << "psopt";
        break;
    case Relinking:
        outs << "rlink";
        break;
    case PoolType:
        outs << "pool";
        break;
    case NoType:
        outs << "notyp";
        break;
    }
}

LogEntry::~LogEntry()
{

}

Log::Log()
{
    data = new vector<LogEntry *>();
    start_time = clock();
}

void Log::Add(log_entry_type type, float value)
{
    Add(clock(), type, value);
}

void Log::Add(clock_t time, log_entry_type type, float value)
{
    Add(new LogEntry(time, type, value));
}

void Log::Add(LogEntry *entry)
{
#pragma omp critical
    data->push_back(entry);
}

void Log::SetStartTime(time_t start_time)
{
    this->start_time = start_time;
}

void Log::Export(ostream &outs)
{
    for (IND i = 0; i < data->size(); i++)
    {
        LogEntry *entry = (*data)[i];
        entry->Export(outs, start_time);
    }
}

void Log::Export(std::ostream &outs, log_entry_type type)
{
    for (IND i = 0; i < data->size(); i++)
    {
        LogEntry *entry = (*data)[i];
        if (entry->type == type)
        {
            entry->Export(outs, start_time);
        }
    }
}

void Log::Export(std::ostream &outs, log_entry_type type1, log_entry_type type2)
{
    for (IND i = 0; i < data->size(); i++)
    {
        LogEntry *entry = (*data)[i];
        if (entry->type == type1 || entry->type == type2)
        {
            entry->Export(outs, start_time);
        }
    }
}

Log::~Log()
{
    for (IND i = 0; i < data->size(); i++)
    {
        delete (*data)[i];
    }
    delete data;
}

}