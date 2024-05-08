package complexnumbers;

/**
 * 
 * This class is used to hold certain mathematical constants, operations, and functions that will be useful for the <code>Complex</code> class.
 * 
 * @author Math Machine
 * @version 1.1.0
 */

public class Mafs {
	/////////////////// CONSTANTS ///////////////////
	
	/** Positive infinity */
	final public static double INF=1.0D/0;
	/** π/2 */
	final public static double HALFPI=1.57079632679489662D;
	/** √(2) */
	final public static double ROOT2=1.41421356237309505D;
	/** √(π)/2.  Is used in calculation of the <code>erf</code> function */
	final public static double ROOTPI2=0.886226925452758D;
	/** ln(2) */
	final public static double LOG2=0.6931471805599453D;
	/** ln(π) */
	final public static double LOGPI=1.1447298858494001D;
	/** The Euler-Mascheroni constant, commonly represented by the Greek letter γ (gamma).  Not to be confused with the
	 *  Cpx2 method, gamma, which returns the gamma function.
	 */
	final public static double GAMMA=0.57721566490153286D;
	
	/////////////////// STRING BASED STUFF ///////////////////
	
	/**
	 * Removes (or "unsplices") the substring between chop1 (inclusive) and chop2 (exclusive)
	 * @param s the input string
	 * @param chop1 the starting index of the removed substring
	 * @param chop2 the ending index of the removed substring (+1, like with iterators)
	 * @return the output string
	 */
	public static String unsplice(String s, int chop1, int chop2) { //this takes a string and returns the same string with everything between chop1 and chop2 removed
		return s.substring(0, chop1) + s.substring(chop2, s.length());
	}
	
	/**
	 * Converts a double to a string like a human would (without .0).
	 * @param dub input double
	 * @return output string
	 */
	public static String str(double dub) { //converts a double to a string, formatted so it doesn't show -0 or have any unnecessary ".0"s.
		
		if(dub==0) { return "0"; } //to avoid returning -0, we treat 0 as a special case
		
		String res = dub+"";    //cast double to a string
		int len = res.length(); //find the string length
		
		if(res.charAt(len-1)=='0' && res.charAt(len-2)=='.') { //if it ends with ".0":
			res = res.substring(0,len-2);                        //remove the ".0"
		}
		else if(res.contains(".0E")) { //if it's in sci notation, and has ".0" right before the E:
			int ind = res.indexOf('E');     //find the index of E
			res = unsplice(res, ind-2,ind); //remove the ".0"
		}
		
		return res; //return result
	}
	
	/**
	 * Converts a double to a string like a human would, but with a specific number of decimal places.
	 * (-1 defaults to the previous function)
	 * @param dub input double
	 * @param dig number of digits
	 * @return output string
	 */
	public static String str(double dub, int dig) { //this converts a double to a string, formatted so it doesn't have too many digits or any leading zeros
		
		if(dig<0) { return str(dub); } //negative number: cast to a string with the recommended number of places
		
		//first we check for some special cases:
		if     (dub==0  ) { return "0";   } //if the number is 0, return "0" (this is specified to make sure "-0" isn't returned)
		else if(dub!=dub) { return "NaN"; } //if the number is NaN, return "undefined"
		
		//we'll now convert the number into a string, and unsplice from it any trailing zeros
		//(for the purposes of keeping comments short, a decimal point with nothing but zeros after it will also be considered a "trailing zero")
		
		String res; //"RESult"; this is the string we will return
		int cut1;   //position of the 1st cut (right before the first trailing zero)
		int cut2;   //position of the 2nd cut (right after the coefficient)
		
		double minSci=1; //the point when we go from standard to scientific notation depends on the number of digits
		switch(dig) {
			case 0: case 1: minSci=1;     break; //0, 1: all fractions show in scientific
			case 2: case 3: minSci=0.1D;  break; //2, 3: anything smaller than 1/10
			case 4: case 5: minSci=0.01D; break; //4, 5: anything smaller than 1/100
			default:        minSci=1E-3D; break; //everything else: anything smaller than 1E-3
		}
		
		if(Math.abs(dub)<1E7D && Math.abs(dub)>=minSci) { //if the number is normal sized, we will return a coefficient w/ no base or exponent
			res=String.format("%1."+dig+"f",dub);         //format the number to 12 decimal places
			cut2=res.length();                            //since the text is only a coefficient, cut2 happens at the end of the string
		}
		
		else {                                    //number is very large / small: return in full scientific notation
			res=String.format("%1."+dig+"E",dub); //format the number to 12 decimal places with scientific notation
			cut2=res.indexOf("E");                //the coefficient ends right before the "E"
			
		    //before we move on to remove any trailing zeros, let's first remove any unnecessary 0s or +s in the exponent
		    if(res.charAt(cut2+2)=='0') { res=unsplice(res,cut2+2,cut2+3); } //if there's a 0 after the "E+" or "E-", remove it
		    if(res.charAt(cut2+1)=='+') { res=unsplice(res,cut2+1,cut2+2); } //if there's a + after the "E", remove it
		}
		
		cut1=cut2;                                 //at first, we'll assume there are no trailing zeros
		while(res.charAt(cut1-1)=='0') { cut1--; } //continually decrement cut1 until there are no more zeros before it
		if   (res.charAt(cut1-1)=='.') { cut1--; } //after that, if there's a decimal point before cut1, decrement cut1 once more
		
		if(cut1!=cut2) { res=unsplice(res,cut1,cut2); } //now, we just remove everything between the two cuts (if applicable)
		
		return res; //return the resulting string
	}
	
	////////////////// MATH FUNCTIONS ////////////////////
	
	/**
	 * The factorial of an integer, represented as a double-precision floating point
	 * @param n input
	 * @return n!
	 */
	public static double factorial(int n) { //computes the factorial
		if(n<0) { return (n&1)==0 ? -INF : INF; }
		if(n>170) { return INF; }
		
		double prod = 1;                   //initialize product
		for(int k=2;k<=n;k++) { prod*=k; } //multiply all numbers from [2,n]
		return prod;                       //return result
	}
	
	/**
	 * The square of a double.
	 * @param d input
	 * @return square
	 */
	public static double sq(double d) { return d*d; }
	
	/**
	 * The cube of a double.
	 * @param d input
	 * @return cube
	 */
	public static double cub(double d) { return d*d*d; }
	
	/**
	 * The binomial coefficients function
	 * @param n the number of elements in the set
	 * @param r the number of elements you choose from the set
	 * @return the number of unique combinations of unordered elements, n!/(r!(n-r)!)
	 */
	public static double nCr(int n, int r) {
		if((r<<1)<n) { return nCr(n,n-r); } //nCr = nC(n-r), so choose the r with the least mults
		
		if(n>=0 ? r<0 : r>n) { return 0; } //if r is negative & n isn't, or r is greater than n and n is negative, the answer is 0
		
		double comb = 1;                     //initialize product to 1
		for(int k=r+1;k<=n;k++) { comb*=k; } //multiply all #s (r,n]
		comb /= factorial(n-r);              //divide by (n-r)!
		return comb;                         //return result
	}
	
	/**
	 * Raises double to integer power.  Calculated via exponentiation by squaring.
	 * 
	 * @param d the base
	 * @param a the exponent
	 * @return the base raised to the exponent
	 */
	public static double pow(double d, int a) { //compute double d ^ integer a (exponentiation by squaring)
		if(a==Integer.MIN_VALUE) { return sq(pow(1/d,0xC0000000)); } //special case: exponent is minimum integer, raise to the power of -2^30, then square result.
		//NOTE: without the above code, raising a number to the power of -2^31 would result in a stack overflow, since a would be repeatedly negated (to no effect) and z would be repeatedly inverted
		
		if(a<0)  { return pow(1/d,-a); } //a is negative: return (1/d)^(-a)
		                                 //general case:
	
		double ans=1;                   //return value, d^a (init to 1 in case a==0)
		int ex=a;                       //copy of a
		double iter=d;                  //d ^ (2 ^ (whatever digit we're at))
		boolean inits=false;            //true once ans is initialized
		
		while(ex!=0) {                          //loop through all of a's digits
			if((ex&1)==1) {
				if(inits) { ans*=iter;            } //multiply ans by iter ONLY if this digit is 1
				else      { ans=iter; inits=true; } //if ans still = 1, set ans=iter (instead of multiplying by iter)
			}
			ex >>= 1;                             //remove the last digit
		  	if(ex!=0)   { iter*=iter; }           //square the iterator (unless the loop is over)
		}
		
		return ans; //return the result
	}
	
	/**
	 * The sign (or signum) function.
	 * 
	 * @param d the input
	 * @return 1 if d is positive, -1 if d is negative, 0 if d is 0.
	 */
	public static int sgn(double d) { if(d==0) { return 0; } return d>0 ? 1 : -1; } //return 0 if input is 0, else return sign
	
	/**
	 * The complex sign function.
	 * 
	 * @param d the input
	 * @return -1 if d is negative, 1 if non-negative
	 */
	public static int csgn(double d) { return d>=0 ? 1 : -1; } //return -1 if negative, 1 if non-negative.
	
	/**
	 * A variation of the modulo who's range is always from 0 to d2.
	 * When d2 is positive, the output will always be positive (or rather, non-negative), and since d2 is almost never negative,
	 * this function is called "modPos", standing for modulo positive.
	 * @param d1 the dividend
	 * @param d2 the divisor
	 * @return the remainder
	 */
	public static double modPos(double d1, double d2) { //returns the mod, but never negative (except when d2 is negative)
		double ret = d1%d2;                                 //perform built-in mod
		return (ret!=0 && (ret>=0 ^ d2>=0)) ? ret+d2 : ret; //if it's negative, make it positive
	}
	
	/**
	 * A slight variation of the sine.  The variation makes it so, if the angle is a multiple of π, the output is always 0.
	 * @param d the input
	 * @return the sine
	 */
	public static double sin(double d) { //computes the sine without roundoff errors
		if(d%Math.PI==0) { return 0; }   //if a multiple of π, return 0
		return Math.sin(d);              //return sine
	}
	
	/**
	 * A slight variation of the cosine.  The variation makes it so, if the angle is an odd multiple of π/2, the output is always 0.
	 * @param d the input
	 * @return the cosine
	 */
	public static double cos(double d) { //computes the cosine without roundoff errors
		if(Math.abs(d%Math.PI)==HALFPI) { return 0; } //if an odd multiple of π/2, return 0
		return Math.cos(d);                           //return cosine
	}
	
	/**
	 * A slight variation of the tangent.  The variation makes it so, if the angle is a multiple of π, the output is always 0, and if the
	 * angle is an odd multiple of π/2, the output is always ∞.
	 * @param d the input
	 * @return the tangent
	 */
	public static double tan(double d) { //computes the tangent without roundoff errors
		final double mod = d%Math.PI;
		if(mod==0)                { return 0;   } //if a multiple of π, return 0
		if(Math.abs(mod)==HALFPI) { return INF; } //if an odd multiple of π/2, return ∞
		return Math.tan(d);                       //return tangent
	}
	
	/**
	 * Hyperbolic sine & cosine both computed simultaneously. The name is inspired by the <code>FSINCOS</code> instruction used in
	 * assembly code, which is slower than <code>cos</code> and slower than <code>sin</code>, but faster than doing them both.
	 * This function calculates the exponential and its reciprocal, then uses arithmetic to find the <code>sinh</code> and <code>cosh</code>.
	 * 
	 * @param d the hyperbolic argument
	 * @return the array {sinh(d), cosh(d)}
	 */
	public static double[] fsinhcosh(double d) { //returns the cosh & sinh, computed simultaneously
		if(Math.abs(d)<1E-4D) { double sq = d*d; return new double[] {d+d*sq/6, 1+0.5*sq+sq*sq/24}; }                   //small input: return Taylor's series
		if(Math.abs(d)>20)    { double exp = Math.exp(Math.abs(d)-LOG2); return new double[] {d>0 ? exp : -exp, exp}; } //large input: return +-e^(|x|-ln(2))
		
		double part = Math.exp(d); //regular input: compute e^d
		double inv  = 1.0D/part;   //and e^-d
		return new double[] {0.5*(part-inv), 0.5*(part+inv)}; //return their sum & difference (over 2)
	}
	
	/**
	 * Sine & cosine both computed simultaneously. It performs about as well as taking them individually, but since we handle special cases for
	 * multiples of π/2, it helps that we only have to compute the modulo once
	 * @param d
	 * @return
	 */
	public static double[] fsincos(double d) { //returns the sine & cosine, computed (almost) simultaneously
		final double mod = d%Math.PI; //take the modulo WRT π
		if(mod==0)                      { return new double[] {0,Math.cos(d)}; } //if a multiple of π, sine is 0
		if(mod==HALFPI || mod==-HALFPI) { return new double[] {Math.sin(d),0}; } //if a half multiple of π, cosine is 0
		return new double[] {Math.sin(d),Math.cos(d)}; //otherwise, just compute both of them
	}
}
