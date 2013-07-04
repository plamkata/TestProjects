#include "stdafx.h"
#include "relinking.h"
#include "improvement.h"
#include "improvement_factory.h"

namespace metaheuristic {

Pool::Pool(IND poolSize)
{
    this->maxSize = poolSize;
    this->data = new vector<Plan *>();
}

IND Pool::Size() const
{
    return (IND)data->size();
}

bool Pool::IsFull() const
{
    return Size() == maxSize;
}

Plan *Pool::Best()
{
    Plan *best = NULL;
    if (Size() > 0)
    {
        best = data->front();
    }
    return best;
}

Plan *Pool::Worst()
{
    Plan *worst = NULL;
    if (Size() > 0)
    {
        worst = data->back();
    }
    return worst;
}

int Pool::IndexOf(float value) const
{
    int index = -1;
    // perform a binary search to locate the index for the objective value
    if (Size() > 0)
    {
        IND first = 0;
        IND last = Size() - 1;
        while (first <= last) 
        {
            int mid = (first + last) / 2;
            float mid_value = (*data)[mid]->Value();
            if (Parameters::eq(value, mid_value))
            {
                index = mid;
                break;
            }
            else if (value < mid_value) 
            {
                if (mid > 0) last = mid - 1; // repeat search in bottom half.
                else 
                {
                    index = mid;
                    break;
                }
            }
            else if (value > mid_value)
            {
                if (mid + 1 == Size() || 
                    value < (*data)[mid + 1]->Value())
                {
                    index = mid + 1;
                    break;
                }
                else first = mid + 1;  // repeat search in top half.
            }
        }
    }
    return index;
}

int Pool::ClosestIndexTo(Plan *p) const
{
    // replace with the most similar
    int close_index = -1;
    int close_diff = -1;
    for (IND i = 0; i < Size(); i++)
    {
        if (p->Value() < (*data)[i]->Value() + Parameters::PRECISION)
        {
            IND diff = Path::Diff(p, (*data)[i]);
            if (close_diff == -1 || diff < close_diff)
            {
                close_diff = diff;
                close_index = i;
            }
        }
    }
    return close_index;
}

bool Pool::Contains(Plan *p) const
{
    bool found = false;
    int index = IndexOf(p->Value());
    if (index >= 0 && index < Size())
    {
        if (p->Equal(*(*data)[index]))
        {
            found = true;
        }
    }
    return found;
}

bool Pool::ContainsObject(Plan *p) const
{
    bool found = false;
    for (IND index = 0; index < Size(); index++)
    {
        Plan *plan = (*data)[index];
        if (p == plan) { found = true; break; }
    }
    return found;
}

bool Pool::Add(Plan *p)
{
    if (IsFull())
    {
        if (p->Value() < Worst()->Value())
        {
            // free up some space by removing the worst plan
            RemoveWorst();
        }
        else return false;
    }

    int index = IndexOf(p->Value());
    if (index == -1) index = 0;
    if (index < Size() && p->Equal(*(*data)[index]))
    {
        // solution is already in pool; reject
        return false;
    }
    else 
    {
        vector<Plan *>::iterator it = data->begin();
        it = it + index;
        data->insert(it, p);
        return true;
    }
}

bool Pool::Accept(ExecutionContext *ctx, Plan *p)
{
    bool accepted = false;
    if (p != NULL && p->IsConstrained() == 1)
    {
#pragma omp critical(accept_in_pool)
        {
            if (IsFull()) 
            {
                if (p->Value() < Worst()->Value() + Parameters::PRECISION)
                {
                    // replace the most similar plan in the pool
                    int index = ClosestIndexTo(p);
                    if (index >= 0) 
                    {
                        if (p->Equal(*(*data)[index]))
                        {
                            // solution is already in pool, reject
                            accepted = false;
                        }
                        else 
                        {
                            Remove(index);
                            accepted = Add(p);
                        }
                    }
                }
                else accepted = false;
            }
            else 
            {
                accepted = Add(p);
            }

            if (accepted && ctx->GetParams()->printLog) 
            {
                ctx->GetLog()->Add(PoolType, p->Value());
            }
        }
    }
    return accepted;
}

void Pool::Remove(IND index)
{
    if (index >= 0 && index < Size())
    {
        vector<Plan *>::iterator it = data->begin();
        it = it + index;
        delete (*data)[index];
        (*data)[index] = NULL;
        it = data->erase(it);
    }
}

void Pool::RemoveWorst()
{
    if (Size() > 0)
    {
        IND index = Size() - 1;
        vector<Plan *>::iterator it = data->begin();
        it = it + index;
        delete (*data)[index];
        it = data->erase(it);
    }
}

void Pool::Clear()
{
    for (IND i = 0; i < Size(); i++) 
    {
        delete (*data)[i];
    }
    data->clear();
}

float Pool::Link(ExecutionContext *ctx, Plan *p, Plan *q, Rand *rand)
{
    //return LinkSingle(p, q, rand);
    return LinkDouble(ctx, p, q, rand);
}

float Pool::LinkDouble(ExecutionContext *ctx, Plan *p, Plan *q, Rand *rand)
{
    // generate paths in asymmetric manner (forward-relinking)
    Path* path = new Path(p, q, true);
    Solution* best = path->Execute(ctx); // construct the path
    float pbest_value = path->Explore(ctx, this, rand);
    if (ctx->GetParams()->printInfo) path->Export(cout);
    if (ctx->GetParams()->printLog) ctx->GetLog()->Add(Relinking, pbest_value);
    delete path;
    delete best;
    
    path = new Path(q, p, true);
    best = path->Execute(ctx); // construct the path
    float qbest_value = path->Explore(ctx, this, rand);
    if (ctx->GetParams()->printInfo) path->Export(cout);
    if (ctx->GetParams()->printLog) ctx->GetLog()->Add(Relinking, qbest_value);
    delete path;
    delete best;
    
    float best_value = pbest_value < qbest_value ? pbest_value : qbest_value;
    //cout << "Forward relinking: " << "\t path: " << p->Value() << " <-> " << q->Value() << 
    //        " best: " << best_value << endl; 
    return best_value;
}

float Pool::LinkSingle(ExecutionContext *ctx, Plan *p, Plan *q, Rand *rand)
{
    // generate the path in a symmetric manner (mixed-relinking)
    Path* path = new Path(p, q, false);
    Solution* best = path->Execute(ctx); // construct the path
    float pbest_value = path->Explore(ctx, this, rand);
    if (ctx->GetParams()->printInfo) path->Export(cout);
    if (ctx->GetParams()->printLog) ctx->GetLog()->Add(Relinking, pbest_value);
    delete path;
    delete best;

    //cout << "Mixed relinking: " << "\t path: " << p->Value() << " -> " << q->Value() << 
    //        " best: " << pbest_value << endl; 
    return pbest_value;
}

float Pool::LinkTo(ExecutionContext *ctx, Plan *p, Rand *rand)
{
    float best_value = Parameters::MAX_COST;
    if (Size() > 0)
    {
        Plan *q = NULL;
#pragma omp critical(select_from_pool)
        {
            q = SelectEdge(p, rand);
            // duplicate selected plan, since it may get replaced in the pool by another thread
            q = new Plan(*q, true);
        }

        best_value = Link(ctx, p, q, rand);
        // delete duplicated plan
        delete q;
    }
    return best_value;
}

float Pool::LinkAll(ExecutionContext *ctx, int seed)
{
    return LinkAll(ctx, seed, true);
}

float Pool::LinkAll(ExecutionContext *ctx, int seed, bool withRestart)
{
    float best_value = Best()->Value();
    float old_value = best_value;
    int size = Size();
    bool restart = false;
    IND job_size = JobSizePR(size, omp_get_num_procs());

#pragma omp parallel shared(ctx, seed, withRestart, best_value, old_value, size, restart, job_size)
    {
        // create a separate random generator in each thread 
        Rand rand(seed); 
        // and parametrize it with the thread id
        int seeds[2] = {seed, omp_get_thread_num()};
        rand.RandomInitByArray(seeds, 2);
        int num_iter = size * (size - 1) / 2;

#pragma omp for schedule(dynamic, job_size)
        for (int k = 0; k < num_iter; k++)
        {
#pragma omp flush (restart)
            if (!restart)
            {
                Plan *left;
                Plan *right;
                // generate all different sets of indices {i, j}, i != j with only one parallel cycle;
                IND s = Parameters::min_s(num_iter - k);
                IND i = size - s - 1;
                IND j = i + k + 1 + (s * (s + 1)) / 2 - num_iter;
                if (i != j)
                {
#pragma omp critical(accept_in_pool)
                    {
                        // copy selected plans in current thread because they might get replaced in pool by another thread
                        left = new Plan(*(*data)[i], true); 
                        right = new Plan(*(*data)[j], true); 
                    }

                    // execute the relinking in parallel
                    float value = Link(ctx, left, right, &rand);

#pragma omp critical(accept_in_pool)
                    {
                        if (ctx->GetParams()->printLog) ctx->GetLog()->Add(PostOpt, value);
                        // restart if the best value in pool have been improved
                        if (value < best_value - Parameters::PRECISION)
                        {
                            best_value = Best()->Value();
                            if (withRestart)
                            {
                                restart = true;
#pragma omp flush (restart)
                            }
                            if (left->GetParam()->printInfo) ExportElements(cout);
                            if (ctx->GetParams()->printLog) ctx->GetLog()->Add(GRASP, best_value);
                        }
                    }

                    delete left;
                    delete right;
                }
            }
        }
    }

    if (withRestart && restart) 
    {
        cout << "Restart pool... " << best_value << endl;
        return LinkAll(ctx, int(time(0)), withRestart);
    }
    else 
    {
        cout << "End pool... " << best_value << endl;
        return best_value;
    }
}

Plan *Pool::SelectEdge(Plan *p, Rand *rand)
{
    Plan *q = NULL;
    if (Size() > 0)
    {
        int close_index = -1;
        int close_diff = -1;
        int far_index = -1;
        int far_diff = -1;
        for (IND i = 0; i < Size(); i++)
        {
            IND diff = Path::Diff(p, (*data)[i]);
            if (close_diff == -1 || diff < close_diff)
            {
                close_diff = diff;
                close_index = i;
            }
            if (far_diff == -1 || diff > far_diff)
            {
                far_diff = diff;
                far_index = i;
            }
        }

        // flip a coin and choose either the closest or the farest plan by dissimilarity
        int choise = Parameters::random(rand, 1);
        if (choise == 0) q = (*data)[far_index];
        if (choise == 1) q = (*data)[close_index];
    }
    return q;
}

void Pool::ExportElements(ostream &outs)
{
    outs << "Pool contents: " << endl;
    for (IND i = 0; i < Size(); i++)
    {
        Plan *p = (*data)[i];
        outs << omp_get_thread_num() << ": " << "Pool element at index " << i << 
            " has value " << Parameters::TabbedSpace(p->Value()) << endl;
    }
}

Pool::~Pool()
{
    Clear();
    delete data;
}

Path::Path(const Path &p)
{
    this->init = p.init;
    this->guide = p.guide;

    path = new vector<Solution*>();
    path->push_back(init);

    for (IND index = 1; index < p.Size(); index++)
    {
        path->push_back(p.SolutionAt(index));
    }
    
    nhood = new Solution*[MAX_NHOOD_SIZE];
    nsize = 0;

    this->asymmetric = p.asymmetric;
}

Path::Path(Plan *init, Plan *guide)
{
    this->init = init;
    this->guide = guide;

    path = new vector<Solution *>();
    path->push_back(init);

    nhood = new Solution*[MAX_NHOOD_SIZE];
    nsize = 0;

    asymmetric = true;
}

Path::Path(Plan *init, Plan *guide, bool asymmetric)
{
    this->init = init;
    this->guide = guide;

    path = new vector<Solution*>();
    path->push_back(init);

    nhood = new Solution*[MAX_NHOOD_SIZE];
    nsize = 0;

    this->asymmetric = asymmetric;
}

IND Path::Size() const
{
    return (IND) path->size();
}

Rand *Path::GetRand()
{
    return NULL;
}

IND Path::GetNhoodSize() const
{
    return nsize;
}

void Path::SetFactory(INhoodFactory* factory)
{
    // TODO: set the correct factory and use it
}

Solution* Path::SolutionAt(IND index) const
{
    Solution* p = NULL;
    if (index >= 0 && index < Size())
    {
        p = (*path)[index];
    }
    return p;
}

int Path::IndexOf(Solution *plan) const
{
    int index = -1;
    // note that the path is sorted according to the disimilarity to the guide;
    // however disimilarity measure requires much more computation than direct comparison
    for (IND i = 0; i < path->size(); i++)
    {
        if ((*path)[i]->Equal(*plan))
        {
            index = i;
            break;
        }
    }
    return index;
}

Solution* Path::Initial() const
{
    return init;
}

Solution* Path::Guiding() const
{
    return guide;
}

Solution* Path::Last() const
{
    Solution* plan = NULL;
    if (path->size() > 0)
    {
        size_t last_index = path->size() - 1;
        plan = (*path)[last_index];
    }
    return plan;
}

Solution* Path::PreLast() const
{
    Solution* plan = NULL;
    if (path->size() > 1)
    {
        size_t last_index = path->size() - 2;
        plan = (*path)[last_index];
    }
    return plan;
}

Solution* Path::GetBest()
{
    Solution *best = NULL;
    for (IND index = 0; index < Size(); index++)
    {
        Solution* p = SolutionAt(index);
        if (p == init) continue;
        if (p == guide) continue;

        if (best == NULL || p->Value() < best->Value())
        {
            best = p;
        }
    }
    return best;
}

bool Path::AddToNhood(Solution *sol)
{
    return AddToNhood(Last(), dynamic_cast<Plan*>(sol));
}

bool Path::AddToNhood(Solution *last, Solution *p)
{
    if (nsize < MAX_NHOOD_SIZE)
    {
        if (p != NULL)
        {
            nhood[nsize] = p;
            nsize++;
        }
        return true;
    }
    else return false;
}

void Path::ClearNhood()
{
    // only leave the best in nhood
    Plan *p = NULL;
    ClearNhood(p);
}

void Path::ClearNhood(Solution *excl)
{
    // only leave the best in nhood
    while (nsize > 0)
    {
        nsize--;
        if (nhood[nsize] != NULL && nhood[nsize] != excl)
        {
            delete nhood[nsize];
            nhood[nsize] = NULL;
        }
    }
}

Solution* Path::EvaluateNhood()
{
    return EvaluateNhood(0);
}

Solution* Path::EvaluateNhood(IND diff)
{
    return EvaluateNhood(guide, diff);
}

Solution* Path::EvaluateNhood(Solution* goal, IND diff)
{
    return EvaluateNhood(Last(), goal, diff);
}

Solution* Path::EvaluateNhood(Solution* last, Solution* goal, IND diff)
{
    Solution *nhood_best = NULL; // the best among feasible solutions
    Solution *nhood_best1 = NULL; // this is the-non strictly improving best
    Solution *inf_best = NULL; // the best among infeasible solutions
    Solution *inf_best1 = NULL; // this is the non-strictly improving best
    Solution *diff_best = NULL; // this is the non-strictly diff improving best
    for (IND i = 0; i < nsize; i++)
    {
        Solution *p = nhood[i];
        IND pdiff = Diff(p, goal);
        bool allowed = false;
        if (diff == 0 || pdiff < diff || 
            (pdiff == diff && (allowed = IsAllowed(p, last, goal))))
        {
            if (p->IsConstrained() == 1)
            {
                p->Execute(false);
                if (nhood_best == NULL || 
                    p->Value() < nhood_best->Value())
                {
                    if (pdiff < diff) nhood_best = p;
                    else nhood_best1 = p;
                }
            }
            else 
            {
                // evaluate an infeasible plan
                p->Execute(true);
                if (inf_best == NULL || 
                    p->Value() < inf_best->Value())
                {
                    if (pdiff < diff) inf_best = p;
                    else inf_best1 = p;
                }
            }
        }
        else if (allowed)
        {
            if (p->IsConstrained() == 1) p->Execute(false);
            else p->Execute(true);
            if (diff_best == NULL || 
                p->Value() < diff_best->Value())
            {
                diff_best = p;
            }
        }
    }
    if (nhood_best == NULL) nhood_best = nhood_best1;
    if (nhood_best == NULL) nhood_best = inf_best;
    if (nhood_best == NULL) nhood_best = inf_best1;
    if (nhood_best == NULL) nhood_best = diff_best;
    return nhood_best;
}

bool Path::IsAllowed(Solution *p, Solution *last, Solution *goal)
{
    if (asymmetric)
    {
        return IndexOf(p) == -1;
    }
    else 
    {
        return !p->Equal(*last) && !p->Equal(*goal);
    }
}

IND Path::Diff(Plan *p) const
{
    return Path::Diff(p, guide);
}

sett *Path::FirstDiff(Plan *p) const
{
    return Path::FirstDiff(p, dynamic_cast<Plan*>(guide));
}

Solution* Path::Execute(ExecutionContext *ctx)
{
    if (asymmetric) 
        return ForwardRelinking();
    else 
        return MixedRelinking();
}

Plan *Path::ForwardRelinking()
{
    Solution *p = Last();
    IND d = 0; // the current difference from the guiding solution
    while ((d = Diff(p, guide)) > 0)
    {
        Plan* psol = dynamic_cast<Plan*>(p);
        Plan* pguide = dynamic_cast<Plan*>(guide);

        sett *psett = FirstDiff(psol, pguide);
        if (psett != NULL)
        {
            sett *gsett = pguide->GetSetting(psett->m, psett->t);
            if (gsett != NULL)
            {
                if (psett->j != gsett->j) FixItemDiff(psol, psett);
                else if (psett->m == gsett->m && 
                    psett->t != gsett->t) FixTimeDiff(psol, psett);
                else if (psett->m == gsett->m &&
                    psett->t == gsett->t) FixSizeDiff(psol, psett);
            }
            else; // should never happen 
        }

        Solution *plan = EvaluateNhood(d);
        if (plan != NULL)
        {
            // update path
            path->push_back(plan);
            p = plan;
        }
        else break; // should not happen
        
        ClearNhood(p);
    }
    Plan *best = dynamic_cast<Plan*>(GetBest());
    ClearNhood(best);
    return best;
}

Plan *Path::MixedRelinking()
{
    Plan *p = dynamic_cast<Plan*>(Initial());
    Plan *q = dynamic_cast<Plan*>(Guiding());
    path->push_back(guide);
    vector<Solution *>::iterator mid_it = path->begin() + 1;

    IND d = 0; // the current difference from the guiding solution
    bool switched = false; // disable double switches of (p <-> q)
    while ((d = Path::Diff(p, q)) > 2)
    {
        sett *psett = Path::FirstDiff(p, q);
        if (psett != NULL)
        {
            sett *qsett = q->GetSetting(psett->m, psett->t);
            if (qsett != NULL)
            {
                if (psett->j != qsett->j) FixItemDiff(p, q, psett);
                else if (psett->m == qsett->m && 
                    psett->t != qsett->t) FixTimeDiff(p, q, psett);
                else if (psett->m == qsett->m &&
                    psett->t == qsett->t) FixSizeDiff(p, q, psett);
            }
            else; // should never happen 
        }

        Solution *plan = EvaluateNhood(p, q, d);
        if (plan != NULL)
        {
            switched = false;
            // update path
            bool update_it = *mid_it != p;
            mid_it = path->insert(mid_it, plan);
            if (update_it) mid_it = mid_it + 1;
        }
        else if (!switched) { plan = p; switched = true; }
        else break;

        ClearNhood(plan);
        // switch roles
        p = q;
        q = dynamic_cast<Plan*>(plan);
    }
    Plan *best = dynamic_cast<Plan*>(GetBest());
    ClearNhood(best);
    return best;
}

float Path::Explore(ExecutionContext *ctx, Pool *pool, Rand *rand) const
{
    float best_value = Parameters::MAX_COST;
    int best_index = -1;
    float best_local_value = Parameters::MAX_COST;
    if (Size() >= 3)
    {
        for (int i = 1; i < Size() - 1; i++)
        {
            Plan *p = dynamic_cast<Plan*>(SolutionAt(i));
            if (p != init && p != guide) 
            {
                float p_value = p->Value();

                // explore feasible local path minimums
                if (p->IsConstrained() == 1 && IsLocalMin(i, p_value))
                {
                    // explore every local min in the path
                    Plan *local = Explore(ctx, i, rand);
                    best_local_value = UpdatePool(ctx, pool, best_local_value, local);
                }
               
                // keep track of the best solution
                if (best_value > p_value) 
                { 
                    best_value = p_value; best_index = i; 
                }
            }
        }

        if (best_index >= 0)
        {
            // explore the best solution found in path
            Plan *local = Explore(ctx, best_index, rand);
            best_local_value = UpdatePool(ctx, pool, best_local_value, local);
        }
    }
    return best_local_value;
}

bool Path::IsLocalMin(IND index, float p_value) const
{
    Solution *prev = SolutionAt(index - 1);
    Solution *next = SolutionAt(index + 1);
    bool isLocalMin = prev != NULL && next != NULL && 
        p_value < prev->Value() && p_value < next->Value();
    return isLocalMin;
}

Plan *Path::Explore(ExecutionContext *ctx, IND index, Rand *rand) const
{
    Plan *p = dynamic_cast<Plan*>(SolutionAt(index));
    //if (p->Value() < float(3) * p->GetParam()->average)
    //{
        return Explore(ctx, p, rand);
    //}
    //else 
    //{
    //    return new Plan(*p);
    //}
}

Plan *Path::Explore(ExecutionContext *ctx, Plan *p, Rand *rand) const
{
    LocalSearch search(*p, rand);
    ImprovementFactory factory(&search);
    search.SetFactory(&factory);

    Plan *plan = dynamic_cast<Plan*>(search.Execute(ctx));
    Plan *result = new Plan(*plan, false);
    result->Execute();
    
    return result;
}

float Path::UpdatePool(ExecutionContext *ctx, Pool *pool, float best_value, Plan *local) const
{    
    float local_value = local->Value();

    if (!pool->Accept(ctx, local)) delete local;
    else if (local_value < best_value)
    {
        best_value = local_value;
    }

    return best_value;
}

IND Path::FixItemDiff(Plan *p, sett *diff)
{
    return FixItemDiff(p, dynamic_cast<Plan*>(guide), diff);
}

IND Path::FixTimeDiff(Plan *p, sett *diff)
{
    return FixTimeDiff(p, dynamic_cast<Plan*>(guide), diff);
}

IND Path::FixSizeDiff(Plan *p, sett *diff)
{
    return FixSizeDiff(p, dynamic_cast<Plan*>(guide), diff);
}

IND Path::FixItemDiff(Plan *p, Plan *goal, sett *diff)
{
    IND count = 0;
    if (p != NULL && diff != NULL)
    {
        IND gindex = goal->FindSettIndex(diff->m, diff->t);
        sett *gsett = goal->GetSettingAt(diff->m, gindex);
        if (gsett != NULL)
        {
            jsett_type *jsetts = new jsett_type();
            // find expected lot locations in current plan
            p->ExportSettings(*jsetts);
            if (jsetts->count(gsett->j) > 0)
                count += ExchangeDiff(p, goal, diff, (*jsetts)[gsett->j]);

            // if no possible/feasible diff fixes have been made, force a fix
            count += ForceIemFix(p, goal, diff, gsett);

            // walk through the whole neighborhood and perform the other fixes
            IND tcount = FixTimeDiffInNhood(goal, gsett, count);
            count += tcount;

            // find current lot locations in goal plan
            jsetts->clear();
            goal->ExportSettings(*jsetts);
            if (jsetts->count(diff->j) > 0)
                // perform a self fix for size and time;
                count += MoveDiffTo(p, goal, diff, (*jsetts)[diff->j]);
            delete jsetts;
        }
    }
    return count;
}

IND Path::FixTimeDiff(Plan *p, Plan *goal, sett *diff)
{
    IND count = 0;
    if (p != NULL && diff != NULL)
    {
        IND pindex = p->FindSettIndex(diff->m, diff->t);
        sett *pprev = p->PrevSettingAt(diff->m, pindex);

        IND gindex = goal->FindSettIndex(diff->m, diff->t);
        sett *gsett = goal->GetSettingAt(diff->m, gindex);
        sett *gnext = goal->NextSettingAt(diff->m, gindex);
        IND gend = gnext == NULL ? goal->GetParam()->T : gnext->t - 1;
        
        if (gsett != NULL && diff->m == gsett->m && diff->t != gsett->t)
        {
            // try extending the current or the previouse lot to fix the starting point of the lot
            count += ExtendDiffTo(p, diff, pindex, gsett);

            // try shifting the diff lot to its supposed start time period
            if (diff->t > gsett->t) 
                count += planning::Plan::ShiftLeftAt(this, p, diff->m, pindex, diff->t - gsett->t);
            if (diff->t < gsett->t)
                count += planning::Plan::ShiftRightAt(this, p, diff->m, pindex, gsett->t - diff->t);

            // try inserting by overwriting
            count += ForceTimeFix(p, diff, gsett, gend);

            // subsequently, fix the size of the lots in all generated solutions in nhood
            IND scount = FixSizeDiffInNhood(goal, gsett, count);
            count += scount;
        }
    }
    return count;
}

IND Path::FixSizeDiff(Plan *p, Plan *goal, sett *diff)
{
    IND count = 0;
    if (p != NULL && diff != NULL)
    {
        IND pindex = p->FindSettIndex(diff->m, diff->t);
        sett *pnext = p->NextSettingAt(diff->m, pindex);
        IND pend = pnext == NULL ? p->GetParam()->T : pnext->t - 1;

        IND gindex = goal->FindSettIndex(diff->m, diff->t);
        sett *gsett = goal->GetSettingAt(diff->m, gindex);
        sett *gnext = goal->NextSettingAt(diff->m, gindex);
        IND gend = gnext == NULL ? goal->GetParam()->T : gnext->t - 1;

        if (gsett != NULL && pend != gend && 
            diff->m == gsett->m && diff->t == gsett->t && diff->j == gsett->j)
        {
            // try extending the current or the next lot 
            // in order to match the end time period of the lot
            count += ExtendDiffSizeTo(p, diff, pend, gend);

            // try shifting the next lot in order to match the end time period of the lot
            if (pend < gend)
                count += planning::Plan::ShiftRightAt(this, p, diff->m, pindex + 1, gend - pend);
            else
                count += planning::Plan::ShiftLeftAt(this, p, diff->m, pindex + 1, pend - gend);

            // try forcing production until the expected end
            count += ForceSizeFix(p, diff, pend, gend);
        }
    }
    return count;
}

IND Path::ExchangeDiff(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts)
{
    return ExchangeDiff(p, goal, diff, vsetts, false);
}

IND Path::ExchangeDiff(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts, bool inGuide)
{
    IND count = 0;
    if (p != NULL && diff != NULL && vsetts.size() > 0)
    {
        Plan *plan = new Plan(*p, false);
        for (IND i = 0; i < vsetts.size(); i++)
        {
            sett *dest = &vsetts[i];
            if (inGuide) dest = p->GetSetting(dest->m, dest->t);
            if (diff->j != dest->j && 
                plan->ExchangeLotsHeads(diff->m, diff->t, dest->m, dest->t))
            {
                if (AddToNhood(p, plan)) 
                {
                    count++;
                    if (inGuide) count += FixTimeDiffInNhood(goal, dest, 1);
                    plan = new Plan(*p, false);
                }
                else break;
            }
            if (diff->j != dest->j && 
                plan->ExchangeLotsHeadForTail(diff->m, diff->t, dest->m, dest->t))
            {
                if (AddToNhood(p, plan)) 
                {
                    count++;
                    if (inGuide) count += FixTimeDiffInNhood(goal, dest, 1);
                    plan = new Plan(*p, false);
                }
                else break;
            }
            if (diff->j != dest->j && 
                plan->ExchangeLotsTails(diff->m, diff->t, dest->m, dest->t)) 
            {
                if (AddToNhood(p, plan)) 
                {
                    count++;
                    if (inGuide) count += FixTimeDiffInNhood(goal, dest, 1);
                    plan = new Plan(*p, false);
                }
                else break;
            }
        }
        delete plan;
    }
    return count;
}

IND Path::MoveDiffTo(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts)
{
    return ExchangeDiff(p, goal, diff, vsetts, true);
}

IND Path::ForceIemFix(Plan *p, Plan *goal, sett *diff, sett *gsett)
{
    IND count = 0;
    
    if (diff->j != gsett->j)
    {
        sett *gnext = goal->NextSetting(gsett->m, gsett->t);
        IND gend = gnext == NULL ? p->GetParam()->T : gnext->t - 1;
        Plan *plan = new Plan(*p, false);
        if (plan->InsertLotHead(gsett->m, gsett->j, gsett->t, gend) >= gsett->t)
        {
            if (AddToNhood(p, plan)) 
            {
                count++;
                plan = new Plan(*p, false);
            }
        }
        if (plan->InsertLotTail(gsett->m, gsett->j, gsett->t, gend) >= gsett->t)
        {
            if (AddToNhood(p, plan)) 
            {
                count++;
                plan = new Plan(*p, false);
            }
        }
        // this might cause infeasibility
        plan->RemoveSetting(diff->m, diff->t);
        plan->AddSetting(gsett->m, gsett->t, gsett->j);
        if (AddToNhood(p, plan)) count++;
        else delete plan;
    }

    return count;
}

IND Path::ExtendDiffTo(Plan *p, sett *diff, IND diff_index, sett *gsett)
{
    IND count = 0;
    sett *pprev = p->PrevSettingAt(diff->m, diff_index);
    if (pprev != NULL)
    {
        if (diff->t > gsett->t)
        {
            Plan *plan = new Plan(*p, false);
            int start = plan->ExtendNextLot(pprev->m, pprev->t, gsett->t);
            if (start == gsett->t)
            {
                if (AddToNhood(p, plan)) count++;
                else { delete plan; return count; }
            }
            else delete plan;
        }
        if (diff->t < gsett->t)
        {
            Plan *plan = new Plan(*p, false);
            int end = plan->ExtendPrevLot(pprev->m, pprev->t, gsett->t - 1);
            if (end + 1 == gsett->t)
            {
                if (AddToNhood(p, plan)) count++;
                else { delete plan; return count; }
            }
            else delete plan;
        }
    }
    return count;
}

IND Path::ForceTimeFix(Plan *p, sett *diff, sett *gsett, IND gend)
{
    IND count = 0;
    if (diff->t != gsett->t)
    {
        int pindex = p->FindSettIndex(gsett->m, gsett->t);
        if (pindex >= 0)
        {
            sett *psett = p->GetSettingAt(gsett->m, pindex);

            // this might cause infeasibility
            Plan *plan = new Plan(*p, false);
            // remove all settings until gend
            IND index = plan->FindSettIndex(diff->m, diff->t);
            sett *s = NULL;
            do 
            {
                plan->RemoveSettingAt(diff->m, index);
                s = plan->GetSettingAt(diff->m, index);
            } while (s != NULL && s->t <= gend);
            // insert the diff at gend + 1
            plan->AddSetting(diff->m, gend + 1, diff->j);
            if (AddToNhood(p, plan)) count++;
            else delete plan;

            if (psett->t < gsett->t) 
            {
                // this might cause infeasibility
                Plan *plan = new Plan(*p, false);
                plan->AddSetting(diff->m, gsett->t, diff->j);
                if (AddToNhood(p, plan)) count++;
                else delete plan;
            }
        }
    }
    return count;
}

IND Path::ExtendDiffSizeTo(Plan *p, sett *diff, IND diffend, IND gend)
{
    IND count = 0;
    
    Plan *plan = new Plan(*p, false);
    int t;
    if (diffend < gend)
        t = plan->ExtendPrevLot(diff->m, diffend + 1, gend);
    else
        t = plan->ExtendNextLot(diff->m, diff->t, gend);
    if (t == gend)
    {
        if (AddToNhood(p, plan)) count++;
        else 
        {
            delete plan;
            return count;
        }
    }
    else delete plan;
    
    return count;
}

IND Path::ForceSizeFix(Plan *p, sett *diff, IND diffend, IND gend)
{
    IND count = 0;
    IND index = p->FindSettIndex(diff->m, diff->t);
    if (diffend < gend)
    {        
        // remove all settings until gend
        Plan *plan = new Plan(*p, false);
        sett *s = NULL;
        sett *last = new sett;
        for (index = index + 1; index < plan->NumSettings(diff->m); index++)
        {
            s = plan->GetSettingAt(diff->m, index);
            if (s->t <= gend) 
            {
                last->m = s->m;
                last->t = s->t;
                last->j = s->j;

                plan->RemoveSettingAt(diff->m, index);
                s = NULL;
            }
            else break;
        }
        
        // enforce a new setting at gend + 1
        if (gend + 1 <= p->GetParam()->T)
        {
            if (s != NULL && s->j != diff->j && s->t > gend + 1)
            {
                plan->AddSetting(diff->m, gend + 1, s->j);
            }
            else if (last != NULL && last->j != diff->j)
            {
                plan->AddSetting(diff->m, gend + 1, last->j);
            }
        }
        
        delete last;
        if (AddToNhood(p, plan)) count++;
        else delete plan;
    }
    else
    {
        Plan *plan = new Plan(*p, false);
        // take the next setting and insert it earlier
        sett *pnext = plan->NextSettingAt(diff->m, index);
        sett *pprev = plan->PrevSettingAt(diff->m, index);
        if (pnext != NULL)
        {
            int t = plan->InsertLotHead(pnext->m, pnext->j, gend + 1, pnext->t);
            if (t < gend)
            {
                plan->AddSetting(pnext->m, gend + 1, pnext->j);  
            }
            if (AddToNhood(p, plan)) 
            {
                count++;
                plan = new Plan(*p, false);
            }
            else {delete plan; return count; }
        }
        
        if (pprev != NULL)
        {
            int t = plan->InsertLotHead(pprev->m, pprev->j, gend + 1, pprev->t);
            if (t < gend)
            {
                plan->AddSetting(pprev->m, gend + 1, pprev->j);  
            }
            if (AddToNhood(p, plan)) count++;
            else delete plan;
        }
    }
    
    return count;
}

IND Path::FixItemDiffInNhood(Plan *goal, sett *gsett, IND size)
{
    IND count = 0;
    if (gsett != NULL && size < nsize)
    {
        for (IND i = nsize - size; i < nsize; i++)
        {
            Plan *p = dynamic_cast<Plan*>(nhood[i]);
            sett *psett = p->GetSetting(gsett->m, gsett->t);
            if (psett != NULL && psett->j != gsett->j)
            {
                count += FixItemDiff(p, goal, psett);   
            }
        }
    }
    return count;
}

IND Path::FixTimeDiffInNhood(Plan *goal, sett *gsett, IND size)
{
    IND count = 0;
    if (gsett != NULL && size < nsize)
    {
        for (IND i = nsize - size; i < nsize; i++)
        {
            Plan *p = dynamic_cast<Plan*>(nhood[i]);
            sett *psett = p->GetSetting(gsett->m, gsett->t);
            if (psett != NULL && psett->t != gsett->t)
            {
                count += FixTimeDiff(p, goal, psett);   
            }
        }
    }
    return count;
}

IND Path::FixSizeDiffInNhood(Plan *goal, sett *gsett, IND size)
{
    IND count = 0;
    if (gsett != NULL && size < nsize)
    {
        sett *gnext = goal->NextSetting(gsett->m, gsett->t);
        IND gend = gnext == NULL ? goal->GetParam()->T : gnext->t - 1;
        float gq = goal->GetProduction(gsett->m, gsett->j, gend - gsett->t + 1, 1);
        for (IND i = nsize - size; i < nsize; i++)
        {
            Plan *p = dynamic_cast<Plan*>(nhood[i]);
            IND pindex = p->FindSettIndex(gsett->m, gsett->t);
            sett *psett = p->GetSettingAt(gsett->m, pindex);
            sett *pnext = p->NextSettingAt(gsett->m, pindex);
            IND pend = pnext == NULL ? p->GetParam()->T : pnext->t - 1;
            float pq = p->GetProduction(psett->m, psett->j, pend - psett->t + 1, 1);

            float q_diff = gq > pq ? gq - pq : pq - gq;
            if (q_diff > 1)
            {
                count += FixSizeDiff(p, goal, psett);   
            }
        }
    }
    return count;
}

IND Path::Diff(Solution *sol, Solution *goal)
{
    return Path::Diff(dynamic_cast<Plan*>(sol), 
        dynamic_cast<Plan*>(goal));
}

IND Path::Diff(Plan *p, Plan *goal)
{
    IND diff = 0;
    for (IND m = 1; m <= p->GetParam()->M; m++)
    {
        diff += Diff(p, goal, m);
    }
    return diff;
}

IND Path::Diff(Plan *p, Plan *goal, IND m)
{
    IND diff = 0;
    if (m >= 1)
    {
        IND pindex = 0;
        IND gindex = 0;
        sett *psett = p->GetSettingAt(m, pindex);
        sett *gsett = goal->GetSettingAt(m, gindex);
        while (psett != NULL || gsett != NULL)
        {
            if (psett != NULL && gsett != NULL && psett->j == gsett->j)
            {
                // check the starting time periods and the production quantities of the lots
                diff += Diff(p, goal, m, pindex, gindex);
            }
            else 
            {
                diff += ITEM_DIFF_COST;
                if (gsett == NULL || (psett != NULL && psett->t < gsett->t))
                {
                    // increment the setting in p
                    pindex++;
                    psett = p->GetSettingAt(m, pindex);     
                    continue;
                } 
                if (psett == NULL || (gsett != NULL && gsett->t < psett->t))
                {
                    // increment the setting in goal
                    gindex++;
                    gsett = goal->GetSettingAt(m, gindex);
                    continue;
                }
            }

            // increment the settings at both p and goal
            pindex++;
            psett = p->GetSettingAt(m, pindex);
            gindex++;
            gsett = goal->GetSettingAt(m, gindex);
        }
    }
    return diff;
}

IND Path::Diff(Plan *p, Plan *goal, IND m, IND pindex, IND gindex)
{
    IND diff = 0;
    sett *psett = p->GetSettingAt(m, pindex);
    sett *gsett = goal->GetSettingAt(m, pindex);
    if (psett != NULL && gsett != NULL && psett->j == gsett->j)
    {
        // compute the starting time difference
        if (psett->t != gsett->t) 
        {
            IND time_diff = psett->t > gsett->t ? psett->t - gsett->t : gsett->t - psett->t;
            if (time_diff >= (TIME_DIFF_THRESHOLD * p->GetParam()->T) / 100)
                diff += LARGE_TIME_DIFF_COST;
            else 
                diff += SMALL_TIME_DIFF_COST;
        }

        // compute the production quantity difference
        sett *pnext = p->NextSettingAt(m, pindex);
        IND pend = pnext == NULL ? p->GetParam()->T : pnext->t - 1;
        float pq = p->GetProduction(m, psett->j, pend - psett->t + 1, 1);

        sett *gnext = goal->NextSettingAt(m, pindex);
        IND gend = gnext == NULL ? p->GetParam()->T : gnext->t - 1;
        float gq = goal->GetProduction(m, gsett->j, gend - gsett->t + 1, 1);

        float q_diff = pq > gq ? pq - gq : gq - pq;
        if (q_diff > 1)
        {
            float q_diff_percent = (q_diff * 100) / gq;
            if (q_diff_percent < SMALL_SIZE_DIFF_THRESHOLD)
                diff += SMALL_SIZE_DIFF_COST;
            else if (q_diff_percent < LARGE_SIZE_DIFF_THRESHOLD)
                diff += MEDIUM_SIZE_DIFF_COST;
            else 
                diff += LARGE_SIZE_DIFF_COST;
        }
    }
    else 
    {
        diff += ITEM_DIFF_COST;
    }
    return diff;
}

sett *Path::FirstDiff(Plan *p, Plan *goal)
{
    sett *first_diff = NULL;
    bool pconstr = p->IsConstrained() == 1;
    bool gconstr = goal->IsConstrained() == 1;
    for (IND m = 1; m <= p->GetParam()->M; m++)
    {
        // if the plan is unconstrained force the choice of a lot, which is causing 
        // the infeasibility and is still different from the expected lot in goal;
        sett *s = FirstDiff(p, goal, m, !pconstr && gconstr);
        if (s != NULL)
        {
            // choose the earliest difference in time
            if (first_diff == NULL) first_diff = s;
            else if (s->t <= first_diff->t) first_diff = s;
        }
    }
    return first_diff;
}

sett *Path::FirstDiff(Plan *p, Plan *goal, IND m)
{
    return FirstDiff(p, goal, m, false);
}

sett *Path::FirstDiff(Plan *p, Plan *goal, IND m, bool infeasDiff)
{
    sett *first_diff = NULL;

    sett *psett;
    sett *pnext;
    for (IND index = 0; index < p->NumSettings(m); index++)
    {
        psett = p->GetSettingAt(m, index);
        pnext = p->NextSettingAt(m, index);
        IND pend = pnext == NULL ? p->GetParam()->T : pnext->t - 1;
        if (!infeasDiff || p->IsConstrainedAt(m, psett->t, pend) != 1)
        {
            // check if this is different in the goal
            IND gindex = goal->FindSettIndex(m, psett->t);
            sett *gsett = goal->GetSettingAt(m, gindex);
            sett *gnext = goal->NextSettingAt(m, gindex);
            IND gend = gnext == NULL ? goal->GetParam()->T : gnext->t - 1;
            if (gsett != NULL)
            {
                if (psett->j == gsett->j && psett->t == gsett->t && pend == gend)
                {
                    continue;
                } 
                else      
                {
                    first_diff = psett;
                    break;
                }
            }
        }
    }

    return first_diff;
}

void Path::Export(ostream &outs)
{
    outs << omp_get_thread_num() << ": " << "Generated path: ";
    for (IND i = 0; i < path->size(); i++)
    {
        Solution *sol = (*path)[i];
        outs << Parameters::TabbedSpace(sol->Value());
        if (i < path->size() - 1) outs << " --> ";
    }
    outs << endl;
}

Path::~Path()
{
    Solution *best = GetBest();
    ClearNhood(best);
    delete nhood;

    for (IND index = 0; index < Size(); index++)
    {
        if ((*path)[index] == this->init) continue;
        if ((*path)[index] == this->guide) continue;
        if ((*path)[index] == this->best) continue;

        delete (*path)[index];
    }
    path->clear();
    delete path;
}

}
