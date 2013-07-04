// GRASP.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "GRASP.h"
#include "improvement_factory.h"

#include <windows.h>

int main(int argc, _TCHAR* argv[])
{
    string command;
    if (argc > 1) ReadArgument(command, 10, argv[1]);
    else
    {
        command = "GRASP";
        //command = "construct";
        //command = "improve";
        //command = "verify";
        //command = "schedule";
    }

    string dir;
    if (argc > 2) ReadArgument(dir, 100, argv[2]);
    else
    {
        //dir = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\problem1\\";
        //dir = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\problem2\\";
        //dir = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\problem3\\";
        //dir = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\toyproblem1\\";
        //dir = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\toyproblem2\\";
        dir = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\toyproblem3\\";
    }

    Parameters *input = new Parameters();
    input->printInfo = false;
    input->printLog = true;
    //input->oldStyleCap = true;
    input->oldStyleCap = false;

    metaheuristic::ExecutionContext context;
    context.SetParams(input);

    Run(&context, command, dir);

	return 0;
}

void ReadArgument(string &arg, size_t arg_size, _TCHAR* argv)
{
    char *mbstr = new char[arg_size];
    arg_size = wcstombs(mbstr, argv, arg_size);
    arg = mbstr;
    delete mbstr;
}

void Run(metaheuristic::ExecutionContext *ctx, string command, string dir)
{
    metaheuristic::Runner r;
    try
    {

        if (ctx->GetParams()->printInfo) cout << "Executing: " << command << endl;
        if (command == string("schedule"))
        {
            r.ReadInput(ctx, dir);
            r.RunManyGRASP(ctx, dir + "Output\\", 10);
            //r.RunAllGRASP();
        }
        else
        { 
            r.ReadInput(ctx, dir);
        }

        if (command == string("GRASP"))
        {
            r.RunGRASP(ctx, dir + "Output\\");
        }
        else if (command == string("construct"))
        {
            r.RunConstruction(ctx, dir + "Output\\");
        }
        else if (command == string("improve"))
        {
            r.RunImprovement(ctx, dir + "Output\\");
        }
        else if (command == string("verify"))
        {
            r.RunValidation(ctx, dir + "Output\\");
        }
    }
    catch(char * str) 
    {
        cout << "Reading parameters failed." << endl << "Reason: " << str << endl;
    }
}

void Display(const Parameters *input)
{
    /*
    cout << "Capacity matrix cap[m][j]: " << endl;
    for (IND m = 1; m <= input->M; m++)
    {
        cout << m << ":\t";
        for (IND j = 1; j <= input->J; j++)
        {
            if (input->cap[m][j] > 0)
                cout << j << Parameters::TabbedSpace(j);
        }
        cout << endl;
    }
    *//*
    cout << "Initial setup matrix init_setup[m]: " << endl;
    for (IND m = 1; m <= input->M; m++)
    {
        cout << m << ":\t" << input->init_setup[m] << endl;
    }
    *//*
    cout << "Initial inventory matrix init_I[j]: " << endl;
    for (IND j = 1; j <= input->J; j++)
    {
        cout << j << ": " << input->init_I[j] << endl;
    }
    cout << endl;
    */
    // 0-th day contains demand for 1-st day; T/3 period contains future demands;
    cout << "Demands matrix demand[j][t]: " << endl;
    for (IND j = 1; j <= input->J; j++)
    {
        cout << j << ":\t";
        for (IND t = 0; t <= input->T/3; t++)
        {
            cout << Parameters::TabbedSpace(input->demand[j][t]);
        }
        if (j%2 == 0) cout << endl;
        else cout << "\t";
    }
}

namespace metaheuristic {

Runner::Runner() : global_log(NULL)
{
}

void Runner::ReadInput(ExecutionContext *ctx, string dir)
{
    ctx->GetParams()->read(dir);
}

void Runner::RunManyGRASP(ExecutionContext *ctx, 
        string dir, IND numRuns)
{
    global_log = new Log();

    IND count = 0;
    IND num = 0;
    while (count < numRuns)
    {
        std::ostringstream out;
        out << dir << "Run" << num << "\\";
        string run_dir = out.str();
        out.clear();

        if (mkdir(run_dir.c_str()) == 0)
        {
            RunGRASP(ctx, run_dir);
            count++;
            Sleep(10000);
        }
        else if (errno == EEXIST)
        {
            num++;
        }
        else 
        {
            cout << strerror(errno) << ": " << run_dir << endl;
            break;
        }
    }

    if (ctx->GetParams()->printLog) ExportLog(global_log, dir, GRASP);
    delete global_log;
}

void Runner::RunAllGRASP(ExecutionContext *ctx)
{
    string dir[6];
    dir[0] = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\problem1\\";
    dir[1] = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\problem2\\";
    dir[2] = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\problem3\\";
    dir[3] = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\toyproblem1\\";
    dir[4] = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\toyproblem2\\";
    dir[5] = "E:\\Misceleniouse\\2008\\Master Thesis, RISC\\Project\\ProductionPlanning\\toyproblem3\\";
    IND num = 0;
    IND i = 0;
    while (i < 6)
    {

        ExecutionContext context;
        context.SetParams(new Parameters());
        context.GetParams()->printInfo = false;
        context.GetParams()->printLog = true;
        context.GetParams()->oldStyleCap = true;
        if (i > 3) context.GetParams()->oldStyleCap = false;
        ReadInput(&context, dir[i]);

        std::ostringstream out;
        out << dir[i] << "Output\\Run" << num << "\\";
        string run_dir = out.str();
        out.clear();

        if (mkdir(run_dir.c_str()) == 0)
        {
            RunGRASP(&context, run_dir);
            i++;
            num = 0;
            Sleep(10000);
        }
        else if (errno == EEXIST)
        {
            num++;
        }
        else 
        {
            cout << strerror(errno) << ": " << run_dir << endl;
            break;
        }
    }
}

void Runner::RunGRASP(ExecutionContext *ctx, string dir)
{
    ctx->SetLog(new Log());
    
    std::clock_t start = std::clock();
    ctx->GetLog()->SetStartTime(start);
    Pool *pool = new Pool(ctx->GetParams()->poolSize);

    try
    {
        // execute basic GRASP with regular path-relinking
        int seed = IND(time(0));
        ExecuteGRASP(ctx, seed, pool);
        if (ctx->GetParams()->printInfo) pool->ExportElements(cout);
        
        // perform postoptimization with path relinking
        cout << "Post-optimization..." << endl;
        seed = IND(time(0)); // use different seed
        pool->LinkAll(ctx, seed);
        if (ctx->GetParams()->printInfo) pool->ExportElements(cout);
    }
    catch(char * str) 
    {
        cout << "GRASP execution failed." << endl << "Reason: " << str << endl;
    }
    std::clock_t end = std::clock();

    Plan *best = pool->Best();
    GRASPInfo(ctx, best, float(end - start), dir);

    delete pool;
}

void Runner::RunConstruction(ExecutionContext *ctx, string dir)
{
    ctx->SetLog(new Log());
    PartialPlan *plan = new PartialPlan(*ctx->GetParams());
    
    Rand rand(int(time(0)));
    std::clock_t start = std::clock();
    ctx->GetLog()->SetStartTime(start);
    try
    {
        // perform contruction
        plan = plan->Construct(&rand);
    }
    catch (char *str) 
    {
        cout << "Construction failed." << endl << "Reason: " << str << endl;
    }
    std::clock_t end = std::clock();
    ConstructionInfo(ctx, plan, float(end - start), dir);
    
    delete plan;
}

void Runner::RunImprovement(ExecutionContext *ctx, string dir)
{
    ctx->SetLog(new Log());
    Plan *pplan = LoadPlan(ctx, dir);

    time_t start = std::clock();
    ctx->GetLog()->SetStartTime(start); 
    Plan *plan = pplan;
    try
    {
        Rand rand((int) time(0));
        plan = LocalSearchImprovement(ctx, pplan, &rand);
        float value = plan->Evaluate();
        if (ctx->GetParams()->printLog) ctx->GetLog()->Add(Improvement, value);
    }
    catch (char *str) 
    {
        cout << "Improvement failed." << endl << "Reason: " << str << endl;
    }
    time_t end = std::clock();
    ImprovementInfo(ctx, plan, float(end - start), dir);
    
    delete plan;
}

void Runner::RunValidation(ExecutionContext *ctx, string dir)
{
    time_t start = std::clock();
    Plan *plan = LoadPlan(ctx, dir);
    time_t end = std::clock();
    ExportInfo(plan, float(end - start), dir, "validation.txt");
    delete plan;
}

void Runner::ExecuteGRASP(ExecutionContext *ctx, int seed, Pool *pool)
{
    float best_value = Parameters::MAX_COST;
    int termCounter = 0;
    bool abort = false;
    IND job_size = JobSizeGRASP(ctx->GetParams(), omp_get_num_procs());
    
#pragma omp parallel shared(ctx, seed, pool, best_value, termCounter, abort, job_size)
{
    // create a separate random generator in each thread 
    Rand rand(seed); 
    // and parametrize it with the thread id
    int seeds[2] = {seed, omp_get_thread_num()};
    rand.RandomInitByArray(seeds, 2);

#pragma omp for schedule(dynamic, job_size)
    for (int i = 0; i < ctx->GetParams()->maxGRASPIter; i++)
    {
// Read about abort workaround: http://www.thinkingparallel.com/2007/06/29/breaking-out-of-loops-in-openmp/
#pragma omp flush (abort)
        if (!abort)
        {
            // randomize beta
            ctx->GetParams()->rand_beta(&rand, 0.001f, 0.003f, 0.003f, 0.003f);
            //cout << "alpha: " << input->alpha << " beta: " << input->beta << 
            //    " beta_t: " << input->beta_t << " beta_v: " << input->beta_v << endl;

            Plan *start = ConstructionPhase(ctx, &rand);
            Plan *plan = ImprovementPhase(ctx, start, &rand);
            float value = plan->Value();
            
            // execute path relinking to a selected solution from the pool, regularly
            if (i % ctx->GetParams()->relinkInterval == 0) 
            {
                float link_value = pool->LinkTo(ctx, plan, &rand);
                if (link_value < value) value = link_value;
            }

            //if (i == input->GRASPTermIter / 2) 
            //{
            //    float link_value = pool->LinkAll(IND(time(0)), false);
            //    if (link_value < value) value = link_value;
            //}
            
            // test each "local optimum" for acceptance in the pool
            if (!pool->Accept(ctx, plan)) delete plan;

#pragma omp critical(update_best)
            {
                //if (i % 10 == 0) input->nhoodTermIter += 1;

                if (value < best_value - Parameters::PRECISION)
                {
                    best_value = value;
                    termCounter = 0;
                    if (ctx->GetParams()->printLog) ctx->GetLog()->Add(GRASP, value);
                }
                else 
                {
                    termCounter++;
                    if (termCounter >= ctx->GetParams()->GRASPTermIter) 
                    {
                        abort = true;
#pragma omp flush (abort)
                    }
                }
            }
        }
    }
}
}

Plan *Runner::ConstructionPhase(ExecutionContext *ctx, Rand *rand)
{
    Plan *plan = GreedyRandomizedConstruction(ctx, rand);
    //if (input->printInfo && plan->Plan::IsConstrained() != 1) 
    //    cout << omp_get_thread_num() << ": ERROR: Construction is not contrained!!!" << endl;
    float value = plan->Evaluate();
    if (ctx->GetParams()->printInfo) 
    { 
        cout << omp_get_thread_num() << 
            ": Constructed plan cost: " << 
            Parameters::TabbedSpace(value) << endl;
    }
    if (ctx->GetParams()->printLog) ctx->GetLog()->Add(Construction, value);
    ctx->GetParams()->average += value / float(ctx->GetParams()->maxGRASPIter);
    return plan;
}

Plan *Runner::ImprovementPhase(ExecutionContext *ctx, Plan *start, Rand *rand)
{
    Plan *plan = LocalSearchImprovement(ctx, start, rand);
    float value = plan->Evaluate();
    if (ctx->GetParams()->printInfo) 
    {
        cout << omp_get_thread_num() << 
            ": Improved plan objective cost is: " << 
            Parameters::TabbedSpace(value) << endl;
        ctx->GetLog()->Add(Improvement, value);
    }
    return plan;
}

Plan *Runner::GreedyRandomizedConstruction(ExecutionContext *ctx, Rand *rand)
{
    PartialPlan *plan = new PartialPlan(*ctx->GetParams());
    plan = plan->Construct(rand);
    return plan;
}

Plan *Runner::LocalSearchImprovement(ExecutionContext *ctx, Plan *start, Rand *rand)
{
    LocalSearch search(*start, rand);
    ImprovementFactory factory(&search);
    search.SetFactory(&factory);
    
    Plan *plan = dynamic_cast<Plan*>(search.Execute(ctx));
    delete start;
    Plan *result = new Plan(*plan, false);
    
    return result;
}

void Runner::GRASPInfo(ExecutionContext *ctx, Plan *plan, float duration, string dir)
{
    if (ctx->GetParams()->printLog) ctx->GetLog()->Add(GRASP, plan->Value());
    if (ctx->GetParams()->printLog && global_log != NULL) 
    {
        global_log->Add(GRASP, plan->Value());
    }
    cout << "Total execution time: " << 
        Parameters::TabbedSpace(duration) << "ms" << endl;
    plan->Export(cout, duration);
    ExportPlan(plan, dir);
    ExportInfo(plan, duration, dir, "GRASP.txt");    
    if (ctx->GetParams()->printLog) 
    {
        ExportLog(ctx->GetLog(), dir, GRASP, Iteration);
    }
}

void Runner::ImprovementInfo(ExecutionContext *ctx, Plan *plan, float duration, string dir)
{
    cout << "Improvement took: ";
    cout << Parameters::TabbedSpace(duration) << "ms" << endl;
    cout << "The cost of the improved plan is: " << plan->Value() << endl;
    plan->Export(cout, duration);
    ExportPlan(plan, dir, "plan-impr.csv");
    ExportInfo(plan, duration, dir, "improvement.txt");
    if (ctx->GetParams()->printLog) 
    {
        ExportLog(ctx->GetLog(), dir);
    }
}

void Runner::ConstructionInfo(ExecutionContext *ctx, Plan *plan, float duration, string dir)
{
    float value = plan->Value();
    cout << "Construction took: ";
    cout << Parameters::TabbedSpace(duration) << "ms" << endl;
    cout << "The cost of the plan is: " << value << endl;

    plan->Export(cout, duration);
    ExportPlan(plan, dir);
    ExportInfo(plan, duration, dir, "construction.txt");
    
    if (ctx->GetParams()->printLog) 
    {
        ctx->GetLog()->Add(Construction, value);
        ExportLog(ctx->GetLog(), dir);
    }
}

Plan *Runner::LoadPlan(ExecutionContext *ctx, string dir)
{
    return LoadPlan(ctx, dir, "plan.csv");
}

Plan *Runner::LoadPlan(ExecutionContext *ctx, string dir, string filename)
{
    Plan *plan = new Plan(*ctx->GetParams());

    // read settings
    if (ctx->GetParams()->printInfo) cout << "Reading settings from file: " << dir << filename << endl;

    std::clock_t start = std::clock();
    ImportPlan(plan, dir, filename); // load the settings from the file
    float value = plan->Evaluate(); // execute and evaluate the plan
    std::clock_t end = std::clock();
    
    if (ctx->GetParams()->printInfo)
    {
        cout << "Import and execution took: ";
        cout << Parameters::TabbedSpace(float(end - start)) << "ms" << endl;
        cout << "The cost of the initial plan is: " << value << endl;
        plan->Export(cout, float(end - start));
    }
    return plan;
}

void Runner::ImportPlan(Plan *p, string dir)
{
    ImportPlan(p, dir, "plan.csv");
}

void Runner::ImportPlan(Plan *p, string dir, string filename)
{
    string file = dir + filename;
    try
    {
        ifstream myfile;
        myfile.open (file.c_str());
        p->Plan::ImportSettings(myfile);
        myfile.close();
    }
    catch (char *str) 
    {
        cout << "Could not import plan from file: " << file << endl;
        cout << "Reason: " << str << endl;
    }
}

void Runner::ExportPlan(Plan *p, string dir)
{
    ExportPlan(p, dir, "plan.csv");
}

void Runner::ExportPlan(Plan *p, string dir, string filename)
{
    string file = dir + filename;

    ofstream ofile;
    ofile.open (file.c_str(), ios::out | ios::trunc);
    p->Plan::ExportSettings(ofile);
    ofile.close();
}

void Runner::ExportInfo(Plan *p, float duration, string dir, string filename)
{
    ofstream ofile;
    string file = dir + filename;
    ofile.open (file.c_str(), ios::out | ios::trunc);
    p->ExportAll(ofile, duration);
    ofile.close();
}

void Runner::ExportLog(Log *log, string dir)
{
    ofstream ofile;
    string file = dir + "log.csv";
    ofile.open (file.c_str(), ios::out | ios::trunc);
    log->Export(ofile);
    ofile.close();
}

void Runner::ExportLog(Log *log, string dir, log_entry_type type)
{
    ofstream ofile;
    string file = dir + "log.csv";
    ofile.open (file.c_str(), ios::out | ios::trunc);
    log->Export(ofile, type);
    ofile.close();
}

void Runner::ExportLog(Log *log, string dir, log_entry_type type1, log_entry_type type2)
{
    ofstream ofile;
    string file = dir + "log.csv";
    ofile.open (file.c_str(), ios::out | ios::trunc);
    log->Export(ofile, type1, type2);
    ofile.close();
}

Runner::~Runner()
{
    
}

}