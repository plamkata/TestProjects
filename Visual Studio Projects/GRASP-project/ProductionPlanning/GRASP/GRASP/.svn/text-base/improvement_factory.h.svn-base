#ifndef _IMPROVEMENT_FACTORY_H_
#define _IMPROVEMENT_FACTORY_H_

#include "stdafx.h"
#include "interface.h"
#include "problem.h"

using namespace metaheuristic;

namespace planning {

class ImprovementFactory : public INhoodFactory {
public:
    // pointer to a member function representing a neighborhood operator
    typedef IND (ImprovementFactory::*NhoodOp)(Plan *p);

    // a global array of available neighborhood operators
    static IND NHOOD_OPRS_SIZE;
    static NhoodOp NHOOD_OPRS[];

public:
    ImprovementFactory(INhoodSearch* nhood_) : nhood(nhood_) {};
    virtual ~ImprovementFactory() {};

private:
    INhoodSearch* nhood;

public:

    IND GetOperatorsSize() const;
    // selects a random index of a neighborhood operator in NHOOD_OPERATORS
    IND SelectOperator(Rand *rand);
    // execute a neighborhood operator in NHOOD_OPERATORS by index
    IND ExecuteOperator(IND opIndex, Solution* sol);

private:
    // execute a neighborhood operator on the specified plan;
    // returns the number of feasible modified plans that were placed in neighborhood
    IND ExecuteOperator(NhoodOp *op, Plan *p);

    // *** LocalSearch Modification Operators ***

    // merge similar lots to generate a list of similar solutions in neighborhood;
    // takes a feasible plan with no execution data as an initial solution to be varied;
    // returns the number of generated feasible solutions in neighborhood;
    IND MergeSimilarLots(Plan *p);
    IND MergeSimilarLots(Plan *p, vector<sett> &setts);
    // generate similar plans in neighborhood by performing a similar lot merge for 
    // the specified lot at machine m in time period t
    IND MergeSimilarLots(Plan *p, sett *msett, sett *nsett);

    // generate similar plans in neighborhood by exchanging production lots on different machines
    IND ExchangeMachineLots(Plan *p);
    // generate similar plans in neighborhood by exchanging production lots on machines m and n
    IND ExchangeMachineLots(Plan *p, IND m, IND n);
    IND ExchangeMachineLotsHeads(Plan *p, IND m, IND n);
    IND ExchangeMachineLotsTails(Plan *p, IND m, IND n);
    bool IsExchangePossible(Plan *p, IND m, IND n);

    // Exchange heads of lots that start in the same time period
    IND ExchangeParallelLots(Plan *p);
    IND ExchangeParallelLots(Plan *p, sett *s);

    // extend production lots of items for which we have an unmet demand
    IND ExtendLotsForUnmetDemands(Plan *p);
    IND ExtendLeftAt(Plan *p, IND m, IND index);
    IND ExtendRightAt(Plan *p, IND m, IND index);

    // insert production lots (by overwriting) of items for which we have an unmet demand
    IND InsertLotsForUnmetDemands(Plan *p);
    IND InsertLotsForUnmetDemand(Plan *p, dem *d);

};

}

#endif /* _IMPROVEMENT_FACTORY_H_ */