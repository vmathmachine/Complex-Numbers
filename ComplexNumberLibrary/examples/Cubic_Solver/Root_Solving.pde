//ADJUSTMENT PARAMETERS

 //overflow/underflow adjustments
double adj1=3.273390607896142E150d,    //2^500
       adj2=1.0715086071862673E301d,   //2^1000
       adjm1=3.0549363634996047E-151d, //2^-500
       adjm2=9.332636185032189E-302d;  //2^-1000

Complex root1 = new Complex(-0.5D, Math.sqrt(0.75D)); //compute one of the complex cube roots of +1

Complex[] quadAdjust(Complex b, Complex c) {                   //solve a depressed quadratic, x^2-2bx+c = 0 (the solution is b+-sqrt(b^2-c))
  if(b.lazyabs()>adj1) {                                       //if b^2 overflows:
    Complex[] res = quadAdjust(b.mul(adjm1),c.mul(adjm2));     //compute a similar quadratic that doesn't overflow
    return new Complex[] {res[0].mul(adj1), res[1].mul(adj1)}; //multiply by some factors to turn it into the original quadratic
  }
  if(c.lazyabs()<1E-15D * b.absq()) { return new Complex[] {b.mul(2),c.div(b.mul(2))}; } //if c << b^2, we have to use an approximation to avoid roundoff errors
  
  Complex sqrt = b.sq().subeq(c).sqrt();           //default case: compute sqrt(b^2-c)
  return new Complex[] {b.add(sqrt), b.sub(sqrt)}; //return b+-sqrt
}



Complex[] quadratic(Complex a, Complex b, Complex c) { //Solves the general case Quadratic Equation ax^2+bx+c = 0
  
  if(a.equals(0)) { return new Complex[] {c.neg().div(b)}; } //a=0: the only answer is -c/b
  
  Complex inv = a.inv();                                    //calculate 1/a
  Complex b2=b.mul(inv).muleq(-0.5D),  c2=c.mul(inv);       //compute -b/(2a) and c/a
  
  if(b2.isInf()) { return new Complex[] {b2,c.neg().div(b)}; }       //if -b/(2a) overflows, approximate the two results as -b/(2a) and -c/b
  if(c2.isInf()) {                                                   //if c/a overflows:
    Complex[] res = quadAdjust(b2.mul(adjm1), c.div(a.mul(adj2)));   //calculate a similar quadratic that doesn't overflow
    return new Complex[] {res[0].mul(adj1), res[1].mul(adj1)};       //multiply by some factors to turn it into the original quadratic
  }
  if(!c.equals(0) && c2.equals(0)) {                                           //if c isn't 0, but c/a underflows:
    Complex[] res = quadAdjust(b.mul(0.5*adj1).diveq(a), c.div(a.mul(adjm2))); //calculate a similar quadratic that doesn't underflow
    return new Complex[] {res[0].mul(adjm1), res[1].mul(adjm1)};               //multiply by some factors to turn it into the original quadratic
  }
  
  return quadAdjust(b2,c2); //in the default case, solve the reduced quadratic x^2+2(b/(2a))x+(c/a) = 0
}



Complex[] cubic(Complex a, Complex b, Complex c, Complex d) { //Solves the general case Cubic Equation ax^3+bx^2+cx+d = 0
  
  if(a.equals(0)) { return quadratic(b,c,d); } //a=0: solve as a quadratic
  
  Complex delta0 = b.sq().subeq(Cpx.mul(3,a,c));                                       //compute b^2-3ac
  Complex delta1 = Cpx.add(b.cub().muleq(2), Cpx.mul(-9,a,b,c), Cpx.mul(27,a.sq(),d)); //compute 2b^3-3abc+27a^2d
  
  Complex[] terms = quadAdjust(delta1.mul(0.5), delta0.cub());  //compute the solutions to the quadratic equation x^2-delta1*x+delta0^3 = 0
  
  Complex cbrt1, cbrt2; //initialize the cube roots of each quadratic term
  
  if(terms[0].equals(0) && terms[1].equals(0)) { cbrt1=new Complex(); cbrt2=new Complex(); } //SPECIAL CASE: both terms are 0, both cube roots are 0
  
  else { //otherwise:
    if(terms[0].equals(0)) { cbrt1 = terms[1].cbrt(); } //compute the cube root of one quadratic solution
    else                   { cbrt1 = terms[0].cbrt(); } //If one of them's 0, don't pick that one!
    
    cbrt2 = delta0.div(cbrt1); //compute the cube root of the other quadratic solution (using division instead of cube roots to save on complexity)
  }
  //Complex cbrt2 = quadAdjust(delta1.mul(0.5),delta0.cub())[1].cbrt();
  
  Complex denom = a.mul(-3).inv(); //compute -1/(3a)
  
  Complex solutions[] = {null,null,null}; //initialize our solution array
  
  for(int n=0;n<3;n++) { //now we loop through all 3 values of cbrt1 (we only had the principal cube root, there's 2 more to consider)
    Complex root = Cpx.add(cbrt1,cbrt2,b).mul(denom); //each root is equal to (-cbrt1-cbrt2-b)/(3a)
    
    Complex deriv = Cpx.add(Cpx.mul(3,a,root.sq()), Cpx.mul(2,b,root), c);
    if(!deriv.equals(0)) {
      root.subeq( Cpx.add(root.cub().mul(a),root.sq().mul(b),root.mul(c),d) .div( deriv ) ); //use one iteration of Newton's method to make it slightly more precise
    }
    
    solutions[n] = root; //set this solution to that root
    
    cbrt1.muleq(root1);        //multiply one of the cube roots by a complex cube root of +1
    cbrt2.muleq(root1.conj()); //divide the other one by the same complex cube root of +1
  }
  
  return solutions; //return our solutions
}

void solve() {
  solut=cubic(paramc[0],paramc[1],paramc[2],paramc[3]);
}
