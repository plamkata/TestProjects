// construction.h : includes files and defines main structures for 
// the construction phase of a feasible solution 

#ifndef _CONSTRUCTION_H_
#define _CONSTRUCTION_H_

#include "stdafx.h"
#include "problem.h"

#include <time.h>

namespace planning {

// represents the best insertion cost for a demand; contains also the insertion position (machine and time period)
struct ins_cost
{
    // the demand
    dem* demand;
    // insertion machine
    IND m;
    // insertion time period
    IND t;
    // length in time periods for the production lot
    IND length;
    // insertion cost
    float cost;
};

class PartialPlan : public Plan
{
public:
    // pointer to a member function for calculating the cost
    typedef ins_cost * (PartialPlan::*CostFn)(dem *demand, IND m, IND start_t, IND end_t);

    PartialPlan(Parameters &input);
    PartialPlan(const PartialPlan &p);
    virtual void InitExecutionData();
    virtual metaheuristic::Solution* Clone(bool copy_exec_data) const;

protected:
    // free_T[M+1] contains the first free time period (for insertion) on machine m
    IND *free_T;

public:
    // constructs and executes the plan
    virtual PartialPlan *Construct(Rand *rand);

    // fetches the next non-zero, constrained demands for each item in CL and filters 
    // the CL according to beta parameter (returns the actual threshold for demands' candidate cost)
    float FetchDemands(vector<dem *> &CL, IND end);
    float FetchDemands(vector<dem *> &CL, IND start, IND end);
    // puts the next non-zero and constrained demand in the CL (candidate list)
    int FetchNextDemand(IND j, vector<dem *> &CL);
    int FetchNextDemand(IND j, vector<dem *> &CL, IND dem_ind);
    
    // consumes all 0.0 cost demands from inventory storage and applies the candidate cost threshold
    void ConsumeDemandsFromInventory(vector<dem *> &CL, float max_ccost);
    
    void MarkEndGaps();
    // tryies to fill in gaps (i.e. lots for item J+1) with demands in CL
    PartialPlan *FillInGaps(bool finalize);
    // Fill in the gap (i.e. the lot for item J+1 in time period t) with demands in CL
    // return false if the gap cannot be modified in any way
    bool FillInGap(vector<dem *> &CL, IND m, IND t);
    PartialPlan *FinalizeGap(vector<PartialPlan *> &path, vector<dem *> &CL, IND m, IND t);
    IND FilterNhood(vector<PartialPlan *> &nhood, vector<PartialPlan *> &path);
    // produce in gap either the previouse, the next or any other product from CL
    IND ExtendInGap(vector<PartialPlan *> &nhood, vector<dem *> &CL, IND m, IND t);
    // exchange gap with a production lot starting in the same time period or earlier
    IND ExchangeGap(vector<PartialPlan *> &nhood, vector<dem *> &CL, IND m, IND t);
    IND ExchangeGap(vector<PartialPlan *> &nhood, vector<dem *> &CL, IND m, IND m_t, IND n, IND n_t);
    void Clear(vector<PartialPlan *> *data);
    
    ins_cost *BestCost(dem *demand, CostFn CostFnPtr);
    ins_cost *BestCost(vector<dem *> &CL, IND m, CostFn CostFnPtr);
    ins_cost *BestCost(vector<dem *> &CL, IND m, IND start_t, IND end_t, CostFn CostFnPtr);

    // compute what is the insertion cost for the specified demand
    ins_cost *InsertionCost(dem *demand, IND m);
    ins_cost *InsertionCost(dem *demand, IND m, IND start_t, IND end_t);

    // compute what is the objective cost for working for the specified demand
    ins_cost *ObjectiveCost(dem *demand, IND m);
    ins_cost *ObjectiveCost(dem *demand, IND m, IND start_t, IND end_t);
    
    virtual bool Insert(const ins_cost &cost);
    virtual bool InsertInGap(const ins_cost &cost, IND m, IND t);

    int IsConstrained(const dem &d);
    IND IsSetupFor(IND j);
    bool IsComplete();
    bool IsContainedIn(const vector<PartialPlan *> &list) const;
    
    // overriden member functions

    int AddSetting(IND m, IND t, IND j);
    bool IsBusy(IND m, IND t);
    float EvaluateBufferOverrun(IND j) const;
    void ExportSettings(ostream &outs, bool zeros);

    virtual void DeleteExecutionData();
    ~PartialPlan();

private:
    void init_free_T(const Parameters &input);
    void init_free_T(const PartialPlan &p);
    
    // distribute the production of the lot specified by the insertion cost and setting x:=0/1
    void UpdatePositiveInventory(const ins_cost &cost, IND x);

    // find out what is the maximum inventory violation if we produce the quantity q, 
    // whith available inventory I, while working for the specified demand
    float GetMaxInventoryViolation(float I, float q, const dem &demand);

    // consumes all demands in CL, calculates the costs and constructs the RCL
    // requires as a parameter also the candidate cost threshold
    void ConsumeDemands(vector<dem *> &CL, ins_cost **costs, IND *RCL, float max_ccost, Rand *r);
};

}

#endif /* _CONSTRUCTION_H_ */