// Copyright (C) 2000-2008 Heiko Bauke <heiko.bauke@mpi-hd.mpg.de>
//  
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License in
// version 2 as published by the Free Software Foundation.
//  
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.
//  
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
// 02111-1307, USA.
//  

#if !(defined TRNG_CHI_SQUARE_DIST_HPP)

#define TRNG_CHI_SQUARE_DIST_HPP

#include <trng/limits.hpp>
#include <trng/utility.hpp>
#include <trng/math.hpp>
#include <trng/special_functions.hpp>
#include <ostream>
#include <istream>
#include <iomanip>
#include <cerrno>

namespace trng {

  // uniform random number generator class
  template<typename float_t=double>
  class chi_square_dist {
  public:
    typedef float_t result_type;
    class param_type;
    
    class param_type {
    private:
      int nu_;
    public:
      int nu() const { return nu_; }
      void nu(int nu_new) { nu_=nu_new; }
      param_type() : nu_(1) {
      }
      explicit param_type(int nu) : nu_(nu) {
      }

      friend class chi_square_dist;

      // Streamable concept
      template<typename char_t, typename traits_t>
      friend std::basic_ostream<char_t, traits_t> &
      operator<<(std::basic_ostream<char_t, traits_t> &out,
		 const param_type &p) {
	std::ios_base::fmtflags flags(out.flags());
	out.flags(std::ios_base::dec | std::ios_base::fixed |
		  std::ios_base::left);
	out << '('
	    << p.nu()
	    << ')';
	out.flags(flags);
	return out;
      }
      
      template<typename char_t, typename traits_t>
      friend std::basic_istream<char_t, traits_t> &
      operator>>(std::basic_istream<char_t, traits_t> &in,
		 param_type &p) {
	int nu;
	std::ios_base::fmtflags flags(in.flags());
	in.flags(std::ios_base::dec | std::ios_base::fixed |
		 std::ios_base::left);
	in >> utility::delim('(')
	   >> nu >> utility::delim(')');
	if (in)
	  p=param_type(nu);
	in.flags(flags);
	return in;
      }
      
    };
    
  private:
    param_type p;

    // inverse cumulative density function
    result_type icdf_(result_type x) const {
      if (x<=math::numeric_limits<result_type>::epsilon())
        return 0;
      const result_type kappa=p.nu()/2;
      const result_type theta=2;
      if (kappa==1)  // special case of exponential distribution
        return -math::ln(1-x)*theta;
      const result_type ln_Gamma_kappa=math::ln_Gamma(kappa);
      result_type y=kappa, y_old;
      if (kappa<1 and x<result_type(1)/result_type(2))
	y=x*x;
      int num_iterations=0;
      do {
	++num_iterations;
        y_old=y;
        result_type f0=math::GammaP(kappa, y)-x;
        result_type f1=math::pow(y, kappa-1)*math::exp(-y-ln_Gamma_kappa);
        result_type f2=f1*(kappa-1-y)/y;
        y-=f0/f1*(1+f0*f2/(2*f1*f1));
      } while (num_iterations<16 &&
               math::abs((y-y_old)/y)>16*math::numeric_limits<result_type>::epsilon());
      return y*theta;
    }
    
  public:
    // constructor
    explicit chi_square_dist(int nu) : p(nu) {
    }
    explicit chi_square_dist(const param_type &p) : p(p) {
    }
    // reset internal state
    void reset() { }
    // random numbers
    template<typename R>
    result_type operator()(R &r) {
      return icdf_(utility::uniformco<result_type>(r));
    }
    template<typename R>
    result_type operator()(R &r, const param_type &p) {
      chi_square_dist g(p);
      return g(r);
    }
    // property methods
    result_type min() const { return 0; }
    result_type max() const { return math::numeric_limits<result_type>::infinity(); }
    param_type param() const { return p; }
    void param(const param_type &p_new) { p=p_new; }
    int nu() const { return p.nu(); }
    void nu(int nu_new) { p.nu(nu_new); }
    // probability density function  
    result_type pdf(result_type x) const {
      if (x<0)
	return 0;
      else {
	x/=2;
	return math::pow(x, p.nu()/result_type(2)-1)/
	  (math::exp(x+math::ln_Gamma(p.nu()/result_type(2)))*2);
      }
    }
    // cumulative density function 
    result_type cdf(result_type x) const {
      if (x<=0)
	return 0;
      else
	return math::GammaP(p.nu()/result_type(2), x/2);
    }
    // inverse cumulative density function 
    result_type icdf(result_type x) const {
      if (x<=0 or x>=1) {
	errno=EDOM;
	return math::numeric_limits<result_type>::quiet_NaN();
      }
      if (x==0)
	return 0;
      if (x==1)
	return math::numeric_limits<result_type>::infinity();
      return icdf_(x);
    }
  };
  
  // -------------------------------------------------------------------

  // EqualityComparable concept
  template<typename float_t>
  inline bool operator==(const typename chi_square_dist<float_t>::param_type &p1, 
			 const typename chi_square_dist<float_t>::param_type &p2) {
    return p1.nu()==p2.nu();
  }

  template<typename float_t>
  inline bool operator!=(const typename chi_square_dist<float_t>::param_type &p1, 
			 const typename chi_square_dist<float_t>::param_type &p2) {
    return not (p1==p2);
  }
    
  // -------------------------------------------------------------------

  // EqualityComparable concept
  template<typename float_t>
  inline bool operator==(const chi_square_dist<float_t> &g1, 
			 const chi_square_dist<float_t> &g2) {
    return g1.param()==g2.param();
  }

  template<typename float_t>
  inline bool operator!=(const chi_square_dist<float_t> &g1, 
			 const chi_square_dist<float_t> &g2) {
    return g1.param()!=g2.param();
  }
  
  // Streamable concept
  template<typename char_t, typename traits_t, typename float_t>
  std::basic_ostream<char_t, traits_t> &
  operator<<(std::basic_ostream<char_t, traits_t> &out,
	     const chi_square_dist<float_t> &g) {
    std::ios_base::fmtflags flags(out.flags());
    out.flags(std::ios_base::dec | std::ios_base::fixed |
	      std::ios_base::left);
    out << "[chi_square " << g.param() << ']';
    out.flags(flags);
    return out;
  }
  
  template<typename char_t, typename traits_t, typename float_t>
  std::basic_istream<char_t, traits_t> &
  operator>>(std::basic_istream<char_t, traits_t> &in,
	     chi_square_dist<float_t> &g) {
    typename chi_square_dist<float_t>::param_type p;
    std::ios_base::fmtflags flags(in.flags());
    in.flags(std::ios_base::dec | std::ios_base::fixed |
	     std::ios_base::left);
    in >> utility::ignore_spaces()
       >> utility::delim("[chi_square ") >> p >> utility::delim(']');
    if (in)
      g.param(p);
    in.flags(flags);
    return in;
  }
  
}

#endif