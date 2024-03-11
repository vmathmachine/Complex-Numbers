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
 * @version 1.1.0
 */
public class Cpx2 extends Cpx {
	
	//the Bernoulli numbers are useful in several numerical computations
	/**
	 * The Bernoulli numbers are useful in several numerical computations.
	 * <br> Notes: B[1]=+1/2, this only holds indices 0-20.
	 */
	public static double[] Bernoulli={1D, 0.5D, 0.1666666666666667D, 0, -0.0333333333333333D, 0, 0.0238095238095238D, 0, -0.0333333333333333D, 0, 0.0757575757575758D, 0, -0.2531135531135531D, 0, 1.1666666666666667D, 0D,
			                          -7.092156862745098039D, 0D, 54.97117794486215539D, 0D, -529.1242424242424242D};
	
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
		if(z.isInt() && z.re<22) {                      //if the input is an integer (and it's small enough):
			return new Complex(factorial((int)z.re-1)); //cast to an integer & compute the traditional factorial
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
	    if(z.isInt() && z.re<22) {                                //if the input is an integer (and it's small enough)
	    	return new Complex(Math.log(factorial((int)z.re-1))); //cast to an integer, compute the traditional factorial, return the log
	    }
	    
	    //////////////// GENERAL CASE ///////////////////
	    
	    int shift = getGammaShift(-1,z.re);
	    //int shift = 6;
	    Complex eval = stirlingApprox(z.add(shift)); //evaluate the Stirling approximation on z+6
	    
	    Complex[] arr = new Complex[shift];
	    for(int k=0;k<shift;k++) { arr[k]=z.add(k); }
	    eval.subeq(logSum(arr)); //subtract the preceding logarithms
	    
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
	    
	    for(int n=2;n<=14;n+=2) {                         //loop through B_2 to B_14 (ignore the 0s)
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
		if(z.equals(1)) { return new Complex(-Mafs.GAMMA); } //to make things consistent
	    
		int shift = getGammaShift(0,z.re);
	    Complex eval = digammaApprox(z.add(shift)); //evaluate the digamma approximation at z+shift
	    
	    for(int n=0;n<shift;n++) { eval.subeq(z.add(n).inv()); } //subtract the inverses of the numbers between z and z+shift
	    
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
		
		for(int n=2;n<=14;n+=2) {                 //loop through B_2 to B_14 (ignore the 0s)
			sum.subeq(mul(expo, Bernoulli[n]/n)); //subtract B_n/(n*z^n)
			if(n!=14) { expo.muleq(iter); }       //multiply expo by iter each time (except the last time)
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
			Complex reflector = polygammaReflector(m,z); //compute the reflector
			Complex eval      = polygamma(m, sub(1,z));         //evaluate ψ(m,1-z)
			if((m&1)==0) { return add(reflector, eval); } //if m is even, ψ(m,z) = reflector + ψ(m,1-z)
			else         { return sub(reflector, eval); } //if m is  odd, ψ(m,z) = reflector - ψ(m,1-z)
		}
	    
		//////////// GENERAL CASE ///////////////////
		int shift = getGammaShift(m,z.re);
	    Complex eval = polygammaApprox(m, z.add(shift)); //evaluate ±ψ(m,z+shift)
	    
	    double fact = factorial(m); //compute m!
	    
	    for(int n=0;n<shift;n++) { //loop through the (shift) preceding numbers
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
		
		double fact = factorial(m-1); //(factor) this'll be (k+m-1)!/k! in the loop (init to (m-1)!)
		
		sum=mul(expo,add(1,inv.mul(0.5*m)),-fact); //set the sum to the sum of the first and second terms: (m-1)!/z^m, m!/z^(m+1)/2
		//we do this step outside the loop because otherwise B_1 would be skipped
		
		int max = getMaxBernoulli(m); //compute the maximum bernoulli number to use
		
		for(int k=2;k<=max;k+=2) {    //loop through B_2 to B_14 (ignore the 0s)
			expo.muleq(iter);       //multiply expo by iter each time
			fact*=(k+m-1)*(k+m-2);  //update (k+m-1)!/k! each time
			fact/=k*(k-1);
			sum.subeq(mul(expo, Bernoulli[k]*fact)); //subtract B_k/(z^(k+m)) * (k+m-1)!/k!
		}
		
		return sum; //return result
	}
	
	private static int getMaxBernoulli(int m) { //gets the best bernoulli number to stop at depending on m
		if(m<=1) return 14;                     //these numbers were computed by analyzing which stopping point is usually more efficient
		if(m<=3) return 16;
		return 18;
	}
	
	private static int getGammaShift(int m, double x) { //computes how much to shift by based on m and the real part
		//if(m==-1) { return (int)Math.max(0,Math.ceil(-1.3416346*x+10.098411+1)); }
		if(m==-1) { return (int)Math.max(0,Math.ceil(-1.344445  *x+10.503084+1)); }
		if(m== 0) { return (int)Math.max(0,Math.ceil(-1.1013515 *x+ 9.898758)); }
		if(m== 1) { return (int)Math.max(0,Math.ceil(-0.8828083 *x+10.081272)); }
		if(m== 2) { return (int)Math.max(0,Math.ceil(-0.8055056 *x+ 9.359367)); }
		if(m== 3) { return (int)Math.max(0,Math.ceil(-0.73040926*x+ 9.713969)); }
		//double slope = -1d/(0.1329726*m+0.9149952);
		double slope = -1d/(0.1387092*m+0.9103506); //approximate the best slope
		double xinter = 1.3583619*m+7.4961775;      //approximate the best x-intercept
		return (int)Math.max(0,Math.ceil(slope*(x-xinter))); //return result
	}
	
	/**
	 * The reflector for the polygamma function ψ(m,z). It basically returns the m-th derivative of -πcot(πz), for m>0. It constructs said
	 * derivative as (-π)^(m+1)*csc²(πz)*(some polynomial of cot(πz)). It first finds the coefficients of the polynomial, which are found recursively
	 * via the coefficients of the polynomial for m-1. It should be noted that the larger m is, the longer this takes, and the more coefficients
	 * the polynomial will have. After it finds these coefficients, it uses Horner's method to compute the polynomial, then multiplies by what we
	 * said it'd multiply by. This function is not public because...I just didn't think it'd be that useful... But, if you find it useful (i.e. you
	 * need the derivatives of tan or cot), here's the code, feel free to copy it (just remove the private and static).
	 * 
	 * @param m integer >= 1
	 * @param z complex input
	 * @return the reflector
	 */
	
	private static Complex polygammaReflector(int m, Complex z) {
		//first, compute the coefficients:
		long[] c1 = {1}; //these will store the coefficients (c1 is the previous row, c2 is the current row)
		long[] c2;
		
		for(int n=2;n<=m;n++) { //loop through all rows in the table of coefficients
			c2 = new long[(n+1)>>1]; //init c2
			for(int k=0;k<(n+1)>>1;k++) {
				c2[k] = (k==0?0:c1[k-1]) + (k==c1.length?0:c1[k]);
				c2[k] *= (n&1)==0 ? ((n>>1)-k) : (n-(k<<1)); //here, it should multiply by (n-2k).
				//however, that means the whole row goes up 1 power of 2 every time n is even, so we divide by 2 on those rows, then multiply back
				//when we've casted it all to a floating point (so as to avoid overflows).
			}
			c1 = c2; //set previous row to current row
		}
		
		Complex cot = cot(mul(z,Math.PI)); //compute the cotangent
		Complex cot2 = cot.sq();           //square it
		
		Complex reflector = new Complex(c1[0]);
		for(int k=1;k<c1.length;k++) {
			reflector.muleq(cot2).addeq(c1[k]);
		}
		if((m&1)==0) { reflector.muleq(cot); }
		reflector.scalbeq(m>>1); //scale up by 2^(the amount of times we divided by 2)
		
		return reflector.muleq(cot2.addeq(1)).muleq(Mafs.pow(-Math.PI,m+1));
	}
	
	////////////////////////////////////////////////// ERROR FUNCTIONS ////////////////////////////////////////////////
	
	private static Complex erfi_Taylor(Complex z) { //takes a Taylor's series approximation of √(π)erfi(z)/2
		Complex term = z.copy(); //the term z^(2n+1)/n!
		Complex iter = z.sq();   //what the term multiplies by each time
		Complex sum = zero();    //the sum (initialized to 0)
		for(int n=0;n<45;n++) {  //loop through the first 45 terms
			sum.addeq(term.div(n<<1|1)); //add each term z^(2n+1)/(n!(2n+1))
			term.muleq(iter.div(n+1));   //compute the next value for z^(2n+1)/n! by *= z²/(n+1)
		}
		return sum; //return the sum
	}
	
	private static Complex erfc_Fraction(Complex z) { //computes an adjusted continued fraction expansion of erfc(z)
		Complex frac = z.copy();         //initialize the continued fraction
		for(int n=45;n>0;n--) {          //loop backwards through numbers 1-45
			frac = div(n,frac).addeq(z); //compute z + n/(fraction so far)
		}
		return frac.inv(); //return the reciprocal of what we came up with
	}                      //should return 1/(z+1/(z+2/(z+3/(z+4/(...(z+44/(z+45/z))...)))))
	
	private static void divByExpSq(Complex inp1, Complex inp2) { //divides input 1 by e to the square of input 2
		if(Math.abs(inp2.re) == Math.abs(inp2.im) && Math.abs(inp2.re) >= 1E100) { //large multiple of √(±i)
			double d1 = 8.98846567431158E307, d2 = 1.1125369292536007E-308; //store 2^1023 and 2^-1023
			
			double modulo = (inp2.re*d2*inp2.re) % (Math.PI*d2);        //modulo with π, but scaled down to prevent overflow
			double angle = modulo * d1 * (inp2.re == inp2.im ? -2 : 2); //scale it back up, then multiply by ±2
			inp1.rotateEq(angle);                                       //finally, rotate the first input by this amount 
		}
		else { //otherwise:
			Complex pow = inp2.sq().neg(); //compute the negative square of input2
			if(pow.re>709) { inp1.muleq(exp(pow.sub(10*LOG2))).muleq(1024); } //special case: exponent overflows, do this thing
			else           { inp1.muleq(exp(pow));                          } //otherwise, just multiply by the exponential
		}
	}
	
	
	/**The error function. Most commonly used in statistics, it is the solution to the famous non-elementary integral, integral e^-x² dx, also known
	 * as the area under the bell curve. It also has many other applications, from differential equations to plotting an object as it rotationally
	 * accelerates. Technically, it's actually the integral of 2/√(π)*e^-x² dx, measured from 0 to x. It's an odd function, it takes the value of 0
	 * at x=0, 1 as x goes to +∞, and -1 as x goes to -∞. Its graph is shaped like a sigmoid. It takes complex inputs.
	 * 
	 * @param z the complex input
	 * @return the error function
	 */
	
	public static Complex erf(Complex z) { //returns the error function
		if(z.re*z.re*0.340455D + z.im*z.im*0.0405612D < 1) { //if the input is within a range close to 0:
			return erfi_Taylor(z.mulI()).divI(ROOTPI2);      //Taylor's series for erfi, then multiply appropriately
		}
		Complex out = erfc_Fraction(z.mul(ROOT2)); //otherwise, continued fraction, then make adjustments
		divByExpSq(out,z);                         //divide by e^z²
		
		out.diveq(-ROOTPI2*ROOT2); //make more adjustments
		out.addeq(sgn(z.re));      //add -1, 0, or 1
		
		return out; //return the result
	}
	
	/**
	 * The complementary error function. Instead of measuring the area from x=0, it measures the area from x=+∞. This function is especially important
	 * for programming, as when the input gets higher and higher, the output of erf will rapidly approach 1. It doesn't take very long for the
	 * difference from 1 to be less than machine precision can store, causing it to round up to 1. That's why the complementary error function is
	 * important, as it measures 1 minus the error function, allowing for the difference from 1 to be stored much more precisely.
	 * 
	 * @param z the complex input
	 * @return the complementary error function
	 */
	public static Complex erfc(Complex z) { //complementary error function.
		if(z.re*z.re*0.340455D + z.im*z.im*0.0405612D < 1) {   //if the input is within a range close to 0:
			return sub(1,erfi_Taylor(z.mulI()).divI(ROOTPI2)); //Taylor's series for erfi, then multiply appropriately & sub from 1
		}
		Complex out = erfc_Fraction(z.mul(ROOT2)); //otherwise, continued fraction, then make adjustments
		divByExpSq(out,z);                         //divide by e^z²
		
		out.diveq(ROOTPI2*ROOT2); //make more adjustments
		out.addeq(1-sgn(z.re));   //add 2, 1, or 0
		
		return out; //return the result
	}
	
	/**
	 * The scaled complementary error function. It's the same as the complementary error function, but multiplied by e^x², preventing it from
	 * underflowing and causing it to decline as O(1/x).
	 * @param z the complex input
	 * @return the scaled complementary error function
	 */
	public static Complex erfcx(Complex z) { //scaled complementary error function
		if(z.re*z.re*0.340455D + z.im*z.im*0.0405612D < 1) {   //if the input is within a range close to 0:
			return sub(1,erfi_Taylor(z.mulI()).divI(ROOTPI2)).muleq(exp(z.sq())); //Taylor's series
		}
		Complex out = erfc_Fraction(z.mul(ROOT2)); //otherwise, continued fraction, then make adjustments
		out.diveq(ROOTPI2*ROOT2);                  //make more adjustments
		if(z.re<=0) { out.addeq(exp(z.sq()).muleq(z.re==0?1:2)); } //make another adjustment
		
		return out; //return the result
	}
	
	
	/**
	 * The imaginary error function. It's equivalent to erf(xi)/i. Instead of being used to integrate e^-x² (which shrinks fast), it integrates
	 * e^+x² (which grows fast).
	 * 
	 * @param z the complex input
	 * @return the imaginary error function
	 */
	public static Complex erfi(Complex z) { //returns the imaginary error function
		return erf(z.mulI()).diveqI();      //this is easy.  Just multiply by i, erf, divide by i
	}
	
	/**
	 * The cumulative distribution function, measures the area under the bell curve. Unlike the error function, which integrates 2/√(π)*e^-x² from 0,
	 * this integrates 1/√(2π)*e^(-x²/2) from -∞. It's taking the area under the standard Gaussian distribution, with mean 0 and standard deviation
	 * of 1, measuring all the area to the left of x. It still takes complex inputs.
	 * 
	 * @param z the complex input
	 * @return the cumulative distribution
	 */
	public static Complex cumulative_distribution(Complex z) { //area under bell curve from -∞ to z
		return erfc(z.div(-ROOT2)).muleq(0.5);                 //it's just erfc(-z/√2)/2
	}
	
	/**
	 * The Fresnel cosine integral. It integrates cos(x²) dx, measured from 0. It's an odd function that becomes infinitely wavy as x goes to ±∞. It,
	 * along with the sine integral, can be used to parameterize the Euler spiral, which can be used to plot the motion of an object as it rotates
	 * faster and faster. It's related to the error function, since cos(x)=(e^(xi)+e^(-xi))/2, so the Fresnel C integral is equal to
	 * (erf(x√(i))/√(i)+erf(x√(-i))/√(-i))/2.
	 * 
	 * @param z the complex input
	 * @return the Fresnel cosine integral
	 */
	public static Complex fresnelC(Complex z) { //computes the Fresnel C integral
		Complex rootI = new Complex(0.5*ROOT2,0.5*ROOT2); //compute √(i)
		Complex part1 = erf(z.mul(rootI)).muleq(rootI.conj()); //compute erf(z√(i))/√(i)
		
		if(z.im==0) { return new Complex(part1.re*ROOTPI2); } //real input: return real part times √π/2
		
		Complex part2 = erf(z.mul(rootI.conj())).muleq(rootI); //compute erf(z√(-i))/√(-i)
		return part1.add(part2).muleq(0.5*ROOTPI2);            //return their sum, over 2, times √π/2
	}
	
	/**
	 * The Fresnel sine integral. It integrates sin(x²) dx, measured from 0. It's an odd function that becomes infinitely wavy as x goes to ±∞. It,
	 * along with the cosine integral, can be used to parameterize the Euler spiral, which can be used to plot the motion of an object as it rotates
	 * faster and faster. It's related to the error function, since sin(x)=(e^(xi)-e^(-xi))/(2i), so the Fresnel S integral is equal to
	 * (erf(x√(i))/√(i)-erf(x√(-i))/√(-i))/(2i).
	 * 
	 * @param z the complex input
	 * @return the Fresnel sine integral
	 */
	public static Complex fresnelS(Complex z) { //computes the Fresnel S integral
		Complex rootI = new Complex(0.5*ROOT2,0.5*ROOT2); //compute √(i)
		Complex part1 = erf(z.mul(rootI)).muleq(rootI.conj()); //compute erf(z√(i))/√(i)
		
		if(z.im==0) { return new Complex(-part1.im*ROOTPI2); } //real input: return -imag part times √π/2
		
		Complex part2 = erf(z.mul(rootI.conj())).muleq(rootI); //compute erf(z√(-i))/√(-i)
		return part1.sub(part2).muleqI(0.5*ROOTPI2);           //return their difference, over 2i, times √π/2
	}
	
	/*static Complex erf_Calc(Complex z, boolean comp, boolean daws) { //This takes √(π)/2 times erfi(z). If comp, we subtract this from √(π)i/2. If daws, we multiply it by e^-z².
    
    if(z.re*z.re*0.0405612D+z.im*z.im*0.340455D<1) {    //if the input is within a certain range, we solve through a taylor series expansion
      Complex term=z.copy();          //this is the term (z^(2n+1)/n!)
      Complex iter=sq(z);             //this is what the term will be multiplying by each time (well, that divided by n+1)
      Complex sum=zero();             //this is the sum
      for(int n=0;n<45;n++) {         //loop through the first 45 terms of the series
        sum.addeq(term.div(2*n+1));   //add each term
        term.muleq(iter.div(n+1));    //calculate the next term
      }
      if(comp) { sum=sub(iTimes(ROOTPI2),sum); } //if it's the complementary erf, subtract from √(π)i/2
      if(daws) { sum.muleq(exp(sq(z).neg()));  }
      return sum; //return the result
    }
    
    else { //otherwise, solve through continued fraction expansion
      Complex frac=zero(); //this will store the continued fraction as we solve it (initialize to 0)
      
      if(Math.abs(z.im)!=INF) { //if the imaginary part isn't infinite, then it doesn't stay 0
        Complex iter=z.sq();   //we will be frequently using the square of the input
        frac=iter.sub(22.5);  //set frac to z²-22.5
        for(int n=22;n>0;n--) {                                //loop through 22 iterations
          frac.set( iter.sub(div(n-0.5,sub(1,div(n,frac)))) ); //through each iteration, replace the fraction with z²-(n-0.5)/(1-n/frac)
        }
        frac=z.div(frac.muleq(2)); //divide z/2 by the continued fraction
        if(!daws) { frac.muleq(exp(iter)); } //unless it's a dawson function, multiply by e^z²
      }
      
      Complex addThis; //we have to add something to make this approximation accurate
      
      if(Math.abs(z.re)<1.5) { addThis=iTimes(ROOTPI2*csgn(z.im));        } //if the real part is close enough to 0, we can just set addThis to ±√(π)i/2
      else                   { addThis=iTimes(ROOTPI2*Math.tanh(3*z.im)); } //otherwise, for the sake of making things a bit more continuous, addThis will be on a tanh scale
      
      if(comp) { //if the erf is complementary
        addThis=sub(iTimes(ROOTPI2),addThis); //subtract addThis from √(π)i/2
        frac.negeq();                        //negate the continued fraction
      }
      
      if(daws && !addThis.equals(0)) {
        addThis.muleq(exp(sq(z).neg())); //if it's a dawson function (and addThis isn't 0), multiply addThis by e^-z²
      }
      
      frac.addeq(addThis); //add the thingy
      
      return frac; //return the result
    }
  }
  
  static Complex fresnel(Complex a, boolean CorS) { //this takes the fresnel C or fresnel S function
    Complex ret=erf_Calc(a.mul(new Complex(0.7071067811865475D,0.7071067811865475D)),false,false).mul(new Complex(0.7071067811865475D,-0.7071067811865475D));
    if(a.im==0) { //if the input is real
      if(CorS) { return complex(ret.re); } //if it's the C function, return the real part
      else     { return complex(ret.im); } //if it's the S function, return the imaginary part
    }
    else { //otherwise
      Complex complement=erf_Calc(a.mul(new Complex(0.7071067811865475D,-0.7071067811865475D)),false,false).mul(new Complex(0.7071067811865475D,0.7071067811865475D)); //find the equal and opposite expression
      if(CorS) { return add(ret,complement).mul ( 0.5D); } //if it's the C function, return half their sum
      else     { return sub(ret,complement).mulI(-0.5D); } //if it's the S function, return half their difference over i
    }
  }
  
  static Complex inv_erf_Calc(Complex inp, boolean comp) { //This computes the inverse of erf_Calc.  If comp, we perform this on √(π)i/2-inp.
    
    if(inp.equalsI(comp ? 0 :  ROOTPI2)) { return iTimes( INF); } //special cases: erfinv(±√(π)i/2)=±∞i
    if(inp.equalsI(comp ? 0 : -ROOTPI2)) { return iTimes(-INF); }
    
    Complex z=comp ? sub(iTimes(ROOTPI2),inp): inp;
    
    Complex res; //this is the result
    
    Complex sto; //this is a storage variable for complex numbers
    
    if(0.92730391966D*z.re*z.re+1.615742790257D*z.im*z.im<1.0D) { //if the imaginary part is less than a certain amount, then we can solve through a taylor's series expansion
    
      double[] coef={1, -1.0D/3, 7.0D/30, -127.0D/630, 4369.0D/22680, -34807.0D/178200, 20036983.0D/97297200.0D, -2280356863.0D/10216206000.0D, 49020204823.0D/198486288000.0D, -65967241200001.0D/237588086736000.0D};
      //here are our coefficients
      
      Complex term=z.copy();    //initialize the first term to the input
      Complex iter=sq(z);       //initialize "iter", a number for the term to multiply by each time
      Complex sum=zero();       //initialize the sum to 0
      for(double d: coef) {     //loop through the coefficients
        sum.addeq(term.mul(d)); //add the next term in the series
        term.muleq(iter);       //compute the next term
      }
      res=sum; //set the result equal to this sum
    }
    
    else { //otherwise, there's another really good approximation we can use
      
      if(z.im==0) { //if the input is real:
        sto=ln(z.abs2()).muleq(2).addeq(LOG2); //set our storage variable to twice the logarithm of [ our input times ±√(2) ]
        res=sqrt(ln(sto).add(sto).mul(0.5D));
      }
      else { //if the input is imaginary, we'll be doing basically the same thing, but shifted by √(π)/2
        if(z.im>0) {
          if(comp) { sto=ln(inp.divI()).mul(-2).sub(LOG2);              }
          else     { sto=ln(add(ROOTPI2,inp.mulI())).mul(-2).sub(LOG2); }
        }
        else       { sto=ln(sub(ROOTPI2,z.mulI())).mul(-2).sub(LOG2);   }
        
        res=sqrt(ln(sto).sub(sto).mul(0.5D));
      }
      
      res.muleqcsgn(z); //multiply by z's parity
    }
    
    //Now, we apply three iterations of Halley's approximation
    
    for(short n=0;n<3;n++) {
      sto=erf_Calc(res,comp,false).sub(inp);
      if(comp) { res.addeq(sto.div(exp(sq(res)).add(sto.mul(res)))); }
      else     { res.subeq(sto.div(exp(sq(res)).sub(sto.mul(res)))); }
    }
    
    return res; //and return the result
  }
  
  static Complex erf (Complex z) { return erf_Calc(z.mulI(),false,false).mulI(-1.0D/ROOTPI2); } //erf (z) = -2i/√(π) * erf_Calc(zi)
  static Complex erfi(Complex z) { return erf_Calc(z,false,false).div(ROOTPI2);               } //erfi(z) = 2/√(π) * erf_Calc(z)
  static Complex erfc(Complex z) { return erf_Calc(z.mulI(),true,false).mulI(-1.0D/ROOTPI2);  } //erfc(z) = -2i/√(π) * erf_Calc(zi) (complementary)
  
  static Complex erfinv (Complex z) { return inv_erf_Calc(z.mulI(ROOTPI2),false).divI(); } //erf^(-1) (z) = -i inv_erf_Calc(√(π)zi/2)
  static Complex erfiinv(Complex z) { return inv_erf_Calc(z.mul(ROOTPI2),false);         } //erfi^(-1)(z) = inv_erf_Calc(√(π)z/2)
  static Complex erfcinv(Complex z) { return inv_erf_Calc(z.mulI(ROOTPI2),true).divI();  } //erfc^(-1)(z) = -i inv_erf_Calc(√(π)zi/2) (complementary)
  
  static Complex FresnelC(Complex z) { return fresnel(z,true);  } //Fresnel's C Integral
  static Complex FresnelS(Complex z) { return fresnel(z,false); } //Fresnel's S Integral
  
  static Complex Dawson(Complex z, boolean plus) {                    //Dawson plus/minus function
    Complex result = erf_Calc(plus ? z : z.mulI(),false,true); //compute erf_Calc of either z or z*i (with dawson value true)
    return plus ? result : result.divI();                      //return either the result or the result over i
  }
  */
	
	
	/* THINGS TO DO:
	 * add fsincos
	 * 
	 * add inverse error functions
	 * add Dawson functions
	 * add Faddeeva functions
	 * make polygamma functions more accurate
	 * 
	 * make the Fresnel functions work for large inputs with a tiny imaginary part
	 * 
	 * 
	 */
}