// problem.h : includes files and defines abstract structures and 
// common parameters of the problem of production planning

#ifndef _PROBLEM_H_
#define _PROBLEM_H_

#include "stdafx.h"
#include "interface.h"
#include "parameters.h"
#include "logging.h"

#include <math.h>
#include <vector>
#include <map>

using namespace std;

namespace planning {

// represents a setting
struct sett
{
    // machine of the setting
    IND m;
    // time period of the setting
    IND t;
    // item for which the setting should be performed
    IND j;
};

typedef map<IND, vector<sett>> jsett_type;

// represents a demand by the item requested and the due time period
class dem
{
public:
    typedef  bool (dem::*CheckFn)();

    // the item, which is demanded
    IND j;
    // the number of units from item j, which are demanded
    float value;
    // the due time period for the demand
    IND due_t;
    // wether the demand is processed or not
    bool processed;

public:
    bool IsProcessed();
    bool IsZero();
    float CandidateCost(Parameters *param);
};


// a class representing a production plan
class Plan : public metaheuristic::Solution
{
public:
    // pointer to a member function for exchanging lots
    typedef int (Plan::*ExchangeLotsFn)(IND m, IND m_t, IND n, IND n_t);
    
    // constructors
    Plan(Parameters &param);
    Plan(const Plan &p);
    Plan(const Plan &p, bool copyExecData);
    virtual void InitExecutionData();
    virtual Solution* Clone(bool copy_exec_data) const;
    virtual ~Plan();

// attributes
private:
    // two dimmensional dynammic array of settings, specified per machine, 
    // and sorted according to the time period of the settings; numsett[M+1]; settings[M+1][T+1] (= j);
    sett **settings;
    // changed to array of vector<sett>s containing the settings.
    vector<sett> *vsett;
    // the real data for the unmet demands: udemand_data*[(J + 1) * (T/3 + 1)] (initialized per execution)
    dem *udemand_data;
    // account the positive inventory (used for reporting purpouses in execution)
    // *used as the main inventory storage in construction of a partial plan
    float **pos_I;
    float *pos_I_data;

protected:
    // number of machines
    IND M;
    // number of time periods
    IND T;
    // udemand[J+1][T/3 + 1] contains the unmet demands for each item (might be 0) (initialized per execution)
    dem **udemand;
    // marks whether execution have started (i.e. if demands and inventory are initialized)
    bool executing;
    // strores the total unconstrained production quantity for later penalization in the evaluation
    float totalBadQ;

// operations
public:
    // total number of settings performed on machine m
    IND NumSettings(IND m) const;
    // get the item, which machine m is set to produce in time period t
    IND GetItem(IND m, IND t);
    // find the index of the setting on machine m that starts the identified lot
    int FindSettIndex(IND m, IND t) const;
    // find the setting on machine m that starts the identified lot
    sett *GetSetting(IND m, IND t) const;
    sett *GetSettingAt(IND m, IND index) const;
    // navigate to the next setting
    sett *NextSetting(IND m, IND t) const;
    sett *NextSettingAt(IND m, IND index) const;
    // navigate to the previose setting
    sett *PrevSetting(IND m, IND t) const;
    sett *PrevSettingAt(IND m, IND index) const;
    // add a new setting on machine m in beginning of time period t, for producing product J
    virtual int AddSetting(IND m, IND t, IND j);
    virtual int AddSetting(IND m, IND t, IND j, bool checkNext);
    // remove the setting, if there is a setting in that time period
    bool RemoveSetting(IND m, IND t);
    bool RemoveSettingAt(IND m, IND index);
    // clears all settings except for the initial setup of machines
    void ClearSettings();
    void ClearSettings(IND m);
    // counts all settings in the plan in the specified time period
    IND CountSettings(IND t) const;
    IND CountSettingsForItem(IND j) const;

    float GetProduction(IND m, IND j, IND lot_size, IND x);
    
    // exchange the constrained heads of the lots, 
    // return the exchanged lot size if possible and -1 if not possible
    virtual bool ExchangeLotsHeads(IND m, IND m_t, IND n, IND n_t);
    virtual bool ExchangeLotsTails(IND m, IND m_t, IND n, IND n_t);
    virtual bool ExchangeLotsHeadForTail(IND m, IND m_t, IND n, IND n_t);

    // extends the previouse lot into the lot specified, returns the end of the extension or -1
    virtual int ExtendPrevLot(IND m, IND t, IND max_t);
    // extends the next lot into the lot specified, returns the beginning of the extension or -1
    virtual int ExtendNextLot(IND m, IND t, IND min_t);

    // insestrts a lot (by overwriting) at the specified possition for producing the specified item
    virtual int InsertLotHead(IND m, IND j, IND t, IND end_t);
    virtual int InsertLotTail(IND m, IND j, IND t, IND end_t);

    static IND ShiftLeftAt(metaheuristic::INhoodSearch* nhood, Plan* p, IND m, IND index, IND size);
    static IND ShiftRightAt(metaheuristic::INhoodSearch* nhood, Plan* p, IND m, IND index, IND size);

    virtual int IsConstrained();
    int IsConstrainedAt(IND start_t, IND end_t);
    int IsConstrainedAt(IND m, IND start_t, IND end_t);
    int IsConstrained(IND m, IND j, IND t);
    int IsConstrained(IND m, IND j, IND t, bool checkP);
    int IsConstrained(IND m, IND j, IND t, bool checkP, bool checkPrevSett);
    int IsConstrained(IND m, IND j, IND t, bool checkP, bool checkPrevSett, bool checkAllMach);
    // determine constrained start and constrained end for producing item j
    int ConstrainedStart(IND m, IND j, IND t, IND max_t);
    int ConstrainedEnd(IND m, IND j, IND t, IND max_t);
    // assumes that production is constrained in the skip period
    int ConstrainedEnd(IND m, IND j, IND t, IND max_t, IND skip_t, IND skip_end);
    // tells you the earliest point in time between t and max_t 
    // when you can start production for item j and finish not earlier than max_t
    int ConstrainedTailStart(IND m, IND j, IND t, IND max_t);
    // assumes that production is constrained in the skip time period
    int ConstrainedTailStart(IND m, IND j, IND t, IND max_t, IND skip_t, IND skip_end);
    virtual bool IsBusy(IND m, IND t);

    // finds the index of the next unprocessed demand in udemand
    int NextUnmetDemandInd(IND j);
    int NextUnmetDemandInd(IND j, IND start);
    int NextUnmetDemandInd(IND j, IND start, IND end);
    int NextUnmetDemandInd(IND j, IND start, IND end, dem::CheckFn unmetFn);
    
    // fetch all demands between start and end period, not satisfying the demand check function, 
    // into the candidate list CL
    void FetchAllDemands(vector<dem *> &CL, IND start, IND end, dem::CheckFn chkFn);
    // find the index of the specified demand in CL
    int IndexOf(const dem &d, vector<dem *> &CL);
    // find the index of a demand in CL for which production 
    // in the specified time period is possible
    int IndexOf(IND j, IND t, vector<dem *> &CL);

    // Mark the specified demand as met
    void MarkDemandMet(dem &demand);
    // mark the specified demand as unmet
    void MarkDemandUnmet(dem &demand);

    // computes the number of time periods of production on machine m required to meet the demand;
    // x = 0/1 and specifies whether a setting should be made; returns -1 if production is not possible;
    int RequiredLotSize(IND m, IND x, const dem &demand);
    int RequiredLotSize(IND m, IND x, float inventory, const dem &demand);

    bool DemandsProcessed();
    bool DemandsProcessed(IND end);
    virtual bool DemandsProcessed(IND start, IND end);

    // get the objective value for the plan (no execution performed)
    virtual float Value() const;
    // evaluate will force plan re-execution
    virtual float Evaluate();
    virtual float EvaluateSettings() const;
    virtual float EvaluateUnmetDemands() const;
    virtual float EvaluateUnmetDemands(IND j) const;
    virtual float EvaluateFutureDemands() const;
    virtual float EvaluateFutureDemands(IND j) const;
    virtual float EvaluateBufferOverrun() const;
    virtual float EvaluateBufferOverrun(IND j) const;
    
    // execute the plan to find the actual unmet demands and report positive inventory
    virtual void Execute();
    // if checking constraints is enabled unconstrained production will not be included in evaluation 
    // but rather will be returned by this method for further penalization.
    virtual void Execute(bool checkConstraints);
    virtual bool Equal(const Solution& other) const;
    
    virtual void Export(ostream &outs, float duration);
    virtual void ExportAll(ostream &outs, float duration);
    
    // exports the settings indexed by item in a map of vectors
    virtual void ExportSettings(jsett_type &jsetts);
    virtual void ExportSettings(ofstream &outs);
    virtual void ImportSettings(ifstream &in);
    virtual void ExportSettings(ostream &outs);
    virtual void ExportSettings(ostream &outs, bool zeros);
    virtual void ExportSettingsHead(ostream &outs);
    virtual void ExportSettingsFoot(ostream &outs);

    virtual void ExportInventory(ostream &outs);
    virtual void ExportInventoryFoot(ostream &outs);
    virtual void ExportUnmetDemands(ostream &outs);
    virtual void ExportDemandsHead(ostream &outs);
    virtual void ExportDemandsFoot(ostream &outs);

    virtual string ToString();
    
    virtual void DeleteExecutionData();

protected:
    void init_settings(const Parameters &input);
    void init_settings(const Plan &p);
    void init_udemand(const Parameters &input);
    void init_udemand(const Plan &p);
    void init_pos_I(const Parameters &input);
    void init_pos_I(const Plan &p);

    // appends a setting at afater the last setting on machine m (might replace the last setting)
    bool AppendSetting(IND m, IND t, IND j);
    
    void UpdatePositiveInventory(IND m, IND j, IND start_t, IND end_t, IND x);
    // satisfy as much as you can of the demand
    bool UpdateNegativeInventory(dem &d);
    // return available inventory (i.e. from the last day) in time period t
    float GetAvailableInventory(IND j, IND t) const;

    // update the unmet demand for the demand specified;
    void UpdateUnmetDemand(dem &d, float u_value);

private:
    void init_I(const Parameters &input, float *I);
    bool SatisfyDemandFromInventory(dem &d, float *I);
};

}

#endif /* _PROBLEM_H_ */