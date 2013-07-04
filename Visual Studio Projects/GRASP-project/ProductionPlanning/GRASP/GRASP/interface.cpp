#include "stdafx.h"
#include "interface.h"

namespace metaheuristic {

ExecutionContext::ExecutionContext() : 
    params(NULL), log(NULL)
{
}

ExecutionContext::ExecutionContext(const ExecutionContext& ctx) : 
    params(ctx.params), log(ctx.log)
{
}

ExecutionContext::ExecutionContext(
        planning::Parameters *p, Log *l) : 
    params(p), log(l) 
{
}

ExecutionContext::~ExecutionContext() 
{
    if (this->params != NULL) 
    {
        delete this->params;
        this->params = NULL;
    }

    if (this->log != NULL) 
    {
        delete this->log;
        this->log = NULL;
    }
}

planning::Parameters *ExecutionContext::GetParams()  
{
    return this->params;
}

void ExecutionContext::SetParams(planning::Parameters *params) 
{
    if (this->params != NULL) 
    {
        delete this->params;
    }
    this->params = params;
}

Log *ExecutionContext::GetLog() 
{
    return this->log;
}

void ExecutionContext::SetLog(Log *log) 
{
    if (this->log != NULL) 
    {
        delete this->log;
    }
    this->log = log;
}

Solution::Solution(planning::Parameters& params) 
{
    this->param = &params;
}

Solution::Solution(const Solution& sol)
{
    this->param = sol.GetParam();
}

Solution::~Solution() {
}

planning::Parameters* Solution::GetParam() const 
{
    return this->param;
}

bool Solution::Equal(const Solution& other) const
{
    return (this == &other);
}

bool Solution::operator<(const Solution& other) const 
{
    return this->Value() < other.Value();
}

bool Solution::operator>(const metaheuristic::Solution& other) const
{
    return this->Value() > other.Value();
}

bool Solution::operator<=(const metaheuristic::Solution& other) const
{
    return this->Value() <= other.Value();
}

bool Solution::operator>=(const metaheuristic::Solution& other) const
{
    return this->Value() >= other.Value();
}

}