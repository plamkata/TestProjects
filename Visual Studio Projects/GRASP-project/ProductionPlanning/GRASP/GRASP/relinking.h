
#ifndef _RELINKING_H_
#define _RELINKING_H_

#include "stdafx.h"
#include "interface.h"
#include "problem.h"

// TODO: use an interface to a general Solution
using namespace planning;

namespace metaheuristic {

class Pool
{
public:
    // create a pool with poolSize maximum number of elements
    Pool(IND poolSize);

private:
    
    // sorted array of solutions in the pool
    vector<Plan *> *data;

    // maximum size of the pool
    IND maxSize;

public:
    // check if the maximum number of plans are already filled in
    bool IsFull() const;

    // retrieve the best plan by objective value
    Plan *Best();

    // retrieve the worst plan by objective value
    Plan *Worst();

    // check by value if the specified plan is already in the pool
    bool Contains(Plan *p) const;
    // check by reference if the specified plan is already in the pool
    bool ContainsObject(Plan *p) const;

    // add a new plan in the pool without checking the acceptance criteria; 
    // in case the maximum size is reached - replace the worst;
    bool Add(Plan *p);

    // returns true if the plan has better objective value than all other plans in the pool, 
    // or if it is highly dissimilar with all other plans in the pool;
    // the plan will be added to the pool in case it is accepted;
    bool Accept(ExecutionContext *ctx, Plan *p);

    // remove the worst plan in the pool
    void RemoveWorst();

    // execute path relinking on the specified plans and add the best found solution
    // to the pool; the best solution is added to the pool and its value is returned
    float Link(ExecutionContext *ctx, Plan *p, Plan *q, Rand *rand);
    float LinkSingle(ExecutionContext *ctx, Plan *p, Plan *q, Rand *rand);
    float LinkDouble(ExecutionContext *ctx, Plan *p, Plan *q, Rand *rand);

    // executes path relinking to the specified new plan as one of the edges and 
    // to a selected solution from the pool as the other edge; 
    // the best solution is added to the pool and its value is returned by the method;
    float LinkTo(ExecutionContext *ctx, Plan *p, Rand *rand);

    // select an appropriate second edge from the pool for path relinking;
    Plan *SelectEdge(Plan *p, Rand *rand);

    // link all tuples of different solutions in the pool and return the best plan
    float LinkAll(ExecutionContext *ctx, int seed);
    float LinkAll(ExecutionContext *ctx, int seed, bool withRestart);

    // export the values of the elements in the pool
    void ExportElements(ostream &outs);

    // destructor
    ~Pool();

private:
    
    // find out the current size of the pool
    IND Size() const;

    // find the index for the specified objective value
    int IndexOf(float value) const;

    // find the index for the closest plan in the pool to the specified one, which has worse value
    int ClosestIndexTo(Plan *p) const;

    // remove the plan at the specified index in the pool
    void Remove(IND index);

    // delete all plans in the pool
    void Clear();

};

// A class for representing a path generated by path-relinking algorithm.
// Path-relinking can be seen as a special type of neighborhood search 
// from an initial solution, towards a guiding, final solution.
class Path : INhoodSearch
{
public:

    static const IND MAX_NHOOD_SIZE = 500;

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

    // compute the dissimilarity between the specified plans
    // Note: this operation is not guaranteed to be symetric
    static IND Diff(Solution *sol, Solution *goal);
    static IND Diff(Plan *p, Plan *goal);
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
    
    // copy constructor of a Path
    Path(const Path &path);

    // Initialize the starting, initial solution and the end, guiding solution of the path
    Path(Plan *init, Plan *guide);

    // specify if forward relinking (asymmetric) or mixed relinking (symmetric) should be used
    Path(Plan *init, Plan *guide, bool asymmetric);

private:
    // initial, starting solution for the path
    Solution* init;

    // guiding, end solution for the path
    Solution* guide;

    // store the current generated path in a vector
    vector<Solution*> *path;

    // store all plans with local modification fixes in a local neighborhood
    IND nsize;
    Solution** nhood;

    // specify whether this path is constructed with forward relinking (asymmetric) 
    // or mixed relinking (symmetric);
    bool asymmetric;

public:
    // return the total number of solutions in the current path
    virtual IND Size() const;
    virtual Rand* GetRand();
    virtual IND GetNhoodSize() const;

    virtual void SetFactory(INhoodFactory* factory);

    // direct access to the solution at the specified index in the path
    virtual Solution* SolutionAt(IND index) const;
    // find the location of the specified plan on the path
    int IndexOf(Solution* plan) const;

    // return the initial, starting solution of the path
    Solution* Initial() const;
    // return the guiding, final solution for the path
    Solution* Guiding() const;
    // return the last solution in the current path
    Solution* Last() const;
    // return the last solution in the current path
    Solution* PreLast() const;

    // get the best solution found so far
    virtual Solution* GetBest();

    // add to local neighborhood
    virtual bool AddToNhood(Solution* sol);
    bool AddToNhood(Solution* last, Solution* sol);
    
    // clear local neighborhood at each step
    virtual void ClearNhood();
    void ClearNhood(Solution *excl);

    // Evaluate the local neighborhood and return the best plan from nhood
    virtual Solution* EvaluateNhood();
    // choose only among those solutions which decrease the dissimilarity
    // diff is the previouse dissimilarity
    Solution* EvaluateNhood(IND diff);
    Solution* EvaluateNhood(Solution* goal, IND diff);
    Solution* EvaluateNhood(Solution* p, Solution* goal, IND diff);
    bool IsAllowed(Solution *p, Solution *last, Solution *goal);

    // find the dissimilarity of p and the guiding solution
    IND Diff(Plan *p) const;
    // find the first difference in p with the guiding solution
    // Note: the earliest possible difference will be returned
    sett *FirstDiff(Plan *p) const;

    // generates the path and returns the best intermediate solution
    virtual Solution* Execute(ExecutionContext* ctx);
    // generate the path starting from the initial solution towards the guiding solution
    Plan *ForwardRelinking();
    // generate the path by simultanousely switching the roles of the initial and the guiding solutions
    Plan *MixedRelinking();

    // explore the path by executing local search from certain intermediate solutions 
    // and adding them to the pool
    float Explore(ExecutionContext *ctx, Pool *pool, Rand *rand) const;
    // explore/improve the solution in the path at the specified index
    Plan *Explore(ExecutionContext *ctx, IND index, Rand *rand) const;
    Plan *Explore(ExecutionContext *ctx, Plan *p, Rand *rand) const;
    // check if the solution at the specified index is a local min in the path
    bool IsLocalMin(IND index, float p_value) const;
    // synchronised update of the current best solution with the specified candidate
    float UpdatePool(ExecutionContext *ctx, Pool *pool, float best_value, Plan *local) const;

    // perform the necessary fix in p in order to make the specified different lot 
    // the same as the lot at this position in the guiding solution;
    // store all possibilities with fixed difference in local neighborhood;
    IND FixItemDiff(Plan *p, sett *diff);
    IND FixTimeDiff(Plan *p, sett *diff);
    IND FixSizeDiff(Plan *p, sett *diff);

    IND FixItemDiff(Plan *p, Plan *goal, sett *diff);
    IND FixTimeDiff(Plan *p, Plan *goal, sett *diff);
    IND FixSizeDiff(Plan *p, Plan *goal, sett *diff);

    // perform the necessary fixes for each of the last size number of 
    // solutions in the local neighborhood;
    // return the number of new solutions inserted in the neighborhood;
    IND FixItemDiffInNhood(Plan *goal, sett *gsett, IND size);
    IND FixTimeDiffInNhood(Plan *goal, sett *gsett, IND size);
    IND FixSizeDiffInNhood(Plan *goal, sett *gsett, IND size);

    // print out the objective values of the elements in the path
    void Export(ostream &outs);

    virtual ~Path();

private:
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

#endif /* _RELINKING_H_ */