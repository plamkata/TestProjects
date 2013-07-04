#include "stdafx.h"
#include "improvement.h"

#include <map>

namespace metaheuristic {

LocalSearch::LocalSearch(const Solution& sol, Rand *rand)
{
    this->rand = rand;
    this->termCounter = 0;

    // path = new vector<Solution*>();
    best = sol.Clone(false);

    nhood = new Solution*[sol.GetParam()->maxNhoodSize];
    nsize = 0;
}

void LocalSearch::SetBest(Solution* sol)
{
    if (sol != best)
    {
        best = sol;
        //path->push_back(best);
    }
}

Rand *LocalSearch::GetRand() 
{
    return this->rand;
}

IND LocalSearch::GetNhoodSize() const
{
    return nsize;
}

Solution* LocalSearch::GetBest()
{
    return best;
}

Solution* LocalSearch::SolutionAt(IND index) const
{
    if (index < GetNhoodSize())
    {
        return this->nhood[index];
    }
    else 
    {
        return NULL;
    }
}

void LocalSearch::SetFactory(INhoodFactory* factory) 
{
    this->factory = factory;
}

bool LocalSearch::AddToNhood(Solution* sol)
{
    if (sol != NULL)
    {
        if (nsize < sol->GetParam()->maxNhoodSize)
        {
            nhood[nsize] = sol;
            nsize++;

            return true;
        }
    }
    return false;
}

void LocalSearch::ClearNhood()
{
    // only leave the best in nhood
    while (nsize > 0)
    {
        nsize--;
        if (nhood[nsize] != best && nhood[nsize] != NULL)
        {
            delete nhood[nsize];
            nhood[nsize] = NULL;
        }
    }
}

void LocalSearch::ClearNhood(ExecutionContext *ctx, float coeff)
{
    IND index = 1;
    // put the best plan always in the beginning
    IND i;
    for (i = 0; i < nsize; i++)
    {
        if (nhood[i] == best) 
        {
            ReplaceInNhood(i, 0);
        }
        else if (nhood[i]->Value() < (1.0 + coeff) * best->Value()) 
        {
            ReplaceInNhood(i, index);
            index++;
        }
        if (index >= ctx->GetParams()->minNhoodSize) break;
    }

    // delete the rest of the values
    while (nsize > index)
    {
        nsize--;
        if (nhood[nsize] != best)
        {
            delete nhood[nsize];
            nhood[nsize] = NULL;
        }
    }
}

void LocalSearch::ReplaceInNhood(IND i, IND j)
{
    if (i < nsize && j < nsize && i != j)
    {
        Solution* tmp = nhood[i];
        nhood[i] = nhood[j];
        nhood[j] = tmp;
    }
}

Solution *LocalSearch::EvaluateNhood()
{
    Solution* nhood_best = NULL;
    for (IND i = 0; i < nsize; i++)
    {
        Solution* p = nhood[i];
        p->Execute(false);
        
        if (nhood_best == NULL || 
            p->Value() < nhood_best->Value())
        {
            nhood_best = p;
        }
    }
    return nhood_best;
}

Solution* LocalSearch::Execute(ExecutionContext *ctx)
{
    // start with the current best solution in neighborhood
    AddToNhood(best);
    if (ctx->GetParams()->printLog) 
    {
        best->Execute(false);
        ctx->GetLog()->Add(Iteration, best->Value());
    }

    IND imprCounter = 0;
    IND prevImprCounter = imprCounter;
    termCounter = 0;

    IND op_index = 0;
    IND old_op_index = factory->GetOperatorsSize();
    
    // terminate if there is no improvement in param->nhoodTermIter
    while (termCounter < ctx->GetParams()->nhoodTermIter)
    {
        // choose a random local neighborhood operator (different from the old one)
        do 
        {
            op_index = factory->SelectOperator(rand);
        }
        while (termCounter > 0 && op_index == old_op_index);
        old_op_index = op_index;

        // select a plan for modification from local neighborhood
        int nhood_index = SelectFromNhood(ctx);
        
        // generate local neighborhood
        factory->ExecuteOperator(op_index, nhood[nhood_index]);

        // perform execution and evaluation of local neighborhood
        Solution* nhood_best = EvaluateNhood();
        if (nhood_best->Value() < best->Value() - Parameters::PRECISION) 
        {
            imprCounter++;
            SetBest(nhood_best);
            if (imprCounter % (200 / ctx->GetParams()->M) == 0)
            {
                if (ctx->GetParams()->printLog)
                {
                    ctx->GetLog()->Add(Iteration, nhood_best->Value());
                }
                int pid = omp_get_thread_num();
                if (ctx->GetParams()->printInfo) 
                { 
                    cout << pid << ": Improve progress: " << 
                        Parameters::TabbedSpace(nhood_best->Value()) << endl;
                }
            }
        }
        // clear neighborhood
        FilterNhood(ctx);
        
        if (prevImprCounter == imprCounter) termCounter++;
        else 
        {
            prevImprCounter = imprCounter;
            termCounter = 0;
        }
    }

    if (ctx->GetParams()->printLog) 
    { 
        best->Execute(false);
        ctx->GetLog()->Add(Iteration, best->Value());
    }
    return best;
}

int LocalSearch::SelectFromNhood(ExecutionContext *ctx)
{
    int nhood_index = -1;
    if (nsize > 0)
    {
        // loose the selection pressure after nhoodPressure proportion have been reached
        if (IsStuck(ctx))
        {
            // select randomly a solution to be modified
            IND size = Parameters::min(nsize, ctx->GetParams()->minNhoodSize);
            nhood_index = Parameters::random(rand, size - 1);
        }
        else 
        {
            // the best solution is always at position 0 after filtering the nhood
            nhood_index = 0;
        }
    }
    return nhood_index;
}

IND LocalSearch::FilterNhood(ExecutionContext *ctx)
{
    if (IsStuck(ctx))
    {
        ClearNhood(ctx, ctx->GetParams()->nhoodCoeff);
    }
    else
    {
        ClearNhood(ctx, 0.0);
    }
    return nsize;
}

bool LocalSearch::IsStuck(ExecutionContext *ctx)
{
    float termProgress = float(termCounter) / 
        float(ctx->GetParams()->nhoodTermIter);
    return termProgress > ctx->GetParams()->nhoodPressure;
}

LocalSearch::~LocalSearch()
{
    ClearNhood();
    delete [] nhood;

    delete best;

    // for (IND i = 0; i < path->size(); i++) delete (*path)[i];
    // delete path;
}

}