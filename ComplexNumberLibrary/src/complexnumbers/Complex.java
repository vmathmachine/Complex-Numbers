package complexnumbers;

////////////////////////////////////////////////////////// COMPLEX CLASS ////////////////////////////////////////////////////////////////////////////////////////

public class Complex extends Mafs { //this object represents a complex number
	
////////////////////////////////////////////////MEMBERS/////////////////////////////////////////
	
	public double re, im; //there are only two members of this class: the real and imaginary parts
	
/////////////////////////////////////////////// CONSTRUCTORS ////////////////////////////////////
	
	public Complex()                   { re=im=0;    } //default constructor
	public Complex(double x, double y) { re=x; im=y; } //normal constructor, inputs of real and imaginary part
	public Complex(double x)           { re=x; im=0; } //alternate constructor, inputs of real part with imaginary part assumed 0
	
////////////////////////////////////////////// BASIC FUNCTIONS /////////////////////////////////
	
	@Override
	public Complex clone()                    { return new Complex(re,im);     } //create clone
	public Complex copy ()                    { return new Complex(re,im);     } //create deep copy
	
	public void set(double x, double y)       { re=x;    im=y;                 } //assign components w/out copying reference
	public void set(Complex z)                { re=z.re; im=z.im;              }
	public void set(double x)                 { re=x;    im=0;                 }
	public void setI(double y)                { re=0;    im=y;                 }
	
	public void setPolar(double r, double ang) { re=r*Math.cos(ang); im=r*Math.sin(ang); }
	
	public boolean equals(Complex z)          { return (re==z.re && im==z.im); } //test for equality
	public boolean equals(double x, double y) { return (re==x    && im==y   ); }
	public boolean equals(double x)           { return (re==x    && im==0   ); }
	public boolean equalsI(double y)          { return (re==0    && im==y   ); } //test for equality w/ an imaginary number
	
	public boolean equalsPolar(double r, double ang) { return re==r*Math.cos(ang) && im==r*Math.sin(ang); }
	
	@Override
	public boolean equals(Object o) { //test for equality with another object
		if(o.getClass()==Complex.class) { return equals((Complex)o); }
		return false;
	}
	
	@Override
	public int hashCode() { //returns the hash code
		long x=Double.doubleToLongBits(re), y=Double.doubleToLongBits(im);
		int result = 1;
		result = 31*result + (int)(x>>32);
		result = 31*result + (int)x;
		result = 31*result + (int)(y>>32);
		result = 31*result + (int)y;
		return result;
	}
	
///////////////////////////////////////////// CLASSIFYING NUMBERS /////////////////////////////
	
	public boolean isInf()     { return Math.abs(re)==inf || Math.abs(im)==inf; } //this is true if the complex number is infinite
	public boolean isReal()    { return im==0;                     } //test for real
	public boolean isImag()    { return re==0;                     } //test for imaginary
	public boolean isInt()     { return im==0 && re%1==0;          } //test for integer
	public boolean isWhole()   { return im==0 && re%1==0 && re>=0; } //whole number
	public boolean isNatural() { return im==0 && re%1==0 && re> 0; } //natural number
	
//////////////////////////////////////////// CAST TO A STRING ////////////////////////////////
	
	@Override
	public String toString() {                                            //Complex -> String
		
		if(isInf()) {                                           //special case: infinite input
			if(Math.abs(im)==inf) { return "Complex Overflow";  } //  Complex Overflow
			else if(re<0)         { return "Negative Overflow"; } // Negative Overflow
			else                  { return "Overflow";          } //[regular] Overflow
		}
		
		Complex sto=copy();                                                    //copy input
		if(Math.abs(re)<1e-12 && Math.abs(im)>1e11*Math.abs(re)) { sto.re=0; } //small real, big imag: remove real from copy
		if(Math.abs(im)<1e-12 && Math.abs(re)>1e11*Math.abs(im)) { sto.im=0; } //big real, small imag: remove imag from copy
		
		String ret="";                                   //initialize return String to ""
		
		if(sto.re!=0||sto.im==0) { ret+=str(sto.re); }   //display real part if it's nonzero (or if the entire number is 0)
		if(sto.re!=0&&sto.im> 0) { ret+="+";         }   //real "+" imag*i, include only if real!=0 & imag is positive
		
		String Imag=str(sto.im);
		if     (Imag.equals( "1")) { ret+= "i";     } //imag part =  1 : print  i instead of  1i
		else if(Imag.equals("-1")) { ret+="-i";     } //imag part = -1 : print -i instead of -1i
		else if(sto.im!=0)         { ret+=Imag+"i"; } //else if imag!=0: print imag + "i"
		
		return ret;                                                 //return the result
	}
	
	public String polarString() { //convert to a string, but written in polar notation
		return str(abs())+" ∠ "+str(arg()); //absolute value, phaser, argument
	}
	
////////////////////////////////////////////// OBSCURE YET REALLY USEFUL FUNCTIONS //////////////////////////////////////////////
	
	public double lazyabs() { //lazy absolute value (minimal cost size test, useful for testing overflow/underflow errors)
		return Math.max(Math.abs(re),Math.abs(im)); //return the biggest of the two components
	}
	
	public boolean isRoot() { return re>0 || re==0 && im>=0; } //returns true iff z==√(z²)
	
	public int csgn() { return isRoot()?1:-1; } //csgn(z): return z/√(z²) (special case z==0: return 1)
	
	public Complex mulsgn  (double a) { return a>=0 ? copy() : neg(); } //multiply by sgn of a & create new instance
	public Complex muleqsgn(double a) { return a>=0 ? this : negeq(); } //multiply equals by sgn of a
	
	public Complex mulsgn  (Complex z) { return z.isRoot() ? copy() : neg(); } //multiply by csgn of z & create new instance
	public Complex muleqsgn(Complex z) { return z.isRoot() ? this : negeq(); } //multiply equals by csgn of z
	
	public Complex abs2() { return mulsgn(this); } //secondary absolute value, return √(z²)
	
	public Complex rotate(double ang) { //returns a rotated copy
		double cos = Math.cos(ang), sin = Math.sin(ang); return new Complex(re*cos-im*sin,re*sin+im*cos);
	}
	public Complex rotateEq(double ang) { //rotates & returns
		double cos = Math.cos(ang), sin = Math.sin(ang); set(re*cos-im*sin,re*sin+im*cos); return this;
	}
	
///////////////////////////////////////////// BASIC ARITHMETIC /////////////////////////////////////////////////
	
	//create a new instance
	
	public Complex add(double a) { return new Complex(re+a, im  ); } //overload the 4 basic arithmetic operations, both for
	public Complex sub(double a) { return new Complex(re-a, im  ); } //one real double & one complex...
	public Complex mul(double a) { return new Complex(re*a, im*a); }
	public Complex div(double a) {
		if(Math.abs(a)<=5.562684646268E-309D) { return new Complex(re/a, im/a); } //for small a, we divide each component by a
		double rec=1.0D/a; return new Complex(re*rec, im*rec);                    //else, multiply each component by 1/a
	}
	
	public Complex add(double x, double y) { return new Complex(re+x, im+y); } //...two double components & one complex...
	public Complex sub(double x, double y) { return new Complex(re-x, im-y); } 
	public Complex mul(double x, double y) { return new Complex(re*x-im*y, re*y+im*x); }
	public Complex div(double x, double y) { return div(new Complex(x,y)); }
	
	public Complex addI(double a) { return new Complex(re, im+a);    } //...one imaginary double & one complex...
	public Complex subI(double a) { return new Complex(re, im-a);    }
	public Complex mulI(double a) { return new Complex(-im*a, re*a); }
	public Complex divI(double a) {
		if(Math.abs(a)<=5.562684646268E-309D) { return new Complex(im/a, -re/a); } //for small a, we divide each component by a
		double rec=1.0D/a; return new Complex(im*rec, -re*rec);                    //else, multiply each component by 1/a
	}
	
	public Complex add(Complex a) { return new Complex(re+a.re, im+a.im);                 } //...and for 2 Complexes
	public Complex sub(Complex a) { return new Complex(re-a.re, im-a.im);                 }
	public Complex mul(Complex a) { return new Complex(re*a.re-im*a.im, re*a.im+im*a.re); }
	public Complex div(Complex a) { return mul(a.inv());                                  } //(note the inverse "inv" operation is defined later)
	
	//assign equals
	
	public Complex addeq(Complex a) { re+=a.re; im+=a.im;                    return this; } // +=
	public Complex subeq(Complex a) { re-=a.re; im-=a.im;                    return this; } // -=
	public Complex muleq(Complex a) { set(re*a.re-im*a.im, re*a.im+im*a.re); return this; } // *=
	public Complex diveq(Complex a) { return muleq(a.inv());                              } // /=
	
	public Complex addeq(double x, double y) { re+=x; im+=y;              return this; }
	public Complex subeq(double x, double y) { re-=x; im-=y;              return this; }
	public Complex muleq(double x, double y) { set(re*x-im*y, re*y+im*x); return this; }
	public Complex diveq(double x, double y) { return diveq(new Complex(x,y));         }
	
	public Complex addeqI(double a) { im+=a;            return this;   }
	public Complex subeqI(double a) { im-=a;            return this;   }
	public Complex muleqI(double a) { set(-im*a, re*a); return this;   } // Complex *= double*i
	public Complex diveqI(double a) {
		if(Math.abs(a)<=5.562684646268E-309D) { set(im/a,-re/a); return this; }
		return muleqI(-1.0D/a);
	}
	
	public Complex addeq(double a) { re+=a;         return this; } // Complex+=double
	public Complex subeq(double a) { re-=a;         return this; } // Complex-=double
	public Complex muleq(double a) { re*=a; im*=a;  return this; } // Complex*=double
	public Complex diveq(double a) {                               // Complex/=double
		if(Math.abs(a)<=5.562684646268E-309D) { re/=a; im/=a; return this; }
		return muleq(1.0D/a);
	}
	
/////////////////////////////////////// NEGATION AND OTHER SIMPLE OPERATIONS ////////////////////////////////
	
	//new instance
	public Complex neg () { return new Complex(-re,-im); } //negation
	public Complex conj() { return new Complex( re,-im); } //complex conjugate
	public Complex mulI() { return new Complex(-im, re); } //multiply by i
	public Complex divI() { return new Complex( im,-re); } //divide   by i
	
	//assign equals
	public Complex negeq()  { set(-re, -im); return this; } // negate-equals
	public Complex conjeq() { set( re, -im); return this; } // conjugate-equals (if you ever need that)
	public Complex muleqI() { set(-im,  re); return this; } // *= i
	public Complex diveqI() { set( im, -re); return this; } // /= i
	
/////////////////////////////////////// COMPLEX PARTS ////////////////////////////////////////////////////
	
	public double re() { return re; } //real part
	public double im() { return im; } //imaginary part
	
	public double absq() { return re*re+im*im; } //absolute square
	
	public double abs()  {                       //absolute value
		if(im==0) { return Math.abs(re); } //if it's real, return abs(Re(x))
		if(re==0) { return Math.abs(im); } //if it's imaginary, return abs(Im(x))
		if(isInf()) { return inf; }
		
		double L=lazyabs();                    //take lazy abs for a quick sense of scale
		if(L<=1.055E-154D || L>=9.481E+153D) { //absolute square overflows/underflows:
			return div(L).abs()*L;             //divide by L, take absolute value, multiply back by L
		}
		return Math.sqrt(absq()); //general case: return the square root of the absolute square
	}
	
	public double arg()  {                             //polar argument
		if(im==0) { return re>=0 ? 0 : Math.PI;      } //real      number: return either 0 or π
		if(re==0) { return im>=0 ? HALFPI : -HALFPI; } //imaginary number: return ±π/2
		return Math.atan2(im, re);                     //general case: find the angle with atan2
	}
	
	public Complex sgn() { //signum
		if(equals(0)) { return new Complex(); } //0: return 0
		return div(abs());                      //otherwise: return this divided by the absolute value
	} //NOTE: doesn't work with infinite numbers
	
///////////////////////////////////////// RECIPROCAL, SQUARE ROOT, AND OTHER IMPORTANT FUNCTIONS ////////////////////
	
	public Complex inv() {                       //reciprocal
		if(im==0)   { return new Complex(1.0/re);    } //real      number: return  1/(real part)
		if(re==0)   { return new Complex(0,-1.0/im); } //imaginary number: return -i/(imag part)
		if(isInf()) { return new Complex();          } //infinite  number: return 0
		
		double L=lazyabs();                    //take lazy abs for a quick sense of scale
		
		if(L<=1.055E-154D || L>=9.481E+153D) {                 //absolute square overflows/underflows:
			if(L<=5.563E-309D) { return div(L).inv().div(L);   } //divide by L, invert, divide by L again
			double k=1.0/L;      return mul(k).inv().muleq(k);   //if L > 2^-1024, solve 1/L beforehand to save on divisions
		}
		return conj().muleq(1.0/absq()); //general case: return the conjugate over the absolute square
	}
	
	public Complex sq () { return new Complex((re+im)*(re-im),2*re*im); } //z²
	public Complex cub() { return mul(sq());                            } //z³
	
	public Complex sqrt() { //√(z)
		
		if(equals(0))                      { return new Complex();                        } //special case: √(0)=0
		if(isInf()) {                                                             		    //special case: infinite input
			if(im!=inf) { return re==inf ? new Complex(inf) : new Complex(0,sgn(im)*inf); } //non-inf imag part: return either ∞ or ±∞i
			return new Complex(inf,im);                                           		   //otherwise, reutrn ∞±∞i
		}
		if(2*Math.abs(re)+Math.abs(im)==inf) { return mul(0.25D).sqrt().muleq(2D); } //special case: |z|+|x|=Overflow, return √(z/4)*2
		
		//the formula for √(z) = √((|z|+x)/2) + sgn(y)√((|z|-x)/2)i    (where z = x + yi)
		//since √((|z|+x)/2) * sgn(y)√((|z|-x)/2) = y/2, we'll just find one sqrt & use division to find the other
		
		double part1=Math.sqrt(0.5D*(abs()+Math.abs(re))); //compute whichever sqrt has the least roundoff error
		
		if(re>=0) { return new Complex(part1, im/(2.0D*part1));              } //if x>0, that'd be the real part
		else      { return new Complex(im/(2.0D*part1), part1).muleqsgn(im); } //if x<0, that'd be the imaginary part
	}
	
	public Complex cbrt() { //cube root of complex z
		if(im==0)                { return new Complex(Math.cbrt(re));              } //z is real    : return cbrt(re(z))
		if(isInf())              { return new Complex(inf,Math.abs(im)==inf?im:0); } //special case: infinite, return ∞
		if(lazyabs()>1.271E308D) { return mul(0.125D).cbrt().muleq(2D);            } //|z| overflows: return 2cbrt(z/8)
		
		return new Complex(0,arg()/3).exp().muleq(Math.cbrt(abs()));                 //general case : return e^(arg(z)/3)*cbrt(|z|)
	}
	
	public Complex exp() { //e^z
		if(im==0) { return new Complex(Math.exp(re));                  }   //real number : return e^(real part)
		if(re==0) { return new Complex(Math.cos(im),Math.sin(im));     }   //imag number : return cos(imag)+sin(imag)*i
		if(re>709.78 && re<710.13) { return sub(log2).exp().muleq(2D); }   //large real number: subtract ln(2), take exponent, multiply by 2
		
		return new Complex(Math.cos(im),Math.sin(im)).muleq(Math.exp(re)); //general case: return e^(real)*(cos(imag)+sin(imag)*i)
	}
	
	public Complex ln() { //ln(z)
		if(re==0||im==0) { return new Complex(Math.log(abs()), arg()); } //real/imaginary number: return ln|z|+arg(z)i
		if(isInf())      { return new Complex(inf, arg());             } //infinite number: return ∞+arg(z)i
		
		double L=lazyabs();                      //take lazy abs for a quick sense of scale
		if(L<=1.055E-154D || L>=9.481E+153D) {   //absolute square overflows/underflows:
			return div(L).ln().addeq(Math.log(L)); //divide by L, take the log, and add back ln(L)
		}
		return new Complex(0.5D*Math.log(absq()), arg()); //general case: return ln(|z|²)/2+arg(z)i
	}
	
	public Complex pow(int a) { //Complex z ^ int a (exponentiation by squaring)
		
		if(im==0) { return new Complex(dPow(re,a)); } //input is real: use the other implementation for doubles (results in ~1/4 the # of multiplications)
		
		if(a<0)  { return inv().pow(-a); } //a is negative: return (1/z)^(-a)
		//general case:
		Complex ans=new Complex(1,0);   //return value: z^a (init to 1 in case a==0)
		int ex=a;                       //copy of a
		Complex iter=copy();            //z ^ (2 ^ (whatever digit we're at))
		boolean inits=false;            //true once ans is initialized (to something other than 1)
		
		while(ex!=0) {                               //loop through all a's digits (if a==0, exit loop, return 1)
			if((ex&1)==1) {
				if(inits) { ans.muleq(iter);           } //mult ans by iter ONLY if this digit is 1
				else      { ans.set(iter); inits=true; } //if ans still = 1, set ans=iter (instead of multiplying by iter)
			}
			ex >>= 1;                                    //remove the last digit
			if(ex!=0)     { iter.muleq(iter); }          //square the iterator (unless the loop is over)
		}
		
		return ans; //return the result
	}
	
	public Complex pow(double a) { //complex z ^ double a
		if(Math.abs(a)<=2147483647 && a%1==0) { return pow((int)a);                 } //exponent is an integer: there's a faster (and more accurate) way of doing this
		if(im==0 && (re>0||a%1==0))           { return new Complex(Math.pow(re,a)); } //if the base is real and non-negative, or it's negative but the exponent is an integer, we'll just use the built in power function 
		
		double mag;                                   //compute |z|^a
		double L=lazyabs();                           //use lazy absolute value for a sense of scale
		if     (L>=1.05476867E-154D && L<=9.48075190E+153D) { mag=Math.pow(absq(),0.5*a); } //if within range, take (|z|²)^(a/2)
		else if(L>=7.86682407E-309D && L<=1.27116100E+308D) { mag=Math.pow(abs() ,    a); } //if within another range, take |z|^a
		else                                   { return div(L).pow(a).mul(Math.pow(L,a)); } //if outside both ranges, divide by L, raise to a-th power, mult by L^a
		
		Complex unit=new Complex(0,a*arg()).exp();    //create a phaser with angle a*θ
		return unit.muleq(mag);                       //return the magnitude times the phaser
	}
	
	public Complex pow(Complex a) {              //complex z ^ complex a
		if(equals(e)) { return a.exp();   } //z==e        : return e^a
		if(a.im==0)   { return pow(a.re); } //a is real   : return complex z ^ double a.re
		return ln().muleq(a).exp();         //general case: return e to the power of the log times a
	}
	
//////////////////// ROUNDING & MODULOS ////////////////////////////////////
	
	public double floor() { return Math.floor(re); }
	public double ceil () { return Math.ceil(re);  }
	public double round() { return Math.round(re); }
	
	public Complex mod(Complex a) {         //Complex modulo (sign standard: +%+ = +, +%- = -, -%+ = +, -%- = -)
		Complex part=a.mul(div(a).floor()); //part: the largest* integer multiple of a that fits in "this": / by a, round down, * back by a
		return sub(part);                   //subtract this multiple
	}
	//* "largest" meaning the largest integer that multiplies by a
	
	public Complex mod_v2(Complex a) {                  //Secondary modulo: drops sign convention & just returns whichever modulo is closer to 0
		Complex part=a.mul(-div(a).neg().round()); //part: the closest integer multiple of a to "this": / by -a, round, * back by -a
		return sub(part);                          //subtract this multiple
	}                                            //note, if two multiples are equally close, we default to the multiple corresponding to the lower integer (hence why a is negated)
	
	//when adding logarithms, you can use this tool (with "a" set to 2πi) to ensure the imaginary part is within the range (-π,π]
	
//////////////////// TRIGONOMETRY //////////////////////////////////////
	
	public Complex cosh() {                                                 //cosh
		if(im==0) { return new Complex(Math.cosh(re)); } //real input: return cosh
		if(re==0) { return new Complex(Math.cos (im)); } //imag input: return cos
		
		double[] sinhcosh=fsinhcosh(re);                 //compute sinh & cosh of real part
		return new Complex(sinhcosh[1]*Math.cos(im),sinhcosh[0]*Math.sin(im)); //cosh(x+yi) = cosh(x)cos(y)+sinh(x)sin(y)i
	}
	
	public Complex sinh() {                                                  //sinh
		if(im==0) { return new Complex( Math.sinh(re)); } //real input: return sinh
		if(re==0) { return new Complex(0,Math.sin(im)); } //imag input: return sin*i
		
		double[] sinhcosh=fsinhcosh(re);                  //compute sinh & cosh of real part
		return new Complex(sinhcosh[0]*Math.cos(im),sinhcosh[1]*Math.sin(im)); //sinh(x+yi) = sinh(x)cos(y)+cosh(x)sin(y)i
	}
	public Complex tanh() {                                                     //tanh
		if(im==0) 	 { return new Complex( Math.tanh(re)); } //real input: return tanh
		if(re==0)    { return new Complex(0,Math.tan(im)); } //imag input: return tan*i
		if(Math.abs(re)>20) { return new Complex(sgn(re)); } //large input: return +-1
		
		double[] sinhcosh=fsinhcosh(2*re);                   //compute sinh & cosh of twice the real part
		double denom = 1.0D/(sinhcosh[1]+Math.cos(2*im));    //compute 1/(cosh(2x)+cos(2y))
		return new Complex(sinhcosh[0]*denom, Math.sin(2*im)*denom); //tanh(x+yi) = (sinh(2x)+sin(2y)i)/(cosh(2x)+cos(2y))
	}
	
	public Complex cos() { return mulI().cosh();          } //cos
	public Complex sin() { return mulI().sinh().diveqI(); } //sin
	public Complex tan() { return mulI().tanh().diveqI(); } //tan
	
	public Complex sec() { return cos().inv(); }  public Complex sech() { return cosh().inv(); } //sec & sech
	public Complex csc() { return sin().inv(); }  public Complex csch() { return sinh().inv(); } //csc & csch
	public Complex cot() { return tan().inv(); }  public Complex coth() { return tanh().inv(); } //cot & coth
	
//////////////////////////////////////////////////////////////////////////INVERSE TRIGONOMETRY////////////////////////////////////////////////////////////////////////////////////
	
	public Complex acosh() {                                                //arcosh
		if(im==0&&Math.abs(re)<=1) { return new Complex(0,Math.acos(re)); } //real input [-1,1]: return acos*i
		
		if(absq()>1E18D) { return ln().addeq(log2); }           //huge input: return asymptotic approximation
		
		return add( sq().subeq(1).sqrt().muleqsgn(this) ).ln(); //else: return ln(z+csgn(z)√(z²-1))
	}
	public Complex asinh() {                                                //arsinh
		if(re==0&&Math.abs(im)<=1) { return new Complex(0,Math.asin(im)); } //imag input [-i,i]: return asin*i
		
		if(absq()>1E18D) { return abs2().ln().addeq(log2).muleqsgn(this); } //huge input: return asymptotic approximation
		if(lazyabs()<=1E-4D) { return mul(Cpx.sub(1,sq().div(6))); }        //tiny input: return taylor's series
		
		return (abs2().addeq( sq().add(1).sqrt() )).ln().muleqsgn(this); //else: return csgn(z)ln(|z|+√(z²+1))
	}
	public Complex atanh() {                                                               //artanh
		if(im==0 && Math.abs(re)==1)           { return new Complex(re==1 ? inf : -inf); } //special case: atanh(±1)=±∞
		if(isInf()) { return new Complex(0,(im>0 || im==0 && re<=1) ? HALFPI : -HALFPI); } //special case: z is infinite, return ±πi/2
		
		if(re==0)            { return new Complex(0,Math.atan(im)); } //imag input: return atan*i
		if(lazyabs()<=1E-4D) { return mul(sq().div(3).addeq(1));    } //tiny input: return taylor's series
		
		Complex ans=(add(1).diveq(Cpx.sub(1,this))).ln().muleq(0.5D); //else      : atanh(z)=ln((1+z)/(1-z))/2
		if(im==0&&re>1) { ans.im=-HALFPI; }                           //(special case) z is real & >1: negate im to keep function odd
		return ans;                                                   //return answer
	}
	
	public Complex acos() {                                                 //acos
		if(im==0 && Math.abs(re)<=1) { return new Complex(Math.acos(re)); } //real input [-1,1]: return acos
		return mulI().asinh().muleqI().addeq(HALFPI);                       //else             : return π/2-asin
	}
	public Complex asin() { return mulI().asinh().diveqI(); } //asin
	public Complex atan() { return mulI().atanh().diveqI(); } //atan
	
	public Complex asec() { return inv().acos(); }  public Complex asech() { return inv().acosh(); } //asec and asech
	public Complex acsc() { return inv().asin(); }  public Complex acsch() { return inv().asinh(); } //acsc and acsch
	public Complex acot() { return inv().atan(); }  public Complex acoth() { return inv().atanh(); } //acot and acoth
}
