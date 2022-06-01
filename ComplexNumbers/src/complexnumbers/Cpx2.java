package complexnumbers;

/**
 * An additional class for more complicated, special functions.
 * 
 * <br><br> Such functions currently include the Gamma function, as well as several related functions such as the factorial and the digamma
 * function.
 * 
 * <br><br> In later versions, I plan on implementing functions related to the error function, the Riemmann Zeta function, the exponential
 * integral, polylogarithms, and elliptic integrals.  All of said functions have been built, but they have yet to be rigorously tested.
 * Also, the Elliptic integral of the third kind is extremely in the beta stages, as while I know how to compute it, I haven't the foggiest
 * idea how to remove any of its branch cuts.
 * 
 * <br><br> I also have plans to implement the Bessel functions, the incomplete gamma function, the Hurwitz zeta function, and the Lambert W
 * function (also known as the product logarithm).  However, none of said functions have been built.
 * 
 * <br><br> This class also acts as an extra piece of sample code, showing off the capabilities of the complex number library and why you might want
 * to use certain seemingly useless functions and constants.
 * 
 * @author Math Machine
 * @version 1.0.2
 */
public class Cpx2 extends Cpx {
	
	//the Bernoulli numbers are useful in several numerical computations
	/**
	 * The Bernoulli numbers are useful in several numerical computations.
	 * <br> Notes: B[1]=+1/2, this only holds indices 0-14.
	 */
	public static double[] Bernoulli={1D, 0.5D, 0.1666666666666667D, 0, -0.0333333333333333D, 0, 0.0238095238095238D, 0, -0.0333333333333333D, 0, 0.0757575757575758D, 0, -0.2531135531135531D, 0, 1.1666666666666667D};
	
	////////////////////////////////////////////////// GAMMA FUNCTIONS ////////////////////////////////////////////////
	
	/**
	 * The Gamma function: Denoted Γ(z), equivalent to (z-1)! for all integer values of z.  Not to be confused with the Euler-Mascheroni
	 * constant, GAMMA.  This function is special because it follows several identities, such as Γ(z+1) = z*Γ(z) and Γ(z)Γ(1-z) = πcsc(πz),
	 * and because it can be used to evaluate a wide multitude of definite integrals.  This library calculates the Gamma function using the
	 * Lanczos Approximation, a highly efficient approximation that combines asymptotic behavior with harmonic sums to give an accurate
	 * approximation for IEEE 64-bit double floating points.  For even better accuracy, when the input is an integer between 1 and 21, the
	 * gamma function is evaluated as (z-1)! using repeated multiplication.
	 * 
	 * @param z complex input
	 * @return Γ(z)
	 */
	public static Complex gamma(Complex z) { //this returns the gamma function of complex input a using the Lanczos approximation
		
		///////// INPUTS IN THE LEFT BRANCH ////////
		
		if(z.re<0.5) { // input is less than 1/2
			if(z.isInt()) { return new Complex((z.re%2==0) ? INF:-INF); }     //integer inputs: return ±∞
			return div(Math.PI, z.mul(Math.PI).sin().muleq(gamma(sub(1,z)))); //otherwise: return πcsc(πz)/Γ(1-z)
		}
		
		//////// OVERFLOWING INPUTS /////////
		if(z.re>171.6243769563027) { return new Complex(INF); } //if the input is too large, return an overflow (∞)
		
		//////// LARGE INPUTS THAT DON'T OVERFLOW /////////
		if(z.re>142) { //once the input exceeds ~142, the Lanczos approximation will yield an overflow, even though the output isn't an overflow
			Complex b = new Complex((z.re-(int)z.re)+141, z.im); //to solve this, we compute the gamma function of a smaller number
			Complex res = gamma(b);
			
			while(b.re<z.re) { //then, we use repeated multiplication to find the gamma function of our actual input
				res.muleq(b);  //multiply by b
				b.re++;        //increment b
			}                  //repeat
			
			return res;        //return result
		}
		
		//////// INTEGER INPUTS ////////
		if(z.isInt() && z.re<22) {                              //if the input is an integer (and it's small enough):
			return new Complex((double)factorial((int)z.re-1)); //cast to an integer & compute the traditional factorial
		}
		
		//////// GENERAL CASE /////////
		return lanczos(z); //In the general case, use the Lanczos approximation to calculate the gamma function
	}
	
	/**
	 * The Lanczos approximation.  This is a highly efficient approximation for Γ(z) for all inputs with a positive real part and which don't
	 * overflow.  The approximation combines the asymptotic relation Γ(z) ~ z^(z-1/2)*e^-z*√(2π) with a harmonic sum of reciprocals to give an
	 * estimate which works for the IEEE standard double floating points.  The coefficients of the harmonic sum are delicate, as you need to
	 * compute a different set of coefficients depending on how many sig figs you need.  These particular coefficients were calculated
	 * specifically for 64-bit floats.
	 * 
	 * @param z complex input
	 * @return  the lanczos approximation of Γ(z)
	 */
	private static Complex lanczos(Complex z) {
		double[] coef={676.5203681218851D,  -1259.1392167224028D,  771.32342877765313D,   -176.61502916214059D,
                       12.507343278686905D, -0.13857109526572012D, 9.9843695780195716e-6D, 1.5056327351493116e-7D}; //generate the coefficients
		
		Complex sum=new Complex(0.99999999999980993D); //this approximation requires doing a summation (init to about 1)
		for(int n=0;n<coef.length;n++) {               //loop through the list of coefficients
			sum.addeq(div(coef[n],z.add(n)));          //each term in the summation is the coefficient divided by (the input plus the index)
		}
		Complex t=z.add(6.5);                                                        //set value t to store the input minus 6.5
		sum.muleq(pow(t,z.sub(0.5)).muleq(exp(t.neg())).muleq(2.5066282746310005D)); //multiply the sum by √(2π)t^(z-0.5)e^(-t)
		
		return sum; //return the result
	}
	
	/**
	 * z!. The factorial function, extended to complex number inputs.  The input does not need to be an integer, or even a real number for
	 * that matter.
	 * @param z complex input
	 * @return z!
	 */
	public static Complex factorial(Complex z) { return gamma(z.add(1)); }
	
	
	/**
	 * The logarithm of the gamma function, lnΓ(z).  Computed via the Stirling approximation. (Note, it's not necessarily the principal value
	 * of the logarithm).  Obeys the relation lnΓ(z+1) = lnΓ(z)+ln(z).
	 * @param z complex input
	 * @return lnΓ(z)
	 */
	public static Complex loggamma(Complex z) { //this takes the log gamma function of complex number z
		
		////////////// INPUTS IN THE LEFT BRANCH //////////////
		
		if(z.re<0.5) { // input is less than 1/2
			if(z.isInt()) { return new Complex(-INF); }            //integer input: return -∞
			return logGammaReflector(z).subeq(loggamma(sub(1,z))); //otherwise: return ln(πcsc(πz))-lnΓ(1-z) (w/ a slight parity adjustment)
		}
		
		////////////// INTEGER INPUTS //////////////////////
	    if(z.isInt() && z.re<22) {                                        //if the input is an integer (and it's small enough)
	    	return new Complex(Math.log((double)factorial((int)z.re-1))); //cast to an integer, compute the traditional factorial, return the log
	    }
	    
	    //////////////// GENERAL CASE ///////////////////
	    
	    Complex eval = stirlingApprox(z.add(6)); //evaluate the Stirling approximation on z+6
	    
	    eval.subeq(logSum(z, z.add(1), z.add(2), z.add(3), z.add(4), z.add(5))); //subtract the 6 preceding logarithms
	    
	    return eval; //return the result
	}
	
	/**
	 * Computes the Stirling approximation of lnΓ(z).  Only works for sufficiently large values on the right half of the complex plane.
	 * @param z complex input
	 * @return result
	 */
	private static Complex stirlingApprox(Complex z) { //computes the Stirling approximation of lnΓ(z)
		Complex sum=ln(z).muleq(z.sub(0.5)).subeq(z).addeq(0.5*(LOG2+LOGPI)); //initialize something to store our sum to (z-1/2)ln(z)-z+ln(2π)/2
	    Complex expo=inv(z);                                                  //this'll store the (-2n+1)th power of z
	    Complex iter=sq(expo);                                                //this is what expo will multiply by each time
	    
	    for(int n=2;n<15;n+=2) {                          //loop through B_2 to B_14 (ignore the 0s)
	    	sum.addeq(mul(expo, Bernoulli[n]/(n*(n-1)))); //add B_(2n)/(n*(2n-1)*z^(2n-1))
	    	if(n!=14) { expo.muleq(iter); }               //multiply expo by iter each time (except the last time)
	    }
	    
	    return sum; //return result
	}
	
	/**
	 * Equivalent to lnΓ(z)+lnΓ(1-z).  Which is equal to ln(πcsc(πz)) plus some multiple of 2πi.  It's continuous everywhere, except on
	 * the branch cut where Im(z)==0 and Re(z) isn't between 0 and 1.  I made this function public because, honestly, it can't hurt to use it.
	 * Especially if you want to try and plot an (almost) continuous variation of ln(sin(πz)) that doesn't overflow.
	 * @param z complex input
	 * @return result
	 */
	public static Complex logGammaReflector(Complex z) {
		Complex sin = sin(z.mul(Math.PI)); //compute the sine
		
		if(!sin.isInf()) { //if it doesn't overflow
			Complex res = sub(LOGPI, sin.abs2().log()); //find ln(π)-ln|sin(πz)| (abs2)
			res.im += HALFPI*(z.divI().csgn())*(Math.floor(z.re)+Math.ceil(z.re)-1); //perform an adjustment to keep it continuous
			if(z.re==(int)z.re) { res.im+=HALFPI; }                              //perform another adjustment for when the real part is an integer
			return res;                                                          //return result
		}
		else { //if it does overflow
			return z.sub(0.5).muleqI(Math.PI*csgn(z.im)).addeq(LOG2+LOGPI); //return approximation πisgn(im)(z-1/2) + ln(2π)
		}
	}
	
	/**
	 * Computes the digamma function of z, denoted ψ(z).  It's equivalent to the harmonic series up to z-1, minus the Mascheroni constant γ.
	 * It's also equal to the derivative of the loggamma function.  Unlike the loggamma function, though, it has no branch cuts or non-pole
	 * discontinuities.
	 * @param z complex input
	 * @return ψ(z)
	 */
	public static Complex digamma(Complex z) {
		///////////// INPUTS IN THE LEFT BRANCH /////////////
		if(z.re<0.5) { //input is less than 1/2
			return digamma(sub(1,z)).subeq( div(Math.PI, tan(z.mul(Math.PI))) ); //calculate reflection formula, ψ(z) = ψ(1-z)-πcot(πz)
		}
	    
	    Complex eval = digammaApprox(z.add(6)); //evaluate the digamma approximation at z+6
	    
	    for(int n=0;n<=5;n++) { eval.subeq(z.add(n).inv()); } //subtract the inverses of the 6 preceding numbers
	    
	    return eval; //return the result
	}
	
	/**
	 * Approximates the digamma function using the derivative of the Stirling approximation.  Only works for sufficiently large values on the
	 * right half of the complex plane.
	 * @param z complex input
	 * @return approximation
	 */
	private static Complex digammaApprox(Complex z) { //an approximation for the digamma function over the right branch of the complex plane
		Complex sum=ln(z).subeq(z.mul(2).inv()); //initialize something to store our sum to ln(z)-1/(2z)
	    Complex expo=sq(inv(z));                 //this'll store the (-2n)th power of z
	    Complex iter=expo.copy();                //this is what expo will multiply by each time
		
		for(int n=2;n<15;n+=2) {                  //loop through B_2 to B_14 (ignore the 0s)
			sum.subeq(mul(expo, Bernoulli[n]/n)); //subtract B_n/(n*z^n)
			if(n!=7) { expo.muleq(iter); }        //multiply expo by iter each time (except the last time)
	    }
		
		return sum; //return the result
	}
	
	/**
	 * Computes the polygamma function, denoted ψ_m(z) or ψ(m,z).  It's equal to the m-th derivative of the digamma function, or the indefinite
	 * sum of (-1)^m*m!/z^(m+1).  (-1)^m(ψ(m,max+1)-ψ(m,min))/m! is equal to min^(-m-1)+(min+1)^(-m-1)+(min+2)^(-m-1)+...+max^(-m-1), provided
	 * max - min is an integer.  ψ(m,1) = (-1)^m*m!*ζ(m+1) for m>0.
	 * 
	 * @param m an integer >= -1
	 * @param z the complex input
	 * @return ψ(m,z)
	 */
	public static Complex polygamma(int m, Complex z) { //this takes the m-th polygamma function of complex number z
		//////////// SPECIAL CASES /////////////////
		//if(m==-2) { return KFunction(z,false).addeq(mul(sub(Math.log(2*Math.PI)+1,z),z,0.5)); } //not yet implemented, requires polylogarithms
		if(m==-1) { return loggamma(z);             } //ψ(-1,z) = lnΓ(z)
		if(m==0)  { return digamma (z);             } //ψ(0,z)  = ψ(z)
		if(m<-1)  { return new Complex(Double.NaN); } //negative values of m are too complicated to implement
		
		//////////// INPUTS IN THE LEFT BRANCH /////////////
		
		if(z.re<0.5) { //input is less than 1/2
			Complex reflector = polygammaReflector(m,sub(1,z)); //compute the reflector
			Complex eval      = polygamma(m, sub(1,z));         //evaluate ψ(m,1-z)
			if((m&1)==0) { return add(reflector, eval); } //if m is even, ψ(m,z) = reflector + ψ(m,1-z)
			else         { return sub(reflector, eval); } //if m is  odd, ψ(m,z) = reflector - ψ(m,1-z)
		}
	    
		//////////// GENERAL CASE ///////////////////
	    Complex eval = polygammaApprox(m, z.add(6)); //evaluate ±ψ(m,z+6)
	    
	    long fact=factorial(m); //compute m!
	    
	    for(int n=0;n<=5;n++) { //loop through the 6 preceding numbers
	    	eval.subeq(div(fact,z.add(n).pow(m+1))); //subtract m!/(z+n)^(m+1)
	    }
	    
	    if((m&1)==1) { eval.negeq(); } //if m is odd, negate
	    
	    return eval; //return the result
	}
	
	/**
	 * Approximates the polygamma function using the (m+1)th derivative of the Stirling approximation.  Only works for sufficiently large
	 * values on the right half of the complex plane.  It should also be noted that, when m is odd, this returns the negative of the correct
	 * answer. The formula evaluates to Σ[k=0,∞] B_k*(k+m-1)!/(k!*z^(k+m)), assuming B_1=-1/2.  Unfortunately, something tells me doing an
	 * infinite number of times might take kinda long, so I'll settle for 15 iterations (6 of which are ignored as they evaluate to 0). 
	 * @param m integer >= 1
	 * @param z complex input
	 * @return approximation
	 */
	private static Complex polygammaApprox(int m, Complex z) { //compute Σ[k=0,14] B_k*(k+m-1)!/(k!*z^(k+m)), assuming B_1=-1/2
		Complex sum;             //this is our sum
		Complex inv =inv(z);     //compute 1/z one time (for a slight speed up)
		Complex expo=inv.pow(m); //this is the (-k-m)th power of z
		Complex iter=sq(inv);    //this is what expo will multiply by each time
		
		long fact=factorial(m-1); //(factor) this'll be (k+m-1)!/k! in the loop (init to (m-1)!)
		
		sum=mul(expo,add(1,inv.mul(0.5*m)),-fact); //set the sum to the sum of the first and second terms: (m-1)!/z^m, m!/z^(m+1)/2
		//we do this step outside the loop because otherwise B_1 would be skipped
		
		for(int k=2;k<15;k+=2) {    //loop through B_2 to B_14 (ignore the 0s)
			expo.muleq(iter);       //multiply expo by iter each time
			fact*=(k+m-1)*(k+m-2);  //update (k+m-1)!/k! each time
			fact/=k*(k-1);
			sum.subeq(mul(expo, Bernoulli[k]*fact)); //subtract B_k/(z^(k+m)) * (k+m-1)!/k!
		}
		
		return sum; //return result
	}
	
	/**
	 * The reflector for the polygamma function ψ(m,z)  I gotta be honest, I wrote this a long time ago, and I only kinda remember how it works.
	 * Basically, this function returns the m-th derivative of -πcot(πz), for m>0.  First, it generates the coefficients for that power series,
	 * using some pattern that I honestly don't remember.  Then, it uses said coefficients to compute a power series of cot(πz).  Then it
	 * multiplies by csc²(πz) (actually, it does that beforehand, but who cares), before finally multiplying by some other stuff.  Somehow, this
	 * generates the m-th derivative of -πcot(πz).  And ψ(m,z)±ψ(m,1-z) = that m-th derivative, thus this is the reflector.
	 * @param m integer >= 1
	 * @param z complex input
	 * @return the reflector
	 */
	private static Complex polygammaReflector(int m, Complex z) {
		long[] b1={1,0}; //these will store the coefficients
		long[] b2;
		
		for(int n=3;n<=m;n++) { //loop through a process until we reach the coefficients we need
			b2=new long[(n+3)>>1];       //reset b2
			for(int k=0;k<(n+1)/2;k++) { //loop through all coefficients
				if((n&1)==0) { b2[k]=(k+1)*(b1[k]+b1[k+1]);             } //each b2 will be a function of the neighboring b1s. The function itself is dependent on if n is even or odd
				else         { b2[k]=(2*k+1)*(b1[k]+ (k==0?0:b1[k-1])); }
			}
			b2[b2.length-1]=0; //set the last term to 0
			
			b1=new long[b2.length];
			java.lang.System.arraycopy(b2,0,b1,0,b2.length); //copy b2 onto b1
		}
		
		Complex reflector=zero();                      //this is the term we'll add in our reflection formula
		Complex iter=sq(tan(mul(z.add(0.5),Math.PI))); //each iteration, we'll multiply by cot²(πz)
		Complex term=iter.add(1);                      //the first term will be csc²(πz)
		
		for(int k=0;k<=(m-1)/2;k++) {         //loop through all coefficients
			reflector.addeq(mul(term,b1[k])); //add the term times the coefficient
			term.muleq(iter);                 //multiply the term by the iterator
		}
		reflector.muleq(Math.pow(2,m>>1)*pow(Math.PI,m+1));     //multiply the reflector by 2^floor(m/2) times π^(m+1)
		if((m&1)==0) { reflector.diveq(tan(mul(z,-Math.PI))); } //if m is even, we'll multiply that by -cot(πz)
		
		return reflector; //return the reflector
	}
}
