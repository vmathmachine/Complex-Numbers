package complexnumbers;

import java.util.ArrayList;

//Don't be intimidated by the line count.  Without javadoc comments, it's only about 200 lines.

/**
 * A collection of utilities for complex numbers that can't be called as <code>Complex</code> instance methods.
 * 
 * @author Math Machine
 * @version 1.1.0
 */

public class Cpx extends Mafs {
	
////////////////EXTERNAL CONSTRUCTORS//////////////////////
	
	/**
	 * Returns the number 0
	 * @return the complex number 0+0i
	 */
	public static Complex zero() { return new Complex(); } //create instance of  0+0i
	/**
	 * Returns the number 1
	 * @return the complex number 1+0i
	 */
	public static Complex one () { return new Complex(1); } //create instance of  1+0i
	/**
	 * Returns the number -1
	 * @return the complex number -1+0i
	 */
	public static Complex mOne() { return new Complex(-1); } //create instance of -1+0i
	/**
	 * Returns the number 2
	 * @return the complex number 2+0i
	 */
	public static Complex two () { return new Complex(2); } //create instance of  2+0i
	/**
	 * Returns the number i
	 * @return the complex number 0+1i
	 */
	public static Complex i() { return new Complex(0,1); } //create instance of 0+1i
	/**
	 * Casts a <code>double</code> to a <code>Complex</code>.
	 * @param d the double
	 * @return d+0i
	 */
	public static Complex complex(double d) { return new Complex(d); } //create instance of  d+0i (cast double to Complex)
	/**
	 * Creates an imaginary number.
	 * @param d the imaginary part
	 * @return 0+di
	 */
	public static Complex iTimes (double d) { return new Complex(0,d); } //create instance of  0+di
	
	/**
	 * Casts a <code>String</code> to a <code>Complex</code>. (Invalid configuration returns NaN)
	 * @param s the complex as a string
	 * @return the complex number
	 */
	public static Complex complex(String s) { //convert a String to a Complex number
		int numPlusMinus=0; //first, we have to count the number of plus or minuses (with some exceptions)
		int separator = -1; //if there's only 1, this'll be used to record the position of that one
		for(int n=1;n<s.length();n++) { //we start from 1, since we won't count ones @ the start
			if((s.charAt(n)=='+' || s.charAt(n)=='-') && (s.charAt(n-1)!='e' && s.charAt(n-1)!='E')) { //if this is + or -, and the previous char wasn't e or E:
				numPlusMinus++;                                                                        //increment the counter
				separator = n;                                                                         //record the position of this plus or minus
			}
		}
  
		String[] compArray = {"0","0"}; //create an array for the real & imaginary parts as strings
  
		switch(numPlusMinus) {
			case 0: //if there's no +/- separating the components, we're dealing with either a real or imaginary number
				if(s.endsWith("i")) {                         //if it ends with i, then it's imaginary
					compArray[1]=s.substring(0,s.length()-1); //set the second component to this, but without an i at the end
				}
				else {              //otherwise, it's real
					compArray[0]=s; //set the first component to this
				}
				break;
			case 1: //if there's 1 +/- separating the components, we're dealing with a complex number
				if(!s.endsWith("i")) { return new Complex(Double.NaN); } //if it doesn't end with 'i', return NaN
				compArray[0] = s.substring(0,separator);            //have the first string be from 0 up to but not including the +/-
				compArray[1] = s.substring(separator,s.length()-1); //have the second string be from +/- up to but not including the i
				break;
			default: //if there are more than 1 +/-s, we're dealing with an invalid configuration
				return new Complex(Double.NaN); //return NaN
		}
  
		Complex result = new Complex(); //initialize the result
  
		try {
			result.re = Double.parseDouble(compArray[0]); //try to cast the first part to a double and make that the real part
		}
		catch(NumberFormatException e) { return new Complex(Double.NaN); } //if it's invalid, return NaN
		
		if(compArray[1].equals("+") || compArray[1].equals("")) { result.im =  1; } //if the imaginary part is either shown as i or +i, convert that to the number 1
		else if(compArray[1].equals("-"))                       { result.im = -1; } //if the imaginary part is shown as -i, convert that to the number -1
		else {
			try {
				result.im = Double.parseDouble(compArray[1]); //otherwise, try to cast the second part to a double and make that the imaginary part
			}
			catch(NumberFormatException e) { return new Complex(Double.NaN); } //if it's invalid, return NaN
		}
		
  		return result; //otherwise, return result
	}
	
	/**
	 * Constructs a complex number in polar notation.
	 * @param r the absolute value
	 * @param ang the argument
	 * @return the complex number
	 */
	public static Complex polar(double r, double ang) { return new Complex(r*cos(ang),r*sin(ang)); } //constructs a number w/ polar notation
	
	//we will now overload (most of) the functions from complex so that the complex numbers can be called as parameters
	
	/**
	 * Casts a <code>Complex</code> to a <code>String</code>.
	 * @param z complex input
	 * @return the string
	 */
	public static String str(Complex z) { return z.toString(); } //this generates the complex number in text form
	
///////////////COMPLEX PARTS///////////////////////////
	/**
	 * Real part
	 * @param z complex input
	 * @return the real part
	 */
	public static double re(Complex z) { return z.re; } //real & imaginary parts
	/**
	 * Imaginary part
	 * @param z complex input
	 * @return the imaginary part
	 */
	public static double im(Complex z) { return z.im; }
	
	/**
	 * Absolute square (absolute value squared)
	 * @param z complex input
	 * @return absolute square
	 */
	public static double absq(Complex z) { return z.absq(); } //square absolute value
	/**
	 * Absolute value
	 * @param z complex input
	 * @return absolute value
	 */
	public static double abs (Complex z) { return z.abs();  } //absolute value
	/**
	 * Polar argument
	 * @param z complex input
	 * @return argument
	 */
	public static double arg (Complex z) { return z.arg();  } //polar argument
	
	/**
	 * Complex conjugate
	 * @param z complex input
	 * @return complex conjugate
	 */
	public static Complex conj(Complex z) { return z.conj(); } //complex conjugate
	
//////////////// BASIC ARITHMETIC //////////////////////
	
	/**
	 * Adds two complexes
	 * @param a complex addend (a number we add)
	 * @param b complex addend
	 * @return sum
	 */
	public static Complex add(Complex a, Complex b) { return a.add(b); }
	/**
	 * Adds a complex to a real
	 * @param a complex addend
	 * @param b real addend
	 * @return sum
	 */
	public static Complex add(Complex a, double b) { return a.add(b); }
	/**
	 * Adds a real to a complex
	 * @param a real addend
	 * @param b complex addend
	 * @return sum
	 */
	public static Complex add(double a, Complex b) { return b.add(a); }
	/**
	 * Adds 2 complexes and one real
	 * @param a First complex addend
	 * @param b Second complex addend
	 * @param c Third real addend
	 * @return sum
	 */
	public static Complex add(Complex a, Complex b, double c) { return a.add(b).addeq(c); } //3 input add (with doubles)
	/**
	 * Adds an indefinite number of complex numbers
	 * @param a First complex addend
	 * @param b Second complex addend
	 * @param c The rest of the complex addends
	 * @return Their collective sum
	 */
	public static Complex add(Complex a, Complex b, Complex... c) { //unlimited input sum (all Complex)
		Complex ret=a.add(b);                 //add initial two inputs
		for(Complex c2: c) { ret.addeq(c2); } //add equal all other inputs
		return ret;                           //return the result
	}
	/**
	 * Adds one real to an indefinite number of complexes
	 * @param a First real addend
	 * @param b Second complex addend
	 * @param c The rest of the complex addends
	 * @return Their collective sum
	 */
	public static Complex add(double a, Complex b, Complex... c) { //unlimited input sum (1 double + n Complex)
		Complex ret=b.add(a);                 //add initial 2 inputs
		for(Complex c2: c) { ret.addeq(c2); } //add equal all other inputs
		return ret;                           //return the result
	}
	
	/**
	 * Subtracts two complexes
	 * @param a complex minuend (the number we subtract from)
	 * @param b complex subtrahend (the number we subtract)
	 * @return difference
	 */
	public static Complex sub(Complex a, Complex b) { return a.sub(b); }
	/**
	 * Subtracts a real from a complex
	 * @param a complex minuend
	 * @param b real subtrahend
	 * @return difference
	 */
	public static Complex sub(Complex a, double b) { return a.sub(b); }
	/**
	 * Subtracts a complex from a real
	 * @param a real minuend
	 * @param b complex subtrahend
	 * @return difference
	 */
	public static Complex sub(double a, Complex b) { return new Complex(a-b.re,-b.im); }
	
	/**
	 * Multiplies two complexes
	 * @param a complex multiplicand (the first number in a multiplication)
	 * @param b complex multiplier (the second number in a multiplication)
	 * @return product
	 */
	public static Complex mul(Complex a, Complex b) { return a.mul(b); }
	/**
	 * Multiplies a complex by a real
	 * @param a complex multiplicand
	 * @param b real multiplier
	 * @return product
	 */
	public static Complex mul(Complex a, double b) { return a.mul(b); }
	/**
	 * Multiplies a real by a complex
	 * @param a real multiplicand
	 * @param b complex multiplier
	 * @return product
	 */
	public static Complex mul(double a, Complex b) { return b.mul(a); }
	/**
	 * Multiplies 2 complexes and one real
	 * @param a First complex input
	 * @param b Second complex input
	 * @param c Third real input
	 * @return product
	 */
	public static Complex mul(Complex a, Complex b, double c) { return a.mul(b).muleq(c); } //3 input multiply (with doubles)
	/**
	 * Multiplies an indefinite number of complex numbers
	 * @param a First complex input
	 * @param b Second complex input
	 * @param c The rest of the complex inputs
	 * @return Their collective product
	 */
	public static Complex mul(Complex a, Complex b, Complex... c) { //unlimited input multiply (all Complex)
		Complex ret=a.mul(b);                 //multiply initial two inputs
		for(Complex c2: c) { ret.muleq(c2); } //multiply equal all other inputs
		return ret;                           //return the result
	}
	/**
	 * Multiplies one real by an indefinite number of complexes
	 * @param a First real input
	 * @param b Second complex input
	 * @param c The rest of the complex inputs
	 * @return Their collective product
	 */
	public static Complex mul(double a, Complex b, Complex... c) { //unlimited input multiply (1 double * n Complex)
		Complex ret = b.mul(a);               //multiply initial two inputs
		for(Complex c2: c) { ret.muleq(c2); } //multiply equal all other inputs
		return ret;                           //return the result
	}
	
	/**
	 * Divides two complexes
	 * @param a complex dividend (the first number in a division)
	 * @param b complex divisor (the number we divide by)
	 * @return quotient
	 */
	public static Complex div(Complex a, Complex b) { return a.div(b); }
	/**
	 * Divides a complex by a double
	 * @param a complex dividend
	 * @param b real divisor
	 * @return quotient
	 */
	public static Complex div(Complex a, double b) { return a.div(b); }
	/**
	 * Divides a real by a complex
	 * @param a real dividend
	 * @param b complex divisor
	 * @return quotient
	 */
	public static Complex div(double a, Complex b) { return inv(b).muleq(a); }
	
	/**
	 * Returns the input negated
	 * @param z complex input
	 * @return negated copy
	 */
	public static Complex neg(Complex z) { return z.neg(); }
	
////////////////RECIPROCAL, SQUARE ROOT, & OTHER IMPORTANT FUNCTIONS////////////
	
	/**
	 * The inverse or reciprocal of the input
	 * @param z complex input
	 * @return inverse
	 */
	public static Complex inv (Complex z) { return z.inv();  } //1/z
	/**
	 * The square of the input
	 * @param z complex input
	 * @return z²
	 */
	public static Complex sq  (Complex z) { return z.sq();   } //z²
	/**
	 * The cube of the input
	 * @param z complex input
	 * @return z³
	 */
	public static Complex cub (Complex z) { return z.cub();  } //z cubed
	
	/**
	 * The square root of the input
	 * @param z complex input
	 * @return √z
	 */
	public static Complex sqrt(Complex z) { return z.sqrt(); } //√(z)
	/**
	 * The cube root of the input
	 * @param z complex input
	 * @return ∛z
	 */
	public static Complex cbrt(Complex z) { return z.cbrt(); } //cbrt(z)
	/**
	 * e (Euler's number) raised to the power of the input
	 * @param z complex input
	 * @return the exponential
	 */
	public static Complex exp(Complex z)  { return z.exp();  } //e^z
	/**
	 * Natural logarithm of the input
	 * @param z complex input
	 * @return the natural logarithm
	 */
	public static Complex log(Complex z)  { return z.log();  } //log(z)
	/**
	 * Natural logarithm of the input
	 * @param z complex input
	 * @return the natural logarithm
	 */
	public static Complex ln(Complex z)   { return z.log();  } //ln(z)
	
	/**
	 * Common (base 10) logarithm of the input
	 * @param z complex input
	 * @return the common logarithm
	 */
	public static Complex log10(Complex z) { return z.log().muleq(0.434294481903251828D); } //base 10 log
	
	/**
	 * Complex raised to the power of an integer
	 * @param a complex base
	 * @param b integer exponent
	 * @return result
	 */
	public static Complex pow(Complex a, int     b) { return a.pow(b); } //this computes a to the power of an integer
	/**
	 * Complex raised to the power of a real number
	 * @param a complex base
	 * @param b real exponent
	 * @return result
	 */
	public static Complex pow(Complex a, double  b) { return a.pow(b); } //this computes a to the power of a real double
	/**
	 * Complex raised to the power of a complex
	 * @param a complex base
	 * @param b complex exponent
	 * @return result
	 */
	public static Complex pow(Complex a, Complex b) { return a.pow(b); } //this computes a to the power of another complex number
	
////////////////////////////////////////////////////////// TRIGONOMETRY ////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Cosine
	 * @param z complex input
	 * @return cos
	 */
	public static Complex cos(Complex z) { return z.cos(); } //cos
	/**
	 * Sine
	 * @param z complex input
	 * @return sin
	 */
	public static Complex sin(Complex z) { return z.sin(); } //sin
	/**
	 * Tangent
	 * @param z complex input
	 * @return tan
	 */
	public static Complex tan(Complex z) { return z.tan(); } //tan
	
	/**
	 * Hyperbolic cosine
	 * @param z complex input
	 * @return cosh
	 */
	public static Complex cosh(Complex z) { return z.cosh(); } //cosh
	/**
	 * Hyperbolic sine
	 * @param z complex input
	 * @return sinh
	 */
	public static Complex sinh(Complex z) { return z.sinh(); } //sinh
	/**
	 * Hyperbolic tangent
	 * @param z complex input
	 * @return tanh
	 */
	public static Complex tanh(Complex z) { return z.tanh(); } //tanh
	
	/**
	 * Secant
	 * @param z complex input
	 * @return sec
	 */
	public static Complex sec(Complex z) { return z.sec(); } //sec
	/**
	 * Cosecant
	 * @param z complex input
	 * @return csc
	 */
	public static Complex csc(Complex z) { return z.csc(); } //csc
	/**
	 * Cotangent
	 * @param z complex input
	 * @return cot
	 */
	public static Complex cot(Complex z) { return z.cot(); } //cot
	
	/**
	 * Hyperbolic secant
	 * @param z complex input
	 * @return sech
	 */
	public static Complex sech(Complex z) { return z.sech(); } //sech
	/**
	 * Hyperbolic cosecant
	 * @param z complex input
	 * @return csch
	 */
	public static Complex csch(Complex z) { return z.csch(); } //csch
	/**
	 * Hyperbolic cotangent
	 * @param z complex input
	 * @return coth
	 */
	public static Complex coth(Complex z) { return z.coth(); } //coth
	
	/**
	 * The Gudermannian function.  It's a useful function such that the sinh, cosh, tanh, sech, csch, and coth of the input is equal to
	 * the tan, sec, sin, cos, cot, and csc (respectively) of the output. This can be handy when evaluating certain integrals. It's
	 * also equal to the antiderivative of the hyperbolic secant (sech) function.
	 * @param z complex input
	 * @return Gudermannian function
	 */
	public static Complex gd(Complex z) { //Gudermannian function
		//first, the special cases. If the input is too close to one of the poles, we have to use an approximation
		Complex z2 = z.mod_v2(iTimes(2*Math.PI)); //take a modulo with 2πi
		if(z2.subI(HALFPI).lazyabs()<1E-4) { return ln(z2.mulI().addeq(HALFPI)).diveqI().addeqI(LOG2); } //approximation with logarithm
		if(z2.addI(HALFPI).lazyabs()<1E-4) { return ln(z2.divI().addeq(HALFPI)).muleqI().subeqI(LOG2); } //approximation with logarithm
		
		double[] sinhcosh = fsinhcosh(z.re);     //compute the cosh & sinh of the real part
		double cos = cos(z.im), sin = sin(z.im); //compute the cos & sin of the imaginary part
		return new Complex(Math.atan2(sinhcosh[0],cos), 0.5*Math.log((sinhcosh[1]+sin)/(sinhcosh[1]-sin)));
	    //gd(x+yi) = atan2(sinh(x),cos(y)) + iln((cosh(x)+sin(y))/(cosh(x)-sin(y)))/2
	}
	
//////////////////////////////////////////////////////////////////////////INVERSE TRIGONOMETRY////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Inverse hyperbolic cosine, also known as area hyperbolic cosine
	 * @param z complex input
	 * @return acosh
	 */
	public static Complex acosh(Complex z) { return z.acosh(); } //arcosh
	/**
	 * Inverse hyperbolic sine, also known as area hyperbolic sine
	 * @param z complex input
	 * @return asinh
	 */
	public static Complex asinh(Complex z) { return z.asinh(); } //arsinh
	/**
	 * Inverse hyperbolic tangent, also known as area hyperbolic tangent
	 * @param z complex input
	 * @return atanh
	 */
	public static Complex atanh(Complex z) { return z.atanh(); } //artanh
	
	/**
	 * Inverse cosine, also known as arc cosine
	 * @param z complex input
	 * @return acos
	 */
	public static Complex acos(Complex z) { return z.acos(); } //acos
	/**
	 * Inverse sine, also known as arc sine
	 * @param z complex input
	 * @return asin
	 */
	public static Complex asin(Complex z) { return z.asin(); } //asin
	/**
	 * Inverse tangent, also known as arc tangent
	 * @param z complex input
	 * @return atan
	 */
	public static Complex atan(Complex z) { return z.atan(); } //atan
	
	/**
	 * Inverse / arc secant
	 * @param z complex input
	 * @return asec
	 */
	public static Complex asec(Complex z) { return z.asec(); } //asec
	/**
	 * Inverse / arc cosecant
	 * @param z complex input
	 * @return acsc
	 */
	public static Complex acsc(Complex z) { return z.acsc(); } //acsc
	/**
	 * Inverse / arc cotangent
	 * @param z complex input
	 * @return acot
	 */
	public static Complex acot(Complex z) { return z.acot(); } //acot
	
	/**
	 * Inverse / area hyperbolic secant
	 * @param z complex input
	 * @return asech
	 */
	public static Complex asech(Complex z) { return z.asech(); } //asech
	/**
	 * Inverse / area hyperbolic cosecant
	 * @param z complex input
	 * @return acsch
	 */
	public static Complex acsch(Complex z) { return z.acsch(); } //acsch
	/**
	 * Inverse / area hyperbolic cotangent
	 * @param z complex input
	 * @return acoth
	 */
	public static Complex acoth(Complex z) { return z.acoth(); } //acoth
	
	/**
	 * Inverse Gudermannian function.  It's a useful function such that the sin, cos, tan, sec, csc, and cot of the input is equal to the
	 * tanh, sech, sinh, cosh, coth, and csch (respectively) of the output.  This can be handy when evaluating certain integrals.  It's
	 * also equal to the antiderivative of the secant (sec) function.
	 * @param z complex input
	 * @return Inverse Gudermannian function
	 */
	public static Complex invGd(Complex z) { //inverse Gudermannian function
		return gd(z.mulI()).diveqI();        //as it turns out, the inverse is just the gd, but with z*i and divided by i
	}                                        //gd : invGd :: sin : sinh
	
///////////////////////////// ADDING LOGARITHMS //////////////////////////////////
	
	/**
	 * Logarithm offset: Returns ( (sum of ln(each input)) - (ln(product of each input)) ) / (2πi).
	 * 
	 * You see, if you add together the logarithms of several positive real numbers, that's the same as multiplying those numbers
	 * together and taking the logarithm of their product. However, the same can't be said for negative or complex numbers. Technically,
	 * the sum will be <em>a</em> logarithm of the product, but it won't necessarily be the principal value of that logarithm. However,
	 * a fundamental rule of complex numbers is that all non-zero numbers have an infinite number of natural logarithms, and if you
	 * subtract any two of those logarithms, you'll get an integer multiple of 2πi. This function simply answers the question "what is
	 * that integer multiple?" And it does so without performing any logarithms.
	 * @param z each complex input
	 * @return the logarithm offset
	 */
	public static int logOffset(Complex... z) { //the difference of the sum of logs and the log of the product (over 2πi)
		ArrayList<Complex> inp = new ArrayList<Complex>();
		for(Complex c : z) { inp.add(c); }                   //convert array to arraylist
		return recursiveLogOffset(new int[1],one(),inp); //use private function to compute the offset
	}
	
	/**
	 * Returns the sum of the logarithms of each complex input. It does this by computing the product, taking the logarithm, adding
	 * 2πi times the log offset, and performing some overflow/underflow corrections along the way. Only one logarithm is needed to
	 * calculate this.
	 * @param z each complex input
	 * @return the sum of their logarithms
	 */
	public static Complex logSum(Complex... z) {
		ArrayList<Complex> inp = new ArrayList<Complex>();
		for(Complex c : z) { inp.add(c); } //convert array to arraylist
		
		Complex prod = one();      //initialize product
		int[] change = new int[1]; //initialize change
		int offset = recursiveLogOffset(change,prod,inp); //compute the log offset (as well as the product & change)
		
		Complex ln = ln(prod);                                //find the logarithm of the product
		if(change[0]!=0) { ln.addeq(change[0]*(1022*LOG2)); } //make an adjustment for the number of times we overflowed / underflowed
		ln.addeqI(2*Math.PI*offset);                          //make another adjustment for the number of times we went around in a circle
		
		return ln; //return the result
	}
	
	/**
	 * Complicated.
	 * For a set of complex inputs, this gives us the logarithm offset, the product of the inputs, and the number of times we overflowed
	 * minus the number of times we underflowed while performing this calculation. This function is private because the information we gain
	 * from it has to be interpreted properly
	 * 
	 * @param change the # of times we overflowed - the # of times we underflowed (updated at the end of each iteration)
	 * @param prod the product (adjusted for overflow / underflow)
	 * @param z the set of complex inputs
	 * @return the logarithm offset of those inputs
	 */
	private static int recursiveLogOffset(int[] change, Complex prod, ArrayList<Complex> z) {
		switch(z.size()) {        //return depends on # of inputs
		case 1: prod.set(z.get(0)); case 0: return 0; //0 or 1 inputs: no difference (but in the case of 1, we should update the product)
		case 2:
			Complex prod2 = z.get(0).mul(z.get(1)); //compute the product
			if(prod2.equals(0))    { prod.set(z.get(0).scalb( 1022).mul(z.get(1))); --change[0]; } //underflow: grow, record
			else if(prod2.isInf()) { prod.set(z.get(0).scalb(-1022).mul(z.get(1))); ++change[0]; } //overflow: shrink, record
			else { prod.set(prod2); }                                                              //else: set
			
			boolean side1 = (z.get(0).im>0 || z.get(0).im==0&&z.get(0).re<0), //find which side of the x-axis z[0] is on,
					side2 = (z.get(1).im>0 || z.get(1).im==0&&z.get(1).re<0), //which side z[1] is on,
					side3 = (    prod.im>0 ||     prod.im==0&&    prod.re<0); //and which side their product is on
			if( side1 &&  side2 && !side3) { return  1; } //if the first two are on the same side,
			if(!side1 && !side2 &&  side3) { return -1; } //but the product isn't, record offset
			return 0;                                     //otherwise, no offset
		default:
			Complex z1=z.remove(z.size()-1);               //remove the last term
			int sum = recursiveLogOffset(change, prod, z); //perform the function on our reduced list (changing change & prod and finding the sum)
			
			side1 = (  z1.im>0 ||   z1.im==0&&  z1.re<0); //find which side the last term is on
			side2 = (prod.im>0 || prod.im==0&&prod.re<0); //find which side the product is on
			
			prod2 = prod.mul(z1); //compute product
			if(prod2.equals(0))    { prod.set(z.get(0).scalb( 1022).mul(z.get(1))); --change[0]; } //underflow adjustment
			else if(prod2.isInf()) { prod.set(z.get(0).scalb(-1022).mul(z.get(1))); ++change[0]; } //overflow adjustment
			else { prod.set(prod2); }                                                              //set
			
			side3 = (prod.im>0 || prod.im==0 && prod.re<0); //find which side the product is on
			
			if( side1 &&  side2 && !side3) { return sum+1; } //if the first two are on the same side, but the last isn't,
			if(!side1 && !side2 &&  side3) { return sum-1; } //record offset.  Otherwise, offset = 0
			return sum;                                      //regardless of which offset we get, we have to add it to our calculated sum
		}
	}
}
