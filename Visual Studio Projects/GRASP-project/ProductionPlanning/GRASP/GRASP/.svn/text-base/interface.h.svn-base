#ifndef _INTERFACE_H_
#define _INTERFACE_H_

#include "stdafx.h"
#include "logging.h"
#include "parameters.h"

using namespace planning;

namespace metaheuristic {

class ExecutionContext {
public:
    ExecutionContext();
    ExecutionContext(const ExecutionContext& ctx);
    ExecutionContext(planning::Parameters *params, Log *log);

    virtual ~ExecutionContext();

public:
    planning::Parameters *GetParams();
    void SetParams(planning::Parameters *params);

    Log *GetLog();
    void SetLog(Log *log);

private:
    planning::Parameters *params;

    Log *log;
};

class Solution 
{
public:
    Solution(planning::Parameters& params);
    Solution(const Solution& solution);
    virtual ~Solution();

public:
    planning::Parameters* GetParam() const;
    virtual Solution* Clone(bool copy_exec_data) const = 0;

    virtual int IsConstrained() = 0;
    virtual float Value() const = 0;
    virtual void Execute(bool check_constr) = 0;
    virtual bool Equal(const Solution& other) const = 0;

    // Make sure the operands for comparison have been executed
    virtual bool operator<(const Solution& other) const;
    virtual bool operator>(const Solution& other) const;
    virtual bool operator<=(const Solution& other) const;
    virtual bool operator>=(const Solution& other) const;

protected:
    planning::Parameters* param;
};

class INhoodFactory 
{
public:
    
    virtual IND GetOperatorsSize() const = 0;
    // selects a random index of a neighborhood operator in NHOOD_OPERATORS
    virtual IND SelectOperator(Rand *rand) = 0;
    // execute a neighborhood operator in NHOOD_OPERATORS by index
    virtual IND ExecuteOperator(IND opIndex, Solution* sol) = 0;

};

// an interface that encapsulates functionality of an iterative neighborhood search
class INhoodSearch
{
public:
    // Set the factory that nhood search would use to create nhood elements
    virtual void SetFactory(INhoodFactory* factory) = 0;

    // get the current size of the nhood
    virtual IND GetNhoodSize() const = 0;
    virtual Rand* GetRand() = 0;
    // get the best solution found so far
    virtual Solution* GetBest() = 0;
    virtual Solution* SolutionAt(IND index) const = 0;

    // add to local neighborhood
    virtual bool AddToNhood(Solution* sol) = 0;
    // clear local neighborhood at each step
    virtual void ClearNhood() = 0;
    // Evaluate the local neighborhood and return the best plan from nhood
    virtual Solution* EvaluateNhood() = 0;

    // executes the global search and returns the best solution
    virtual Solution* Execute(ExecutionContext *ctx) = 0;
};

}

#endif /* _INTERFACE_H_ */