package complexnumbers;

//Don't be intimidated by the line count.  About half of the lines are just javadoc comments.

////////////////////////////////////////////////////////// COMPLEX CLASS ////////////////////////////////////////////////////////////////////////////////////////

/**
 * A Complex Object.
 * 
 * <br>Each instance of <code>Complex</code> represents a complex number.
 * <br>It has two components, <code>re</code> and <code>im</code>, each double precision floating points representing the
 * real and imaginary parts of the number.  There are no illegal permutations of <code>re</code> and <code>im</code>, and thus
 * both attributes are public.
 * 
 * <br>It should be noted that, due to the way this was implemented, the Infinite numbers and NaN numbers are practically interchangeable.
 * 
 * @author Math Machine
 * @version 1.0.0
 */

public class Complex extends Mafs { //this object represents a complex number.
	
//////////////////////////////////////////////// ATTRIBUTES /////////////////////////////////////////
	
	/** The real part. Since there are no illegal numbers, this attribute is public. */
	public double re;
	/** The imaginary part. Since there are no illegal numbers, this attribute is public. */
	public double im;
	
	/** (static variable) If true, cbrt(-1)=-1.  If false, cbrt(-1)=(1+√(3)i)/2. */
	public static boolean cbrt_Option=true;
	
/////////////////////////////////////////////// CONSTRUCTORS ////////////////////////////////////
	
	/** Default constructor: returns 0+0i*/
	public Complex() { re=im=0; }
	/**
	 * Constructor with real and imaginary parts as parameters, constructs x+yi
	 * @param x - real part
	 * @param y - imaginary part
	 */
	public Complex(double x, double y) { re=x; im=y; }
	/**
	 * Constructor with one parameter, constructs x+0i.
	 * @param x - real part
	 */
	public Complex(double x) { re=x; im=0; }
	
	/**
	 * Pseudo-constructor with real and imaginary parts as parameters, constructs x+yi, before performing a check to avoid
	 * the undesirable permutations of NaN±∞i or ±∞+NaN*i.  See <code>validate()</code> for more information.
	 * 
	 * @param x - the real part
	 * @param y - the imaginary part
	 * @return - the constructed object
	 */
	public Complex compCheck(double x, double y) { return new Complex(x,y).validate(); }
	
////////////////////////////////////////////// BASIC FUNCTIONS /////////////////////////////////
	
	/** Returns a deep copy @return a deep copy */
	@Override
	public Complex clone() { return new Complex(re,im); } //create clone
	/** Returns a deep copy @return a deep copy */
	public Complex copy () { return new Complex(re,im); } //create deep copy
	
	/**
	 * Setter - sets the real and imaginary parts
	 * @param x - the new real part
	 * @param y - the new imaginary part
	 */
	public void set(double x, double y) { re=x;    im=y;    }
	/**
	 * Setter - sets this equal to a deep copy of z
	 * @param z - what gets copied
	 */
	public void set(Complex z)          { re=z.re; im=z.im; }
	/**
	 * Setter - sets this equal to the real number x+0i
	 * @param x - the new real part
	 */
	public void set(double x)           { re=x;    im=0;    }
	/**
	 * Setter - sets this equal to the imaginary number 0+yi
	 * @param y - the new imaginary part
	 */
	public void setI(double y)          { re=0;    im=y;    }
	
	/**
	 * Setter - sets equal to x+yi, then performs a check to avoid the undesirable permutations of NaN±∞i or ±∞+NaN*i.
	 * See <code>validate()</code> for more information.
	 * @param x - the new real part
	 * @param y - the new imaginary part
	 */
	public void setCheck(double x, double y) { re=x; im=y; validate(); }
	
	/**
	 * Setter - sets the components in polar notation, setting the magnitude and angle rather than the real and imaginary parts
	 * @param r - the new absolute value
	 * @param ang - the new argument
	 */
	public void setPolar(double r, double ang) { re=r*cos(ang); im=r*sin(ang); validate(); }
	
	/**
	 * Setter - sets the real part to x
	 * @param x the real part
	 */
	public void setRe(double x) { re=x; }
	/**
	 * Setter - sets the imaginary part to y
	 * @param y the imaginary part
	 */
	public void setIm(double y) { im=y; }
	/**
	 * Setter - sets the absolute value to r (without changing the angle)
	 * @param r the absolute value
	 */
	public void setAbs(double r) { muleq(r/abs()); }
	/**
	 * Setter - sets the argument to a (without changing the absolute value)
	 * @param a the argument
	 */
	public void setArg(double a) { double abs = abs(); set(abs*cos(a), abs*sin(a)); }
	
	/**
	 * Tests for equality between two <code>Complex</code> objects. Returns true if their components are equal or if they're both NaN.
	 * @param z the complex number we test for equality with
	 * @return  whether or not they equal
	 */
	public boolean equals(Complex z) { return (re==z.re && im==z.im) || (isNaN() && z.isNaN()); }
	/**
	 * Tests for equality with x+yi.
	 * @param x - the real part
	 * @param y - the imaginary part
	 * @return  - whether or not they equal
	 */
	public boolean equals(double x, double y) { return (re==x && im==y) || (isNaN() && (x!=x||y!=y)); }
	/**
	 * Tests for equality with a real number x.  Returns true if re==x and im==0.
	 * @param x - the real number
	 * @return  - whether or not they equal.
	 */
	public boolean equals(double x) { return (re==x && im==0) || (isNaN() && x!=x); }
	/**
	 * Tests for equality with imaginary number y.  Returns true if re==0 and im==y
	 * @param y - the imaginary number
	 * @return  - whether or not they equal
	 */
	public boolean equalsI(double y) { return (re==0 && im==y) || (isNaN() && y!=y); } //test for equality w/ an imaginary number
	
	/**
	 * Tests for equality with the number r∠ang (in polar notation).
	 * @param r - the magnitude
	 * @param ang - the angle
	 * @return - whether or not they equal
	 */
	public boolean equalsPolar(double r, double ang) { return re==r*cos(ang) && im==r*sin(ang); }
	
	/** Returns true if the object is a complex with the same value */
	@Override
	public boolean equals(Object o) { //test for equality with another object
		if(o.getClass()==Complex.class) { return equals((Complex)o); }
		return false;
	}
	
	@Override
	public int hashCode() { //returns the hash code
		if(isNaN()) { return -585754751; } //if NaN, return the same hashCode we'd get from NaN+0i
		
		long x=Double.doubleToLongBits(re), y=Double.doubleToLongBits(im); //get the bits from the real & imaginary parts
		int result = 1;
		result = 31*result + (int)(x>>32); //compute the hashcode from 4 ints.  2 from splitting up the re, 2 from splitting up the im.
		result = 31*result + (int)x;
		result = 31*result + (int)(y>>32);
		result = 31*result + (int)y;
		return result;
	}
	
	/**
	 * Performs a check to avoid the undesirable state of NaN±∞i or ±∞+NaN*i, replacing them with 0±∞i and ±∞+0i, respectively.  It then
	 * returns the result.  This function is a mutator.
	 * 
	 * These states typically arise when dividing (0+yi)/0 or (x+0i)/0.  The actual answer is supposed to be 0±∞i or ±∞+0i, but the
	 * result comes up with NaN, since technically that 0 component could be anything.  It should be noted, this library does not do a
	 * very good job at dealing with infinite values.  Often times, calculations that should yield some sort of infinity instead yield a
	 * NaN, and while using this function doesn't completely fix this issue, it does help prevent the issue most of the time.  With the
	 * notable exception of ±∞±∞i multiplied by x+0i or 0+yi, which results in NaN+NaN*i.
	 * 
	 * @return the object which was validated
	 */
	public Complex validate() { //validate: removes an undesirable (though not illegal) state
		if(re==re && im==im) { return this; } //if not NaN, leave
		if     (Math.abs(re)==inf) { im=0; }  //if re is infinite & im is NaN, make im=0
		else if(Math.abs(im)==inf) { re=0; }  //if im is infinite & re is NaN, make re=0
		return this;                          //return
	}
	
///////////////////////////////////////////// CLASSIFYING NUMBERS /////////////////////////////
	
	/** Whether the input is infinite @return whether the number is infinite*/
	public boolean isInf()     { return Double.isInfinite(re) || Double.isInfinite(im); } //this is true if the complex number is infinite
	/** @return whether the number is NaN*/
	public boolean isNaN()     { return re!=re || im!=im;                               } //this is true if the complex number is NaN
	
	/** @return whether the number is real*/
	public boolean isReal()    { return im==0;                     } //test for real
	/** @return whether the number is imaginary*/
	public boolean isImag()    { return re==0;                     } //test for imaginary
	/** @return whether the number is a real integer*/
	public boolean isInt()     { return im==0 && re%1==0;          } //test for integer
	/** @return whether the number is a whole number (a non-negative integer)*/
	public boolean isWhole()   { return im==0 && re%1==0 && re>=0; } //whole number
	/** @return whether the number is a natural number (a positive integer)*/
	public boolean isNatural() { return im==0 && re%1==0 && re> 0; } //natural number
	
//////////////////////////////////////////// CAST TO A STRING ////////////////////////////////
	
	/**
	 * If you were to write this complex number out as a string (in rectangular notation), this is what you'd get.
	 */
	@Override
	public String toString() {                                    //Complex -> String
		
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
		
		String imag=str(sto.im);
		if     (imag.equals( "1")) { ret+= "i";     } //imag part =  1 : print  i instead of  1i
		else if(imag.equals("-1")) { ret+="-i";     } //imag part = -1 : print -i instead of -1i
		else if(sto.im!=0)         { ret+=imag+"i"; } //else if imag!=0: print imag + "i"
		
		return ret;                                                 //return the result
	}
	
	/**
	 * If you were to write this complex number out as a string (in polar notation), this is what you'd get.
	 * @return casts to a string, but in polar notation
	 */
	public String polarString() { //convert to a string, but written in polar notation
		return str(abs())+" ∠ "+str(arg()); //absolute value, phaser, argument
	}
	
////////////////////////////////////////////// OBSCURE YET REALLY USEFUL FUNCTIONS //////////////////////////////////////////////
	
	/**
	 * "lazy absolute value", a minimal cost test which returns the approximate magnitude of the number.
	 * It's useful for certain algorithms where overflow/underflow can be a problem.  Namely, where you need to use one algorithm when
	 * the input is small, and another algorithm when the input is large, but there's a large range of values where both algorithms will
	 * perform equally well.
	 * @return the lazy absolute value, equivalent to max(|re|, |im|)
	 */
	public double lazyabs() { //lazy absolute value (minimal cost size test, useful for testing overflow/underflow errors)
		return Math.max(Math.abs(re),Math.abs(im)); //return the biggest of the two components
	}
	
	/**
	 * Whether or not the input is the principal square root of its square.  For real numbers, this calculation is just z>=0.
	 * But for complex numbers, it's a bit more complicated.  This function often comes in handy when determining the sign of a square
	 * root, for instance, in the <code>acosh</code> and <code>asinh</code> functions.
	 * @return true if and only if z==√(z²)
	 */
	public boolean isRoot() { return re>0 || re==0 && im>=0; }
	
	/**
	 * "Complex signum", an official function which returns 1 if the input is the square root of its square, -1 otherwise.  In other words,
	 * it's equal to z/√(z²) for z!=0.
	 * @return z/√(z²) if z!=0, 1 if z==0.
	 */
	public int csgn() { return isRoot()?1:-1; }
	
	/**
	 * "Multiply by csgn", returns a copy of the input multiplied by the csgn of a.
	 * This gets its own function because the calculation doesn't require any multiplications.
	 * @param a - the number we multiply by the sign of.
	 * @return - the product
	 */
	public Complex mulcsgn  (double a) { return a>=0 ? copy() : neg(); }
	/**
	 * "Multiply by csgn", returns a copy of the input multiplied by the csgn of z.
	 * This gets its own function because the calculation doesn't require any multiplications.
	 * @param z - the number we multiply by the sign of.
	 * @return - the product
	 */
	public Complex mulcsgn  (Complex z) { return z.isRoot() ? copy() : neg(); } //multiply by csgn of z & create new instance
	/**
	 * "Multiply equals csgn", multiplies the input by the csgn of a, then returns the result.  Mutates the original object.
	 * This gets its own function because the calculation doesn't require any multiplications.
	 * @param a - the number we multiply by the sign of
	 * @return - the product
	 */
	public Complex muleqcsgn(double a) { return a>=0 ? this : negeq(); } //multiply equals by sgn of a
	/**
	 * "Multiply equals csgn", multiplies the input by the csgn of a, then returns the result.  Mutates the original object.
	 * This gets its own function because the calculation doesn't require any multiplications.
	 * @param z - the number we multiply by the sign of
	 * @return - the product
	 */
	public Complex muleqcsgn(Complex z) { return z.isRoot() ? this : negeq(); } //multiply equals by csgn of z
	
	/**
	 * "Secondary absolute value", a non-official function which behaves similarly to the absolute value for real inputs.
	 * While √(z²) is often just shortened to |z| whenever we assume the input is a real number, for complex numbers, this is no
	 * longer the case.  However, since this still comes up for complex numbers just as it does real numbers, and since square roots
	 * are computationally expensive, I've added this function to evaluate √(z²) without using any square roots (or even any
	 * multiplications).
	 * @return √(z²)
	 */
	public Complex abs2() { return mulcsgn(this); } //secondary absolute value, return √(z²)
	
	/**
	 * Returns a copy that's been rotated by a specified amount counterclockwise in the complex plane.
	 * @param ang - the angle we rotate by
	 * @return - the rotated copy
	 */
	public Complex rotate(double ang) { //returns a rotated copy
		double cos = cos(ang), sin = sin(ang); return new Complex(re*cos-im*sin,re*sin+im*cos);
	}
	/**
	 * "Rotate Equals", rotates the number by a specified amount counterclockwise in the complex plane.  Returns the result.
	 * @param ang - the angle we rotate by
	 * @return - the rotated result
	 */
	public Complex rotateEq(double ang) { //rotates & returns
		double cos = cos(ang), sin = sin(ang); set(re*cos-im*sin,re*sin+im*cos); return this;
	}
	
///////////////////////////////////////////// BASIC ARITHMETIC /////////////////////////////////////////////////
	
	//create a new instance
	
	/**
	 * Addition with a complex number
	 * @param a - the complex number we add (known as the addend)
	 * @return the sum
	 */
	public Complex add(Complex a) { return new Complex(re+a.re, im+a.im); }
	/**
	 * Addition with x+yi
	 * @param x the real part of the addend
	 * @param y the imaginary part of the addend
	 * @return the sum
	 */
	public Complex add(double x, double y) { return new Complex(re+x, im+y); }
	/**
	 * Addition with a real double
	 * @param a - the real addend
	 * @return - the sum
	 */
	public Complex add(double a) { return new Complex(re+a, im  ); }
	/**
	 * Addition with imaginary double
	 * @param a - the imaginary number we add
	 * @return - the sum
	 */
	public Complex addI(double a) { return new Complex(re, im+a); }
	
	/**
	 * Subtraction with a complex number
	 * @param a - the complex number we subtract (known as the subtrahend)
	 * @return the difference
	 */
	public Complex sub(Complex a) { return new Complex(re-a.re, im-a.im); }
	/**
	 * Subtraction with x+yi
	 * @param x the real part of the subtrahend
	 * @param y the imaginary part of the subtrahend
	 * @return the difference
	 */
	public Complex sub(double x, double y) { return new Complex(re-x, im-y); }
	/**
	 * Subtraction with a real double
	 * @param a - the real subtrahend
	 * @return - the difference
	 */
	public Complex sub(double a) { return new Complex(re-a, im  ); }
	/**
	 * Subtraction with imaginary double
	 * @param a - the imaginary number we subtract
	 * @return - the difference
	 */
	public Complex subI(double a) { return new Complex(re, im-a); }
	
	/**
	 * Multiplication with a complex number
	 * @param a - the complex number we multiply by (known as the multiplier)
	 * @return the product
	 */
	public Complex mul(Complex a) { return new Complex(re*a.re-im*a.im, re*a.im+im*a.re); }
	/**
	 * Multiplication with x+yi
	 * @param x the real part of the multiplier
	 * @param y the imaginary part of the multiplier
	 * @return the product
	 */
	public Complex mul(double x, double y) { return new Complex(re*x-im*y, re*y+im*x); }
	/**
	 * Multiplication with a real double
	 * @param a - the real multiplier
	 * @return - the product
	 */
	public Complex mul(double a) { return new Complex(re*a, im*a); }
	/**
	 * Multiplication with imaginary double
	 * @param a - the imaginary number we multiply by
	 * @return - the product
	 */
	public Complex mulI(double a) { return new Complex(-im*a, re*a); }
	
	/**
	 * Division with a complex number
	 * @param a - the complex number we divide by (known as the divisor)
	 * @return the quotient
	 */
	public Complex div(Complex a) { return mul(a.inv()); }
	/**
	 * Division with x+yi
	 * @param x the real part of the divisor
	 * @param y the imaginary part of the divisor
	 * @return the quotient
	 */
	public Complex div(double x, double y) { return div(new Complex(x,y)); }
	/**
	 * Division with a real double
	 * @param a - the divisor
	 * @return - the quotient
	 */
	public Complex div(double a) {
		if(Math.abs(a)<=5.562684646268E-309D) { return new Complex(re/a, im/a); } //for small a, we divide each component by a
		double rec=1.0D/a; return new Complex(re*rec, im*rec);                    //else, multiply each component by 1/a
	}
	/**
	 * Division with imaginary double
	 * @param a - the imaginary number we divide by
	 * @return - the quotient
	 */
	public Complex divI(double a) {
		if(Math.abs(a)<=5.562684646268E-309D) { return new Complex(im/a, -re/a); } //for small a, we divide each component by a
		double rec=1.0D/a; return new Complex(im*rec, -re*rec);                    //else, multiply each component by 1/a
	}
	
	//assign equals
	
	/**
	 * Plus-equals a complex number
	 * @param a - the number we add (or addend)
	 * @return the sum
	 */
	public Complex addeq(Complex a) { re+=a.re; im+=a.im; return this; } // +=
	/**
	 * Plus-equals x+yi
	 * @param x - the real part of the addend
	 * @param y - the imaginary part of the addend
	 * @return the sum
	 */
	public Complex addeq(double x, double y) { re+=x; im+=y; return this; }
	/**
	 * Plus-equals a real double
	 * @param a - the real addend
	 * @return the sum
	 */
	public Complex addeq(double a) { re+=a; return this; }
	/**
	 * Plus-equals an imaginary double
	 * @param a - the imaginary addend
	 * @return the sum
	 */
	public Complex addeqI(double a) { im+=a; return this; }
	
	
	/**
	 * Minus-equals a complex number
	 * @param a - the number we subtract (or subtrahend)
	 * @return the difference
	 */
	public Complex subeq(Complex a) { re-=a.re; im-=a.im; return this; } // -=
	/**
	 * Minus-equals x+yi
	 * @param x - the real part of the subtrahend
	 * @param y - the imaginary part of the subtrahend
	 * @return the difference
	 */
	public Complex subeq(double x, double y) { re-=x; im-=y; return this; }
	/**
	 * Minus-equals a real double
	 * @param a - the real subtrahend
	 * @return the difference
	 */
	public Complex subeq(double a) { re-=a; return this; }
	/**
	 * Minus-equals an imaginary double
	 * @param a - the imaginary subtrahend
	 * @return the difference
	 */
	public Complex subeqI(double a) { im-=a; return this; } // Complex -= double*i
	
	
	/**
	 * Times-equals a complex number
	 * @param a - the number we multiply by (or multiplier)
	 * @return the product
	 */
	public Complex muleq(Complex a) { set(re*a.re-im*a.im, re*a.im+im*a.re); return this; } // *=
	/**
	 * Times-equals x+yi
	 * @param x - the real part of the multiplier
	 * @param y - the imaginary part of the multiplier
	 * @return the product
	 */
	public Complex muleq(double x, double y) { set(re*x-im*y, re*y+im*x); return this; }
	/**
	 * Times-equals a real double
	 * @param a - the real multiplier
	 * @return the product
	 */
	public Complex muleq(double a) { re*=a; im*=a; return this; }
	/**
	 * Times-equals an imaginary double
	 * @param a - the imaginary multiplier
	 * @return the product
	 */
	public Complex muleqI(double a) { set(-im*a, re*a); return this; }
	
	
	/**
	 * Divide-equals a complex number
	 * @param a - the number we divide by (or divisor)
	 * @return the quotient
	 */
	public Complex diveq(Complex a) { return muleq(a.inv()); } // /=
	/**
	 * Divide-equals x+yi
	 * @param x - the real part of the divisor
	 * @param y - the imaginary part of the divisor
	 * @return the quotient
	 */
	public Complex diveq(double x, double y) { return diveq(new Complex(x,y)); }
	/**
	 * Divide-equals a real double
	 * @param a - the real divisor
	 * @return the quotient
	 */
	public Complex diveq(double a) {
		if(Math.abs(a)<=5.562684646268E-309D) { re/=a; im/=a; return this; }
		return muleq(1.0D/a);
	}
	/**
	 * Divide-equals an imaginary double
	 * @param a - the imaginary divisor
	 * @return the quotient
	 */
	public Complex diveqI(double a) {
		if(Math.abs(a)<=5.562684646268E-309D) { set(im/a,-re/a); return this; }
		return muleqI(-1.0D/a);
	}
	
/////////////////////////////////////// NEGATION AND OTHER SIMPLE OPERATIONS ////////////////////////////////
	
	//new instance
	/**
	 * Negate (AKA multiply by -1)
	 * @return a negated copy
	 */
	public Complex neg () { return new Complex(-re,-im); } //negation
	/**
	 * Complex conjugate (the same number, but with a negated imaginary part)
	 * @return a conjugated copy
	 */
	public Complex conj() { return new Complex( re,-im); } //complex conjugate
	/**
	 * Multiply by i
	 * @return a copy that's multiplied by i; rotated 90 degrees counter-clockwise
	 */
	public Complex mulI() { return new Complex(-im, re); } //multiply by i
	/**
	 * Divide by i
	 * @return a copy that's divided by i; multiplied by -i; rotated 90 degrees clockwise
	 */
	public Complex divI() { return new Complex( im,-re); } //divide   by i
	
	//assign equals
	/**
	 * Negate-equals (x+yi -> -x-yi)
	 * @return the negated result
	 */
	public Complex negeq()  { set(-re, -im); return this; } // negate-equals
	/**
	 * Complex conjugate-equals (x+yi -> x-yi)
	 * @return the conjugated result
	 */
	public Complex conjeq() { set( re, -im); return this; } // conjugate-equals (if you ever need that)
	/**
	 * Times-equals i (x+yi -> -y+xi)
	 * @return the product
	 */
	public Complex muleqI() { set(-im,  re); return this; } // *= i
	/**
	 * Divide-equals i (x+yi -> y-xi)
	 * @return the quotient
	 */
	public Complex diveqI() { set( im, -re); return this; } // /= i
	
/////////////////////////////////////// COMPLEX PARTS ////////////////////////////////////////////////////
	
	/** Getter - returns the real part @return the real part */
	public double re() { return re; } //real part
	/** Getter - returns the imaginary part @return the imaginary part */
	public double im() { return im; } //imaginary part
	
	/**
	 * "Absolulte square", the square of the absolute value, equal to re²+im²
	 * @return the absolute square
	 */
	public double absq() { return re*re+im*im; } //absolute square
	
	/**
	 * "Absolute value", the distance from 0 on the complex plane, equal to √(re²+im²).
	 * @return the absolute value
	 */
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
	
	/**
	 * "Argument", the angle of the input, measured counterclockwise from the positive real axis.
	 * @return the argument
	 */
	public double arg()  {                             //polar argument
		if(im==0) { return re>=0 ? 0 : Math.PI;      } //real      number: return either 0 or π
		if(re==0) { return im>=0 ? HALFPI : -HALFPI; } //imaginary number: return ±π/2
		return Math.atan2(im, re);                     //general case: find the angle with atan2
	}
	
	/**
	 * "Sign/Signum", the input divided by its absolute value.  Similar to putting a hat over a vector.
	 * In the special case that the input is 0, the result is 0.
	 * @return the sign
	 */
	public Complex sgn() { //signum
		if(equals(0)) { return new Complex(); } //0: return 0
		if(isInf()) {                           //∞: it depends
			if(Math.abs(re)==Math.abs(im)) { return new Complex(0.5*ROOT2*sgn(re),0.5*ROOT2*sgn(im)); } //both components are infinite
			return new Complex(sgn(re),sgn(im));                                                        //one component is infinite
		}
		return div(abs());                      //otherwise: return this divided by the absolute value
	}
	
///////////////////////////////////////// RECIPROCAL, SQUARE ROOT, AND OTHER IMPORTANT FUNCTIONS ////////////////////
	
	/**
	 * "Inverse", the reciprocal of the input.  Equivalent to the conjugate divided by the absolute square
	 * @return the reciprocal
	 */
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
	
	/**
	 * "Square".  (x+yi)² = (x²-y²)+2xy*i = (x+y)(x-y) + 2xyi
	 * @return the square
	 */
	public Complex sq () { return new Complex((re+im)*(re-im),2*re*im); } //z²
	/** @return the cube */
	public Complex cub() { return mul(sq());                            } //z³
	
	/**
	 * The square root.  More specifically, the principal square root, whichever of the two square roots has the largest real part.
	 * If it's a tie, we return whichever one has the largest imaginary part.
	 * @return the square root
	 */
	public Complex sqrt() { //√(z)
		
		if(equals(0)) { return new Complex(); } //special case: √(0)=0
		if(isInf()) {                                                             		                 //special case: infinite input
			if(Math.abs(im)!=inf) { return re==inf ? new Complex(inf) : new Complex(0,im>=0?inf:-inf); } //non-inf imag part: return either ∞ or ±∞i
			return new Complex(inf,im);                                           		                 //otherwise, reutrn ∞±∞i
		}
		if(2*Math.abs(re)+Math.abs(im)==inf) { return mul(0.25D).sqrt().muleq(2D); } //special case: |z|+|x|=Overflow, return √(z/4)*2
		
		//the formula for √(z) = √((|z|+x)/2) + sgn(y)√((|z|-x)/2)i    (where z = x + yi)
		//since √((|z|+x)/2) * sgn(y)√((|z|-x)/2) = y/2, we'll just find one sqrt & use division to find the other
		
		double part1=Math.sqrt(0.5D*(abs()+Math.abs(re))); //compute whichever sqrt has the least roundoff error
		
		if(re>=0) { return new Complex(part1, im/(2.0D*part1));               } //if x>0, that'd be the real part
		else      { return new Complex(im/(2.0D*part1), part1).muleqcsgn(im); } //if x<0, that'd be the imaginary part
	}
	
	/** 
	 * The cube root.  More specifically, the principal cube root.  We simply return whichever of the 3 cube roots has the largest real part.
	 * If cbrt_Option is true, we'll make an exception for negative reals, instead returning the negative real cube root.  Otherwise, we return
	 * that negative cube root times (-1-√(3)i)/2.
	 * @return the cube root
	 */
	public Complex cbrt() { //cube root of complex z
		if(im==0 && (cbrt_Option||re>=0)) { return new Complex(Math.cbrt(re)); } //z is real: return cbrt(re(z)) (we may or may not exclude negatives)
		
		if(re==-inf)             { return new Complex(inf,im>=0?inf:-inf);         } //special case : -∞+something*i
		if(isInf())              { return new Complex(inf,Math.abs(im)==inf?im:0); } //special case : something else infinite
		if(lazyabs()>1.271E308D) { return mul(0.125D).cbrt().muleq(2D);            } //|z| overflows: return 2cbrt(z/8)
		
		return new Complex(0,arg()/3).exp().muleq(Math.cbrt(abs()));                 //general case : return e^(arg(z)/3)*cbrt(|z|)
	}
	
	/**
	 * Exponential.  Euler's number raised to the power of this number.  It has an absolute value of e^re and an angle equal to im.
	 * @return the exponential
	 */
	public Complex exp() { //e^z
		if(im==0) { return new Complex(Math.exp(re));                  }   //real number : return e^(real part)
		if(re==0) { return new Complex(cos(im),sin(im));               }   //imag number : return cos(imag)+sin(imag)*i
		if(re>709.78 && re<710.13) { return sub(log2).exp().muleq(2D); }   //large real part: subtract ln(2), take exponent, multiply by 2
		
		double exp = Math.exp(re);                   //compute exp of real part
		return new Complex(exp*cos(im),exp*sin(im)); //return e^(real)*(cos(imag)+sin(imag)*i)
	}
	
	/**
	 * Standing for "logarithmus naturalis", or natural logarithm in English, this returns the base e logarithm of this number.  Its real part
	 * is the logarithm of the absolute value, its imaginary part is the argument.  Technically, this returns the principal value of the
	 * logarithm, since all non-zero numbers have infinitely many natural logarithms, but the principal value is whichever one has the
	 * imaginary part closest to 0.
	 * @return the natural logarithm
	 */
	public Complex ln() { //ln(z)
		if(re==0||im==0) { return new Complex(Math.log(abs()), arg()); } //real/imaginary number: return ln|z|+arg(z)i
		if(isInf())      { return new Complex(inf, arg());             } //infinite number: return ∞+arg(z)i
		
		double L=lazyabs();                        //take lazy abs for a quick sense of scale
		if(L<=1.055E-154D || L>=9.481E+153D) {     //absolute square overflows/underflows:
			return div(L).ln().addeq(Math.log(L)); //divide by L, take the log, and add back ln(L)
		}
		return new Complex(0.5D*Math.log(absq()), arg()); //general case: return ln(|z|²)/2+arg(z)i
	}
	
	/**
	 * This number raised to the power of integer a.  Computed via exponentiation by squaring, a highly efficient combination of squaring
	 * and multiplication that's ultimately equivalent to the base multiplied by itself a times (but more efficient).
	 * @param a - the exponent
	 * @return - the power
	 */
	public Complex pow(int a) { //Complex z ^ int a (exponentiation by squaring)
		
		if(im==0) { return new Complex(dPow(re,a)); } //input is real: use the other implementation for doubles (results in ~1/4 the # of multiplications)
		
		if(a<0)  { return inv().pow(-a); } //a is negative: return (1/z)^(-a)
		//general case:
		Complex ans=new Complex(1);     //return value: z^a (init to 1 in case a==0)
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
	
	/**
	 * This number raised to the power of the real double a.  Computed by raising the absolute value to the power of a, multiplying the
	 * angle by a, and returning the result.  If this is a real number, we use dPow to reduce multiplications.  If a is an integer, we
	 * use the previous pow function to improve performance and accuracy.
	 * @param a - the exponent
	 * @return - the result
	 */
	public Complex pow(double a) { //complex z ^ double a
		if(Math.abs(a)<=2147483647 && a%1==0) { return pow((int)a);                 } //exponent is an integer: there's a faster (and more accurate) way of doing this
		if(im==0 && (re>=0||a%1==0))          { return new Complex(Math.pow(re,a)); } //if the base is real and non-negative, or it's negative but the exponent is an integer, we'll just use the built in power function 
		
		double mag;                                   //compute |z|^a
		double L=lazyabs();                           //use lazy absolute value for a sense of scale
		if     (L>=1.05476867E-154D && L<=9.48075190E+153D) { mag=Math.pow(absq(),0.5*a); } //if within range, take (|z|²)^(a/2)
		else if(L>=7.86682407E-309D && L<=1.27116100E+308D) { mag=Math.pow(abs() ,    a); } //if within another range, take |z|^a
		else                                   { return div(L).pow(a).mul(Math.pow(L,a)); } //if outside both ranges, divide by L, raise to a-th power, mult by L^a
		
		double arg = arg();                                //compute the argument
		return new Complex(mag*cos(a*arg),mag*sin(a*arg)); //return complex number with magnitude |z|^a & angle a*θ
	}
	
	/**
	 * This number raised to the power of complex number a.  Computed by multiplying the logarithm by a, then taking the exponential.
	 * If a is real, we use the <code>pow(double a)</code> function.  If this is Euler's number, we return the <code>exp</code> function.
	 * @param a - the exponent
	 * @return - the result
	 */
	public Complex pow(Complex a) {              //complex z ^ complex a
		if(equals(e)) { return a.exp();   } //z==e        : return e^a
		if(a.im==0)   { return pow(a.re); } //a is real   : return complex z ^ double a.re
		return ln().muleq(a).exp();         //general case: return e to the power of the log times a
	}
	
//////////////////// ROUNDING & MODULOS ////////////////////////////////////
	
	/** @return the floor of the real part*/
	public double floor() { return Math.floor(re); }
	/** @return the ceiling of the real part*/
	public double ceil () { return Math.ceil(re);  }
	/** @return the real part rounded to the nearest int*/
	public double round() { return Math.round(re); }
	
	/**
	 * @param a what we're modulo-ing with
	 * @return this minus the largest integer multiple of a that fits in this. (Read the code for more information)
	 */
	public Complex mod(Complex a) {         //Complex modulo (sign standard: +%+ = +, +%- = -, -%+ = +, -%- = -)
		Complex part=a.mul(div(a).floor()); //part: the largest* integer multiple of a that fits in "this": / by a, round down, * back by a
		return sub(part);                   //subtract this multiple
	}
	//* "largest" meaning the largest integer that multiplies by a
	
	/**
	 * Modulo, version 2.  Here, we subtract a very specific integer multiple of a, whichever integer multiple causes our output to be
	 * closest to 0.
	 * @param a - what we're modulo-ing with
	 * @return the secondary modulo.
	 */
	public Complex mod_v2(Complex a) {             //Secondary modulo: drops sign convention & just returns whichever modulo is closer to 0
		Complex part=a.mul(-div(a).neg().round()); //part: the closest integer multiple of a to "this": / by -a, round, * back by -a
		return sub(part);                          //subtract this multiple
	}                                            //note, if two multiples are equally close, we default to the multiple corresponding to the lower integer (hence why a is negated)
	
	//when adding logarithms, you can use this tool (with "a" set to 2πi) to ensure the imaginary part is within the range (-π,π]
	
//////////////////// TRIGONOMETRY //////////////////////////////////////
	
	/** complex cosine @return the cosine*/
	public Complex cos() {                                                  //cos
		if(im==0) { return new Complex(cos(re));       } //real input: return cos
		if(re==0) { return new Complex(Math.cosh(im)); } //imag input: return cosh
		
		double[] sinhcosh=fsinhcosh(im);                 //compute sinh & cosh of imag part
		return new Complex(cos(re)*sinhcosh[1],-sin(re)*sinhcosh[0]); //cos(x+yi) = cos(x)cosh(y)-sin(x)sinh(y)i
	}
	/** complex sine @return the sine*/
	public Complex sin() {                                                    //sin
		if(im==0) { return new Complex(sin(re));         } //real input: return sin
		if(re==0) { return new Complex(0,Math.sinh(im)); } //imag input: return sinh*i
		
		double[] sinhcosh=fsinhcosh(im);                   //compute sinh & cosh of imag part
		return new Complex(sin(re)*sinhcosh[1],cos(re)*sinhcosh[0]); //sin(x+yi) = sin(x)cosh(y)+cos(x)sinh(y)i
	}
	/** complex tangent @return the tangent*/
	public Complex tan() {                                                        //tan
		if(im==0) 	 { return new Complex(tan(re));          } //real input: return tan
		if(re==0)    { return new Complex(0,Math.tanh(im));  } //imag input: return tan*i
		if(Math.abs(im)>20) { return new Complex(0,sgn(im)); } //large input: return +-1
		
		double[] sinhcosh=fsinhcosh(2*im);                     //compute sinh & cosh of twice the imag part
		double denom = 1.0D/(cos(2*re)+sinhcosh[1]);           //compute 1/(cos(2x)+cosh(2y))
		return new Complex(sin(2*re)*denom, sinhcosh[0]*denom); //tan(x+yi) = (sin(2x)+sinh(2y)i)/(cos(2x)+cosh(2y))
	}
	
	/** hyperbolic cosine @return the hyperbolic cosine*/
	public Complex cosh() { return mulI().cos();          } //cosh
	/** hyperbolic sine @return the hyperbolic sine*/
	public Complex sinh() { return mulI().sin().diveqI(); } //sinh
	/** hyperbolic tangent @return the hyperbolic tangent*/
	public Complex tanh() { return mulI().tan().diveqI(); } //tanh
	
	/** secant    @return    secant*/ public Complex sec () { return cos ().inv(); } //sec
	/** cosecant  @return  cosecant*/ public Complex csc () { return sin ().inv(); } //csc
	/** cotangent @return cotangent*/ public Complex cot () { return tan ().inv(); } //cot
	
	/** hyperbolic    secant @return hyperbolic    secant*/ public Complex sech() { return cosh().inv(); } //sech
	/** hyperbolic  cosecant @return hyperbolic  cosecant*/ public Complex csch() { return sinh().inv(); } //csch
	/** hyperbolic cotangent @return hyperbolic cotangent*/ public Complex coth() { return tanh().inv(); } //coth
	
//////////////////////////////////////////////////////////////////////////INVERSE TRIGONOMETRY////////////////////////////////////////////////////////////////////////////////////
	
	/** inverse cosh @return the inverse hyperbolic cosine*/
	public Complex acosh() {                                                //arcosh
		if(im==0&&Math.abs(re)<=1) { return new Complex(0,Math.acos(re)); } //real input [-1,1]: return acos*i
		
		if(absq()>1E18D) { return ln().addeq(log2); }            //huge input: return asymptotic approximation
		
		return add( sq().subeq(1).sqrt().muleqcsgn(this) ).ln(); //else: return ln(z+csgn(z)√(z²-1))
	}
	/** inverse sinh @return the inverse hyperbolic sine*/
	public Complex asinh() {                                                //arsinh
		if(re==0&&Math.abs(im)<=1) { return new Complex(0,Math.asin(im)); } //imag input [-i,i]: return asin*i
		
		if(absq()>1E18D) { return abs2().ln().addeq(log2).muleqcsgn(this); } //huge input: return asymptotic approximation
		if(lazyabs()<=1E-4D) { return mul(Cpx.sub(1,sq().div(6))); }         //tiny input: return taylor's series
		
		return (abs2().addeq( sq().addeq(1).sqrt() )).ln().muleqcsgn(this);  //else: return csgn(z)ln(|z|+√(z²+1))
	}
	/** inverse tanh @return the inverse hyperbolic tangent*/
	public Complex atanh() {                                                               //artanh
		if(im==0 && Math.abs(re)==1)           { return new Complex(re==1 ? inf : -inf); } //special case: atanh(±1)=±∞
		if(isInf()) { return new Complex(0,(im>0 || im==0 && re<=1) ? HALFPI : -HALFPI); } //special case: z is infinite, return ±πi/2
		
		if(re==0)            { return new Complex(0,Math.atan(im)); } //imag input: return atan*i
		if(lazyabs()<=1E-4D) { return mul(sq().div(3).addeq(1));    } //tiny input: return taylor's series
		
		Complex ans=(add(1).diveq(Cpx.sub(1,this))).ln().muleq(0.5D); //else      : atanh(z)=ln((1+z)/(1-z))/2
		if(im==0&&re>1) { ans.im=-HALFPI; }                           //(special case) z is real & >1: negate im to keep function odd
		return ans;                                                   //return answer
	}
	
	/**arc cos @return the principal value of the arc cosine*/
	public Complex acos() {                                                 //acos
		if(im==0 && Math.abs(re)<=1) { return new Complex(Math.acos(re)); } //real input [-1,1]: return acos
		return mulI().asinh().muleqI().addeq(HALFPI);                       //else             : return π/2-asin
	}
	/**arc sin @return the principal value of the arc sine*/
	public Complex asin() { return mulI().asinh().diveqI(); } //asin
	/**arc tan @return the principal value of the arc tangent*/
	public Complex atan() { return mulI().atanh().diveqI(); } //atan
	
	
	/**arc sec @return the arc    secant*/ public Complex asec() { return inv().acos(); } //asec
	/**arc csc @return the arc  cosecant*/ public Complex acsc() { return inv().asin(); } //acsc
	/**arc cot @return the arc cotangent*/ public Complex acot() { return inv().atan(); } //acot
	
	/**inverse sech @return the inverse hyperbolic secant*/    public Complex asech() { return inv().acosh(); } //asech
	/**inverse csch @return the inverse hyperbolic cosecant*/  public Complex acsch() { return inv().asinh(); } //acsch
	/**inverse coth @return the inverse hyperbolic cotangent*/ public Complex acoth() { return inv().atanh(); } //acoth
}
