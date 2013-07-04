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

#if !(defined TRNG_LCG64_SHIFT_HPP)

#define TRNG_LCG64_SHIFT_HPP

#include <trng/limits.hpp>
#include <climits>
#include <stdexcept>
#include <ostream>
#include <istream>
#include <trng/utility.hpp>
#include <trng/generate_canonical.hpp>

namespace trng {
  
  class lcg64_shift;
  
  class lcg64_shift {
  public:

    // Uniform random number generator concept
    typedef unsigned long long result_type;
    result_type operator()() const;
    static const result_type min=0;
    static const result_type max=18446744073709551615ull;
  private:
    static const bool use_mask=math::numeric_limits<result_type>::digits>64;
  public:
    // Parameter and status classes
    class parameter_type;
    class status_type;

    class parameter_type {
      result_type a, b;
    public:
      parameter_type() :
	a(0), b(0) { };
      parameter_type(result_type a, result_type b) :
	a(a), b(b) { };

      friend class lcg64_shift;

      // Equality comparable concept
      friend bool operator==(const parameter_type &, const parameter_type &);
      friend bool operator!=(const parameter_type &, const parameter_type &);

      // Streamable concept
      template<typename char_t, typename traits_t>
      friend std::basic_ostream<char_t, traits_t> & 
      operator<<(std::basic_ostream<char_t, traits_t> &out, 
		 const parameter_type &P) {
	std::ios_base::fmtflags flags(out.flags());
	out.flags(std::ios_base::dec | std::ios_base::fixed | 
		  std::ios_base::left);
	out << '(' 
	    << P.a << ' ' << P.b 
	    << ')';
	out.flags(flags);
	return out;
      }

      template<typename char_t, typename traits_t>
      friend std::basic_istream<char_t, traits_t> & 
      operator>>(std::basic_istream<char_t, traits_t> &in, 
		 parameter_type &P) {
	parameter_type P_new;
	std::ios_base::fmtflags flags(in.flags());
	in.flags(std::ios_base::dec | std::ios_base::fixed | 
		 std::ios_base::left);
	in >> utility::delim('(')
	   >> P_new.a >> utility::delim(' ')
	   >> P_new.b >> utility::delim(')');
	if (in)
	  P=P_new;
	in.flags(flags);
	return in;
      }
      
    };
    
    class status_type {
      result_type r;
    public:
      status_type() : r(0) { };
      explicit status_type(result_type r) : r(r) { };
      
      friend class lcg64_shift;

      // Equality comparable concept
      friend bool operator==(const status_type &, const status_type &);
      friend bool operator!=(const status_type &, const status_type &);

      // Streamable concept
      template<typename char_t, typename traits_t>
      friend std::basic_ostream<char_t, traits_t> & 
      operator<<(std::basic_ostream<char_t, traits_t> &out, 
		 const status_type &S) {
	std::ios_base::fmtflags flags(out.flags());
	out.flags(std::ios_base::dec | std::ios_base::fixed | 
		  std::ios_base::left);
	out << '(' 
	    << S.r 
	    << ')';
	out.flags(flags);
	return out;
      }

      template<typename char_t, typename traits_t>
      friend std::basic_istream<char_t, traits_t> & 
      operator>>(std::basic_istream<char_t, traits_t> &in, 
		 status_type &S) {
	status_type S_new;
	std::ios_base::fmtflags flags(in.flags());
	in.flags(std::ios_base::dec | std::ios_base::fixed | 
		 std::ios_base::left);
	in >> utility::delim('(')
	   >> S_new.r >> utility::delim(')');
	if (in)
	  S=S_new;
	in.flags(flags);
	return in;
      }

    };
    
    static const parameter_type Default;
    static const parameter_type LEcuyer1;
    static const parameter_type LEcuyer2;
    static const parameter_type LEcuyer3;

    // Random number engine concept
    explicit lcg64_shift(parameter_type=Default); 
    explicit lcg64_shift(unsigned long, parameter_type=Default); 
    
    template<typename gen>
    explicit lcg64_shift(gen &g, parameter_type P=Default) : P(P), S() {
      seed(g);
    }
    
    void seed(); 
    void seed(unsigned long); 
    template<typename gen>
    void seed(gen &g) {
      result_type r=0;
      for (int i=0; i<2; ++i) {
	r<<=32;
	r+=g();
      }
      S.r=r;
    }
    void seed(result_type); 
    
    // Equality comparable concept
    friend bool operator==(const lcg64_shift &, const lcg64_shift &);
    friend bool operator!=(const lcg64_shift &, const lcg64_shift &);

    // Streamable concept
    template<typename char_t, typename traits_t>
    friend std::basic_ostream<char_t, traits_t> & 
    operator<<(std::basic_ostream<char_t, traits_t> &out, const lcg64_shift &R) {
      std::ios_base::fmtflags flags(out.flags());
      out.flags(std::ios_base::dec | std::ios_base::fixed | 
		std::ios_base::left);
      out << '[' << lcg64_shift::name() << ' ' << R.P << ' ' << R.S << ']';
      out.flags(flags);
      return out;
    }

    template<typename char_t, typename traits_t>
    friend std::basic_istream<char_t, traits_t> & 
    operator>>(std::basic_istream<char_t, traits_t> &in, lcg64_shift &R) {
      lcg64_shift::parameter_type P_new;
      lcg64_shift::status_type S_new;
      std::ios_base::fmtflags flags(in.flags());
      in.flags(std::ios_base::dec | std::ios_base::fixed | 
	       std::ios_base::left);
      in >> utility::ignore_spaces();
      in >> utility::delim('[')
	 >> utility::delim(lcg64_shift::name()) >> utility::delim(' ')
	 >> P_new >> utility::delim(' ')
	 >> S_new >> utility::delim(']');
      if (in) { 
	R.P=P_new;
	R.S=S_new;
      }
      in.flags(flags);
      return in;
    }
    
    // Parallel random number generator concept
    void split(unsigned int, unsigned int);
    void jump2(unsigned int);
    void jump(unsigned long long);
    
    // Other useful methods
    static const char * name();
    long operator()(long) const;

  private:
    parameter_type P;
    mutable status_type S;
    static const char * const name_str;
    
    void backward();
    void step() const;
  };
  
  // Inline and template methods
  
  inline void lcg64_shift::step() const {
    if (use_mask)
      S.r=(P.a*S.r+P.b) & max;
    else
      S.r=(P.a*S.r+P.b);
    }
    
  inline lcg64_shift::result_type lcg64_shift::operator()() const {
    step();
    unsigned long long t=S.r;
    t^=(t>>17);
    t^=(t<<31);
    if (use_mask)
      t&=max;
    t^=(t>>8);
    return t;
  }

  inline long lcg64_shift::operator()(long x) const {
    return static_cast<long>(utility::uniformco<double, lcg64_shift>(*this)*x);
  }

}

#endif
