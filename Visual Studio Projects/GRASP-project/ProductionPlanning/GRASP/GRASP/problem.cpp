#include "stdafx.h"
#include "problem.h"


namespace planning {

Plan::Plan(Parameters& param) : 
    metaheuristic::Solution(param)
{
    this->M = param.M;
    this->T = param.T;
    init_settings(param);
    this->executing = false;
    this->totalBadQ = float(0);
}

Plan::Plan(const Plan& p) : 
    metaheuristic::Solution(p)
{
    this->M = p.M;
    this->T = p.T;
    init_settings(p);
    if (p.executing)
    {
        init_pos_I(p);
        init_udemand(p);
    }
    this->executing = p.executing;
    this->totalBadQ = p.totalBadQ;
}

Plan::Plan(const Plan& p, bool copyExecData) : 
    metaheuristic::Solution(p)
{
    this->M = p.M;
    this->T = p.T;
    init_settings(p);
    if (copyExecData)
    {
        if (p.executing)
        {
            init_pos_I(p);
            init_udemand(p);
        }
        this->executing = p.executing;
        this->totalBadQ = p.totalBadQ;
    }
    else 
    {
        this->executing = false;
        this->totalBadQ = float(0);
    }
}

void Plan::InitExecutionData()
{
    init_pos_I(*this->param);
    init_udemand(*this->param);
    this->executing = true;
}

metaheuristic::Solution* Plan::Clone(bool copy_exec_data) const
{
    return new Plan(*this, copy_exec_data);
}

Plan::~Plan()
{
    delete [] this->vsett;
    delete [] this->settings;
    if (this->executing)
    {
        delete [] this->udemand_data;
        delete [] this->udemand;
        delete [] this->pos_I;
        delete [] this->pos_I_data;

        this->executing = false;
    }
}

void Plan::init_settings(const Parameters &input)
{
    vsett = new vector<sett>[M + 1];
    settings = new sett*[M + 1];

    sett init_sett;
    vsett[0].push_back(init_sett);
    settings[0] = &vsett[0][0];
    for (IND m = 1; m <= M; m++)
    {
        sett copy_sett;
        copy_sett.m = m;
        copy_sett.t = 0;
        copy_sett.j = input.init_setup[m];
        vsett[m].push_back(copy_sett);
        settings[m] = &vsett[m][0];
    }
}

void Plan::init_settings(const Plan &p)
{
    vsett = new vector<sett>[M + 1];
    settings = new sett*[M + 1];
    
    sett init_sett;
    vsett[0].push_back(init_sett);
    settings[0] = &vsett[0][0];
    for (IND m = 1; m <= M; m++)
    {
        for (IND k = 0; k < p.NumSettings(m); k++)
        {
            sett *p_sett = p.GetSettingAt(m, k);
            sett copy_sett;
            copy_sett.m = p_sett->m;
            copy_sett.t = p_sett->t;
            copy_sett.j = p_sett->j;
            vsett[m].push_back(copy_sett);
        }
        settings[m] = &vsett[m][0];
    }
}

void Plan::init_udemand(const Parameters &input)
{
    int cols = input.T/3 + 1;
    udemand = new dem*[input.J + 1];
    udemand_data = new dem[(input.J + 1) * cols];
    for (IND j = 1; j <= input.J; j++)
    {
        for (IND day = 0; day < cols; day++)
        {
            IND index = j*cols + day;
            udemand_data[index].j = j;
            udemand_data[index].value = input.demand[j][day];
            if (day == input.T/3)
            {
                udemand_data[index].due_t = 3*day + 1;
            }
            else 
            {
                udemand_data[index].due_t = 3*(day + 1);
            }

            if (udemand_data[index].value > 0.0)
            {
                udemand_data[index].processed = false;
            }
            else
            {
                udemand_data[index].processed = true;
            }
        }
    }

    // initialize two dimmensional array by rows
    dem *index = udemand_data;
    for (IND j = 0; j < input.J + 1; j++)
    {
        udemand[j] = index;
        index += cols;
    }
}

void Plan::init_udemand(const Plan &p)
{
    int cols = p.T/3 + 1;
    this->udemand = new dem*[p.param->J + 1];
    this->udemand_data = new dem[(p.param->J + 1) * cols];
    for (IND j = 1; j <= p.param->J; j++)
    {
        for (IND day = 0; day < cols; day++)
        {
            IND index = j*cols + day;
            this->udemand_data[index].j = p.udemand[j][day].j;
            this->udemand_data[index].due_t = p.udemand[j][day].due_t;
            this->udemand_data[index].value = p.udemand[j][day].value;
            this->udemand_data[index].processed = p.udemand[j][day].processed;
        }
    }

    // initialize two dimmensional array by rows
    dem *index = this->udemand_data;
    for (IND j = 0; j < p.param->J + 1; j++)
    {
        this->udemand[j] = index;
        index += cols;
    }
}

void Plan::init_I(const Parameters &input, float *I) 
{
    I[0] = 0;
    for (IND j = 1; j <= input.J; j++)
    {
        I[j] = input.init_I[j];
    }
}

void Plan::init_pos_I(const Parameters &input)
{
    int cols = input.T/3 + 1;
    pos_I = new float*[input.J + 1];
    pos_I_data = new float[(input.J + 1) * cols];
    for (IND j = 1; j <= this->param->J; j++)
    {
        for (IND day = 0; day < cols; day++)
        {
            IND index = j * cols + day;
            if (day == 0)
            {
                this->pos_I_data[index] = this->param->init_I[j];
            }
            else
            {
                this->pos_I_data[index] = 0.0;
            }
        }
    }

    // initialize two dimmensional array by rows
    float *index = pos_I_data;
    for (IND j = 0; j < input.J + 1; j++)
    {
        pos_I[j] = index;
        index += cols;
    }
}

void Plan::init_pos_I(const Plan &p)
{
    int cols = p.T/3 + 1;
    this->pos_I = new float*[p.param->J + 1];
    this->pos_I_data = new float[(p.param->J + 1) * cols];
    for (IND j = 1; j <= p.param->J; j++)
    {
        for (IND day = 0; day < cols; day++)
        {
            IND index = j * cols + day;
            this->pos_I_data[index] = p.pos_I[j][day];
        }
    }

    // initialize two dimmensional array by rows
    float *index = this->pos_I_data;
    for (IND j = 0; j < p.param->J + 1; j++)
    {
        this->pos_I[j] = index;
        index += cols;
    }
}

IND Plan::NumSettings(IND m) const
{
    return IND(vsett[m].size());
}

IND Plan::GetItem(IND m, IND t)
{
    // find the setting
    sett *found = GetSetting(m, t);
    if (found == NULL)
    {
        return 0;
    }
    else
    {
        return found->j;
    }
}

int Plan::FindSettIndex(IND m, IND t) const 
{
    int index = -1;
    // perform a binary search
    IND count = NumSettings(m);
    if (t >= 0 && count > 0)
    {
        IND first = 0;
        IND last = count - 1;
        while (first <= last) 
        {
            int mid = (first + last) / 2;
            if (t < settings[m][mid].t) 
            {
                if (mid > 0) last = mid - 1; // repeat search in bottom half.
                else break;
            }
            else if (t > settings[m][mid].t) 
            {
                if (mid + 1 == count || 
                    t < settings[m][mid + 1].t)
                {
                    index = mid;
                    break;
                }
                else
                {
                    first = mid + 1;  // repeat search in top half.
                }
            }
            else
            {
                index = mid;
                break;
            }
        }
    }
    return index;
}

sett *Plan::GetSetting(IND m, IND t) const
{
    sett *s = NULL;
    if (m > 0 && m <= M)
    {
        int index = FindSettIndex(m, t);
        if (index >= 0 && index < NumSettings(m))
        {
            s = &settings[m][index];
        }
    }
    return s;
}

sett *Plan::GetSettingAt(IND m, IND index) const
{
    sett *s = NULL;
    if (m > 0 && m <= M && 
        index >= 0 && index < NumSettings(m))
    {
        s = &settings[m][index];
    }
    return s;
}

sett *Plan::NextSetting(IND m, IND t) const
{
    sett *next = NULL;
    if (m > 0 && m <= M)
    {
        int index = FindSettIndex(m, t);
        if (index != -1 && index + 1 < NumSettings(m))
        {
            next = &settings[m][index + 1];
        }
    }
    return next;
}

sett *Plan::NextSettingAt(IND m, IND index) const
{
    sett *next = NULL;
    if (m > 0 && m <= M && 
        index >= 0 && index < NumSettings(m) &&
        index + 1 < NumSettings(m))
    {
        next = &settings[m][index + 1];
    }
    return next;
}

sett *Plan::PrevSetting(IND m, IND t) const
{
    sett *prev = NULL;
    if (m > 0 && m <= M)
    {
        int index = FindSettIndex(m, t);
        if (index != -1 && index - 1 >= 0)
        {
            prev = &settings[m][index - 1];
        }
    }
    return prev;
}

sett *Plan::PrevSettingAt(IND m, IND index) const
{
    sett *prev = NULL;
    if (m > 0 && m <= M && 
        index >= 0 && index < NumSettings(m) &&
        index - 1 >= 0)
    {
        prev = &settings[m][index - 1];
    }
    return prev;
}

int Plan::AddSetting(IND m, IND t, IND j)
{
    return AddSetting(m, t, j, true);
}

int Plan::AddSetting(IND m, IND t, IND j, bool checkNext)
{
    int result = -1;
    if (m > 0 && m <= M && 
        j > 0 && j <= this->param->J && 
        t > 0 && t <= T)
    {
        sett new_sett;
        new_sett.m = m;
        new_sett.j = j;
        new_sett.t = t;

        int index = FindSettIndex(m, t);
        vector<sett>::iterator pos;
        if (index != -1)
        {
            // check if the next setting is for the same item
            if (checkNext && index + 1 < NumSettings(m) && 
                settings[m][index + 1].j == new_sett.j)
            {
                pos = vsett[m].begin() + (index + 1);
                vsett[m].erase(pos);
            }

            // check the previouse setting
            if (settings[m][index].j != new_sett.j)
            {
                if (settings[m][index].t == new_sett.t) 
                {
                    pos = vsett[m].begin() + index;
                    pos = vsett[m].erase(pos);
                    result = index;
                }
                else if (settings[m][index].t < new_sett.t) 
                {
                    pos = vsett[m].begin() + (index + 1);
                    result = index + 1;
                }
                pos = vsett[m].insert(pos, new_sett);
            }
        }
        else 
        {
            result = 0;
            vsett[m].push_back(new_sett);
        }
        settings[m] = &vsett[m][0];
    }
    return result;
}

bool Plan::RemoveSetting(IND m, IND t)
{
    int index = FindSettIndex(m, t);
    if (index != -1 && vsett[m][index].t == t)
    {
        return RemoveSettingAt(m, index);
    }
    else return false;
}

bool Plan::RemoveSettingAt(IND m, IND index)
{
    if (m > 0 && m <= M && 
        index > 0 && index < NumSettings(m))
    {
        vector<sett>::iterator pos = vsett[m].begin();
        pos = pos + index;
        pos = vsett[m].erase(pos);
        
        settings[m] = &vsett[m][0];
        return true;
    }
    else return false;
}

void Plan::ClearSettings()
{
    for (IND m = 1; m <= M; m++) ClearSettings(m);
}

void Plan::ClearSettings(IND m)
{
    if (m >= 1 && m <= M)
    {
        for (size_t index = vsett[m].size() - 1; index > 0; index--)
        {
            vsett[m].erase(vsett[m].begin() + index);
        }

        settings[m] = &vsett[m][0];
    }
}

IND Plan::CountSettings(IND t) const
{
    IND numSetts = 0;
    for (IND m = 1; m <= M; m++)
    {
        sett *m_sett = GetSetting(m, t);
        if (m_sett != NULL && m_sett->t == t && 
            m_sett->j != this->param->J + 1) numSetts++;
    }
    return numSetts;
}

IND Plan::CountSettingsForItem(IND j) const
{
    IND numSetts = 0;
    for (IND m = 1; m <= M; m++)
    {
        for (IND index = 0; index < NumSettings(m); index++)
        {
            sett *m_sett = GetSettingAt(m, index);
            if (m_sett->j == j) numSetts++;
        }
    }
    return numSetts;
}

bool Plan::AppendSetting(IND m, IND t, IND j)
{
    sett new_sett;
    new_sett.m = m;
    new_sett.t = t;
    new_sett.j = j;

    IND lastIndex = NumSettings(m) - 1;
    if (settings[m][lastIndex].t > new_sett.t)
    {
        return false;
    } 
    
    if (settings[m][lastIndex].j != new_sett.j)
    {
        if (settings[m][lastIndex].t == new_sett.t)
        {
            // replace last setting if products are different
            vsett[m].pop_back();
            vsett[m].push_back(new_sett);
        }
        else 
        {
            // append at the end, only if it is different from the previouse sett
            vsett[m].push_back(new_sett);
        }
    }
    return true;
}

float Plan::GetProduction(IND m, IND j, IND lot_size, IND x)
{
    float q = 0.0;
    if (j >= 1 && j <= this->param->J && 
        this->param->cap[m][j] > 0)
    {
         q = this->param->cap[m][j] * (lot_size - x * this->param->v);
    }
    return q;
}

bool Plan::ExchangeLotsHeads(IND m, IND m_t, IND n, IND n_t) 
{
    int m_index = FindSettIndex(m, m_t);
    int n_index = FindSettIndex(n, n_t);
    if (m_index != -1 && n_index != -1)
    {
        sett *msett = GetSettingAt(m, m_index);
        sett *nsett = GetSettingAt(n, n_index);

        if (msett->t == m_t && nsett->t == n_t &&
            (msett->j == this->param->J + 1 || this->param->cap[n][msett->j] > 0) && 
            (nsett->j == this->param->J + 1 || this->param->cap[m][nsett->j] > 0))
        {
            IND j_m = msett->j;
            IND j_n = nsett->j;

            sett *mnext = NextSettingAt(m, m_index);
            sett *nnext = NextSettingAt(n, n_index);
            IND mend = mnext == NULL ? T : mnext->t - 1;
            IND nend = nnext == NULL ? T : nnext->t - 1;

            IND overlap_start = Parameters::max(m_t, n_t);
            IND overlap_end = Parameters::min(mend, nend);
            
            // handle initial settings and check the constrained start and end
            int mcstart;
            if (m_t == 0 && j_m != j_n) mcstart = (j_n == this->param->J + 1) ? 
                m_t + 1 : ConstrainedStart(m, j_n, m_t + 1, m_t + 1);
            else mcstart = (j_n == this->param->J + 1 || m_t == n_t) ? 
                m_t : ConstrainedStart(m, j_n, m_t, m_t);
            int ncstart;
            if (n_t == 0 && j_n != j_m) ncstart = (j_m == this->param->J + 1) ?  
                n_t + 1 : ConstrainedStart(n, j_m, n_t + 1, n_t + 1);
            else ncstart = (j_m == this->param->J + 1 || m_t == n_t) ? 
                n_t : ConstrainedStart(n, j_m, n_t, n_t);
            if (mcstart != m_t && !(m_t == 0 && mcstart == 1)) return false;
            if (ncstart != n_t && !(n_t == 0 && ncstart == 1)) return false;

            int mcend = (j_n == this->param->J + 1) ? 
                mend : ConstrainedEnd(m, j_n, mcstart, mend, overlap_start, overlap_end);
            int ncend = (j_m == this->param->J + 1) ? 
                nend : ConstrainedEnd(n, j_m, ncstart, nend, overlap_start, overlap_end);
            if (mcend == -1 || ncend == -1) return false;

            int lot_size = Parameters::min(mcend - mcstart + 1, ncend - ncstart + 1);
            IND j_mnext = mnext == NULL ? 0 : mnext->j;
            IND j_nnext = nnext == NULL ? 0 : nnext->j;
            do
            {
                mcend = mcstart + lot_size - 1;
                ncend = ncstart + lot_size - 1;

                // exchange the settings
                if (mcstart == m_t) RemoveSetting(m, mcstart);
                if (ncstart == n_t) RemoveSetting(n, ncstart);
                AddSetting(m, mcstart, j_n, mcend == mend);
                AddSetting(n, ncstart, j_m, ncend == nend);
                if (mcend < mend) AddSetting(m, mcend + 1, j_m);
                if (ncend < nend) AddSetting(n, ncend + 1, j_n);

                if (IsConstrainedAt(m, m_t, mend) != 1 || 
                    IsConstrainedAt(n, n_t, nend) != 1) 
                {
                    // revert the changes
                    if (mcend < mend) RemoveSetting(m, mcend + 1);
                    if (ncend < nend) RemoveSetting(n, ncend + 1);
                    RemoveSetting(m, mcstart);
                    RemoveSetting(n, ncstart);
                    if (mcstart == m_t) AddSetting(m, mcstart, j_m);
                    if (ncstart == n_t) AddSetting(n, ncstart, j_n);
                    if (mcend == mend && j_mnext == j_n) AddSetting(m, mend + 1, j_n);
                    if (ncend == nend && j_nnext == j_m) AddSetting(n, nend + 1, j_m);
                }
                else return true;

                lot_size--;
            }
            while (lot_size > 0);
        }
    }
    return false;
}

bool Plan::ExchangeLotsTails(IND m, IND m_t, IND n, IND n_t) 
{
    int m_index = FindSettIndex(m, m_t);
    int n_index = FindSettIndex(n, n_t);
    if (m_index != -1 && n_index != -1)
    {
        sett *msett = GetSettingAt(m, m_index);
        sett *nsett = GetSettingAt(n, n_index);

        if (msett->t == m_t && nsett->t == n_t &&
            (msett->j == this->param->J + 1 || this->param->cap[n][msett->j] > 0) && 
            (nsett->j == this->param->J + 1 || this->param->cap[m][nsett->j] > 0))
        {
            IND j_m = msett->j;
            IND j_n = nsett->j;

            sett *mnext = NextSettingAt(m, m_index);
            sett *nnext = NextSettingAt(n, n_index);
            IND mend = mnext == NULL ? T : mnext->t - 1;
            IND nend = nnext == NULL ? T : nnext->t - 1;

            IND overlap_start = Parameters::max(m_t, n_t);
            IND overlap_end = Parameters::min(mend, nend);
            
            // check the constrained start and end
            int mcstart;
            if (m_t == 0 && j_m != j_n) mcstart = (j_n == this->param->J + 1) ? 
                m_t + 1 : ConstrainedTailStart(m, j_n, m_t + 1, mend, overlap_start, overlap_end);
            else mcstart = (j_n == this->param->J + 1) ? 
                m_t : ConstrainedTailStart(m, j_n, m_t, mend, overlap_start, overlap_end);
            int ncstart;
            if (n_t == 0 && j_n != j_m) ncstart = (j_m == this->param->J + 1) ? 
                n_t + 1 : ConstrainedTailStart(n, j_m, n_t + 1, nend, overlap_start, overlap_end);
            else ncstart = (j_m == this->param->J + 1) ? 
                n_t : ConstrainedTailStart(n, j_m, n_t, nend, overlap_start, overlap_end);
            if (mcstart == -1 || ncstart == -1) return false;

            int mcend = mend;
            int ncend = nend;

            // TODO: handle different production capacities of machines m and n
            int lot_size = Parameters::min(mcend - mcstart + 1, ncend - ncstart + 1);
            IND j_mnext = mnext == NULL ? 0 : mnext->j;
            IND j_nnext = nnext == NULL ? 0 : nnext->j;
            do
            {
                mcstart = mcend - lot_size + 1;
                ncstart = ncend - lot_size + 1;
                if (ncstart == -1 || mcstart == -1)
                {
                    lot_size--;
                    continue;
                }
                if (mcstart > mcend || ncstart > ncend) return false;

                // exchange the settings
                if (mcstart == m_t) RemoveSetting(m, m_t);
                if (ncstart == n_t) RemoveSetting(n, n_t);
                AddSetting(m, mcstart, j_n);
                AddSetting(n, ncstart, j_m);

                if (IsConstrainedAt(m, m_t, mend) != 1 || 
                    IsConstrainedAt(n, n_t, nend) != 1) 
                {
                    // revert the changes
                    RemoveSetting(m, mcstart);
                    RemoveSetting(n, ncstart);
                    if (mcstart == m_t) AddSetting(m, m_t, j_m);
                    if (ncstart == n_t) AddSetting(n, n_t, j_n);
                    if (j_mnext == j_n) AddSetting(m, mend + 1, j_n);
                    if (j_nnext == j_m) AddSetting(n, nend + 1, j_m);
                }
                else return true;

                lot_size--;
            }
            while (lot_size > 0);
        }
    }
    return false;
}

bool Plan::ExchangeLotsHeadForTail(IND m, IND m_t, IND n, IND n_t)
{
    int m_index = FindSettIndex(m, m_t);
    int n_index = FindSettIndex(n, n_t);
    if (m_index != -1 && n_index != -1)
    {
        sett *msett = GetSettingAt(m, m_index);
        sett *nsett = GetSettingAt(n, n_index);

        if (msett->t == m_t && nsett->t == n_t &&
            (msett->j == this->param->J + 1 || this->param->cap[n][msett->j] > 0) && 
            (nsett->j == this->param->J + 1 || this->param->cap[m][nsett->j] > 0))
        {
            IND j_m = msett->j;
            IND j_n = nsett->j;

            sett *mnext = NextSettingAt(m, m_index);
            sett *nnext = NextSettingAt(n, n_index);
            IND mend = mnext == NULL ? T : mnext->t - 1;
            IND nend = nnext == NULL ? T : nnext->t - 1;

            IND overlap_start = Parameters::max(m_t, n_t);
            IND overlap_end = Parameters::min(mend, nend);
            
            // check the constrained start and end
            int mcstart;
            if (m_t == 0 && j_m != j_n) mcstart = (j_n == this->param->J + 1) ? 
                m_t + 1 : ConstrainedStart(m, j_n, m_t + 1, m_t + 1);
            else mcstart = (j_n == this->param->J + 1) ? 
                m_t : ConstrainedStart(m, j_n, m_t, m_t);
            int ncstart;
            if (n_t == 0 && j_n != j_m) ncstart = (j_m == this->param->J + 1) ? 
                n_t + 1 : ConstrainedTailStart(n, j_m, n_t + 1, nend, overlap_start, overlap_end);
            else ncstart = (j_m == this->param->J + 1) ? 
                n_t : ConstrainedTailStart(n, j_m, n_t, nend, overlap_start, overlap_end);
            if (mcstart != m_t && !(m_t == 0 && mcstart == 1)) return false;
            if (mcstart == -1 || ncstart == -1) return false;

            int mcend = (j_n == this->param->J + 1) ? 
                mend : ConstrainedEnd(m, j_n, mcstart, mend, overlap_start, overlap_end);
            int ncend = nend;
            if (mcend == -1) return false;

            // TODO: handle different production capacities of machines m and n
            int lot_size = Parameters::min(mcend - mcstart + 1, ncend - ncstart + 1);
            IND j_mnext = mnext == NULL ? 0 : mnext->j;
            IND j_nnext = nnext == NULL ? 0 : nnext->j;
            do
            {
                mcend = mcstart + lot_size - 1;
                ncstart = ncend - lot_size + 1;
                if (mcstart > mcend || ncstart > ncend) return false;

                // exchange the settings
                if (mcstart == m_t) RemoveSetting(m, m_t);
                if (ncstart == n_t) RemoveSetting(n, n_t);
                AddSetting(m, mcstart, j_n, mcend == mend);
                AddSetting(n, ncstart, j_m);
                if (mcend < mend) AddSetting(m, mcend + 1, j_m);

                if (IsConstrainedAt(m, m_t, mend) != 1 || 
                    IsConstrainedAt(n, n_t, nend) != 1) 
                {
                    // revert the changes
                    if (mcend < mend) RemoveSetting(m, mcend + 1);
                    RemoveSetting(m, mcstart);
                    RemoveSetting(n, ncstart);
                    if (mcstart == m_t) AddSetting(m, m_t, j_m);
                    if (ncstart == n_t) AddSetting(n, n_t, j_n);
                    if (mcend == mend && 
                        j_mnext == j_n) AddSetting(m, mend + 1, j_n);
                    if (j_nnext == j_m) AddSetting(n, nend + 1, j_m);
                }
                else return true;
    
                lot_size--;
            }
            while (lot_size > 0);
        }
    }
    return false;
}


int Plan::ExtendPrevLot(IND m, IND t, IND max_t)
{
    // Pre: there is a setting on machine m in time period t
    int index = FindSettIndex(m, t);
    if (index != -1)
    {
        sett *curr = GetSettingAt(m, index);
        sett *prev = PrevSettingAt(m, index);
        if (curr != NULL && prev != NULL && curr->t == t)
        {
            // find the end of the current lot
            IND end_t = T;
            sett *next = NextSettingAt(m, index);
            if (next != NULL) end_t = next->t - 1;

            int end = ConstrainedEnd(m, prev->j, curr->t, Parameters::min(end_t, max_t));
            if (end >= curr->t)
            {  
                bool retain = end < end_t;
                // check if retain is possible
                while (retain && end >= curr->t) 
                {
                    // currently previouse setting is the same
                    if (IsConstrained(m, curr->j, end + 1, false, false) != 1) end--;
                    else break;
                }

                if (end >= curr->t)
                {
                    IND curr_j = curr->j;
                    if (executing)
                    {
                        UpdatePositiveInventory(m, prev->j, curr->t, end, 0);
                    }
                    RemoveSettingAt(m, index);
                    // rertain the current lot
                    if (retain) AddSetting(m, end + 1, curr_j);
                    return end;
                }
            }
        }
    }
    return -1;
}

int Plan::ExtendNextLot(IND m, IND t, IND min_t)
{
    // Pre: there is a setting on machine m in time period t
    int index = FindSettIndex(m, t);
    if (index != -1)
    {
        sett *curr = GetSettingAt(m, index);
        sett *next = NextSettingAt(m, index);
        if (curr != NULL && next != NULL && curr->t == t)
        {
            IND end_t = next->t - 1;

            // fill in inventory by extending next lot
            IND min = Parameters::max(curr->t, min_t);
            int start = ConstrainedTailStart(m, next->j, min, end_t);
            if (start >= curr->t)
            {
                IND next_j = next->j;
                if (start == curr->t) RemoveSettingAt(m, index);
                AddSetting(m, start, next_j);
                if (executing)
                {
                    UpdatePositiveInventory(m, next_j, start, end_t, 0);
                }
                return start;
            }
        }
    }
    return -1;
}

int Plan::InsertLotHead(IND m, IND j, IND t, IND end_t)
{
    // check if this is feasible
    int start = ConstrainedStart(m, j, t, end_t);
    if (start == t)
    {
        int end = ConstrainedEnd(m, j, t, end_t);
        if (end >= t)
        {
            // check if you have to retain the last lot
            int index = FindSettIndex(m, end);
            sett *last = GetSettingAt(m, index);
            IND last_j = last->j;
            sett *next = NextSettingAt(m, index);
            IND last_t = next == NULL ? T : next->t - 1;
            if (end < last_t && IsConstrained(m, last_j, end + 1, false, false) != 1)
            {
                return -1;
            }
            
            // insertion is constrained, remove the intermediate settings
            index = FindSettIndex(m, start);
            sett *s = NextSettingAt(m, index);
            while (s != NULL && s->t >= start && s->t <= end)
            {
                RemoveSettingAt(m, index + 1);
                s = NextSettingAt(m, index);
            }
            
            s = GetSettingAt(m, index);
            if (s->t == start) RemoveSettingAt(m, index);
            AddSetting(m, start, j, false);
            if (end < last_t) AddSetting(m, end + 1, last_j);

            return end;
        }
    }
    return -1;
}

int Plan::InsertLotTail(IND m, IND j, IND t, IND end_t)
{
    int start = ConstrainedTailStart(m, j, t, end_t);
    if (start >= t)
    {
        int end = end_t;
        // check if you have to retain the last lot
        int index = FindSettIndex(m, end);
        sett *last = GetSettingAt(m, index);
        IND last_j = last->j;
        sett *next = NextSettingAt(m, index);
        IND last_t = next == NULL ? T : next->t - 1;
        if (end < last_t && IsConstrained(m, last_j, end + 1, false, false) != 1)
        {
            return -1;
        }

        // insertion is constrained, remove the intermediate settings
        index = FindSettIndex(m, start);
        sett *s = NextSettingAt(m, index);
        while (s != NULL && s->t >= start && s->t <= end)
        {
            RemoveSettingAt(m, index + 1);
            s = NextSettingAt(m, index);
        }
        
        s = GetSettingAt(m, index);
        if (s->t == start) RemoveSettingAt(m, index);
        AddSetting(m, start, j, false);
        if (end < last_t) AddSetting(m, end + 1, last_j);

        return end;
    }

    return -1;
}


IND Plan::ShiftLeftAt(metaheuristic::INhoodSearch* nhood, Plan* p, IND m, IND index, IND size)
{
    IND count = 0;
    if (m > 0 && m <= p->GetParam()->M && 
        index > 0 && index < p->NumSettings(m))
    {
        sett *curr_s = p->GetSettingAt(m, index);
        IND end = curr_s->t;
        IND last = end;

        Plan *plan = new Plan(*p, false);
        for (IND i = index; i > 0; i--)
        {
            sett *s = p->GetSettingAt(m, i);
            if (s != NULL && s->t > size)
            {
                // Remove all settings from (s->t - size) until (last - 1) 
                for (IND t = last - 1; t >= s->t - size; t--)
                {
                    sett *plan_s = plan->GetSetting(m, t);
                    if (plan_s != NULL && plan_s->t >= s->t - size)
                    {
                        plan->RemoveSetting(m, plan_s->t);
                    }
                }
                plan->AddSetting(m, s->t - size, s->j);
                last = s->t - size;
                if (plan->IsConstrainedAt(m, last, end) == 1)
                {
                    if (!plan->Equal(*p) && nhood->AddToNhood(plan)) 
                    {
                        plan = new Plan(*plan, false);
                        count++;
                    }
                    else break;
                }
                else break;
            }
        }
        delete plan;
    }
    return count;
}

IND Plan::ShiftRightAt(metaheuristic::INhoodSearch* nhood, Plan* p, IND m, IND index, IND size)
{
    IND count = 0;
    if (m > 0 && m <= p->GetParam()->M && 
        index >= 0 && index < p->NumSettings(m))
    {
        sett *curr_s = p->GetSettingAt(m, index);
        IND start = curr_s->t;
        IND last = start;

        Plan *plan = new Plan(*p, false);
        for (IND i = index; i < p->NumSettings(m); i++)
        {
            sett *s = p->GetSettingAt(m, i);
            if (s != NULL)
            {
                // Remove all next settings from last until (s->t + size) 
                for (IND t = last; t <= s->t + size && t <= p->GetParam()->T; t++)
                {
                    sett *plan_s = plan->NextSetting(m, t);
                    if (plan_s != NULL && plan_s->t <= s->t + size)
                    {
                        plan->RemoveSetting(m, plan_s->t);
                    }
                }

                if (s->t + size <= p->GetParam()->T)
                {
                    plan->AddSetting(m, s->t + size, s->j);
                    last = s->t + size;
                }
                
                IND end = p->GetParam()->T;
                sett *plan_next = plan->NextSetting(m, last);
                if (plan_next != NULL) end = plan_next->t - 1;

                if (plan->IsConstrainedAt(m, start, end) == 1)
                {
                    if (!plan->Equal(*p) && nhood->AddToNhood(plan)) 
                    {
                        plan = new Plan(*plan, false);
                        count++;
                    }
                    else break;
                }
                else break;
            }
        }
        delete plan;
    }
    return count;
}

int Plan::IsConstrained()
{
    int constrained = IsConstrainedAt(1, T);
    if (CountSettingsForItem(this->param->J + 1) > 0)
    {
        constrained = -3;
    }
    return constrained;
}

int Plan::IsConstrainedAt(IND start_t, IND end_t)
{
    int constrained = 0;
    if (start_t <= end_t)
    {
        for (IND m = 1; m <= M; m++)
        {
            constrained = IsConstrainedAt(m, start_t, end_t);
            if (constrained != 1) break;
        }
    }
    return constrained;
}

int Plan::IsConstrainedAt(IND m, IND start_t, IND end_t)
{
    int constrained = 0;
    if (start_t <= end_t)
    {
        for (IND t = start_t; t <= end_t && t <= T; t++)
        {
            if (t == 0) continue;
            sett *s = GetSetting(m, t);
            constrained = IsConstrained(m, s->j, t);
            if (constrained != 1) 
                break;
        }
    }
    return constrained;
}

int Plan::IsConstrained(IND m, IND j, IND t)
{
    return IsConstrained(m, j, t, false);
}

int Plan::IsConstrained(IND m, IND j, IND t, bool checkP)
{
    return IsConstrained(m, j, t, checkP, true);
}

int Plan::IsConstrained(IND m, IND j, IND t, bool checkP, bool checkPrevSett)
{
    return IsConstrained(m, j, t, checkP, checkPrevSett, true);
}

int Plan::IsConstrained(IND m, IND j, IND t, bool checkP, bool checkPrevSett, bool checkAllMach)
{
    int constrained = 0;
    if (t > 0 && t <= T && 
        j >= 1 && j <= this->param->J && 
        this->param->cap[m][j] > 0)
    {
        // count the total number of settings in the time period t
        IND numSetts = 0;
        // count the number of settings for item j in the time period t
        IND numSetts_j = 0;
        // whether we should check for all machines or only until the current machine m
        IND endM = checkAllMach ? M : m - 1;
        for (IND n = 1; n <= endM; n++)
        {
            if (n != m)
            {
                sett *n_sett = GetSetting(n, t);
                if (n_sett->j >= 1 && n_sett->j <= this->param->J)
                {
                    if (n_sett->t == t) numSetts++;
                    if (n_sett->j == j && IsBusy(n, t)) numSetts_j++;
                }
            }
        }
        
        sett *m_sett = GetSetting(m, t - 1);
        if (checkP) // checkP controls what do we check first
        {
            if (numSetts_j >= this->param->p) constrained = -2;
            // only if we really need to perform a setting
            else if (checkPrevSett && m_sett->j != j && numSetts >= this->param->sigma) constrained = -1;
            else if (!checkPrevSett && numSetts >= this->param->sigma) constrained = -1;
            else constrained = 1;
        }
        else
        {
            if (checkPrevSett && m_sett->j != j && numSetts >= this->param->sigma) constrained = -1;
            else if (!checkPrevSett && numSetts >= this->param->sigma) constrained = -1;
            // only if we really need to perform a setting
            else if (numSetts_j >= this->param->p) constrained = -2; 
            else constrained = 1;
        }
    }
    else if (t > 0 && t <= T && j == this->param->J + 1)
    {
        constrained = 1;
    }
    return constrained;
}

int Plan::ConstrainedStart(IND m, IND j, IND t, IND max_t)
{
    int start = -1;
    IND k = t;
    if (k == 0) k = 1;
    int constrained;
    while (k <= max_t && k <= T)
    {
        constrained = IsConstrained(m, j, k);
        if (constrained == 1)
        {
            start = k;
            break;
        }
        k++;
    }
    return start;
}

int Plan::ConstrainedEnd(IND m, IND j, IND t, IND max_t)
{
    int end = -1;
    IND k = t;
    while (k <= max_t && k <= T)
    {
        if (IsConstrained(m, j, k, true) == -2) break;
        end = k;
        k++;
    }
    return end;
}

int Plan::ConstrainedEnd(IND m, IND j, IND t, IND max_t, IND skip_t, IND skip_end)
{
    int end = -1;
    IND k = t;
    while (k <= max_t && k <= T)
    {
        if (k >= skip_t && k <= skip_end)
        {
            end = k;
            k++;
            continue;
        }

        if (IsConstrained(m, j, k, true) == -2) break;
        end = k;
        k++;
    }
    return end;
}

int Plan::ConstrainedTailStart(IND m, IND j, IND t, IND max_t)
{
    int start = max_t;
    // check the earliest point in time from where we can have constrined production
    int earliest = -1;
    while (start >= t && ConstrainedEnd(m, j, start, start) != -1)
    {
        earliest = start;
        start--;
    }
    start = earliest;
    if (start != -1)
    {
        start = ConstrainedStart(m, j, start, max_t);
    }
    return start;
}

int Plan::ConstrainedTailStart(IND m, IND j, IND t, IND max_t, IND skip_t, IND skip_end)
{
    int start = max_t;
    // check the earliest point in time from where we can have constrined production
    int earliest = -1;
    while (start >= t)
    {
        if (start <= skip_end && start >= skip_t)
        {
            earliest = start;
            start--;
            continue;
        }
        if (ConstrainedEnd(m, j, start, start) == -1) break;
        earliest = start;
        start--;
    }
    // check the earliest point in time where you can start the lot
    start = earliest;
    if (start != -1)
    {
        start = ConstrainedStart(m, j, start, max_t);
    }
    return start;
}

bool Plan::IsBusy(IND m, IND t)
{
    return t > 0 && t <= T;
}

int Plan::NextUnmetDemandInd(IND j)
{
    return NextUnmetDemandInd(j, 0);
}

int Plan::NextUnmetDemandInd(IND j, IND start)
{
    return NextUnmetDemandInd(j, start, T/3);
}

int Plan::NextUnmetDemandInd(IND j, IND start, IND end)
{
    return NextUnmetDemandInd(j, start, end, &dem::IsProcessed);
}

int Plan::NextUnmetDemandInd(IND j, IND start, IND end, dem::CheckFn unmetFn)
{
    int index = -1;
    for (IND i = start; i <= end; i++)
    {
        if (!CALL_MEMBER_FN(udemand[j][i], unmetFn)())
        {
            index = i;
            break;
        }
    }
    return index;  
}

bool Plan::DemandsProcessed()
{
    return DemandsProcessed(T/3);
}

bool Plan::DemandsProcessed(IND end)
{
    return DemandsProcessed(0, end);
}

bool Plan::DemandsProcessed(IND start, IND end)
{
    bool processed = true;
    for (IND j = 1; j <= this->param->J; j++)
    {
        for (IND index = start; index <= end; index++)
            if (!udemand[j][index].processed)
            {
                // cout << "Demand for item " << j << " is not yet processed: index=" << dem_index[j] << endl;
                processed = false;
                break;
            }
    }
    return processed;
}

void Plan::FetchAllDemands(vector<dem *> &CL, IND start, IND end, dem::CheckFn chkFn)
{
    int index = start;
    for (IND j = 1; j <= this->param->J; j++)
    {
        index = start;
        while ((index = NextUnmetDemandInd(j, index, end, chkFn)) != -1)
        {
            CL.push_back(&udemand[j][index]);
            index++;
        }
    }
}

int Plan::IndexOf(const dem &d, vector<dem *> &CL)
{
    int index = -1;
    for (IND i = 0; i < CL.size(); i++)
    {
        if (CL[i]->j == d.j && 
            CL[i]->due_t == d.due_t)
        {
            index = i;
            break;
        }
    }
    return index;
}

int Plan::IndexOf(IND j, IND t, vector<dem *> &CL)
{
    int index = -1;
    for (IND i = 0; i < CL.size(); i++)
    {
        if (CL[i]->j == j && CL[i]->due_t >= t)
        {
            index = i;
            break;
        }
    }
    return index;
}

void Plan::UpdatePositiveInventory(IND m, IND j, IND start_t, IND end_t, IND x)
{
    if (j >= 1 && j <= this->param->J)
    {
        float q = 0.0;
        for (IND t = start_t; t <= end_t; t++)
        {
            IND index = Parameters::to_upper_ind(float(t)/float(3) - 1);

            if (t == start_t && x == 1)
            {
                q = this->param->cap[m][j] * (1 - x * this->param->v);
            }
            else
            {
                q = this->param->cap[m][j] * float(1);
            }
            pos_I[j][index] += q;
        }
    }
}

bool Plan::UpdateNegativeInventory(dem &d)
{
    bool finished = false;
    if (d.j >= 1 && d.j <= this->param->J)
    {
        for (int index = d.due_t / 3 - 1; index >= 0; index--)
        {
            if (pos_I[d.j][index] > 0 && pos_I[d.j][index] >= d.value)
            {
                // positive sufficient inventory
                pos_I[d.j][index] -= d.value;
                UpdateUnmetDemand(d, 0);
                finished = true;
                break;
            }
            else if (pos_I[d.j][index] > 0)
            {
                // positive but insufficient inventory
                float value = d.value - pos_I[d.j][index];
                pos_I[d.j][index] = 0.0;
                UpdateUnmetDemand(d, value);
            }
        }
    }
    return finished;
}

float Plan::GetAvailableInventory(IND j, IND t) const
{
    float I = 0.0;
    if (j >= 1 && j <= this->param->J)
    {
        IND end = 0;
        if (t >= 3) end = t / 3 - 1;
        for (int index = end; index >= 0; index--)
        {
            I += pos_I[j][index];
        }
    }
    return I;
}

void Plan::UpdateUnmetDemand(dem &d, float u_value)
{
    IND index = (d.due_t - 1) / 3;
    if (udemand[d.j][index].j == d.j && 
        udemand[d.j][index].due_t == d.due_t)
    {
        udemand[d.j][index].value = u_value;
        d.value = u_value;
    }
    else
    {
        // never happens
        // cout << "Adding unmet demand for item " << d.j << " in time period " << d.due_t << " was not successful!";
    }
}

void Plan::MarkDemandMet(dem &demand)
{
    IND index = (demand.due_t - 1) / 3;
    udemand[demand.j][index].processed = true;
    demand.processed = true;
}

void Plan::MarkDemandUnmet(dem &demand)
{
    IND index = (demand.due_t - 1) / 3;
    udemand[demand.j][index].processed = true;
    demand.processed = true;
}

int Plan::RequiredLotSize(IND m, IND x, const dem &demand)
{
    float I = GetAvailableInventory(demand.j, demand.due_t);
    return RequiredLotSize(m, x, I, demand);
}

int Plan::RequiredLotSize(IND m, IND x, float inventory, const dem &demand)
{
    int delta = -1;
    if (this->param->cap[m][demand.j] > 0)
    {
        if (demand.value > inventory)
        {
            // find the number of periods delta, necessary in order to satisfy the full demand
            float units = demand.value - inventory;
            float periods = units / float(this->param->cap[m][demand.j]) + x * this->param->v;
            delta = Parameters::to_upper_ind(periods);
        }
        else
        {
            delta = 0;
        }
    }
    return delta;
}

float Plan::Evaluate()
{
    // force execution in order to evaluate demands
    Execute();

    float cost = 0.0;
    // do not use overriden functions here
    cost += Plan::EvaluateSettings();
    for (IND j = 1; j <= this->param->J; j++)
    {
        cost += Plan::EvaluateUnmetDemands(j);
        cost += Plan::EvaluateFutureDemands(j);
        cost += Plan::EvaluateBufferOverrun(j);
    }
    cost += this->param->omega_k * totalBadQ;
    return cost;
}

float Plan::EvaluateSettings() const
{
    // compute the sum of all setting costs
    float cost = 0.0;
    for (IND m = 1; m <= M; m++)
    {
        // exclude initial settings
        cost += this->param->omega_s * this->param->s * (NumSettings(m) - 1);
    }
    return cost;
}

float Plan::EvaluateUnmetDemands() const
{
    float cost = 0.0;
    for (IND j = 1; j <= this->param->J; j++)
    {
        cost += EvaluateUnmetDemands(j);
    }
    return cost;
}

float Plan::EvaluateFutureDemands() const
{
    float cost = 0.0;
    for (IND j = 1; j <= this->param->J; j++)
    {
        cost += EvaluateFutureDemands(j);
    }
    return cost;
}

float Plan::EvaluateBufferOverrun() const
{
    float cost = 0.0;
    for (IND j = 1; j <= this->param->J; j++)
    {
        cost += EvaluateBufferOverrun(j);
    }
    return cost;
}

float Plan::EvaluateUnmetDemands(IND j) const
{
    float cost = 0.0;

    // add the sum of all backlog costs
    if (j >= 1 && j <= this->param->J)
    {
        IND t = 0;
        while (t < T/3)
        {
            if (udemand[j][t].value > 0.0)
            {
                cost += this->param->omega_u * this->param->c * udemand[j][t].value;
            }
            t++;
        }
    }

    return cost;
}

float Plan::EvaluateFutureDemands(IND j) const
{
    float cost = 0.0;
    if (j >= 1 && j <= this->param->J)
    {
        cost += this->param->omega_f * udemand[j][T/3].value;
    }
    return cost;
}

float Plan::EvaluateBufferOverrun(IND j) const
{
    float cost = 0.0;
    if (j >= 1 && j <= this->param->J)
    {
        // do not include the future demand
        if (pos_I[j][T/3 - 1] > this->param->max_I[j])
        {
            // inventory overrun
            float delta_k = pos_I[j][T/3 - 1] - this->param->max_I[j];
            cost += this->param->omega_k * delta_k;
        }
    }
    return cost;
}

float Plan::Value() const
{
    float cost = 0.0;
    cost += EvaluateSettings();
    for (IND j = 1; j <= this->param->J; j++)
    {
        cost += EvaluateUnmetDemands(j);
        cost += EvaluateFutureDemands(j);
        cost += EvaluateBufferOverrun(j);
    }
    cost += this->param->omega_k * totalBadQ;
    return cost;
}

void Plan::Execute()
{
    // do not check constraints for faster execution
    Execute(false);
}

void Plan::Execute(bool checkConstraints)
{
    DeleteExecutionData();
    // initialize inventory and unmet demand
    // positive inventory is used only for reporting purpouses
    InitExecutionData();

    // I[J+1] contains the current inventory for each item (initialized per execution) (used for execution)
    float *I = new float[this->param->J + 1];
    init_I(*this->param, I);
    float totalBadQ = float(0);
    for (IND t = 1; t <= T; t++)
    {
        for (IND m = 1; m <= M; m++)
        {
            // produce according to settings
            sett *s = GetSetting(m, t);
            IND x = (s->t == t) ? 1 : 0;
            float q = GetProduction(m, s->j, 1, x);
            if (checkConstraints)
            {
                if (IsConstrained(m, s->j, t, true, false, false) != 1 ||
                    (t != s->t && 
                    IsConstrained(m, s->j, s->t, true, false, false) != 1))
                {
                    totalBadQ += q;
                    continue;
                }
            }
            I[s->j] += q;
        }

        if (t % 3 == 0) 
        {
            IND index = t / 3 - 1;
            for (IND j = 1; j <= this->param->J; j++)
            {
                SatisfyDemandFromInventory(udemand[j][index], I);
                pos_I[j][index] = I[j];
            }
        }
    }

    // work for fututre demands
    IND index = T / 3;
    for (IND j = 1; j <= this->param->J; j++)
    {
        SatisfyDemandFromInventory(udemand[j][index], I);
        pos_I[j][index] = I[j];
    }

    delete [] I;
    this->totalBadQ = totalBadQ;
}

bool Plan::SatisfyDemandFromInventory(dem &d, float *I)
{
    if (d.value > 0.0)
    {
        if (d.value <= I[d.j])
        {
            I[d.j] = I[d.j] - d.value;
            UpdateUnmetDemand(d, 0.0);
            MarkDemandMet(d);
            return true;
        }
        else
        {
            UpdateUnmetDemand(d, d.value - I[d.j]);
            I[d.j] = 0.0;
            return false;
        }
    }
    else
    {
        return true;
    }
}

bool Plan::Equal(const Solution& sol) const
{
    const planning::Plan& other = 
        dynamic_cast<const planning::Plan&>(sol);
    bool equal = true;
    if (this->M != other.M || this->T != other.T)
    {
        equal = false;
    }
    else
    {
        sett *tsett;
        sett *osett;
        for (IND m = 1; m <= this->M; m++)
        {
            if (this->NumSettings(m) != other.NumSettings(m))
            {
                equal = false;
                break;
            }

            for (IND index = 0; index < this->NumSettings(m); index++)
            {
                tsett = this->GetSettingAt(m, index);
                osett = other.GetSettingAt(m, index);
                if (tsett->t != osett->t || tsett->j != osett->j)
                {
                    equal = false;
                    break;
                }
            }

            if (!equal) break;
        }
    }
    return equal;
}

void Plan::ExportSettings(jsett_type &jsetts)
{
    for (IND m = 1; m <= M; m++)
    {
        for (IND index = 0; index < NumSettings(m); index++)
        {
            sett *s = GetSettingAt(m, index);
            if (s != NULL)
            {
                jsetts[s->j].push_back(*s);
            }
        }
    }
}

void Plan::ExportSettings(ostream &outs)
{
    ExportSettings(outs, true);
}

void Plan::ExportSettings(ostream &outs, bool zeros)
{
    ExportSettingsHead(outs);
    for (IND m = 1; m <= M; m++)
    {
        outs << Parameters::TabbedSpace(m) << "|";
        IND t = 0;
        for (IND k = 0; k < NumSettings(m); k++)
        {
            sett* curr = GetSettingAt(m, k);
            sett* next = NextSettingAt(m, k);
            
            IND end = T;
            if (next != NULL) end = next->t - 1;

            t = curr->t;
            while (t <= end && t <= T)
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
    ExportSettingsFoot(outs);
}

void Plan::ExportSettingsHead(ostream &outs)
{
    outs << "-----";
    for (IND t = 0; t <= T; t++) outs << "----";
    outs << endl;

    outs << "M:T |";
    for (IND t = 0; t <= T; t++) outs << Parameters::TabbedSpace(t);
    outs << endl;

    outs << "-----";
    for (IND t = 0; t <= T; t++) outs << "----";
    outs << endl;
}

void Plan::ExportSettingsFoot(ostream &outs)
{
    outs << "-----";
    for (IND t = 0; t <= T; t++) outs << "----";
    outs << endl;

    outs << "sum:|";
    for (IND t = 0; t <= T; t++) 
        outs << Parameters::TabbedSpace(CountSettings(t));
    outs << endl;

    outs << "-----";
    for (IND t = 0; t <= T; t++) outs << "----";
    outs << endl;
}

void Plan::ExportSettings(ofstream &outs)
{
    for (int m = 1; m <= M; m++)
    {
        IND t = 0;
        for (int k = 0; k < NumSettings(m); k++)
        {
            sett* curr = GetSettingAt(m, k);
            sett* next = NextSettingAt(m, k);            
            IND end = T;
            if (next != NULL) end = next->t - 1;

            t = curr->t;
            if (t == 0) t++;
            while (t <= end)
            {
                outs << curr->j;    
                if (t < T) outs << ";";
                t++;
            }
        }
        outs << endl;
    }
}

void Plan::ImportSettings(ifstream &in)
{
    if (!in.is_open())
	{
        cout << "Failed to open input stream for importing settings!" << endl;
        return;
	}

    // clear all settings first
    ClearSettings();

    string line;
    size_t pos;
    IND m = 1;
    while (getline(in, line) && m <= M)
	{
        IND t = 1;
        IND prev_j = 0;
		do
		{
            pos = line.find(';');
            string field;
            if (pos >= 0)
			    field = line.substr(0, pos);
            else
                field = line;
            Parameters::trim_spaces(field);
            IND j = atoi(field.c_str());
            if (j != prev_j) 
            {
                AddSetting(m, t, j);
                prev_j = j;
            }

            t++;
            if (pos >= 0) line = line.substr(pos + 1);
		} while(pos >= 0 && t <= T);
        m++;
	}
}

void Plan::ExportInventory(ostream &outs)
{
    ExportDemandsHead(outs);
    // body
    for (IND j = 1; j <= this->param->J; j++)
    {
        outs << Parameters::TabbedSpace(j) << "|";
        outs << Parameters::TabbedSpace(this->param->init_I[j]);
        for (IND t = 0; t <= this->param->T/3; t++)
        {
            outs << Parameters::TabbedSpace(pos_I[j][t]);
        }
        outs << endl;
    }
    ExportInventoryFoot(outs);
}


void Plan::ExportInventoryFoot(ostream &outs)
{
    outs << "-----";
    for (IND t = 0; t <= T/3 + 1; t++) outs << "-------";
    outs << endl;

    outs << "sum:|";
    float sum = 0.0;
    outs << Parameters::TabbedSpace(sum);
    for (IND t = 0; t <= T/3; t++) 
    {
        sum = 0.0;
        for (IND j = 1; j <= this->param->J; j++)
        {
            sum += pos_I[j][t];
        }
        outs << Parameters::TabbedSpace(sum);
    }
    outs << endl;

    outs << "-----";
    for (IND t = 0; t <= T/3 + 1; t++) outs << "-------";
    outs << endl;    
}

void Plan::ExportUnmetDemands(ostream &outs)
{
    ExportDemandsHead(outs);
    // body
    for (IND j = 1; j <= this->param->J; j++)
    {
        outs << Parameters::TabbedSpace(j) << "|";
        outs << Parameters::TabbedSpace((float)0.0);
        for (IND t = 0; t <= this->param->T/3; t++)
        {
            dem *d = &udemand[j][t];
            outs << Parameters::TabbedSpace(d->value);
        }
        outs << endl;
    }
    ExportDemandsFoot(outs);
}

void Plan::ExportDemandsHead(ostream &outs)
{
    outs << "-----";
    for (IND t = 0; t <= T/3 + 1; t++) outs << "-------";
    outs << endl;

    outs << "R:T |";
    for (IND t = 0; t <= T/3; t++) outs << Parameters::TabbedSpace((float)3 * t);
    outs << Parameters::TabbedSpace((float)T + 1) << endl;

    outs << "-----";
    for (IND t = 0; t <= T/3 + 1; t++) outs << "-------";
    outs << endl;
}

void Plan::ExportDemandsFoot(ostream &outs)
{
    outs << "-----";
    for (IND t = 0; t <= T/3 + 1; t++) outs << "-------";
    outs << endl;

    outs << "sum:|";
    float sum = 0.0;
    outs << Parameters::TabbedSpace(sum);
    for (IND t = 0; t <= T/3; t++) 
    {
        sum = 0.0;
        for (IND j = 1; j <= this->param->J; j++)
        {
            sum += udemand[j][t].value;
        }
        outs << Parameters::TabbedSpace(sum);
    }
    outs << endl;

    outs << "-----";
    for (IND t = 0; t <= T/3 + 1; t++) outs << "-------";
    outs << endl;    
}

void Plan::Export(ostream &outs, float duration)
{
    outs << "nM: " << this->param->M << endl;
    outs << "nT: " << this->param->T << endl;
    outs << "nJ: " << this->param->J << endl;
    outs << endl;
    
    int constrained = IsConstrained();
    if (constrained != 1)
    {
        outs << "ERROR: Construction is not constrained: ";
        if (constrained == 0) outs << "capacity matrix violated!";
        else if (constrained == -1) outs << "maximum number of settings violated!";
        else if (constrained == -2) outs << "maximum parallel production violated!";
        else if (constrained == -3) outs << "item dose not exist!";
        else outs << "unknown reason!";
        outs << endl;
        outs << endl;
    }

    float svalue = EvaluateSettings();
    float uvalue = EvaluateUnmetDemands();
    float fvalue = EvaluateFutureDemands();
    float kvalue = EvaluateBufferOverrun();
    float value = svalue + uvalue + fvalue + kvalue;
    
    outs << "Objective value: " << Parameters::TabbedSpace(value) << endl;
    outs << endl;
    outs << "Setup costs:   " << Parameters::TabbedSpace(svalue) << ", ";
    outs << "weight: " << Parameters::TabbedSpace(this->param->omega_s) << endl;
    outs << "Backlog costs: " << Parameters::TabbedSpace(uvalue) << ", ";
    outs << "weight: " << Parameters::TabbedSpace(this->param->omega_u) << endl;
    outs << "Future demand: " << Parameters::TabbedSpace(fvalue) << ", ";
    outs << "weight: " << Parameters::TabbedSpace(this->param->omega_f) << endl;
    outs << "Inv. violation:" << Parameters::TabbedSpace(kvalue) << ", ";
    outs << "weight: " << Parameters::TabbedSpace(this->param->omega_k) << endl;
    outs << endl;
    
    outs << "Comput. time:  " << Parameters::TabbedSpace(duration) << "ms" << endl;
    outs << endl;
    outs << endl;
    
    outs << "Production plan: " << endl;
    ExportSettings(outs, false);
    outs << endl;
    
    outs << "Setup plan: " << endl;
    ExportSettings(outs, true);
    outs << endl;
}

void Plan::ExportAll(ostream &outs, float duration)
{
    Export(outs, duration);
    outs << endl;
    outs << "Positive inventory: " << endl;
    ExportInventory(outs);
    outs << endl;
    outs << endl;
    outs << "Negative inventory: " << endl;
    ExportUnmetDemands(outs);
    outs << endl;
}

string Plan::ToString() 
{
    ostringstream outs;
    Export(outs, float(0));
    return outs.str();
}

void Plan::DeleteExecutionData()
{
    if (this->executing)
    {
        delete [] this->udemand_data;
        delete [] this->udemand;
        delete [] this->pos_I;
        delete [] this->pos_I_data;

        this->executing = false;
    }
}

}