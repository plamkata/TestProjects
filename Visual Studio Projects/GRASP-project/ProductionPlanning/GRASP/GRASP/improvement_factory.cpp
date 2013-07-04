#include "stdafx.h"
#include "improvement_factory.h"

namespace planning {

IND ImprovementFactory::NHOOD_OPRS_SIZE = 5;

ImprovementFactory::NhoodOp ImprovementFactory::NHOOD_OPRS[] = {
    &ImprovementFactory::ExchangeMachineLots, 
    &ImprovementFactory::ExchangeParallelLots, 
    &ImprovementFactory::MergeSimilarLots, 
    &ImprovementFactory::ExtendLotsForUnmetDemands, 
    &ImprovementFactory::InsertLotsForUnmetDemands, 
};

IND ImprovementFactory::GetOperatorsSize() const 
{
    return NHOOD_OPRS_SIZE;
}

IND ImprovementFactory::SelectOperator(Rand *rand)
{
    return Parameters::random(rand, NHOOD_OPRS_SIZE - 1);
}

IND ImprovementFactory::ExecuteOperator(IND opIndex, Solution* sol)
{
    Plan* p = dynamic_cast<Plan*>(sol);
    return ExecuteOperator(&NHOOD_OPRS[opIndex], p);
}

IND ImprovementFactory::ExecuteOperator(NhoodOp *op, Plan *p)
{
    // delete any execution data, leave out only the settings
    p->DeleteExecutionData();

    return CALL_MEMBER_FN(*this, *op) (p);
}

IND ImprovementFactory::MergeSimilarLots(Plan *p)
{
    IND count = 0;
    jsett_type jsetts;
    p->ExportSettings(jsetts);

    if (jsetts.size() > 0)
    {
        IND start = Parameters::random(nhood->GetRand(), (IND) jsetts.size() - 1);
        jsett_type::iterator start_it = jsetts.begin();
        IND i;
        for (i = 0; i < start; i++) start_it++;

        jsett_type::iterator it = jsetts.begin();
        for (i = 0; i < start; i++) it++;
        // walk through all settings with more than one lots for the same item in the plan, with random start
        if (jsetts.size() > 0) do
        {
            if (it->second.size() > 1)
            {
                count += MergeSimilarLots(p, it->second);
                if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;
            }
            it++;
            if (it == jsetts.end()) it = jsetts.begin();
        } while (it != start_it);
    }

    return count;
}

IND ImprovementFactory::MergeSimilarLots(Plan *p, vector<sett> &setts)
{
    IND count = 0;

    IND mstart = Parameters::random(nhood->GetRand(), (IND) setts.size() - 1);
    IND mindex = mstart;
    // walk through all settings with mindex in setts, with random start
    if (setts.size() > 0) do
    {
        IND nstart = 0;
        IND nindex = nstart;
        // walk through all settings with nindex in setts, with random start
        if (setts.size() > 0) do
        {
            // foreach two different settings in vsetts
            if (mindex != nindex)
            {
                count += MergeSimilarLots(p, &setts[mindex], &setts[nindex]);
                if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;
            }

            if (nindex + 1 == setts.size()) nindex = 0;
            else nindex++;
        } while (nindex != nstart);

        if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;

        if (mindex + 1 == setts.size()) mindex = 0;
        else mindex = mindex + 1;
    } while (mindex != mstart);

    return count;
}

IND ImprovementFactory::MergeSimilarLots(Plan *p, sett *msett, sett *nsett)
{
    IND count = 0;
    if (msett != NULL && nsett != NULL && nsett->j == msett->j && 
        (nsett->m != msett->m || nsett->t != msett->t))
    {
        IND mindex = p->FindSettIndex(msett->m, msett->t);
        sett *mprev = p->PrevSettingAt(msett->m, mindex);
        sett *mnext = p->NextSettingAt(msett->m, mindex);

        // find similar settings in p
        Plan *plan = new Plan(*p, false);      
        // exchange with previouse lot
        if (mprev != NULL)
        {
            if (plan->ExchangeLotsTails(nsett->m, nsett->t, mprev->m, mprev->t)) 
            {
                if (nhood->AddToNhood(plan)) 
                {
                    count++;
                    plan = new Plan(*p, false);
                }
                else { delete plan; return count; }
            }

            if (plan->ExchangeLotsHeadForTail(nsett->m, nsett->t, mprev->m, mprev->t))
            {
                if (nhood->AddToNhood(plan)) 
                {
                    count++;
                    plan = new Plan(*p, false);
                }
                else { delete plan; return count; }
            }
        }

        // exchange with next lot
        if (mnext != NULL)
        {
            if (plan->ExchangeLotsHeads(mnext->m, mnext->t, nsett->m, nsett->t))
            {
                if (nhood->AddToNhood(plan)) 
                {
                    count++;
                    plan = new Plan(*p, false);
                }
                else { delete plan; return count; }
            }

            if (plan->ExchangeLotsHeadForTail(mnext->m, mnext->t, nsett->m, nsett->t))
            {
                if (nhood->AddToNhood(plan)) 
                {
                    count++;
                    plan = new Plan(*p, false);
                }
                else { delete plan; return count; }
            }
        }
        delete plan;
    }
    return count;
}

IND ImprovementFactory::ExchangeMachineLots(Plan *p)
{
    IND count = 0;
    
    IND mstart = Parameters::random(nhood->GetRand(), 1, p->GetParam()->M);
    IND m = mstart;
    // walk through all machines m, with random start
    do
    {
        IND nstart = Parameters::random(nhood->GetRand(), m + 1, p->GetParam()->M);
        IND n = nstart;
        // walk through all machines n from m+1 to M, with random start
        if (nstart <= p->GetParam()->M) do
        {
            // foreach different 2-tuple of machines, with random start, but in systematic order
            if (IsExchangePossible(p, m, n))
            {
                count += ExchangeMachineLots(p, m, n);
            }
            count += ExchangeMachineLotsHeads(p, m, n);
            count += ExchangeMachineLotsTails(p, m, n);

            if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;

            if (n == p->GetParam()->M) n = m + 1;
            else n++;
        } while (n != nstart);

        if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;
        
        if (m == p->GetParam()->M) m = 1;
        else m++;
    } while (m != mstart);

    return count;
}

IND ImprovementFactory::ExchangeMachineLots(Plan *p, IND m, IND n)
{
    IND count = 0;
    if (m != n)
    {
        Plan *plan = new Plan(*p, false);
        plan->ClearSettings(m);
        plan->ClearSettings(n);

        IND i;
        for (i = 1; i < p->NumSettings(m); i++)
        {
            sett *s = p->GetSettingAt(m, i);
            if (s != NULL)
            {
                plan->AddSetting(n, s->t, s->j);
            }
        }
        for (i = 1; i < p->NumSettings(n); i++)
        {
            sett *s = p->GetSettingAt(n, i);
            if (s != NULL)
            {
                plan->AddSetting(m, s->t, s->j);
            }
        }

        if (plan->IsConstrainedAt(m, 1, p->GetParam()->T) == 1)
        {
            if (nhood->AddToNhood(plan)) count++;
            else delete plan;
        }
        else delete plan;
    }
    return count;
}

bool ImprovementFactory::IsExchangePossible(Plan *p, IND m, IND n)
{
    bool possible = false;
    if (m != n)
    {
        possible = true;
        IND index;
        for (index = 1; index < p->NumSettings(m); index++)
        {
            sett *s = p->GetSettingAt(m, index);
            if (s != NULL && s->j > 0 && s->j <= p->GetParam()->J)
            {
                if (p->GetParam()->cap[n][s->j] == 0)
                {
                    possible = false;
                    break;
                }
            }
        }
        
        if (possible)
        for (index = 1; index < p->NumSettings(n); index++)
        {
            sett *s = p->GetSettingAt(n, index);
            if (s != NULL && s->j > 0 && s->j <= p->GetParam()->J)
            {
                if (p->GetParam()->cap[m][s->j] == 0)
                {
                    possible = false;
                    break;
                }
            }
        }
    }
    return possible;
}

IND ImprovementFactory::ExchangeMachineLotsHeads(Plan *p, IND m, IND n)
{
    IND count = 0;
    if (m != n)
    {
        Plan *plan = new Plan(*p, false);
        IND changeCount = 0;

        IND m_index = 0, n_index = 0;
        sett *msett;
        sett *nsett;
        sett *mnext;
        sett *nnext;
        while (m_index < plan->NumSettings(m) && 
            n_index < plan->NumSettings(n))
        {
            msett = plan->GetSettingAt(m, m_index);
            nsett = plan->GetSettingAt(n, n_index);
            if (msett != NULL && nsett != NULL)
            {
                if (plan->ExchangeLotsHeads(m, msett->t, n, nsett->t))
                {
                    changeCount++;
                }
                mnext = plan->NextSettingAt(m, m_index);
                nnext = plan->NextSettingAt(n, n_index);
                if (mnext != NULL && nnext != NULL)
                {
                    if (nnext->t >= mnext->t) m_index++;
                    if (mnext->t >= nnext->t) n_index++;
                }
                else
                {
                    if (nsett->t >= msett->t) m_index++;
                    if (msett->t >= nsett->t) n_index++;
                }
            }
            else
            {
                m_index++;
                n_index++;
            }
        }

        if (changeCount > 0)
        {
            if (nhood->AddToNhood(plan)) count++;
            else delete plan;
        }
        else delete plan;
    }
    return count;
}

IND ImprovementFactory::ExchangeMachineLotsTails(Plan *p, IND m, IND n)
{
    IND count = 0;
    if (m != n)
    {
        Plan *plan = new Plan(*p, false);
        IND changeCount = 0;

        int m_index = p->NumSettings(m) - 1;
        int n_index = p->NumSettings(n) - 1;
        sett *msett;
        sett *nsett;
        sett *mprev;
        sett *nprev;
        while (m_index >= 0 && n_index >= 0)
        {
            msett = plan->GetSettingAt(m, m_index);
            nsett = plan->GetSettingAt(n, n_index);
            if (msett != NULL && nsett != NULL)
            {
                if (plan->ExchangeLotsTails(m, msett->t, n, nsett->t)) 
                {
                    changeCount++;
                }
                mprev = plan->PrevSettingAt(m, m_index);
                nprev = plan->PrevSettingAt(n, n_index);
                if (mprev != NULL && nprev != NULL)
                {
                    if (nprev->t <= mprev->t) m_index--;
                    if (mprev->t <= nprev->t) n_index--;
                }
                else
                {
                    if (nsett->t <= msett->t) m_index--;
                    if (msett->t <= nsett->t) n_index--;
                }
            }
            else
            {
                m_index--;
                n_index--;   
            }
        }

        if (changeCount > 0)
        {
            if (nhood->AddToNhood(plan)) count++;
            else delete plan;
        }
        else delete plan;
    }
    return count;
}

IND ImprovementFactory::ExchangeParallelLots(Plan *p)
{
    IND count = 0;

    IND start_m = Parameters::random(nhood->GetRand(), 1, p->GetParam()->M);
    IND m = start_m;
    // walk through all machines, with random start
    if (p->GetParam()->M > 0) do
    {
        IND start_index = Parameters::random(nhood->GetRand(), 1, p->NumSettings(m) - 1);
        IND index = start_index;
        // walk through all settings on machine m, excluding the first one, with random start
        if (p->NumSettings(m) > 1) do
        {
            // foreach setting, chosen with random start, but in systematic order
            sett *s = p->GetSettingAt(m, index);
            count += ExchangeParallelLots(p, s);
            if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;

            if (index + 1 == p->NumSettings(m)) index = 1;
            else index = index + 1;
        } while (index != start_index);

        if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;

        if (m == p->GetParam()->M) m = 1;
        else m++;
    } while (m != start_m);

    return count;
}

IND ImprovementFactory::ExchangeParallelLots(Plan *p, sett *s)
{
    IND count = 0;
    Plan *plan = new Plan(*p, false);

    for (IND m = s->m + 1; m <= p->GetParam()->M; m++)
    {
        // find all different settings in the same time period, with random start
        sett *m_sett = p->GetSetting(m, s->t);
        if (m_sett != NULL && m_sett->t == s->t && m_sett->j != s->j)
        {
            if (plan->ExchangeLotsHeads(s->m, s->t, m_sett->m, m_sett->t))
            {
                if (nhood->AddToNhood(plan)) 
                {
                    count++;
                    plan = new Plan(*p, false);
                }
                else { delete plan; return count; }
            }
        }
        
        if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;
    }
    
    delete plan;
    return count;
}

IND ImprovementFactory::ExtendLotsForUnmetDemands(Plan *p)
{
    IND count = 0;

    // find the list of all unmet external demands
    vector<dem *> udemands;

    // clone p into p1 and execute p1 in order to fetch references to the real demands of p1;
    Plan p1(*p, false);
    p1.Execute();
    p1.FetchAllDemands(udemands, 0, p->GetParam()->T/3 - 1, &dem::IsZero);

    if (udemands.size() > 0)
    {
        IND start_m = Parameters::random(nhood->GetRand(), 1, p->GetParam()->M);
        IND m = start_m;
        // walk through all machines, with random start
        if (p->GetParam()->M > 0) do
        {
            IND start_index = Parameters::random(nhood->GetRand(), p->NumSettings(m) - 1);
            IND index = start_index;
            // walk through all settings on machine m, with random start
            if (p->NumSettings(m) > 0) do
            {
                sett *curr = p->GetSettingAt(m, index);
                // foreach setting in the plan with unmet demand
                if (p->IndexOf(curr->j, curr->t, udemands) != -1)
                {
                    count += ExtendLeftAt(p, m, index);
                    if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize - 1) break;

                    count += ExtendRightAt(p, m, index);
                    if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize - 1) break;
                }

                if (index + 1 == p->NumSettings(m)) index = 0;
                else index = index + 1;
            } while (index != start_index);
    
            if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize - 1) break;

            if (m == p->GetParam()->M) m = 1;
            else m++;
        } while (m != start_m);
    }

    return count;
}

IND ImprovementFactory::ExtendLeftAt(Plan *p, IND m, IND index)
{
    IND count = 0;
    if (m > 0 && m <= p->GetParam()->M && 
        index > 0 && index < p->NumSettings(m))
    {
        sett *curr = p->GetSettingAt(m, index);
        sett *prev = p->PrevSettingAt(m, index);
        if (prev != NULL)
        {
            int begin = -1;
            int prev_begin = -1;
            int ext_size = curr->t - prev->t;
            Plan *plan = new Plan(*p, false);
            while (ext_size > 0)
            {
                count += planning::Plan::ShiftLeftAt(nhood, p, m, index, ext_size);
                if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;
                
                begin = plan->ExtendNextLot(m, prev->t, curr->t - ext_size);
                if (begin != -1 && begin != prev_begin)
                {
                    if (nhood->AddToNhood(plan)) 
                    {
                        count++;
                        plan = new Plan(*p, false);
                    }
                    else break;
                }
                else if (begin != -1)
                {
                    delete plan;
                    plan = new Plan(*p, false);
                }
                prev_begin = begin;
                ext_size--;
            }
            delete plan;
        }
    }
    return count;
}

IND ImprovementFactory::ExtendRightAt(Plan *p, IND m, IND index)
{
    IND count = 0;
    if (m > 0 && m <= p->GetParam()->M && 
        index >= 0 && index < p->NumSettings(m))
    {
        sett *next = p->NextSettingAt(m, index);
        if (next != NULL)
        {
            int end = -1;
            int prev_end = -1;
            sett *nnext = p->NextSettingAt(m, index + 1);
            int ext_size = (nnext != NULL) ? 
                nnext->t - next->t : p->GetParam()->T - next->t + 1;
            Plan *plan = new Plan(*p, false);
            while (ext_size > 0)
            {
                count += planning::Plan::ShiftRightAt(nhood, p, m, index, ext_size);
                if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize) break;
                
                end = plan->ExtendPrevLot(m, next->t, next->t + ext_size - 1);
                if (end != -1 && end != prev_end)
                {
                    if (nhood->AddToNhood(plan)) 
                    {
                        count++;
                        plan = new Plan(*p, false);
                    }
                    else break;
                }
                else if (end != -1)
                {
                    delete plan;
                    plan = new Plan(*p, false);
                }
                prev_end = end;
                ext_size--;
            }
            delete plan;
        }
    }
    return count;
}

IND ImprovementFactory::InsertLotsForUnmetDemands(Plan *p)
{
    IND count = 0;

    // find the list of all unmet external demands
    vector<dem *> udemands;
    // clone p into p1 and execute p1 in order to fetch references to the real demands of p1;
    Plan p1(*p, false);
    p1.Execute();
    p1.FetchAllDemands(udemands, 0, p->GetParam()->T/3 - 1, &dem::IsZero);

    if (udemands.size() > 0)
    {
        /*
        // clear all unmet demands that there is production for, they are handled by another operator
        for (IND m = 1; m <= this->param->M; m++)
        {
            for (IND index = 0; index < p->NumSettings(m); index++)
            {
                sett *curr = p->GetSettingAt(m, index);
                int uindex;
                if ((uindex = p->IndexOf(curr->j, curr->t, &udemands)) != -1)
                {
                    udemands.erase(udemands.begin() + uindex);
                    if (udemands.size() == 0) break;
                }
            }
        }
        */

        IND start = Parameters::random(nhood->GetRand(), (IND) udemands.size() - 1);
        vector<dem *>::iterator start_it = udemands.begin() + start;

        vector<dem *>::iterator it = udemands.begin() + start;
        // walk through all unmet demands, with random start
        if (udemands.size() > 0) do
        {
            // foreach unmet demand, try inserting a production lot at several positions
            count += InsertLotsForUnmetDemand(&p1, *it);
            if (nhood->GetNhoodSize() >= p->GetParam()->maxNhoodSize - 1) break;
            it++;
            if (it == udemands.end()) it = udemands.begin();
        } while (it != start_it);
    }

    return count;
}

IND ImprovementFactory::InsertLotsForUnmetDemand(Plan *p, dem *d)
{
    IND count = 0;
    if (d != NULL && d->value > 0)
    {
        Plan *plan = new Plan(*p, false);
        int result;
        IND start_m = Parameters::random(nhood->GetRand(), 1, p->GetParam()->M);
        IND m = start_m;
        // walk through all machines m, with random start
        if (p->GetParam()->M > 0) do
        {
            IND lot_size = p->RequiredLotSize(m, 1, *d);
            IND start = d->due_t > lot_size - 1 ? d->due_t - lot_size + 1 : 1;
            IND end = d->due_t;
            result = plan->InsertLotTail(m, d->j, start, end);
            if (result > 0)
            {
                if (nhood->AddToNhood(plan)) 
                {
                    count++;
                    plan = new Plan(*p, false);
                }
                else break;
            }

            sett *s = p->GetSetting(m, start);
            start = (s != NULL && s->t > 0) ? s->t : 1;
            end = Parameters::min(start + lot_size - 1, d->due_t);
            result = plan->InsertLotHead(m, d->j, start, end);
            if (result > 0)
            {
                if (nhood->AddToNhood(plan)) 
                {
                    count++;
                    plan = new Plan(*p, false);
                }
                else break;
            }

            if (m == p->GetParam()->M) m = 1;
            else m++;
        } while (m != start_m);
        delete plan;
    }
    return count;
}

}