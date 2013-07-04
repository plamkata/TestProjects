#include "stdafx.h"
#include "relinking_factory.h"

namespace planning {

RelinkingFactory::RelinkingFactory(INhoodSearch *nhood_) : 
        nhood(nhood_)
{
}

IND RelinkingFactory::Diff(Solution *sol, Solution *goal)
{
    IND diff = 0;
    for (IND m = 1; m <= sol->GetParam()->M; m++)
    {
        diff += RelinkingFactory::Diff(
            static_cast<Plan *>(sol), 
            static_cast<Plan *>(goal), m);
    }
    return diff;
}

IND RelinkingFactory::Diff(Plan *p, Plan *goal, IND m)
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

IND RelinkingFactory::Diff(Plan *p, Plan *goal, IND m, IND pindex, IND gindex)
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

sett *RelinkingFactory::FirstDiff(Plan *p, Plan *goal)
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

sett *RelinkingFactory::FirstDiff(Plan *p, Plan *goal, IND m)
{
    return FirstDiff(p, goal, m, false);
}

sett *RelinkingFactory::FirstDiff(Plan *p, Plan *goal, IND m, bool infeasDiff)
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

IND RelinkingFactory::GetOperatorsSize() const
{
    return 1;
}

// selects a random index of a neighborhood operator in NHOOD_OPERATORS
IND RelinkingFactory::SelectOperator(Rand *rand, Solution* args...)
{
    Plan *p = static_cast<Plan *>(&args[0]);
    Plan *guide = static_cast<Plan *>(&args[1]);

    sett* diff = FirstDiff(p, guide);
    if (diff != NULL) 
    {
        return diff->m * p->GetParam()->T + diff->t;
    }
    else 
    {
        return p->GetParam()->M * p->GetParam()->T;
    }
}

// execute a neighborhood operator in NHOOD_OPERATORS by index
IND RelinkingFactory::ExecuteOperator(IND sol_indices, Solution* args...) 
{
    Plan *p = static_cast<Plan *>(&args[0]);
    if (sol_indices == p->GetParam()->M * 
        p->GetParam()->T) return 0;

    sett *psett = p->GetSetting(
        sol_indices / p->GetParam()->T, 
        sol_indices % p->GetParam()->T);

    Plan *guide = static_cast<Plan *>(&args[1]);
    sett *gsett = guide->GetSetting(psett->m, psett->t);
    if (gsett == NULL) 
    {
        return 0; // should never happen
    }

    // Determine the diff type based on the guiding solution
    if (psett->j != gsett->j) 
    {
        return FixItemDiff(p, guide, psett);
    }
    else if (psett->m == gsett->m && 
        psett->t != gsett->t)
    {
        return FixTimeDiff(p, guide, psett);
    }
    else if (psett->m == gsett->m &&
        psett->t == gsett->t) 
    {
        return FixSizeDiff(p, guide, psett);
    } 
    else 
    {
        return 0;
    }
}

void RelinkingFactory::SetNhood(INhoodSearch *nhood)
{
    this->nhood = nhood;
}

IND RelinkingFactory::FixItemDiff(Plan *p, Plan *goal, sett *diff)
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

IND RelinkingFactory::FixTimeDiff(Plan *p, Plan *goal, sett *diff)
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
                count += ShiftLeftAt(this->nhood, p, diff->m, pindex, diff->t - gsett->t);
            if (diff->t < gsett->t)
                count += ShiftRightAt(this->nhood, p, diff->m, pindex, gsett->t - diff->t);

            // try inserting by overwriting
            count += ForceTimeFix(p, diff, gsett, gend);

            // subsequently, fix the size of the lots in all generated solutions in nhood
            IND scount = FixSizeDiffInNhood(goal, gsett, count);
            count += scount;
        }
    }
    return count;
}

IND RelinkingFactory::FixSizeDiff(Plan *p, Plan *goal, sett *diff)
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
                count += ShiftRightAt(this->nhood, p, diff->m, pindex + 1, gend - pend);
            else
                count += ShiftLeftAt(this->nhood, p, diff->m, pindex + 1, pend - gend);

            // try forcing production until the expected end
            count += ForceSizeFix(p, diff, pend, gend);
        }
    }
    return count;
}

IND RelinkingFactory::ExchangeDiff(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts)
{
    return ExchangeDiff(p, goal, diff, vsetts, false);
}

IND RelinkingFactory::ExchangeDiff(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts, bool inGuide)
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
                if (this->nhood->AddToNhood(plan)) 
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
                if (this->nhood->AddToNhood(plan)) 
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
                if (this->nhood->AddToNhood(plan)) 
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

IND RelinkingFactory::MoveDiffTo(Plan *p, Plan *goal, sett *diff, vector<sett> &vsetts)
{
    return ExchangeDiff(p, goal, diff, vsetts, true);
}

IND RelinkingFactory::ForceIemFix(Plan *p, Plan *goal, sett *diff, sett *gsett)
{
    IND count = 0;
    
    if (diff->j != gsett->j)
    {
        sett *gnext = goal->NextSetting(gsett->m, gsett->t);
        IND gend = gnext == NULL ? p->GetParam()->T : gnext->t - 1;
        Plan *plan = new Plan(*p, false);
        if (plan->InsertLotHead(gsett->m, gsett->j, gsett->t, gend) >= gsett->t)
        {
            if (this->nhood->AddToNhood(plan)) 
            {
                count++;
                plan = new Plan(*p, false);
            }
        }
        if (plan->InsertLotTail(gsett->m, gsett->j, gsett->t, gend) >= gsett->t)
        {
            if (this->nhood->AddToNhood(plan)) 
            {
                count++;
                plan = new Plan(*p, false);
            }
        }
        // this might cause infeasibility
        plan->RemoveSetting(diff->m, diff->t);
        plan->AddSetting(gsett->m, gsett->t, gsett->j);
        if (this->nhood->AddToNhood(plan)) count++;
        else delete plan;
    }

    return count;
}

IND RelinkingFactory::ExtendDiffTo(Plan *p, sett *diff, IND diff_index, sett *gsett)
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
                if (this->nhood->AddToNhood(plan)) count++;
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
                if (this->nhood->AddToNhood(plan)) count++;
                else { delete plan; return count; }
            }
            else delete plan;
        }
    }
    return count;
}

IND RelinkingFactory::ForceTimeFix(Plan *p, sett *diff, sett *gsett, IND gend)
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
            if (this->nhood->AddToNhood(plan)) count++;
            else delete plan;

            if (psett->t < gsett->t) 
            {
                // this might cause infeasibility
                Plan *plan = new Plan(*p, false);
                plan->AddSetting(diff->m, gsett->t, diff->j);
                if (this->nhood->AddToNhood(plan)) count++;
                else delete plan;
            }
        }
    }
    return count;
}

IND RelinkingFactory::ExtendDiffSizeTo(Plan *p, sett *diff, IND diffend, IND gend)
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
        if (this->nhood->AddToNhood(plan)) count++;
        else 
        {
            delete plan;
            return count;
        }
    }
    else delete plan;
    
    return count;
}

IND RelinkingFactory::ForceSizeFix(Plan *p, sett *diff, IND diffend, IND gend)
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
        if (this->nhood->AddToNhood(plan)) count++;
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
            if (this->nhood->AddToNhood(plan)) 
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
            if (this->nhood->AddToNhood(plan)) count++;
            else delete plan;
        }
    }
    
    return count;
}

IND RelinkingFactory::FixItemDiffInNhood(Plan *goal, sett *gsett, IND size)
{
    IND count = 0;
    IND nsize = this->nhood->GetNhoodSize();
    if (gsett != NULL && size < nsize)
    {
        for (IND i = nsize - size; i < nsize; i++)
        {
            Plan *p = static_cast<Plan *>(this->nhood->SolutionAt(i));
            sett *psett = p->GetSetting(gsett->m, gsett->t);
            if (psett != NULL && psett->j != gsett->j)
            {
                count += FixItemDiff(p, goal, psett);   
            }
        }
    }
    return count;
}

IND RelinkingFactory::FixTimeDiffInNhood(Plan *goal, sett *gsett, IND size)
{
    IND count = 0;
    IND nsize = this->nhood->GetNhoodSize();
    if (gsett != NULL && size < nsize)
    {
        for (IND i = nsize - size; i < nsize; i++)
        {
            Plan *p = static_cast<Plan *>(this->nhood->SolutionAt(i));
            sett *psett = p->GetSetting(gsett->m, gsett->t);
            if (psett != NULL && psett->t != gsett->t)
            {
                count += FixTimeDiff(p, goal, psett);   
            }
        }
    }
    return count;
}

IND RelinkingFactory::FixSizeDiffInNhood(Plan *goal, sett *gsett, IND size)
{
    IND count = 0;
    IND nsize = this->nhood->GetNhoodSize();
    if (gsett != NULL && size < nsize)
    {
        sett *gnext = goal->NextSetting(gsett->m, gsett->t);
        IND gend = gnext == NULL ? goal->GetParam()->T : gnext->t - 1;
        float gq = goal->GetProduction(gsett->m, gsett->j, gend - gsett->t + 1, 1);
        for (IND i = nsize - size; i < nsize; i++)
        {
            Plan *p = static_cast<Plan *>(this->nhood->SolutionAt(i));
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

}