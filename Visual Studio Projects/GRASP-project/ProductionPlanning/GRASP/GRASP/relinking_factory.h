#ifndef _RELINKING_FACTORY_H_
#define _RELINKING_FACTORY_H_

#include "stdafx.h"
#include "interface.h"
#include "problem.h"

using namespace metaheuristic;

namespace planning {

class RelinkingFactory : public INhoodFactory 
{
public:
    RelinkingFactory(INhoodSearch *nhood);
    virtual ~RelinkingFactory() {};

public:
    // compute the dissimilarity between the specified plans
    // Note: this operation is not guaranteed to be symetric
    static IND Diff(Solution *sol, Solution *goal);

private:
    // dissimilarity costs for computing the difference between two plans
    static const IND ITEM_DIFF_COST = 3;

    static const IND SMALL_TIME_DIFF_COST = 1;
    static const IND LARGE_TIME_DIFF_COST = 2;
    // a percent of difference in start time periods of the whole planning horizon
    static const IND TIME_DIFF_THRESHOLD = 30;
    
    static const IND SMALL_SIZE_DIFF_COST = 1;
    static const IND MEDIUM_SIZE_DIFF_COST = 2;
    static const IND LARGE_SIZE_DIFF_COST = 3;
    // a percent of difference in quantity of items of the target production quantity of the lot
    static const IND SMALL_SIZE_DIFF_THRESHOLD = 20;
    static const IND LARGE_SIZE_DIFF_THRESHOLD = 50;


    // finds the dissimilarity between p and goal only on machine m
    static IND Diff(Plan *p, Plan *goal, IND m);
    // finds the dissimilarity between p and goal, according to the lot sizes of the lots specified
    static IND Diff(Plan *p, Plan *goal, IND m, IND pindex, IND gindex);

    // return a pointer to the first found difference in p of p and goal
    static sett *FirstDiff(Plan *p, Plan *goal);
    // return a pointer to the first found difference in p of p and goal at machine m
    static sett *FirstDiff(Plan *p, Plan *goal, IND m);
    // return a pointer to the first infeasible lot which is different in the goal plan at machine m
    static sett *FirstDiff(Plan *p, Plan *goal, IND m, bool infeasDiff);

private:
    INhoodSearch *nhood;

public:
    virtual IND GetOperatorsSize() const;
    // Selects the first difference between the first solution and the second (guiding) solution;
    // Returns a double index of a machine and time period, where the first diff is located in the first solution;
    virtual IND SelectOperator(Rand *rand, Solution* args...);
    // Execute an operator that fixes the specified diff in first solution;
    // The function requires a second (guiding) solution
    virtual IND ExecuteOperator(IND diff_indices, Solution* args...);

    void SetNhood(INhoodSearch *nhood);

private:
    // perform the necessary fix in p in order to make the specified different lot 
    // the same as the lot at this position in the guiding solution;
    // store all possibilities with fixed difference in local neighborhood;
    IND FixItemDiff(Plan *p, Plan *goal, sett *diff);
    IND FixTimeDiff(Plan *p, Plan *goal, sett *diff);
    IND FixSizeDiff(Plan *p, Plan *goal, sett *diff);

    // perform the necessary fixes for each of the last size number of 
    // solutions in the local neighborhood;
    // return the number of new solutions inserted in the neighborhood;
    IND FixItemDiffInNhood(Plan *goal, sett *gsett, IND size);
    IND FixTimeDiffInNhood(Plan *goal, sett *gsett, IND size);
    IND FixSizeDiffInNhood(Plan *goal, sett *gsett, IND size);

    // helper methods for fixing the item diff
    IND ExchangeDiff(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts);
    IND ExchangeDiff(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts, bool inGuide);
    IND MoveDiffTo(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts);
    IND ForceIemFix(Plan *p, Plan *goal, sett *diff, sett *gsett);

    // helper methods for fixing the time diff
    IND ExtendDiffTo(Plan *p, sett *diff, IND diff_index, sett *gsett);
    IND ForceTimeFix(Plan *p, sett *diff, sett *gsett, IND gend);

    // helper methods for fixing the size diff
    IND ExtendDiffSizeTo(Plan *p, sett *diff, IND diffend, IND gend);
    IND ForceSizeFix(Plan *p, sett *diff, IND diffend, IND gend);

};

}

#endif /* _RELINKING_FACTORY_H_ */