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

#include <trng/mrg2.hpp>

namespace trng {

  // Uniform random number generator concept
  
  // Parameter and status classes

  // EqualityComparable concept
  bool operator==(const mrg2::parameter_type &P1,
                  const mrg2::parameter_type &P2) {
    return P1.a1==P2.a1 and P1.a2==P2.a2;
  }

  bool operator!=(const mrg2::parameter_type &P1,
                  const mrg2::parameter_type &P2) {
    return not (P1==P2);
  }

  // Equality comparable concept
  bool operator==(const mrg2::status_type &S1,
                  const mrg2::status_type &S2) {
    return S1.r1==S2.r1 and S1.r2==S2.r2;
  }

  bool operator!=(const mrg2::status_type &S1,
                  const mrg2::status_type &S2) {
    return not (S1==S2);
  }

  const mrg2::parameter_type 
  mrg2::LEcuyer1=parameter_type(1498809829l, 1160990996l);
  const mrg2::parameter_type 
  mrg2::LEcuyer2=parameter_type(46325l, 1084587l);
  
  // Random number engine concept
  mrg2::mrg2(mrg2::parameter_type P) :
    P(P), S() { }

  mrg2::mrg2(unsigned long s, mrg2::parameter_type P) :
    P(P), S() {
    seed(s);
  }
  
  void mrg2::seed() {
    (*this)=mrg2();
  }

  void mrg2::seed(unsigned long s) {
    long long t=s;
    t%=modulus;
    if (t<0)
      t+=modulus;
    S.r1=static_cast<result_type>(t);
    S.r2=1;
  }
  
  void mrg2::seed(mrg2::result_type s1, mrg2::result_type s2) {
    S.r1=s1%modulus;
    if (S.r1<0)
      S.r1+=modulus;
    S.r2=s2%modulus;
    if (S.r2<0)
      S.r2+=modulus;
  }
    
  // Equality comparable concept
  bool operator==(const mrg2 &R1, const mrg2 &R2) {
    return R1.P==R2.P and R1.S==R2.S;
  }

  bool operator!=(const mrg2 &R1, const mrg2 &R2) {
    return not (R1==R2);
  }

  // Parallel random number generator concept
  void mrg2::split(unsigned int s, unsigned int n) {
    if (s<1 or n>=s)
      throw std::invalid_argument("invalid argument for trng::mrg2::split");
    long q0, q1, q2, q3;
    if (s>1) {
      for (unsigned int i(0); i<=n; ++i) step();
      q0=S.r1;
      for (unsigned int i(0); i<s; ++i) step();
      q1=S.r1;
      for (unsigned int i(0); i<s; ++i) step();
      q2=S.r1;
      for (unsigned int i(0); i<s; ++i) step();
      q3=S.r1;
      std::vector<long> a(2), b(4);
      a[0]=q2;  b[0]=q1;  b[1]=q0;
      a[1]=q3;  b[2]=q2;  b[3]=q1;
      utility::gauss(b, a, modulus);
      P.a1=a[0];  P.a2=a[1];
      S.r1=q1;    S.r2=q0;
      backward();
      backward();
    }
  }

  void mrg2::jump2(unsigned int s) {
    std::vector<long> b(4), c(4), d(2), r(2);
    long t1(P.a1), t2(P.a2);
    b[0]=P.a1;  b[1]=P.a2;
    b[2]=1l;    b[3]=0l;
    for (unsigned int i(0); i<s; ++i)
      if ((i&1)==0)
	utility::matrix_mult(b, b, c, modulus);
      else
	utility::matrix_mult(c, c, b, modulus);
    r[0]=S.r1;  r[1]=S.r2;
    if ((s&1)==0)
      utility::matrix_vec_mult(b, r, d, modulus);
    else
      utility::matrix_vec_mult(c, r, d, modulus);
    S.r1=d[0];  S.r2=d[1];
    P.a1=t1;    P.a2=t2;
  }

  void mrg2::jump(unsigned long long s) {
    unsigned int i(0);
    while (s>0) {
      if (s%2==1)
	jump2(i);
      ++i;
      s>>=1;
    }
  }
  
  // other usefull methods
  const char * const mrg2::name_str="mrg2";
  
  const char * mrg2::name() {
    return name_str;
  }

  void mrg2::backward() {
    long t;
    if (P.a2!=0l) {
      t=S.r1;
      t-=static_cast<long>((static_cast<long long>(P.a1)*
			    static_cast<long long>(S.r2))%modulus);
      if (t<0l)
	t+=modulus;
      t=static_cast<long>((static_cast<long long>(t)*
			   static_cast<long long>
			   (utility::modulo_invers(P.a2, modulus)))%modulus);
    } else if (P.a1!=0l) {
      t=S.r2;
      t=static_cast<long>((static_cast<long long>(t)*
			   static_cast<long long>
			   (utility::modulo_invers(P.a1, modulus)))%modulus);
    } else
      t=0l;
    S.r1=S.r2;  S.r2=t;
  }
  
}
