//ADJUSTMENT PARAMETERS

 //overflow/underflow adjustments
double adj1=3.273390607896142E150d,    //2^500
       adj2=1.0715086071862673E301d,   //2^1000
       adjm1=3.0549363634996047E-151d, //2^-500
       adjm2=9.332636185032189E-302d;  //2^-1000

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

void solve() {
  solut=quadratic(paramc[0],paramc[1],paramc[2]);
}
