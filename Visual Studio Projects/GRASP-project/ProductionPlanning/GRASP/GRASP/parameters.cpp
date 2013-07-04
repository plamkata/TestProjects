#include "stdafx.h"
#include "parameters.h"

namespace planning {

float Parameters::MAX_COST = numeric_limits<float>::max();
float Parameters::MIN_COST = numeric_limits<float>::max();
float Parameters::PRECISION = float(0.0001);

int Parameters::MAX_SEED = numeric_limits<int>::max();

Parameters::Parameters()
{
    average = float(0);
}

void Parameters::read(string dir)
{
    read_params(dir + "params.txt");
    read_capacity(dir + "capacity.csv");
    read_initial_setups(dir + "init.csv");
    read_initial_inventory(dir + "stock.csv");
    read_demands(dir + "demand.csv", dir + "fdemand.csv");  
    read_buffer(dir + "buffer.csv");

    float sum = 0.0;
    for (IND j = 1; j <= this->J; j++)
    {
        sum += demand[j][this->T / 3];
    }
    
    if (Parameters::eq(sum, 0.0)) omega_f = 1;
    else omega_f = 1 / sum;
}

bool Parameters::read_params(string filename)
{
    string line;
	size_t pos;

    ifstream in(filename.c_str());
	if (!in.is_open())
	{
        cout << "Failed to open file: " << filename << "!" << endl;
        return false;
	}
	while (getline(in, line))
	{
		string field;
        string value;

        pos = line.find('=');
        if (pos >= 0)
        {
		    field = line.substr(0, pos);
            value = line.substr(pos + 1);

            trim_spaces(field);
            trim_spaces(value);
        }
        else continue;

        // make the entry in the array
        fill_in(field, (float)atof(value.c_str()));
	}
    return true;
}

void Parameters::fill_in(string field, float value)
{
    if (field == string("M"))       M = IND(value);
    if (field == string("J"))       J = IND(value);
    if (field == string("T"))       T = IND(value);
    if (field == string("s"))       s = float(value);
    if (field == string("c"))       c = float(value);
    if (field == string("v"))       v = float(value);
    if (field == string("p"))       p = IND(value);
    if (field == string("sigma"))   sigma = IND(value);
    if (field == string("omega_s")) omega_s = float(value);
    if (field == string("omega_u")) omega_u = float(value);
    if (field == string("omega_k")) omega_k = float(value);
    if (field == string("omega_f")) omega_f = float(value);

    // construction
    if (field == string("omega_q")) omega_q = float(value);
    if (field == string("omega_t")) omega_t = float(value);
    if (field == string("omega_l")) omega_l = float(value);
    if (field == string("omega_m")) omega_m = float(value);
    if (field == string("beta"))    beta = float(value);
    if (field == string("beta_t"))  beta_t = float(value);
    if (field == string("beta_v"))  beta_v = float(value);
    if (field == string("alpha"))   alpha = float(value);

    // local search
    if (field == string("nhoodTermIter"))   nhoodTermIter = IND(value);
    if (field == string("maxNhoodSize"))    maxNhoodSize = IND(value);
    if (field == string("minNhoodSize"))    minNhoodSize = IND(value);
    if (field == string("nhoodCoeff"))      nhoodCoeff = float(value);
    if (field == string("nhoodPressure"))   nhoodPressure = float(value);

    // GRASP
    if (field == string("maxGRASPIter"))    maxGRASPIter = IND(value);
    if (field == string("GRASPTermIter"))   GRASPTermIter = IND(value);

    // Path relinking
    if (field == string("poolSize"))        poolSize = IND(value);
    if (field == string("relinkInterval"))  relinkInterval = IND(value);
}

bool Parameters::read_capacity(string filename) 
{
    cap = new IND*[this->M + 1];
    cap_data = new IND[(this->M + 1) * (this->J + 1)];

    if (oldStyleCap)
    {
        read_csv(filename, 3, cap_data, 'c');
    }
    else
    {
        read_csv(filename, 7, cap_data, 'a');
    }

    // initialize the two dim array by rows 
    IND *index = cap_data;
    for (int i = 0; i < this->M + 1; i++)
    {
        cap[i] = index;
        index += (J + 1);
    }
    return true;
}

bool Parameters::read_initial_inventory(string filename)
{
    init_I = new float[J + 1];
    init_I[0] = float(0);

    read_csv(filename, 2, init_I, 'i');
    return true;
}

bool Parameters::read_initial_setups(string filename)
{
    init_setup = new IND[this->M + 1];
    init_setup[0] = 0;
    read_csv(filename, 2, init_setup, 's');
    return true;
}

bool Parameters::read_demands(string dfilename) 
{
    int cols = this->T/3 + 1;
    demand = new IND*[this->J + 1];
    demand_data = new IND[(this->J + 1) * cols];

    read_csv(dfilename, cols, demand_data, 'd');

    // initialize the two dim array by rows 
    IND *index = demand_data;
    for (int j = 0; j < this->J + 1; j++)
    {
        demand[j] = index;
        index += cols;
    }

    return true;
}

bool Parameters::read_fdemands(string ffilename) 
{
    // the fallowing initialization was already performed by read_demands()
    // demand_data = new IND[(J + 1) * (T/3 + 1)];
    read_csv(ffilename, 2, demand_data, 'f');
    return true;
}

bool Parameters::read_demands(string dfilename, string ffilename)
{
    bool success = read_demands(dfilename);
    if (!success) return false;
    success = read_fdemands(ffilename);
    if (!success) return false;
    return true;
}

bool Parameters::read_buffer(string filename) 
{
    // initialize the maximum inventory allowed (per item)
    max_I = new float[J + 1];

    IND *buff = new IND[J + 1];
    read_csv(filename, 2, buff, 'b');
    
    max_I[0] = 0;
    for (IND j = 1; j <= J; j++)
    {
        // compute maximum allowed inventory
        max_I[j] = Parameters::max(float(demand[j][T/3] + p * buff[j]), init_I[j]);
    }

    delete [] buff;

    return true;
}


void Parameters::read_csv(string filename, int cols, IND *arr, char type)
{
    string line;
	size_t pos;

    ifstream in(filename.c_str());
	if (!in.is_open())
	{
        cout << "Failed to open file: " << filename << "!" << endl;
	}
    
    IND *ln = new IND[cols];
	while (getline(in, line))
	{
        int i = 0;
		do
		{
            pos = line.find(';');

            string field;
            if (pos >= 0)
			    field = line.substr(0, pos);
            else
                field = line;
            
            trim_spaces(field);
            IND value = atoi(field.c_str());
            ln[i] = value;
            if (type == 'd' && i > 0)
            {
                // fill in demand data
                arr[ln[0]*(T/3 + 1) + i - 1] = ln[i];
            } 
            else if (type == 'a' && i > 0)
            {
                arr[i*(J + 1) + ln[0]] = ln[i];
            }
            i++;

            if (pos >= 0) line = line.substr(pos + 1);

		} while(pos >= 0 && i < cols);
        
        // make the entry in the array
        fill_in(arr, ln, type);
	}
    delete [] ln;
}

void Parameters::fill_in(IND *arr, IND *ln, char type)
{
    switch(type)
    {
    case 'c': 
        // fill in capacity matrix
        arr[ln[0]*(J + 1) + ln[1]] = ln[2];
        break;
    case 'i': // initial inventory per item
    case 's': // initial setup per machine
    case 'b': // buffer overrun per item
        arr[ln[0]] = ln[1];
        break;
    case 'd':
        // fill in demand data (done earlier)
        break;
    case 'f':
        arr[ln[0]*(T/3 + 1) + T/3] = ln[1];
        break;
    default:
        break;
    }
}

void Parameters::read_csv(string filename, int cols, float *arr, char type)
{
    string line;
	size_t pos;

    ifstream in(filename.c_str());
	if (!in.is_open())
	{
        cout << "Failed to open file: " << filename << "!" << endl;
	}
    
    float *ln = new float[cols];
	while (getline(in, line))
	{
        int i = 0;
		do
		{
            pos = line.find(';');

            string field;
            if (pos >= 0)
			    field = line.substr(0, pos);
            else
                field = line;
            
            trim_spaces(field);
            float value = (float) atof(field.c_str());
            ln[i] = value;
            if (type == 'd' && i > 0)
            {
                // fill in demand data
                arr[IND(ln[0])*(T/3 + 1) + i - 1] = ln[i];
            } 
            else if (type == 'a' && i > 0)
            {
                arr[i*(J + 1) + IND(ln[0])] = ln[i];
            }
            i++;

            if (pos >= 0) line = line.substr(pos + 1);

		} while(pos >= 0 && i < cols);
        
        // make the entry in the array
        fill_in(arr, ln, type);
	}
    delete [] ln;
}

void Parameters::fill_in(float *arr, float *ln, char type)
{
    switch(type)
    {
    case 'c': 
        // fill in capacity matrix
        arr[IND(ln[0])*(J + 1) + IND(ln[1])] = ln[2];
        break;
    case 'i': // initial inventory per item
    case 's': // initial setup per machine
    case 'b': // buffer overrun per item
        arr[IND(ln[0])] = ln[1];
        break;
    case 'd':
        // fill in demand data (done earlier)
        break;
    case 'f':
        arr[IND(ln[0])*(T/3 + 1) + T/3] = ln[1];
        break;
    default:
        break;
    }
}

int *Parameters::generate_seeds(int seed, int seed_count)
{
    int *seeds = new int[seed_count];

    // declare a local mother-of-all generator 
    // starting from the same seed in each thread
    CRandomMother init(seed);
    int size = omp_get_num_threads();
    int rank = omp_get_thread_num();
    for (int j = 0; j < seed_count; j++)
    {
        // use block splitting: jump (rank * seed_size / size) ahead
        for (int k = 0; k < rank * seed_count / size; k++)
        {
            init.IRandom(0, Parameters::MAX_SEED);
        }
        seeds[j] = init.IRandom(0, Parameters::MAX_SEED);
    }

    return seeds;
}

float Parameters::cap_proportion(IND m, IND n, IND j)
{
    float prop = float(1.0);
    if (m > 0 && m <= this->M && n > 0 && n <= this->M)
    {
        if (j > 0 && j <= this->J && cap[n][j] > 0 && cap[m][j] > 0)
        {
            prop = float(cap[m][j]) / float(cap[n][j]);
        }
    }
    return prop;
}

IND Parameters::lot_size(IND lot_size_m, float cap_prop, IND x_m, IND x_n)
{
    float value = cap_prop * (float(lot_size_m) - float(x_m)*v) + float(x_n)*v;
    return Parameters::to_upper_ind(value);
}

float Parameters::get_alpha()
{
    // choose from a fixed array of available alphas
    return alpha;
}

void Parameters::update_alpha(float alpha, float obj_value)
{
    // rank the available alpha values according to the objective value they produce
}

float Parameters::get_beta()
{
    return beta;
}

void Parameters::update_beta(float beta, float obj_value)
{
    // rank the available beta values according to the objective value they produce
}

void Parameters::trim_spaces(string &str)
{
    // Trim Both leading and trailing spaces
    size_t startpos = str.find_first_not_of(" \t");
    size_t endpos = str.find_last_not_of(" \t");

    // if all spaces or empty return an empty string
    if(( string::npos == startpos ) || ( string::npos == endpos))
    {
        str = "";
    }
    else
    {
        str = str.substr( startpos, endpos-startpos+1 );
    }
}

string Parameters::TabbedSpace(IND value) 
{
    ostringstream outs;
    if (value < 10) outs << "   ";
    else if (value < 100) outs << "  ";
    else if (value < 1000) outs << " ";
    else outs << ",";

    outs << value;
    return outs.str();
}

string Parameters::TabbedSpace(float value) 
{
    ostringstream outs1;
    outs1 << value;
    string str = outs1.str();
    
    ostringstream outs;
    if (value > 0.0 && value < 0.001) outs << value;
    else
    {
        str = str.substr(0, 6);
        for (int i = int(str.size()); i <= 6; i++) outs << " ";
        outs << str;
    }
    return outs.str();
}

IND Parameters::random(Rand *rand, IND fromNum, IND toNum)
{
    if (fromNum < toNum)
        return rand->IRandomX(fromNum, toNum);
        //return fromNum + IND((toNum - fromNum + 1) * rand() / (RAND_MAX + 1.0));
    else
        return fromNum;
}

IND Parameters::random(Rand *rand, IND toNum)
{
    return Parameters::random(rand, 0, toNum);
}

void Parameters::rand_beta(Rand *rand, float delta_a, float delta_b, float delta_t, float delta_v)
{
    float min = 0.05f;
    float max = 0.4f;
    float delta = 0.0f;
    if (alpha - delta_a < min) delta = (max - min) / 2;
    if (alpha + delta_a > max) delta = (min - max) / 2;
    alpha = float(rand->IRandomX(
        int(alpha * 1000 - delta_a * 1000 + delta * 1000), 
        int(alpha * 1000 + delta_a * 1000 + delta * 1000))) / float(1000);

    min = 0.05f;
    max = 0.6f;
    delta = 0.0f;
    if (beta - delta_b < min) delta = (max - min) / 2;
    if (beta + delta_b > max) delta = (min - max) / 2;
    beta = float(rand->IRandomX(
        int(beta * 1000 - delta_b * 1000 + delta * 1000), 
        int(beta * 1000 + delta_b * 1000 + delta * 1000))) / float(1000);

    min = 0.05f;
    max = 1.0f;
    delta = 0.0f;
    if (beta_t - delta_t < min) delta = (max - min) / 5;
    if (beta_t + delta_t > max) delta = (min - max) / 5;
    beta_t = float(rand->IRandomX(
        int(beta_t * 1000 - delta_t * 1000 + delta * 1000), 
        int(beta_t * 1000 + delta_t * 1000 + delta * 1000))) / float(1000);

    min = 0.05f;
    max = 1.0f;
    delta = 0.0f;
    if (beta_t - delta_t < min) delta = (max - min) / 5;
    if (beta_t + delta_t > max) delta = (min - max) / 5;
    beta_v = float(rand->IRandomX(
        abs(int(beta_v * 1000 - delta_v * 1000 + delta * 1000)), 
        int(beta_v * 1000 + delta_v * 1000 + delta * 1000))) / float(1000);

    omega_t = float(rand->IRandomX(0, 1));
    omega_l = float(rand->IRandomX(0, 1));
    omega_m = float(rand->IRandomX(0, 1));
}

IND Parameters::to_upper_ind(float value)
{
    if (value < 0.0) return 0;

    IND ind_value = (IND)value;
    
    float diff = (value > ind_value) ? 
        value - ind_value : ind_value - value;

    if (diff >= PRECISION) ind_value++;

    return IND(ind_value);
}

IND Parameters::min_s(IND limit)
{
    // solution to min_s {s * (s + 1) / 2 >= limit}
    return to_upper_ind((sqrt(float(8 * limit + 1)) - 1) / 2);
}

IND Parameters::min(int left, int right)
{
    return left > right ? right : left;
}

float Parameters::min(float left, float right)
{
    return left > right ? right : left;
}

IND Parameters::max(int left, int right)
{
    return left < right ? right : left;
}

float Parameters::max(float left, float right)
{
    return left < right ? right : left;
}

bool Parameters::eq(float left, float right)
{
    float diff = (left > right) ? 
        left - right : right - left; 

    return diff < PRECISION;
}

Parameters::~Parameters()
{
    delete [] cap;
    delete [] cap_data;
    delete [] init_setup;
    delete [] init_I;
    delete [] demand;
    delete [] demand_data;
    delete [] max_I;
}

IND JobSizeGRASP(const Parameters *input, IND num_threads)
{
    IND degree = Parameters::to_upper_ind(log(float(num_threads)));
    if (degree > 0 && degree % 2 == 0) degree = degree - 1;

    IND interval = input->relinkInterval > 20 ? 5 : input->relinkInterval;
    float lower = pow(float(2), degree);
    float fetch_size = Parameters::min(lower * interval, float(input->maxGRASPIter));
    int job_size = Parameters::to_upper_ind(fetch_size / num_threads);

    return job_size;
}

IND JobSizePR(IND pool_size, IND num_threads)
{
    IND degree = IND(log(float(num_threads - 1)));

    float lower = pow(float(2), degree);
    float fetch_size = Parameters::min(lower, float(pool_size - 1)) * (pool_size / 2);
    int job_size = Parameters::to_upper_ind(fetch_size / num_threads);

    return job_size;
}

}