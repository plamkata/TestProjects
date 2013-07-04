// parameters.h : common parameters of the problem of production planning

#ifndef _PARAMETERS_H_
#define _PARAMETERS_H_

#include "stdafx.h"

#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

namespace planning {

class Parameters {

public:
    Parameters();

// attributes and constants
public:
    static float MAX_COST;

    static float MIN_COST;

    static float PRECISION;

    static int MAX_SEED;

    // basic input of the problem:

    // number of parallel machines available in the plan; from 1 to M;
    IND M;

    // number of product types, which can be produced; from 1 to J; 0 indicates no product type;
    IND J;

    // number of time periods in the planning horizon; 
    IND T; 

    // indicates the capacities of machines: cap[M+1][J+1]; cap[0][*] and cap[*][0] are empty;
    IND **cap;

    // initial setup of machines for producing the specified items: init_setup[M+1]; 0 indicates no setup;
    IND *init_setup;

    // initial inventory for each item: init_I[J+1];
    float *init_I;

    // indicates demand for an item: demand[J+1][T/3+1] per 1 day (= 3 shifts); 
    // demands in the last time period T/3 are actually future demands;
    IND **demand;

    // the max_I[J+1] array specifiy the maximum inventory that is allowed (precomputed)
    // max_I[j] = max {future demand[j] + parallel factor * buffer[j], init_I[j]}
    float *max_I;

    
    // input constants of the problem:

    // setting cost s_{m, j} is the same for all indices
    float s;

    // backlog cost c_{j, t} is the same for all indices
    float c;

    // time ratio of a setting v_{m, j} is the same for all indices
    float v;

    
    // p_j - maximum number of machines, that can produce item j (parallel factor of item j)
    // (the parallel factor is the same for all items)
    IND p;

    // maximum number of settings per shift;
    IND sigma;

    
    // omega_s - setting weight
    float omega_s;

    // omega_u - backlog weight
    float omega_u;

    // omega_f - unmet future demand weight
    float omega_f;

    // omega_k - buffer overrun weight
    float omega_k;


    // Construction Parameters


    // the coefficient for producing as much quantity as possible (insertion cost only) 
    float omega_q;

    // the coefficient for producing in the earliest period possible (insertion cost only)
    float omega_t;

    // the coefficient for chosing to make a setting after the longest lot possible (insertion cost only)
    float omega_l;

    // the coefficient for chosing to make a setting on the machine with less settings so far (insertion cost only)
    float omega_m;

    
    // the coefficient for fetching earliest demands first in CL
    float beta_t;

    // the coefficient for fetching largest demands first in CL
    float beta_v;

    
    // the alpha hyper parameter controls the size of the restricted candidate list;
    // determine the balance between variability and greediness
    float alpha;

    // the beta hyper parameter controls how late the demands could be in the candidate list;
    // small beta indicates we are working only for most urgent demands;
    float beta;


    // Local Search Parameters


    // terminating condition of the local search: number of iterations of no further improvement, 
    // after which the local search will terminate
    IND nhoodTermIter;

    // maximum size of the local neighborhood of local search
    IND maxNhoodSize;

    // maximum number of good solutions, that can be selected for improvement in one improvement cycle
    IND minNhoodSize;

    // a proportional delta coefficient arround the current best value of local search, 
    // that indicates which solutions will remain in neighborhood for the next selection
    float nhoodCoeff;

    // a pressure coefficient which specifies a condition when randomized selection should be performed;
    // The condition is: (numTermIter / nhoodTermIter) > nhoodPressure;
    float nhoodPressure;


    // GRASP Parameters


    // maximum number of GRASP iterations
    IND maxGRASPIter;

    // terminating condition of GRASP: number of iterations of no further improvement, 
    // after which the GRASP will terminate
    IND GRASPTermIter;


    // Path Relinking

    // size of thte pool of elite solutions
    IND poolSize;

    // number of GRASP iterations after which relinking will be executed
    IND relinkInterval;

    // Other parameters

    bool printInfo;
    bool printLog;

    bool oldStyleCap;

    float average;

private:
    // array containing the actual data for capacity matrix
    IND *cap_data;
    // array containing the actual data for the demand matrix
    IND *demand_data;

public:
    void read(string dir);
    bool read_params(string filename);
    bool read_capacity(string filename);
    bool read_initial_setups(string filename);
    bool read_initial_inventory(string filename);
    bool read_demands(string dfilename);
    bool read_fdemands(string ffilename);
    bool read_demands(string dfilename, string ffilename);
    bool read_buffer(string filename);

    // generate a different array of seeds per calling thread
    static int *generate_seeds(int seed, int seed_count);

    // find out the corresponding lot size on another machine
    float cap_proportion(IND m, IND n, IND j);
    IND lot_size(IND lot_size_m, float cap_prop, IND x_m, IND x_n);

    float get_alpha();
    void update_alpha(float alpha, float obj_value);

    float get_beta();
    void update_beta(float beta, float obj_value);

    static void trim_spaces(string& str);
    static string TabbedSpace(IND value);
    static string TabbedSpace(float value);

    static IND random(Rand *rand, IND fromNum, IND toNum);
    static IND random(Rand *rand, IND toNum);
    void rand_beta(Rand *rand, 
        float delta_a, float b_delta, float t_delta, float v_delta);

    static IND to_upper_ind(float value);
    // finds min_s {1 + 2 + ... + s >= limit}
    static IND min_s(IND limit);
    static IND min(int left, int right);
    static float min(float left, float right);
    static IND max(int left, int right);
    static float max(float left, float right);
    static bool eq(float left, float right);
    
    virtual ~Parameters();

private:
    void read_csv(string filename, int cols, IND *arr, char type);
    void read_csv(string filename, int cols, float *arr, char type);
    void fill_in(string field, float value);
    void fill_in(IND *arr, IND *ln, char type);
    void fill_in(float *arr, float *ln, char type);

};

IND JobSizeGRASP(const Parameters *input, IND num_threads);
IND JobSizePR(IND pool_size, IND num_threads);

}

#endif /* _PARAMETERS_H_ */