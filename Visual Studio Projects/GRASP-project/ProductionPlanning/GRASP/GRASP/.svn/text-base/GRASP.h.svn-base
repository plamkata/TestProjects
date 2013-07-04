#ifndef _GRASP_H_
#define _GRASP_H_

#include "stdafx.h"
#include "problem.h"
#include "construction.h"
#include "improvement.h"
#include "relinking.h"

#include <cstdlib>
#include <iostream>
#include <sstream>
#include <direct.h>
#include <errno.h> 
#include <time.h>

using namespace planning;

void ReadArgument(string &arg, size_t arg_size, _TCHAR* argv);
void Run(metaheuristic::ExecutionContext *ctx, string command, string dir);
void Display(Parameters &input);

namespace metaheuristic {

class Runner
{
public:
    Runner();
    
private:

    Log *global_log;

public:
    // read input files from a directory
    void ReadInput(ExecutionContext *ctx, string dir);

    // available run commands
    void RunAllGRASP(ExecutionContext *ctx);
    void RunManyGRASP(ExecutionContext *ctx, string dir, IND numRuns);
    void RunGRASP(ExecutionContext *ctx, string dir);
    void RunImprovement(ExecutionContext *ctx, string dir);
    void RunConstruction(ExecutionContext *ctx, string dir);
    void RunValidation(ExecutionContext *ctx, string dir);

    // GRASP related
    void ExecuteGRASP(ExecutionContext *ctx, int seed, Pool *pool);
    Plan *ConstructionPhase(ExecutionContext *ctx, Rand *rand);
    Plan *GreedyRandomizedConstruction(ExecutionContext *ctx, Rand *rand);
    Plan *ImprovementPhase(ExecutionContext *ctx, 
        Plan *start, Rand *rand);
    Plan *LocalSearchImprovement(ExecutionContext *ctx, 
        Plan *start, Rand *rand);

    ~Runner();

private:
    // printing and logging information
    void GRASPInfo(ExecutionContext *ctx, Plan *p, float duration, string dir);
    void ImprovementInfo(ExecutionContext *ctx, Plan *p, float duration, string dir);
    void ConstructionInfo(ExecutionContext *ctx, Plan *p, float duration, string dir);
    
    Plan *LoadPlan(ExecutionContext *ctx, string dir);
    Plan *LoadPlan(ExecutionContext *ctx, string dir, string filename);

    // import/export plans and log
    void ImportPlan(Plan *p, string dir);
    void ImportPlan(Plan *p, string dir, string filename);
    void ExportPlan(Plan *p, string dir);
    void ExportPlan(Plan *p, string dir, string filename);
    void ExportInfo(Plan *p, float duration, string dir, string filename);
    void ExportLog(Log *log, string dir);
    void ExportLog(Log *log, string dir, log_entry_type type);
    void ExportLog(Log *log, string dir, log_entry_type type1, log_entry_type type2);
};

}

#endif /* _GRASP_H_ */