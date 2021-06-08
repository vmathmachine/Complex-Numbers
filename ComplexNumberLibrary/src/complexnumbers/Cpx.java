package complexnumbers;

public class Cpx extends Complex {
	
////////////////EXTERNAL CONSTRUCTORS//////////////////////
	
	public Complex zero()            { return new Complex();    } //create instance of 0+0i
	public Complex one ()            { return new Complex(1);   } //create instance of 1+0i
	public Complex Complex(double d) { return new Complex(d);   } //create instance of d+0i (cast double to Complex)
	public Complex iTimes (double d) { return new Complex(0,d); } //create instance of 0+di
	
	//we will now overload (most of) the functions from complex so that the complex numbers can be called as parameters
	
	public String str(Complex z) { return z.toString(); } //this generates the complex number in text form
	
///////////////COMPLEX PARTS///////////////////////////
	public double re(Complex z) { return z.re; } //real & imaginary parts
	public double im(Complex z) { return z.im; }
	
	public double absq(Complex z) { return z.absq(); } //square absolute value
	public double abs (Complex z) { return z.abs();  } //absolute value
	public double arg (Complex z) { return z.arg();  } //polar argument
	
	public Complex conj(Complex z) { return z.conj(); } //complex conjugate
	
////////////////BASIC ARITHMETIC//////////////////////
	
	public Complex add(Complex a, double b) { return a.add(b); } //these functions perform the four basic arithmetic
	public Complex sub(Complex a, double b) { return a.sub(b); } //operations on one real and one complex number
	public Complex mul(Complex a, double b) { return a.mul(b); }
	public Complex div(Complex a, double b) { return a.div(b); }
	
	public Complex add(double a, Complex b) { return b.add(a);         } //these functions perform the four basic arithmetic
	public Complex sub(double a, Complex b) { return b.neg().addeq(a); } //operations on one real and one complex number
	public Complex mul(double a, Complex b) { return b.mul(a);         }
	public Complex div(double a, Complex b) { return inv(b).muleq(a);  }
	
	public Complex add(Complex a, Complex b) { return a.add(b); } //these functions perform the four basic arithmetic
	public Complex sub(Complex a, Complex b) { return a.sub(b); } //operations on two complex numbers
	public Complex mul(Complex a, Complex b) { return a.mul(b); }
	public Complex div(Complex a, Complex b) { return a.div(b); }
	
	public Complex neg(Complex z) { return z.neg(); }
	
	public Complex add(Complex a, Complex b, double c) { return a.add(b).addeq(c); } //3 input add (with doubles)
	public Complex mul(Complex a, Complex b, double c) { return a.mul(b).muleq(c); } //3 input multiply (with doubles)
	
	public Complex add(Complex a, Complex b, Complex... c) { //unlimited input sum (all Complex)
		Complex ret=a.add(b);                 //add initial two inputs
		for(Complex c2: c) { ret.addeq(c2); } //add equal all other inputs
		return ret;                           //return the result
	}
	public Complex mul(Complex a, Complex b, Complex... c) { //unlimited input multiply (all Complex)
		Complex ret=a.mul(b);                 //multiply initial two inputs
		for(Complex c2: c) { ret.muleq(c2); } //multiply equal all other inputs
		return ret;                           //return the result
	}
	
////////////////RECIPROCAL, SQUARE ROOT, & OTHER IMPORTANT FUNCTIONS////////////
	
	public Complex inv (Complex z) { return z.inv();  } //1/z
	public Complex sq  (Complex z) { return z.sq();   } //z²
	public Complex cub (Complex z) { return z.cub();  } //z cubed
	
	public Complex sqrt(Complex z) { return z.sqrt(); } //√(z)
	public Complex cbrt(Complex z) { return z.cbrt(); } //cbrt(z)
	public Complex exp(Complex z)  { return z.exp();  } //e^z
	public Complex ln(Complex z)   { return z.ln();   } //ln(z)
	
	public Complex log10(Complex z) { return z.ln().muleq(0.434294481903251828D); } //base 10 log...for anyone who wanted that
	
	public Complex pow(Complex a, int     b) { return a.pow(b); } //this computes a to the power of an integer
	public Complex pow(Complex a, double  b) { return a.pow(b); } //this computes a to the power of a real double
	public Complex pow(Complex a, Complex b) { return a.pow(b); } //this computes a to the power of another complex number
	
//////////////////////////////////////////////////////////////////////////////////TRIGONOMETRY////////////////////////////////////////////////////////////////////////////////////
	
	public Complex cosh(Complex z) {                                                //cosh
		if(z.im==0)            { return new Complex(Math.cosh(z.re)); } //real input: return cosh
		if(z.re==0)            { return new Complex(Math.cos (z.im)); } //imag input: return cos
		if(Math.abs(z.re)>709) { return exp(z.abs2().subeq(log2));    } //huge input: return e^(|z|-ln(2))
		Complex part=exp(z);
		return part.addeq(part.inv()).muleq(0.5D);                      //else: return (e^z+e^-z)/2
	}
	public Complex sinh(Complex z) {                                                         //sinh
		if(z.im==0)            { return new Complex(Math.sinh(z.re));          } //real input: return sinh
		if(z.re==0)            { return iTimes(Math.sin(z.im));                } //imag input: return sin*i
		if(Math.abs(z.re)>709) { return exp(z.abs2().subeq(log2)).muleqsgn(z); } //huge input: return ±e^(|z|-ln(2))
		if(z.lazyabs()<=1E-4D) { return z.mul(add(1,sq(z).div(6)));            } //tiny input: return z-z^3/6
		Complex part=exp(z);
		return part.subeq(part.inv()).muleq(0.5D);                               //else: return (e^z-e^-z)/2
	}
	public Complex tanh(Complex z) {                                         //tanh
		if(z.im==0) { return Complex(Math.tanh(z.re));              } //real input: return tanh
		if(z.re==0) { return iTimes(Math.tan(z.im));                } //imag input: return tan*i
		if(Math.abs(z.re)>300) { return new Complex(z.csgn());      } //huge input: return ±1
		if(z.lazyabs()<=1E-4D) { return z.mul(sub(1,sq(z).div(3))); } //tiny input: return z+z^3/3
		Complex part=exp(z.mul(2));
		return div(sub(part,1),add(part,1));                     //else: return (e^(2z)-1)/(e^(2z)+1)
	}
	
	public Complex cos(Complex z) { return cosh(z.mulI());          } //cos
	public Complex sin(Complex z) { return sinh(z.mulI()).diveqI(); } //sin
	public Complex tan(Complex z) { return tanh(z.mulI()).diveqI(); } //tan
	
	public Complex sec(Complex z) { return cos(z).inv(); }  public Complex sech(Complex z) { return cosh(z).inv(); } //sec and sech
	public Complex csc(Complex z) { return sin(z).inv(); }  public Complex csch(Complex z) { return sinh(z).inv(); } //csc and csch
	public Complex cot(Complex z) { return tan(z).inv(); }  public Complex coth(Complex z) { return tanh(z).inv(); } //cot and coth
	
//////////////////////////////////////////////////////////////////////////INVERSE TRIGONOMETRY////////////////////////////////////////////////////////////////////////////////////
	
	public Complex acosh(Complex z) {                                                  //arcosh
		if(z.im==0&&Math.abs(z.re)<=1) { return iTimes(Math.acos(z.re)); } //real input [-1,1]: return acos*i
		
		if(z.absq()>1E18D) { return ln(z).addeq(log2); } //huge input: return asymptotic approximation
		
		return ln(z.add( sqrt(sub(sq(z),1)).muleqsgn(z) )); //else: return ln(z+csgn(z)√(z²-1))
	}
	public Complex asinh(Complex z) {                                                  //arsinh
		if(z.re==0&&Math.abs(z.im)<=1) { return iTimes(Math.asin(z.im)); } //imag input [-i,i]: return asin*i
		
		if(z.absq()>1E18D) { return ln(z.abs2()).addeq(log2).muleqsgn(z); } //huge input: return asymptotic approximation
		if(z.lazyabs()<=1E-4D) { return z.mul(sub(1,sq(z).div(6))); }       //tiny input: return taylor's series
		
		return ln(z.abs2().addeq( sqrt(add(sq(z),1)) )).muleqsgn(z); //else: return csgn(z)ln(|z|+√(z²+1))
	}
	public Complex atanh(Complex z) {                                //artanh
		if(z.im==0 && Math.abs(z.re)==1)    { return new Complex(z.re==1 ? inf : -inf); } //special case: atanh(±1)=±∞
		if(z.isInf()) { return iTimes(z.im>0 || z.im==0 && z.re<=1 ? HALFPI : -HALFPI); } //special case: z is infinite, return ±πi/2
		
		if(z.re==0)            { return iTimes(Math.atan(z.im));    } //imag input: return atan*i
		if(z.lazyabs()<=1E-4D) { return z.mul(add(1,sq(z).div(3))); } //tiny input: return taylor's series
		
		Complex ans=ln(add(1,z).diveq(sub(1,z))).muleq(0.5D); //else      : atanh(z)=ln((1+z)/(1-z))/2
		if(z.im==0&&z.re>1) { ans.im=-HALFPI; }               //(special case) z is real & >1: negate im to keep function odd
		return ans;                                           //return answer
	}
	
	public Complex acos(Complex z) {                                                          //acos
		if(z.im==0 && Math.abs(z.re)<=1) { return new Complex(Math.acos(z.re)); } //real input [-1,1]: return acos
		return asinh(z.mulI()).muleqI().addeq(HALFPI);                            //else             : return π/2-asin
	}
	public Complex asin(Complex z) { return asinh(z.mulI()).diveqI(); } //asin
	public Complex atan(Complex z) { return atanh(z.mulI()).diveqI(); } //atan
	
	public Complex asec(Complex z) { return acos(z.inv()); }  public Complex asech(Complex z) { return acosh(z.inv()); } //asec and asech
	public Complex acsc(Complex z) { return asin(z.inv()); }  public Complex acsch(Complex z) { return asinh(z.inv()); } //acsc and acsch
	public Complex acot(Complex z) { return atan(z.inv()); }  public Complex acoth(Complex z) { return atanh(z.inv()); } //acot and acoth
}
