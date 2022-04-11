import complexnumbers.*;

int iter=0; //how many iterations to go through (Because it can't just be infinite)

/////////////////// CHANGE THIS VALUE TO WHATEVER YOU WANT!!! :) ///////////////////////////
//     ________________________|
//    |
//   \|/
//    v
double d = 2;  //the exponent we apply each iteration

//d=2: Mandelbrot Set (one-fold symmetry)
//d=3: 3-Multibrot set (two-fold symmetry)
//d=4: 4-multibrot set (three-fold symmetry)
//
//Non-negative integer values of d tend to perform better and look better
//Negative integer values perform okay, but look kinda weird
//fractional values tend to look really weird and have no rotational symmetry.
//
//
//
//If you want, you can make d a complex number instead of a double.  But it won't be vertically symmetric, so you'll have to change lines 43 and 57 to reflect that.
//
//Line 43: for(int y=0;y<height/2+1;y++)    ----->    for(int y=0;y<=height;y++)
//Line 57: comment it out
//
//
//And, of course, Line 10:  double d = 2    ----->    Complex d = new Complex(2,0);


void setup() {
  size(700,700);
  
  background(0);
  println("Multibrot Set (d="+d+")\n");
}

void draw() {
  
  fill(0,16); rect(0,0,width,height); //darken everything
  
  stroke(255); //we're gonna draw everything in white
  for(int x=0;x<width;x++) for(int y=0;y<height/2+1;y++) { //loop through all pixels on screen (well, half of them.  Since it's vertically symmetric)
    
    Complex z = new Complex(4*((double)x)/width-2,4*((double)y)/height-2); //generate this point on the complex plane
    Complex z2 = z.copy();                                                 //generate a deep copy
    
    boolean include = z2.absq()<=4;      //whether or not this point is included on the multibrot set
    
    for(int n=0;include && n<iter;n++) { //loop through all the iterations (stop if we know this won't be included)
      z2.set(z2.pow(d).addeq(z));        //raise to the power of d, then add the point z
      
      if(z2.absq()>4) { include=false; } //if |z2|>2, this can't be included
    }
    if(include) {                            //if included,
      point(x,y);                            //draw this point
      if(y!=height/2) { point(x,height-y); } //also draw the complex conjugate
    }
  }
  
  print("Iteration #"+iter+" finished"); //print the iteration number
  
  if(iter<3)       { println("  (left or right click your mouse to perform another iteration)"); }
  else if(iter==3) { println("  (You get the picture)");                                         }
  else             { println();                                                                  }
  
  noLoop(); //Don't do this every frame.  It'll probably eat up your CPU performance.
}

void mousePressed() { //every time the mouse is pressed, go another iteration
  iter++;             //iteration number increments
  loop();             //run the draw function again
}
