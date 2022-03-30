package complexnumbers;

public class Mafs {
	public static double inf=1.0D/0;
	public static double e=2.71828182845904524D;
	public static double HALFPI=1.57079632679489662D;
	public static double ROOTPI2=0.886226925452758D;
	public static double log2=0.6931471805599453D;
	public static double Mascheroni=0.57721566490153286D;
	
	public static Double Double(String string) {
		if(string.equals("∞"))  { return  inf; } // ∞: return  ∞
		if(string.equals("-∞")) { return -inf; } //-∞: return -∞
		return new Double(string);               //otherwise, return the number
	}
	
	public static String unsplice(String s, int chop1, int chop2) { //this takes a string and returns the same string with everything between chop1 and chop2 removed
		return s.substring(0, chop1) + s.substring(chop2, s.length());
	}
	
	public static String str(double dub) {  //this converts a double to a string, formatted so it doesn't have too many digits or any leading zeros
		
		//first we check for some special cases:
		if     (dub==0  ) { return "0";         } //if the number is 0, return "0" (this is specified to make sure "-0" isn't returned)
		else if(dub!=dub) { return "undefined"; } //if the number is NaN, return "undefined"
		  
		//we'll now convert the number into a string, and unsplice from it any trailing zeros
		//(for the purposes of keeping comments short, a decimal point with nothing but zeros after it will also be considered a "trailing zero")
		
		String res; //"RESult"; this is the string we will return
		int cut1;   //position of the 1st cut (right before the first trailing zero)
		int cut2;   //position of the 2nd cut (right after the coefficient)
		
		if(Math.abs(dub)<1E10D && Math.abs(dub)>=1E-3D) { //if the number is normal sized, we will return a coefficient w/ no base or exponent
			res=String.format("%1.12f",dub);             //format the number to 12 decimal places
			cut2=res.length();                          //since the text is only a coefficient, cut2 happens at the end of the string
		}
		
		else {                             //number is very large / small: return in full scientific notation
			res=String.format("%1.12E",dub); //format the number to 12 decimal places with scientific notation
			cut2=res.indexOf("E");           //the coefficient ends right before the "E"
			
		    //before we move on to remove any trailing zeros, let's first remove any unnecessary 0s in the exponent
		    if(res.charAt(cut2+2)=='0') { res=unsplice(res,cut2+2,cut2+3); } //if there's a 0 after the "E+" or "E-", remove it
		    //if(res.charAt(cut2+1)=='+') { res=unsplice(res,cut2+1,cut2+2); } //if there's a + after the "E", remove it
		}
		
		cut1=cut2;                                 //at first, we'll assume there are no trailing zeros
		while(res.charAt(cut1-1)=='0') { cut1--; } //continually decrement cut1 until there are no more zeros before it
		if   (res.charAt(cut1-1)=='.') { cut1--; } //after that, if there's a decimal point before cut1, decrement cut1 once more
		
		if(cut1!=cut2) { res=unsplice(res,cut1,cut2); } //now, we just remove everything between the two cuts (if applicable)
		
		return res; //return the resulting string
	}
	
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
	
	public static int sgn(double d) { if(d==0) { return 0; } return d>0 ? 1 : -1; } //return 0 if input is 0, else return sign
	
	public static double modPos(double d1, double d2) { //returns the mod, but never negative (except when d2 is negative)
		double ret = d1%d2;                    //perform built-in mod
		return (ret!=0 && (ret>=0 ^ d2>=0)) ? ret+d2 : ret; //if it's negative, make it positive
	}
	
	
	public static double[] fsinhcosh(double d) { //returns the cosh & sinh, computed simultaneously
		if(Math.abs(d)<1E-4D) { double sq = d*d; return new double[] {d+d*sq/6, 1+0.5*sq+sq*sq/24}; } //small input: return Taylor's series
		if(Math.abs(d)>20)    { double exp = Math.exp(d-log2); return new double[] {exp, d>0 ? exp : -exp}; } //large input: return +-e^(x-ln(2))
		
		double part = Math.exp(d); //regular input: compute e^d
		double inv  = 1.0D/part;   //and e^-d
		return new double[] {0.5*(part+inv), 0.5*(part-inv)}; //return their sum & difference (over 2)
	}
}
