package complexnumbers;

import java.util.ArrayList;

public class Cpx extends Complex {
	
////////////////EXTERNAL CONSTRUCTORS//////////////////////
	
	public static Complex zero()            { return new Complex();    } //create instance of  0+0i
	public static Complex one ()            { return new Complex(1);   } //create instance of  1+0i
	public static Complex mOne()            { return new Complex(-1);  } //create instance of -1+0i
	public static Complex two ()            { return new Complex(2);   } //create instance of  2+0i
	public static Complex Complex(double d) { return new Complex(d);   } //create instance of  d+0i (cast double to Complex)
	public static Complex iTimes (double d) { return new Complex(0,d); } //create instance of  0+di
	
	public static Complex Complex(String s) { //convert a String to a Complex number
		int numPlusMinus=0; //first, we have to count the number of plus or minuses (with some exceptions)
		int separator = -1; //if there's only 1, this'll be used to record the position of that one
		for(int n=1;n<s.length();n++) { //we start from 1, since we won't count ones @ the start
			if((s.charAt(n)=='+' || s.charAt(n)=='-') && (s.charAt(n-1)!='e' && s.charAt(n-1)!='E')) { //if this is + or -, and the previous char wasn't e or E:
				numPlusMinus++;                                                                          //increment the counter
				separator = n;                                                                           //record the position of this plus or minus
			}
		}
  
		String[] compArray = {"0","0"}; //create an array for the real & imaginary parts as strings
  
		switch(numPlusMinus) {
			case 0: //if there's no +/- separating the components, we're dealing with either a real or imaginary number
				if(s.endsWith("i")) { //if it ends with i, then it's imaginary
					compArray[1]=s.substring(0,s.length()-1); //set the second component to this, but without an i at the end
				}
				else { //otherwise, it's real
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
  
		Complex result = new Complex(); //intialize the result
  
		try {
			result.re = Double.parseDouble(compArray[0]); //try to cast the first part to a double and make that the real part
		}
		catch(NumberFormatException e) { return new Complex(Double.NaN); } //if it's invalid, return NaN
		
		if(compArray[1].equals("+") || compArray[1].equals("")) { result.im = 1;  } //if the imaginary part is either shown as i or +i, convert that to the number 1
		else if(compArray[1].equals("-"))                       { result.im = -1; } //if the imaginary part is shown as -i, convert that to the number -1
		else {
			try {
				result.im = Double.parseDouble(compArray[1]); //otherwise, try to cast the second part to a double and make that the imaginary part
			}
			catch(NumberFormatException e) { return new Complex(Double.NaN); } //if it's invalid, return NaN
		}
		
  		return result; //otherwise, return result
	}
	
	public static Complex polar(double r, double ang) { return new Complex(r*Math.cos(ang),r*Math.sin(ang)); } //constructs a number w/ polar notation
	
	//we will now overload (most of) the functions from complex so that the complex numbers can be called as parameters
	
	public static String str(Complex z) { return z.toString(); } //this generates the complex number in text form
	
///////////////COMPLEX PARTS///////////////////////////
	public static double re(Complex z) { return z.re; } //real & imaginary parts
	public static double im(Complex z) { return z.im; }
	
	public static double absq(Complex z) { return z.absq(); } //square absolute value
	public static double abs (Complex z) { return z.abs();  } //absolute value
	public static double arg (Complex z) { return z.arg();  } //polar argument
	
	public static Complex conj(Complex z) { return z.conj(); } //complex conjugate
	
////////////////BASIC ARITHMETIC//////////////////////
	
	public static Complex add(Complex a, double b) { return a.add(b); } //these functions perform the four basic arithmetic
	public static Complex sub(Complex a, double b) { return a.sub(b); } //operations on one real and one complex number
	public static Complex mul(Complex a, double b) { return a.mul(b); }
	public static Complex div(Complex a, double b) { return a.div(b); }
	
	public static Complex add(double a, Complex b) { return b.add(a);         } //these functions perform the four basic arithmetic
	public static Complex sub(double a, Complex b) { return b.neg().addeq(a); } //operations on one real and one complex number
	public static Complex mul(double a, Complex b) { return b.mul(a);         }
	public static Complex div(double a, Complex b) { return inv(b).muleq(a);  }
	
	public static Complex add(Complex a, Complex b) { return a.add(b); } //these functions perform the four basic arithmetic
	public static Complex sub(Complex a, Complex b) { return a.sub(b); } //operations on two complex numbers
	public static Complex mul(Complex a, Complex b) { return a.mul(b); }
	public static Complex div(Complex a, Complex b) { return a.div(b); }
	
	public static Complex neg(Complex z) { return z.neg(); }
	
	public static Complex add(Complex a, Complex b, double c) { return a.add(b).addeq(c); } //3 input add (with doubles)
	public static Complex mul(Complex a, Complex b, double c) { return a.mul(b).muleq(c); } //3 input multiply (with doubles)
	
	public static Complex add(Complex a, Complex b, Complex... c) { //unlimited input sum (all Complex)
		Complex ret=a.add(b);                 //add initial two inputs
		for(Complex c2: c) { ret.addeq(c2); } //add equal all other inputs
		return ret;                           //return the result
	}
	public static Complex mul(Complex a, Complex b, Complex... c) { //unlimited input multiply (all Complex)
		Complex ret=a.mul(b);                 //multiply initial two inputs
		for(Complex c2: c) { ret.muleq(c2); } //multiply equal all other inputs
		return ret;                           //return the result
	}
	
	public static Complex add(double a, Complex b, Complex... c) { //unlimited input sum (1 double + n Complex)
		Complex ret=b.add(a);                 //add initial 2 inputs
		for(Complex c2: c) { ret.addeq(c2); } //add equal all other inputs
		return ret;                           //return the result
	}
	public static Complex mul(double a, Complex b, Complex... c) { //unlimited input multiply (1 double * n Complex)
		Complex ret = b.mul(a);               //multiply initial two inputs
		for(Complex c2: c) { ret.muleq(c2); } //multiply equal all other inputs
		return ret;                           //return the result
	}
	
////////////////RECIPROCAL, SQUARE ROOT, & OTHER IMPORTANT FUNCTIONS////////////
	
	public static Complex inv (Complex z) { return z.inv();  } //1/z
	public static Complex sq  (Complex z) { return z.sq();   } //z²
	public static Complex cub (Complex z) { return z.cub();  } //z cubed
	
	public static Complex sqrt(Complex z) { return z.sqrt(); } //√(z)
	public static Complex cbrt(Complex z) { return z.cbrt(); } //cbrt(z)
	public static Complex exp(Complex z)  { return z.exp();  } //e^z
	public static Complex ln(Complex z)   { return z.ln();   } //ln(z)
	
	public static Complex log10(Complex z) { return z.ln().muleq(0.434294481903251828D); } //base 10 log...for anyone who wanted that
	
	public static Complex pow(Complex a, int     b) { return a.pow(b); } //this computes a to the power of an integer
	public static Complex pow(Complex a, double  b) { return a.pow(b); } //this computes a to the power of a real double
	public static Complex pow(Complex a, Complex b) { return a.pow(b); } //this computes a to the power of another complex number
	
	private static class Wrapper { int in; Wrapper(int i) { in=i; } }
	
	public static int logOffset(Complex... z) { //the difference of the sum of logs and the log of the product (over 2πi)
		ArrayList<Complex> inp = new ArrayList<Complex>();
		for(Complex c : z) { inp.add(c); }                   //convert array to arraylist
		return recursiveLogOffset(new Wrapper(0),one(),inp); //use private function to compute the offset
	}
	
	public static Complex logSum(Complex... z) {
		ArrayList<Complex> inp = new ArrayList<Complex>();
		for(Complex c : z) { inp.add(c); } //convert array to arraylsit
		
		Complex prod = one();            //initialize product
		Wrapper change = new Wrapper(0); //initialize change
		int offset = recursiveLogOffset(change,prod,inp); //compute the log offset (as well as the product & change)
		
		Complex ln = ln(prod);                          	       //find the logarithm of the product
		if(change.in!=0) { ln.addeq(change.in*Math.log(1E300D)); } //make an adjustment for the number of times we overflowed / underflowed
		ln.addeqI(2*Math.PI*offset);                        	   //make another adjustment for the number of times we went around in a circle
		
		return ln; //return the result
	}
	
	private static int recursiveLogOffset(Wrapper change, Complex prod, ArrayList<Complex> z) {
		switch(z.size()) {        //return depends on # of inputs
		case 0: case 1: return 0; //0 or 1 inputs: no difference
		case 2:
			Complex prod2 = z.get(0).mul(z.get(1)); //compute the product
			if(prod2.equals(0))    { prod.set(z.get(0).mul( 1E300D).mul(z.get(1))); change.in--; } //underflow: grow, record
			else if(prod2.isInf()) { prod.set(z.get(0).mul(1E-300D).mul(z.get(1))); change.in++; } //overflow: shrink, record
			else { prod.set(prod2); }                                                              //else: set
			
			boolean side1 = (z.get(0).im>0 || z.get(0).im==0&&z.get(0).re<0), //find which side of the x-axis z[0] is on,
					side2 = (z.get(1).im>0 || z.get(1).im==0&&z.get(1).re<0), //which side z[1] is on,
					side3 = (prod.im>0 || prod.im==0&&prod.re<0);             //and which side their product is on
			if( side1 &&  side2 && !side3) { return  1; } //if the first two are on the same side,
			if(!side1 && !side2 &&  side3) { return -1; } //but the product isn't, record offset
			return 0;                                     //otherwise, no offset
		default:
			Complex z1=z.remove(z.size()-1);               //remove the last term
			int sum = recursiveLogOffset(change, prod, z); //perform the function on our reduced list (changing change & prod and finding the sum)
			
			side1 = (z1.im>0 || z1.im==0&&z1.re<0);       //find which side the last term is on
			side2 = (prod.im>0 || prod.im==0&&prod.re<0); //find which side the product is on
			
			prod2 = prod.mul(z1); //compute product
			if(prod2.equals(0))    { prod.set(z.get(0).mul( 1E300D).mul(z.get(1))); change.in--; } //underflow adjustment
			else if(prod2.isInf()) { prod.set(z.get(0).mul(1E-300D).mul(z.get(1))); change.in++; } //overflow adjustment
			else { prod.set(prod2); }                                                              //set
			
			side3 = (prod.im>0 || prod.im==0&&prod.re<0); //find which side the product is on
			
			if( side1 &&  side2 && !side3) { return sum+1; } //if the first two are on the same side, but the last isn't,
			if(!side1 && !side2 &&  side3) { return sum-1; } //record offset.  Otherwise, offset = 0
			return sum;                                      //regardless of which offset we get, we have to add it to our calculated sum
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////TRIGONOMETRY////////////////////////////////////////////////////////////////////////////////////
	
	public static Complex cosh(Complex z) { return z.cosh(); } //cosh
	public static Complex sinh(Complex z) { return z.sinh(); } //sinh
	public static Complex tanh(Complex z) { return z.tanh(); } //tanh
	
	public static Complex cos(Complex z) { return z.cos(); } //cos
	public static Complex sin(Complex z) { return z.sin(); } //sin
	public static Complex tan(Complex z) { return z.tan(); } //tan
	
	public static Complex sec(Complex z) { return z.sec(); }  public static Complex sech(Complex z) { return z.sech(); } //sec and sech
	public static Complex csc(Complex z) { return z.csc(); }  public static Complex csch(Complex z) { return z.csch(); } //csc and csch
	public static Complex cot(Complex z) { return z.cot(); }  public static Complex coth(Complex z) { return z.coth(); } //cot and coth
	
	public static Complex gd(Complex z) { //Gudermannian function
		//first, the special cases. If the input is too close to one of the poles, we have to use an approximation
		Complex z2 = z.mod_v2(iTimes(2*Math.PI)); //take a modulo with 2πi
		if(z2.subI(HALFPI).lazyabs()<1E-4) { return ln(z.mulI().addeq(HALFPI)).diveqI().addeqI(log2); } //approximation with logarithm
		if(z2.addI(HALFPI).lazyabs()<1E-4) { return ln(z.divI().addeq(HALFPI)).muleqI().subeqI(log2); } //approximation with logarithm
		
		double[] sinhcosh = fsinhcosh(z.re);               //compute the cosh & sinh of the real part
		double cos = Math.cos(z.im), sin = Math.sin(z.im); //compute the cos & sin of the imaginary part
		return new Complex(Math.atan2(sinhcosh[0],cos), 0.5*Math.log((sinhcosh[1]+sin)/(sinhcosh[1]-sin)));
	    //gd(x+yi) = atan2(sinh(x),cos(y)) + iln((cosh(x)+sin(y))/(cosh(x)-sin(y)))/2
	}
	
//////////////////////////////////////////////////////////////////////////INVERSE TRIGONOMETRY////////////////////////////////////////////////////////////////////////////////////
	
	public static Complex acosh(Complex z) { return z.acosh(); } //arcosh
	public static Complex asinh(Complex z) { return z.asinh(); } //arsinh
	public static Complex atanh(Complex z) { return z.atanh(); } //artanh
	
	public static Complex acos(Complex z) { return z.acos(); } //acos
	public static Complex asin(Complex z) { return z.asin(); } //asin
	public static Complex atan(Complex z) { return z.atan(); } //atan
	
	public static Complex asec(Complex z) { return z.asec(); }  public static Complex asech(Complex z) { return z.asech(); } //asec and asech
	public static Complex acsc(Complex z) { return z.acsc(); }  public static Complex acsch(Complex z) { return z.acsch(); } //acsc and acsch
	public static Complex acot(Complex z) { return z.acot(); }  public static Complex acoth(Complex z) { return z.acoth(); } //acot and acoth
	
	public static Complex invGd(Complex z) { //inverse Gudermannian function
		return gd(z.mulI()).diveqI();        //as it turns out, the inverse is just the gd, but with z*i and divided by i
	}                                        //gd : invGd :: sin : sinh
	
////////////////////////////////////////////////// GAMMA FUNCTIONS ////////////////////////////////////////////////
	
	public static Complex gamma(Complex a) { //this returns the gamma function of complex input a using the Lanczos approximation
		
		if(a.im==0 && a.re%1==0 && a.re<22) { //if the input is an integer, we can just solve via repeated multiplication
			
			if(a.re<=0) { return new Complex((a.re%2==0) ? inf:-inf); } //if it's negative or 0, the result will just be ±∞
			
			long prod=1L;
			for(long n=2L;n<a.re;n++) { prod*=n; } //otherwise, we will multiply together all numbers before the input
			return new Complex((double)prod);      //and return the result
		}
		
		if(a.re<-170.6243769563027) { return zero();           } //if the input is too negative (and it's not an integer), it's an underflow error, return 0
		if(a.re> 171.6243769563027) { return new Complex(inf); } //if the input is too positive, it's an overflow error, return ∞
		
		double[] coef={676.5203681218851D,  -1259.1392167224028D,  771.32342877765313D,   -176.61502916214059D,
                       12.507343278686905D, -0.13857109526572012D, 9.9843695780195716e-6D, 1.5056327351493116e-7D}; //this is a list of coefficients for the Lanczos approximation
		
		Complex b=(a.re>=0)?a.copy():sub(1,a); //the approximation is only valid for a positive real part, so if the real is negative, we'll have to use a reflection formula
		
		if(b.re>142) { b.re=(b.re%1.0)+141; } //if the input is too large, we'll have to evaluate the gamma function at a smaller value and use repeated multiplication to find the result
		
		Complex sum=new Complex(0.99999999999980993D); //this approximation requires doing a summation
		for(int n=0;n<coef.length;n++) {               //loop through the list of coefficients
			sum.addeq(div(coef[n],b.add(n)));            //each term in the summation is the coefficient divided by (the input plus the index)
		}
		Complex t=b.add(6.5); //set value t to store the input minus 6.5
		sum.muleq(pow(t,b.sub(0.5)).mul(exp(t.neg())).mul(2.5066282746310005D)); //multiply the sum by √(2π)t^(b-0.5)e^(-t)
		
		if(a.re>142 || a.re<-141) { //if the input is too large,
			int diff=(int)( ((a.re>0)?a.re:1-a.re) -b.re); //this is the difference between b and a (or 1-a)
			for(int n=0;n<diff;n++) { //loop through all numbers between b and a (or 1-a)
				sum.muleq(b.add(n));    //multiply each number in between
			}
		}
		
		if(a.re<0) { //use reflection formula for negative inputs
			sum=div(Math.PI,sin(a.mul(Math.PI)).mul(sum));
		}
		
		return sum; //return the result
	}
	
	public static Complex factorial(Complex z) { return gamma(z.add(1)); }
}
