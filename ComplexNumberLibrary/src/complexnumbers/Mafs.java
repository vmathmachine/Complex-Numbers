package complexnumbers;

/**
 * 
 * This class is used to hold certain mathematical constants, operations, and functions that will be useful for the <code>Complex</code> class.
 * 
 * @author Math Machine
 * @version 1.0.0
 */

public class Mafs {
	/////////////////// CONSTANTS ///////////////////
	
	/** Positive infinity */
	public static double inf=1.0D/0;
	/** Euler's Number */
	public static double e=2.71828182845904524D;
	/** π/2 */
	public static double HALFPI=1.57079632679489662D;
	/** √(2) */
	public static double ROOT2=1.41421356237309505D;
	/** √(π)/2.  Will be used in calculation of the <code>erf</code> function */
	public static double ROOTPI2=0.886226925452758D;
	/** ln(2) */
	public static double log2=0.6931471805599453D;
	/** ln(π) */
	public static double logPI=1.1447298858494001D;
	/** The Euler-Mascheroni constant.
	 *  It's just called Mascheroni because calling it Euler might cause people to confuse it for Euler's number.
	 */
	public static double Mascheroni=0.57721566490153286D;
	
	/////////////////// STRING BASED STUFF ///////////////////
	
	/**
	 * removes (or "unsplices") the substring between chop1 (inclusive) and chop2 (exclusive)
	 * @param s the input string
	 * @param chop1 the starting index of the removed substring
	 * @param chop2 the ending index of the removed substring (+1, like with iterators)
	 * @return the output string
	 */
	public static String unsplice(String s, int chop1, int chop2) { //this takes a string and returns the same string with everything between chop1 and chop2 removed
		return s.substring(0, chop1) + s.substring(chop2, s.length());
	}
	
	/**
	 * Converts a double to a string like a human would do it
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
	 * Converts a double to a string like a human would do it, but with a customizable number of decimal places
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
	 * The factorial of an integer, represented with 64 bits (returns 2^63-1 if it overflows)
	 * @param n input
	 * @return n!
	 */
	public static long factorial(int n) { //computes the factorial
		if(n<0 || n>=21) { return Long.MAX_VALUE; } //outside the range [0,20], n! overflows
		long prod = 1L;                    //initialize product
		for(int k=2;k<=n;k++) { prod*=k; } //multiply all numbers from [2,n]
		return prod;                       //return result
	}
	
	/**
	 * A double raised to the power of an integer, calculated using exponentiation by squaring.
	 * 
	 * @param d - the base
	 * @param a - the exponent
	 * @return the base raised to the exponent
	 */
	public static double dPow(double d, int a) { //compute double d ^ integer a (exponentiation by squaring)
		if(a<0)  { return dPow(1/d,-a); } //a is negative: return (1/d)^(-a)
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
	 * The sign/signum function.
	 * 
	 * @param d - the input
	 * @return - 1 if d is positive, -1 if d is negative, 0 if d is 0.
	 */
	public static int sgn(double d) { if(d==0) { return 0; } return d>0 ? 1 : -1; } //return 0 if input is 0, else return sign
	
	/**
	 * The complex sign function.
	 * 
	 * @param d - the input
	 * @return - -1 if d is negative, 1 if non-negative
	 */
	public static int csgn(double d) { return d>=0 ? 1 : -1; } //return -1 if negative, 1 if non-negative.
	
	/**
	 * A variation of the modulo who's range is always from 0 to d2.
	 * When d2 is positive, the output will always be positive (or rather, non-negative), and since d2 is almost never negative,
	 * this function is called "modPos", standing for modulo positive.
	 * @param d1 - the dividend
	 * @param d2 - the divisor
	 * @return - the remainder
	 */
	public static double modPos(double d1, double d2) { //returns the mod, but never negative (except when d2 is negative)
		double ret = d1%d2;                                 //perform built-in mod
		return (ret!=0 && (ret>=0 ^ d2>=0)) ? ret+d2 : ret; //if it's negative, make it positive
	}
	
	/**
	 * A slight variation of the sine such that, if the angle is a multiple of π, the output is always 0.
	 * @param d the input
	 * @return the sine
	 */
	public static double sin(double d) { //computes the sine without roundoff errors
		if(d%Math.PI==0) { return 0; }   //if a multiple of π, return 0
		return Math.sin(d);              //return sine
	}
	
	/**
	 * A slight variation of the cosine such that, if the angle is an odd multiple of π/2, the output is always 0
	 * @param d the input
	 * @return the cosine
	 */
	public static double cos(double d) { //computes the cosine without roundoff errors
		if(Math.abs(d%Math.PI)==HALFPI) { return 0; } //if an odd multiple of π/2, return 0
		return Math.cos(d);                           //return cosine
	}
	
	/**
	 * A slight variation of the tangent such that, if the angle is a multiple of π, the output is always 0, and if the angle is an odd
	 * multiple of π/2, the output is always ∞.
	 * @param d the input
	 * @return the tangent
	 */
	public static double tan(double d) { //computes the tangent without roundoff errors
		double mod = d%Math.PI;
		if(mod==0)                { return 0;   } //if a multiple of π, return 0
		if(Math.abs(mod)==HALFPI) { return inf; } //if an odd multiple of π/2, return ∞
		return Math.tan(d);                       //return tangent
	}
	
	/**
	 * The hyperbolic sine & cosine both computed simultaneously.  The name is inspired by the <code>FSINCOS</code> instruction used in
	 * assembly code, which is slower than <code>cos</code> and slower than <code>sin</code>, but faster than doing them both.
	 * This function calculates the exponential and its reciprocal, then uses arithmetic to find the <code>sinh</code> and <code>cosh</code>.
	 * 
	 * @param d - the hyperbolic argument
	 * @return - the array {sinh(d), cosh(d)}
	 */
	public static double[] fsinhcosh(double d) { //returns the cosh & sinh, computed simultaneously
		if(Math.abs(d)<1E-4D) { double sq = d*d; return new double[] {d+d*sq/6, 1+0.5*sq+sq*sq/24}; }                   //small input: return Taylor's series
		if(Math.abs(d)>20)    { double exp = Math.exp(Math.abs(d)-log2); return new double[] {d>0 ? exp : -exp, exp}; } //large input: return +-e^(|x|-ln(2))
		
		double part = Math.exp(d); //regular input: compute e^d
		double inv  = 1.0D/part;   //and e^-d
		return new double[] {0.5*(part-inv), 0.5*(part+inv)}; //return their sum & difference (over 2)
	}
}
