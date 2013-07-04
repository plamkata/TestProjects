
#ifndef _IMPROVEMENT_H_
#define _IMPROVEMENT_H_

#include "stdafx.h"
#include "interface.h"

using namespace planning;

namespace metaheuristic {

class LocalSearch : public INhoodSearch
{
public:
    LocalSearch(const Solution& sol, Rand *rand);
    virtual ~LocalSearch();

private:
    // Random number generator instance
    Rand *rand;

    INhoodFactory *factory;

    // maximum number of consequtive non-improving iterations
    IND termCounter;

    // the best solution found so far
    Solution* best;
    //vector<Solution*> *path;
    
    // declaration of the local neighborhood with fixed size
    IND nsize;
    Solution** nhood;

public:
    virtual Rand* GetRand();
    virtual IND GetNhoodSize() const;
    // get the best solution found
    virtual Solution* GetBest();
    virtual Solution* SolutionAt(IND index) const;

    void SetFactory(INhoodFactory* factory);

    // add to local neighborhood
    virtual bool AddToNhood(Solution* sol);
    // clear local neighborhood at each step
    virtual void ClearNhood();
    // clear local neighborhood by keeping relatively good solutions
    void ClearNhood(ExecutionContext *ctx, float coeff);
    // Evaluate the local neighborhood and return the best plan from nhood
    virtual Solution* EvaluateNhood();
    // selects an index of a valid plan in the neighborhood according to selection pressure
    int SelectFromNhood(ExecutionContext *ctx);
    // filter local neighborhood for the next iteration, according to selection pressure
    IND FilterNhood(ExecutionContext *ctx);

    // apply network operators until no further improvement is possible
    virtual Solution* Execute(ExecutionContext *ctx);

    // determines if local search is stuck in a local optimum
    bool IsStuck(ExecutionContext *ctx);

private:
    // set the best solution
    void SetBest(Solution* sol);
    void ReplaceInNhood(IND i, IND j);
};

}

#endif /* _IMPROVEMENT_H_ */