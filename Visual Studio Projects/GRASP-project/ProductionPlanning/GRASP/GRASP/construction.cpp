#include "stdafx.h"
#include "construction.h"

namespace planning {

bool dem::IsProcessed()
{
    return processed;
}

bool dem::IsZero()
{
    return Parameters::eq(value, 0.0);
}

float dem::CandidateCost(Parameters *param)
{
    return param->beta_t * float(due_t) + (param->beta_v * float(1000)) / value;
}

PartialPlan::PartialPlan(Parameters &input) : Plan(input)
{
    
}

PartialPlan::PartialPlan(const PartialPlan &p) : Plan(p)
{
    if (p.executing)
    {
        init_free_T(p);
    }
}

void PartialPlan::InitExecutionData()
{
    init_pos_I(*this->param);
    init_udemand(*this->param);
    init_free_T(*this->param);
    this->executing = true;
}

metaheuristic::Solution* PartialPlan::Clone(bool copy_exec_data) const
{
    return new PartialPlan(*this);
}

void PartialPlan::init_free_T(const Parameters &input)
{
    free_T = new IND[input.M + 1];
    free_T[0] = 0;
    for (IND m = 1; m <= input.M; m++) free_T[m] = 1;
}

void PartialPlan::init_free_T(const PartialPlan &p) 
{
    this->free_T = new IND[p.M + 1];
    this->free_T[0] = 0;
    for (IND m = 1; m <= p.M; m++) this->free_T[m] = p.free_T[m];
}


ins_cost *PartialPlan::BestCost(dem *demand, CostFn CostFnPtr)
{    
    ins_cost *best_cost = new ins_cost;
    // set initial status to: demand cannot be satisfied
    best_cost->demand = demand;
    best_cost->m = 0;
    best_cost->t = 0;
    best_cost->length = 0;
    best_cost->cost = Parameters::MAX_COST;

    IND j = demand->j;
    if (GetAvailableInventory(j, demand->due_t) > 0.0)
    {
        // demand can be satisfied from inventory
        best_cost->cost = 0.0;
    }
    else
    {
        ins_cost *cost;
        for (IND m = 1; m <= M; m++)
        {
            cost = CALL_MEMBER_FN(*this, CostFnPtr) (demand, m, free_T[m], T);
            if (cost->cost < best_cost->cost)
            {
                best_cost->m = cost->m;
                best_cost->t = cost->t;
                best_cost->length = cost->length;
                best_cost->cost = cost->cost;
            }
            delete cost;
        }
    }

    return best_cost;
}

ins_cost *PartialPlan::BestCost(vector<dem *> &CL, IND m, CostFn CostFnPtr)
{
    return BestCost(CL, m, free_T[m], T, CostFnPtr);
}

ins_cost *PartialPlan::BestCost(
    vector<dem *> &CL, IND m, IND start_t, IND end_t, CostFn CostFnPtr)
{    
    ins_cost *best_cost = new ins_cost;
    // set initial status to: demand cannot be satisfied
    best_cost->m = 0;
    best_cost->t = 0;
    best_cost->length = 0;
    best_cost->cost = Parameters::MAX_COST;

    ins_cost *cost;
    for (IND i = 0; i < CL.size(); i++)
    {
        cost = CALL_MEMBER_FN(*this, CostFnPtr) (CL[i], m, start_t, end_t);
        //cost = InsertionCost(&CL[i], m, start_t, end_t);
        if (cost->cost < best_cost->cost)
        {
            best_cost->demand = CL[i];
            best_cost->m = cost->m;
            best_cost->t = cost->t;
            best_cost->length = cost->length;
            best_cost->cost = cost->cost;
        }
        delete cost;
    }

    return best_cost;
}

ins_cost *PartialPlan::InsertionCost(dem *demand, IND m)
{
    return InsertionCost(demand, m, free_T[m], T);
}

ins_cost *PartialPlan::InsertionCost(dem *demand, IND m, IND start_t, IND end_t)
{    
    ins_cost *cost = new ins_cost;
    // set initial status to: demand cannot be satisfied
    cost->demand = demand;
    cost->m = 0;
    cost->t = 0;
    cost->length = 0;
    cost->cost = Parameters::MAX_COST;

    IND j = demand->j;
    float I = GetAvailableInventory(j, demand->due_t);
    if (I > 0.0)
    {
        cost->cost = 0.0;
    }
    else if (start_t <= end_t && this->param->cap[m][j] > 0.0)
    {
        sett *prev = GetSetting(m, start_t - 1);

        // if new product is to be produced we should make a setting
        IND x = 0;
        if (j != prev->j) x = 1;
        
        // find the next time period with constrained production
        int end = Parameters::min(demand->due_t, end_t);
        int t = -1;
        if (x == 1) t = ConstrainedStart(m, j, start_t, end);
        else if (x == 0)
        {
            // check if we can extend the prev setting at least one period
            if (ConstrainedEnd(m, j, start_t, start_t) != -1)
            {
                t = start_t;
            }
        }

        if (t >= start_t && t <= end)
        {
            // find the end of constrained production (t is already possible)
            IND delta = RequiredLotSize(m, x, I, *demand);
            end = ConstrainedEnd(m, j, t, end);
            IND lot_size = Parameters::min(t + delta - 1, end) - t + 1;

            // find the maximum quantity of units q we can produce for our demand
            float q = GetProduction(m, j, lot_size, x);
            
            // the unmet demand delta_u would be:
            float delta_u = 0.0;
            if (demand->value > I + q) 
            {
                delta_u = demand->value - I - q;
            }
            // the unmet future demand would be:
            float delta_f = 0.0;
            if (demand->due_t == T + 1) {
                delta_f = delta_u;
                delta_u = 0.0;
            }
            float delta_k = GetMaxInventoryViolation(I, q, *demand);
            
            float delta_q = float(100) / q;
            float delta_t = float(1) / float(demand->due_t - t + 1);
            float delta_l = float(x) / float(t - prev->t + 1);
            float delta_m = float(x) * float(NumSettings(m));

            float cost_value = 
                this->param->omega_s * this->param->s * x +  // setting cost
                // this->param->omega_u * this->param->c * delta_u +    // backlog cost for unmet demands
                // this->param->omega_f * delta_f +                     // backlog cost for future demand
                this->param->omega_k * delta_k +             // cost for max inventory overrun

                this->param->omega_q * delta_q +             // produce as much as possible
                this->param->omega_t * delta_t +             // produce as early as possible
                this->param->omega_l * delta_l +             // put a setting after a longer prod. lot            
                this->param->omega_m * delta_m;              // put a setting on a machine with less settings

            cost->m = m;
            cost->t = t;
            cost->length = lot_size;
            cost->cost = cost_value;
        }
    }

    return cost;
}

ins_cost *PartialPlan::ObjectiveCost(dem *demand, IND m)
{
    return ObjectiveCost(demand, m, free_T[m], T);
}

ins_cost *PartialPlan::ObjectiveCost(dem *demand, IND m, IND start_t, IND end_t)
{
    ins_cost *cost = new ins_cost;
    // set initial status to: demand cannot be satisfied
    cost->demand = demand;
    cost->m = 0;
    cost->t = 0;
    cost->length = 0;
    cost->cost = Parameters::MAX_COST;

    IND j = demand->j;
    float I = GetAvailableInventory(j, demand->due_t);
    if (I > 0.0)
    {
        cost->cost = 0.0;
    }
    else if (start_t <= end_t && this->param->cap[m][j] > 0.0)
    {
        sett *prev = GetSetting(m, start_t - 1);
        // if new product is to be produced we should make a setting
        int x = 0;
        if (j != prev->j) x += 1;
        
        // find the next time period with constrained production
        int end = Parameters::min(demand->due_t, end_t);
        int t = -1;
        if (x == 1) t = ConstrainedStart(m, j, start_t, end);
        else if (x == 0)
        {
            // check if we can extend the prev setting at least one period
            if (ConstrainedEnd(m, j, start_t, start_t) != -1)
            {
                t = start_t;
            }
        }

        if (t >= start_t && t <= end)
        {
            // find the end of constrained production (t is already possible)
            IND delta = RequiredLotSize(m, x, I, *demand);
            end = ConstrainedEnd(m, j, t, end);
            IND lot_size = Parameters::min(t + delta - 1, end) - t + 1;

            // find the maximum quantity of units q we can produce for our demand
            float q = GetProduction(m, j, lot_size, x);
            
            // the setting cost might be decreased depending on the next lot
            sett *next = GetSetting(m, end + 1);
            if (next != NULL && next->j == j && next->t == end + 1) x -= 1;

            // the unmet demand delta_u would be:
            float delta_u = 0.0;
            if (demand->value > I + q) 
            {
                delta_u = demand->value - I - q;
            }
            // the unmet future demand would be:
            float delta_f = 0.0;
            if (demand->due_t == T + 1) {
                delta_f = delta_u;
                delta_u = 0.0;
            }
            // the maximum inventory violation would be:
            float delta_k = GetMaxInventoryViolation(I, q, *demand);

            float cost_value = 
                this->param->omega_s * this->param->s * x +         // setting cost
                this->param->omega_u * this->param->c * delta_u +   // backlog cost for unmet demands
                this->param->omega_f * delta_f +                    // backlog cost for future demand
                this->param->omega_k * delta_k;                     // cost for max inventory overrun

            cost->m = m;
            cost->t = IND(t);
            cost->length = lot_size;
            cost->cost = cost_value;
        }
    }

    return cost;
}

float PartialPlan::GetMaxInventoryViolation(float I, float q, const dem &demand)
{
    float delta = 0.0;
    if (demand.due_t == T + 1)
    {
        // do not include future demand in inventory violation
        if (I + q > this->param->max_I[demand.j])
        {
            delta = I + q - this->param->max_I[demand.j];
        }
    }
    else 
    {
        if (I + q > demand.value && 
            I + q - demand.value > this->param->max_I[demand.j]) 
        {
            delta = I + q - demand.value - this->param->max_I[demand.j];
        }
    }
    return delta;
}

bool PartialPlan::InsertInGap(const ins_cost &cost, IND m, IND t)
{
    sett *next = NextSetting(m, t);
    IND end_t = T;
    if (next != NULL) end_t = next->t - 1;

    if (!Parameters::eq(cost.cost, Parameters::MAX_COST))
    {
        // check if insertion is from inventory
        if (cost.m != 0 && cost.t != 0 && cost.cost > 0.0) 
        {
            RemoveSetting(m, t);
        }

        IND free = free_T[m];
        bool inserted = Insert(cost);
        if (cost.t + cost.length - 1 < end_t)
        {
            // we should retain the rest of the gap
            AddSetting(m, cost.t + cost.length, this->param->J + 1);
        }
        // restore free_T[m]
        if (free_T[m] < free) free_T[m] = free;

        return inserted;
    }
    return false;
}

bool PartialPlan::Insert(const ins_cost &cost)
{
    IND j = cost.demand->j;

    if (cost.m == 0 && cost.t == 0 && 
        Parameters::eq(cost.cost, Parameters::MAX_COST))
    {
        // insertion cannot be performed, the whole demand remains unmet
        MarkDemandUnmet(*cost.demand);
        return true;
    }
    else
    {
        bool isZeroCost = cost.m == 0 && cost.t == 0 && Parameters::eq(cost.cost, 0.0);
        if (!isZeroCost) 
        {
            IND x = 0;
            sett *prev = GetSetting(cost.m, cost.t - 1);
            if (prev->j != j)
            {
                // perform a setting
                x = 1;
                AddSetting(cost.m, cost.t, j);
            }

            // produce according to the cost
            UpdatePositiveInventory(cost, x);
            // update the next free time period
            free_T[cost.m] = cost.t + cost.length;
        }

        // satisfy demand, either partially or the whole demand
        bool met = UpdateNegativeInventory(*cost.demand);
        if (met) MarkDemandMet(*cost.demand);
        return met;
    }
}

int PartialPlan::AddSetting(IND m, IND t, IND j)
{
    if (t == 0) return -1;
    sett *prev = GetSetting(m, t - 1);
    if (j != prev->j)
    {
        // handle any gaps first
        if (t > free_T[m])
        {
            // Option 1: simply add the falsch product (and handle in FillGaps)
            // Plan::AddSetting(m, free_T[m], this->param->J + 1);

            // Option 2: production cannot happen for any other item except the previouse one
            // so simply extend the previouse lot until last eligible period and account into inventory
            IND k = free_T[m];
            while (k < t)
            {
                if (Plan::IsConstrained(m, j, k) != -1) 
                {
                    // we can produce another item in period k
                    break;
                }
                k++;
            }

            int end = ConstrainedEnd(m, prev->j, free_T[m], k - 1); 
            if (end != -1)
            {
                Plan::UpdatePositiveInventory(m, prev->j, free_T[m], end, 0);
                if (end + 1 < t)
                {
                    // there is still a gap, fill it in with the falsch product
                    Plan::AddSetting(m, end + 1, this->param->J + 1);
                }
            }
            else 
            {
                Plan::AddSetting(m, free_T[m], this->param->J + 1);
            }
        }

        return Plan::AddSetting(m, t, j);
    }
    return -1;
}

void PartialPlan::UpdatePositiveInventory(const ins_cost &cost, IND x)
{
    IND end = cost.t + cost.length - 1;
    Plan::UpdatePositiveInventory(cost.m, cost.demand->j, cost.t, end, x);
}

PartialPlan *PartialPlan::Construct(Rand *rand) 
{
    DeleteExecutionData();
    // initialize settings, inventory and unmet demand
    ClearSettings();
    InitExecutionData();

    // construct a candidate list of demands for insertion
    vector<dem *> *CL = new vector<dem *>;
    // foreach RCL element store it's index in the CL
    IND *RCL = new IND[this->param->J];
    // insertion costs foreach element in CL
    ins_cost **costs = new ins_cost*[this->param->J];

    // Phase 1: work for external demands
    while (!DemandsProcessed(T/3 - 1))
    {
        // Step 1.1: Satisfy earliest demands first (initialize candidate list CL)
        float max_ccost = FetchDemands(*CL, T/3 - 1);

        // Step 1.2: Consume demands in CL by inserting production lots
        ConsumeDemandsFromInventory(*CL, max_ccost);   
        ConsumeDemands(*CL, costs, RCL, max_ccost, rand);
    }

    // Phase 2: work for future demands
    while (!DemandsProcessed(T/3, T/3))
    {
        float max_ccost = FetchDemands(*CL, T/3, T/3);
        ConsumeDemandsFromInventory(*CL, max_ccost);   
        ConsumeDemands(*CL, costs, RCL, max_ccost, rand);
    }
    MarkEndGaps();

    delete [] RCL;
    delete [] costs;
    delete CL;

    // Phase 3: fill in the gaps of product J + 1
    //PartialPlan *pplan = this->FillInGaps(false);
    //pplan = pplan->FillInGaps(true);
    return this->FillInGaps(true);
}

float PartialPlan::FetchDemands(vector<dem *> &CL, IND end)
{
    return FetchDemands(CL, 0, end);
}

float PartialPlan::FetchDemands(vector<dem *> &CL, IND start, IND end)
{
    float min_ccost = Parameters::MAX_COST;
    float max_ccost = 0;
    int index = start - 1;
    for (IND j = 1; j <= this->param->J; j++)
    {
        while ((index = NextUnmetDemandInd(j, index + 1, end)) != -1)
        {
            dem *d = &udemand[j][index];;
            float ccost = d->CandidateCost(this->param);
            if (ccost < min_ccost) min_ccost = ccost;
            if (ccost > max_ccost) max_ccost = ccost;
        }
    }

    // determine a treshold for candidate cost of the demands
    float threshold = min_ccost + this->param->get_beta()*(max_ccost - min_ccost);
    index = start - 1;
    for (IND j = 1; j <= this->param->J; j++)
    {
        while ((index = NextUnmetDemandInd(j, index + 1, end)) != -1)
        {
            dem *d = &udemand[j][index];
            float ccost = d->CandidateCost(this->param);
            if (ccost <= threshold) CL.push_back(d);
        }
    }
    return threshold;
}

void PartialPlan::ConsumeDemandsFromInventory(vector<dem *> &CL, float max_ccost)
{
    vector<dem *>::iterator it = CL.begin();
    while (it != CL.end())
    {
        // check if inventory is available first
        if (GetAvailableInventory((*it)->j, (*it)->due_t) > 0.0)
        {
            ins_cost *icost = BestCost(*it, &PartialPlan::InsertionCost);

            if (icost->m == 0 && icost->t == 0 && 
                Parameters::eq(icost->cost, 0.0))
            {
                bool inserted = Insert(*icost);
                if (inserted || icost->demand->CandidateCost(this->param) > max_ccost) 
                {
                    it = CL.erase(it);
                }
            }
            else it++;

            delete icost;
        }
        else it++;
    }
}

void PartialPlan::ConsumeDemands(vector<dem *> &CL, ins_cost **costs, IND *RCL, float max_ccost, Rand *r)
{
    while (!CL.empty())
    {
        float min_cost = Parameters::MAX_COST;
        float max_cost = 0.0;
        float min_dem = Parameters::MAX_COST;
        float max_dem = 0.0;
        // compute insertion costs for all demands in CL
        IND costs_size = IND(CL.size());
        for (IND i = 0; i < costs_size; i++)
        {
            ins_cost *ic = BestCost(CL[i], &PartialPlan::InsertionCost);

            if (ic->cost > max_cost) max_cost = ic->cost;
            if (ic->cost < min_cost) min_cost = ic->cost;

            if (ic->demand->value > max_dem) max_dem = ic->demand->value;
            if (ic->demand->value < min_dem) min_dem = ic->demand->value;
            
            costs[i] = ic;
        }

        // construct the new restricted candidate list RCL according to alpha
        float cost = min_cost + this->param->get_alpha() * (max_cost - min_cost);
        IND RCL_size = 0;
        for (IND i = 0; i < costs_size; i++)
        {
            if (costs[i]->cost <= cost) 
            {
                RCL[RCL_size] = i;
                RCL_size++;
            }
        }
        
        // select a demand randomly from RCL
        IND index = Parameters::random(r, RCL_size - 1);
        ins_cost *icost = costs[RCL[index]];
        
        // perform the insertion and remove demand d from CL if required
        bool inserted = Insert(*icost);
        // if demand was satisfied only partially its candidate cost will change (apply threshold of CL)
        if (inserted || icost->demand->CandidateCost(this->param) > max_ccost) 
        { 
            // ExportSettings(cout, true);

            vector<dem *>::iterator it = CL.begin();
            it = it + RCL[index];
            it = CL.erase(it);
        }

        for (IND i = 0; i < costs_size; i++) delete costs[i];
    }
}

void PartialPlan::MarkEndGaps()
{
    for (IND m = 1; m <= M; m++)
    {
        if (free_T[m] <= T)
        {
            // mark gaps in the end
            AddSetting(m, free_T[m], this->param->J + 1);
        }
    }
}

PartialPlan *PartialPlan::FillInGaps(bool finalize)
{
    // put all non-zero demands in CL
    vector<dem *> *CL = new vector<dem *>();
    // include future demands if finalizing
    IND end = finalize ? T/3 : T/3 - 1;
    FetchAllDemands(*CL, 0, end, &dem::IsZero);

    vector<PartialPlan *> *path = new vector<PartialPlan *>();
    PartialPlan *result = this;
    path->insert(path->begin(), result);

    // foreach gap (i.e. J + 1 lot) insert the best demand from CL
    bool gapFound = false;
    do
    {
        gapFound = false;
        for (IND m = 1; m <= M; m++)
        {
            for (IND i = 1; i < result->NumSettings(m); i++)
            {
                sett *curr = result->GetSettingAt(m, i);
                if (curr->j == this->param->J + 1)
                {
                    // mark the gap found only if you managed to do something with it
                    result = result->FinalizeGap(*path, *CL, m, curr->t);
                    if (result != NULL)
                    {
                        gapFound = true;
                        path->insert(path->begin(), result);
                    }
                    else
                    {
                        result = (*path)[0];
                        if (finalize)
                        {
                            if (result == this) 
                            {
                                // terminate the backtrack and clean up
                                Clear(path);
                                delete CL;
                                return result;
                            }
                            else 
                            {
                                // backtrack - put the current value at the end
                                path->push_back(result);
                                path->erase(path->begin());
                                result = (*path)[0];
                            }
                        }
                    }
                }
            }
        }
    } while (finalize && gapFound);

    // cleanup
    Clear(path);
    delete CL;
    return result;
}

bool PartialPlan::FillInGap(vector<dem *> &CL, IND m, IND t)
{
    // consume from inventory only if inventory is available before demand's due_time
    ConsumeDemandsFromInventory(CL, Parameters::MAX_COST);

    sett *next = NextSetting(m, t);
    IND end_t = T;
    if (next != NULL) end_t = next->t - 1;

    // ExportSettings(cout, true);
    ins_cost *best = BestCost(CL, m, t, end_t, &PartialPlan::ObjectiveCost);
    if (!Parameters::eq(best->cost, Parameters::MAX_COST))
    {
        bool inserted = InsertInGap(*best, m, t);
        if (inserted)
        {
            int index = IndexOf(*best->demand, CL);
            vector<dem *>::iterator it = CL.begin();
            it = it + index;
            it = CL.erase(it);
        }
        delete best;
        return true;
    }
    delete best;
    return false;
}

PartialPlan *PartialPlan::FinalizeGap(
    vector<PartialPlan *> &path, vector<dem *> &CL, IND m, IND t)
{
    IND count = 0;
    vector<PartialPlan *> *nhood = new vector<PartialPlan *>();

    count += ExtendInGap(*nhood, CL, m, t);
    count += ExchangeGap(*nhood, CL, m, t); 
    count -= FilterNhood(*nhood, path);
    
    if (nhood->empty()) 
    {
        // leave the gap in the final solution
        free_T[m] = Parameters::max(T + 1, free_T[m]);
        delete nhood;
        return NULL;
    }

    // place the best real objective value plan at position 0
    for (IND j = 1; j < nhood->size(); j++)
    { 
        if ((*nhood)[j]->Value() < (*nhood)[0]->Value()) 
        {
            PartialPlan *tmp = (*nhood)[j];
            (*nhood)[j] = (*nhood)[0];
            (*nhood)[0] = tmp;
        }
    }

    // clear the rest of nhood
    PartialPlan *best = (*nhood)[0];
    for (IND i = 1; i < nhood->size(); i++) delete (*nhood)[i];
    nhood->clear();
    delete nhood;

    return best;
}

IND PartialPlan::FilterNhood(vector<PartialPlan *> &nhood, vector<PartialPlan *> &path)
{
    IND count = 0;
    
    IND oldGaps = path[0]->CountSettingsForItem(this->param->J + 1);
    vector<PartialPlan *>::iterator it = nhood.begin();
    while (it != nhood.end())
    {
        if ((*it)->IsContainedIn(path))
        {
            delete *it;
            it = nhood.erase(it);
            count++;
            continue;
        }

        IND gaps = (*it)->CountSettingsForItem(this->param->J + 1);
        if (gaps > oldGaps)
        {
            delete *it;
            it = nhood.erase(it);
            count++;
            continue;
        }
        
        it++;
    }

    return count;
}

IND PartialPlan::ExtendInGap(
    vector<PartialPlan *> &nhood, vector<dem *> &CL, IND m, IND t)
{
    IND count = 0;

    // fill in inventory by extending previouse/next lot
    PartialPlan *plan = new PartialPlan(*this);
    if (plan->FillInGap(CL, m, t)) 
    {
        count++;
        nhood.push_back(plan);
        plan = new PartialPlan(*this);
    }
    int ext_res;
    if ((ext_res = plan->ExtendPrevLot(m, t, T)) != -1) 
    {
        plan->free_T[m] = Parameters::max(ext_res + 1, plan->free_T[m]);
        count++;
        nhood.push_back(plan);
        plan = new PartialPlan(*this);
    }
    if ((ext_res = plan->ExtendNextLot(m, t, t)) != -1)
    {
        count++;
        nhood.push_back(plan);
        plan = new PartialPlan(*this);
    }
    delete plan;

    return count;
}

IND PartialPlan::ExchangeGap(
    vector<PartialPlan *> &nhood, vector<dem *> &CL, IND m, IND t)
{
    IND count = 0;
    // try exchanging the setting with another setting in time period t
    int n_index;
    sett *nsett;
    for (IND n = 1; n <= M; n++)
    {
        if (n == m) continue;
        
        n_index = FindSettIndex(n, t);
        if (n_index != -1)
        {
            nsett = GetSettingAt(n, n_index);
            if (nsett != NULL)
            {
                count += ExchangeGap(nhood, CL, m, t, n, nsett->t);
            }

            nsett = PrevSettingAt(n, n_index);
            if (nsett != NULL)
            {
                count += ExchangeGap(nhood, CL, m, t, n, nsett->t);
            }

            nsett = NextSettingAt(n, n_index);
            if (nsett != NULL)
            {
                count += ExchangeGap(nhood, CL, m, t, n, nsett->t);
            }
        }
    }
    return count;
}

IND PartialPlan::ExchangeGap(vector<PartialPlan *> &nhood, 
    vector<dem *> &CL, IND m, IND m_t, IND n, IND n_t)
{
    IND count = 0;

    int m_index = FindSettIndex(m, m_t);
    sett *msett = GetSettingAt(m, m_index);
    sett *nsett = GetSetting(n, n_t);
    if (msett != NULL && nsett != NULL)
    {
        // exchange the gap itself
        PartialPlan *plan = new PartialPlan(*this);
        if (nsett->j != this->param->J + 1 && nsett->t == m_t)
        {
            if (plan->ExchangeLotsHeads(n, nsett->t, m, msett->t) &&
                plan->FillInGap(CL, n, nsett->t))
            {
                count++;
                nhood.push_back(plan);
                plan = new PartialPlan(*this);
            }
        }

        // exchange the previouse lot of the gap
        sett *mprev = PrevSettingAt(m, m_index);
        if (mprev != NULL && nsett->j != mprev->j)
        {
            // exchanging with the end of the lots makes more sence
            if (plan->ExchangeLotsHeadForTail(n, nsett->t, m, mprev->t) &&
                plan->FillInGap(CL, m, m_t))
            {
                count++;
                nhood.push_back(plan);
                plan = new PartialPlan(*this);
            }
            
            if (plan->ExchangeLotsTails(n, nsett->t, m, mprev->t) &&
                plan->FillInGap(CL, m, m_t))
            {
                count++;
                nhood.push_back(plan);
                plan = new PartialPlan(*this);
            }
        }

        // exchange the next lot of the gap
        sett *mnext = NextSettingAt(m, m_index);
        if (mnext != NULL && nsett->j != mnext->j)
        {
            // exchanging with the end of the lots makes more sence
            if (plan->ExchangeLotsHeads(n, nsett->t, m, mnext->t) &&
                plan->FillInGap(CL, m, m_t))
            {
                count++;
                nhood.push_back(plan);
                plan = new PartialPlan(*this);
            }

            if (plan->ExchangeLotsHeadForTail(m, mnext->t, n, nsett->t) &&
                plan->FillInGap(CL, m, m_t))
            {
                count++;
                nhood.push_back(plan);
                plan = new PartialPlan(*this);
            }
        }

        delete plan;
    }

    return count;
}

void PartialPlan::Clear(vector<PartialPlan *> *data)
{
    for (IND i = 1; i < data->size(); i++) 
    {
        delete (*data)[i];
    }
    delete data;
}

int PartialPlan::FetchNextDemand(IND j, std::vector<dem *> &CL)
{
    return FetchNextDemand(j, CL, 0);
}

int PartialPlan::FetchNextDemand(IND j, std::vector<dem *> &CL, IND dem_ind)
{
    if (dem_ind <= T/3)
    {
        int index = NextUnmetDemandInd(j, dem_ind);
        if (index != -1)
        {
            dem *next = &udemand[j][index];
            if (next->value > 0.0)
            {
                // check if this is a constrained demand
                int constrained = IsConstrained(*next);
                if (constrained == 1)
                {
                    CL.push_back(next);
                    // dem_index[j] = index + 1;
                    return index;
                }
                else if (constrained == -1)
                {
                    // leave this demand unsatisfied
                    MarkDemandUnmet(*next);
                    // cout << this->ToString() << endl;
                } 
                else 
                {
                    // demand is not constrained now, try later
                }
            }
        }
    }

    return -1;
}

int PartialPlan::IsConstrained(const dem &d) 
{
    bool dueTimePassed = true;
    int constrained = 0;
    for (IND m = 1; m <= M; m++)
    {
        if (this->param->cap[m][d.j] > 0)
        {
            int start = ConstrainedStart(m, d.j, free_T[m], d.due_t);
            if (start >= free_T[m] && start <= d.due_t)
            {
                // there exists at least one machine m and one period t, 
                // when the due time of the demand is not passed and production is possible
                dueTimePassed = false;
                constrained = 1;
                break;
            }
        }
    }

    // if the due time have passed for all machine, where production is possible, we should skip the demand
    if (dueTimePassed) constrained = -1;
    return constrained;
}

bool PartialPlan::IsBusy(IND m, IND t)
{
    return t > 0 && t < free_T[m];
}

bool PartialPlan::IsComplete()
{
    bool complete = true;
    for (IND m = 1; m <= M; m++)
    {
        if (free_T[m] <= T) 
        {
            complete = false;
            break;
        }
    }

    return complete;
}

bool PartialPlan::IsContainedIn(const vector<PartialPlan *> &list) const
{
    bool contained = false;
    for (IND i = 0; i < list.size(); i++)
    {
        if (list[i] != NULL && this->Equal(*list[i]))
        {
            contained = true;
            break;
        }
    }
    return contained;
}

IND PartialPlan::IsSetupFor(IND j)
{
    IND machine = 0;
    IND min_t = T + 2;
    for (IND m = 1; m <= M; m++)
    {
        sett *prev = this->GetSetting(m, free_T[m] - 1);

        if (prev->j == j && free_T[m] <= min_t)
        {
            min_t = free_T[m];
            machine = m;
        }

    }
    return machine;
}

float PartialPlan::EvaluateBufferOverrun(IND j) const
{
    float cost = 0.0;
    if (j >= 1 && j <= this->param->J)
    {
        float I = GetAvailableInventory(j, T);
        if (I > this->param->max_I[j])
        {
            // max inventory overrun
            cost += this->param->omega_k * (I - this->param->max_I[j]);
        }
    }
    return cost;
}

void PartialPlan::ExportSettings(ostream &outs, bool zeros) 
{
    Plan::ExportSettingsHead(outs);
    for (IND m = 1; m <= M; m++)
    {
        outs << Parameters::TabbedSpace(m) << "|";
        IND t = 0;
        for (int index = 0; index < NumSettings(m); index++)
        {
            sett* curr = GetSettingAt(m, index);
            sett* next = NextSettingAt(m, index);
            
            IND end = T;
            if (next != NULL) end = next->t - 1;

            t = curr->t;
            while (t <= end && t < free_T[m] && t <= T)
            {
                if (zeros && t > curr->t) 
                    outs << Parameters::TabbedSpace((IND)0);
                else 
                    outs << Parameters::TabbedSpace(curr->j);
                t++;
            }
        }
        outs << endl;
    }
    Plan::ExportSettingsFoot(outs);
}

void PartialPlan::DeleteExecutionData()
{
    if (executing)
    {
        delete [] free_T;
    }
    Plan::DeleteExecutionData();
}

PartialPlan::~PartialPlan()
{
    if (executing)
    {
        delete [] free_T;
    }
}

}