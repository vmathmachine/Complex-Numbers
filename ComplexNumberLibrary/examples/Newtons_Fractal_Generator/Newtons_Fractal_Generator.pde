import complexnumbers.*; //of course, import complex numbers

///////////// METRICS: /////////////

double unitX, unitY;   //the size of 1.00000 complex units in pixels
double tlReal, tlImag; //the real & imaginary parts of the point on the top left corner

///////////// GAME STATES: /////////////

boolean refresh = true; //if true, we need to redraw the picture.  If false, we need to refine the picture
boolean shift = false;  //if true, we're dragging around the picture with our mouse
Complex grab = null;    //if true, we're dragging around one of the roots with our mouse

///////////// MISC: /////////////

ArrayList<Complex> roots = new ArrayList<Complex>(); //create an arraylist of the roots

color codes[] = {#FF0000, #FF4000, #FF8000, #FFC000, #FFFF00, #C0FF00, #80FF00, #40FF00, #00FF00, #00FF40, #00FF80, #00FFC0, #00FFFF, #00C0FF, #0080FF, #0040FF, #0000FF, #4000FF, #8000FF, #C000FF, #FF00FF, #FF00C0, #FF0080, #FF0040};

PGraphics fractal; //image of the fractal itself.  Displayed on it's own PGraphics object so we can animate the mouse hovering over the roots without changing the rest of the picture.

///////////// SETUP /////////////

void setup() {
  
  ///////////////////////////////// EVERYTHING FROM HERE ///////////////////////////////////////
  
  size(600,600); //set a size of 600 by 600 (you can set it to whatever you want)
  
  roots.add(Cpx.one());                         //set the roots to be the 3 tertiary roots of unity
  roots.add(new Complex(-0.5,Math.sqrt(0.75D))); //that is, set them to the 3 cube roots of +1
  roots.add(roots.get(1).conj());               //again, you can set these to whatever you want.  You don't even have to start with 3
  
  unitX = width/4.0D; unitY = height/4.0D; //set the size of 1 unit to be the dimensions / 4 (though again you can set them to whatever you want)
  tlReal = -2D; tlImag = -2D;              //set the position of the top left corner such that 0 is in the center (again, you can set it to whatever you want)
  
  /////////////////////////////////// TO HERE CAN BE CUSTOMIZED /////////////////////////////////
  
  fractal = createGraphics(width,height);  
  
  background(0); //set background to 0
}

void draw() {
  
  double scaleX = 1.0D/unitX, scaleY = 1.0D/unitY; //calculate the units covered per pixel
  
  //////////////////// DRAW THE FRACTAL HERE ////////////////////
  
  fractal.beginDraw(); //begin drawing
  
  fractal.loadPixels(); //load all the pixels
  
  if(roots.size()==0) { fractal.background(0); } //SPECIAL CASE: 0 roots, paint the whole thing black
  
  //otherwise
  else for(int x=(frameCount&3);x<width;x+=4) for(int y=((frameCount>>2)&3);y<height;y+=4) { //loop through 1/16 of all the pixels (note, we loop through a different 16th each frame)
    Complex inp = new Complex(scaleX*x+tlReal, scaleY*y+tlImag); //match these coordinates to a point on the complex plane
    
    Complex out = newtonRecursion(inp,32); //perform Newton's method on this complex number
    
    int close = closestRoot(out); //find the closest root
    color col = codes[close];     //find the matching color code
    
    //if(out.sub(roots.get(close)).absq()>0.000001D) { col = #000000; } //if the option is turned on, paint all pixels which don't converge to a root black.
    //CONS: if you zoom in too far, the fractal will just be black.  If you zoom out too far, everything will be black, because everything takes too long to converge.
    
    if(refresh) { //if in refresh mode:
      for(int x2=-2;x2!=2;x2++) for(int y2=-2;y2!=2;y2++) { //loop through the surrounding 16 pixels
        fractal.pixels[width*constrain(y+y2,0,height-1)+constrain(x+x2,0,width-1)]=col; //paint them the same color
      }
    }
    else { //if in refine mode
      fractal.pixels[width*y+x]=col; //paint 1 pixel.  If you don't change anything, the picture will be detailed after 16 frames.
    }
    
    /** NOTE:
     * Since we don't have the luxury of utilizing the GPU to perform parallel processing, we instead have to perform all our calculations on the CPU.  This isn't ideal, as it means we have to calculate Newton's method
     * on 360,000 complex numbers every single frame.  Even a lot of modern CPUs just aren't built for that kind of heavy lifting, so instead, I've implemented what's known as "interlacing".  Instead of doing this for every
     * single pixel every single frame, I do it for 1/16th of the pixels every frame, and each frame it's a different 16th.  To be more specific, we do it for every 4 pixels in the x and y direction, starting from a different
     * location every time.  Unfortunately, this tends to cause some unseemly issues when the picture on the screen is constantly changing.  Most programs that utilize it only split it up into halves, and even they seem a
     * bit off, so you can imagine how bad it must be when splitting into 16ths.
     *
     * Luckily, we only do this when the picture is static and unchanging.  Whenever we move the picture around, zoom in or out, or move around one of the roots, we instead lower the quality.  Instead of continually drawing
     * another 1/16th of the pixels over the course of 16 frames, we just calculate the colors of 1/16th of the pixels, then paint the surrounding 4x4 square that exact color.  The resulting image comes out as blurry, but luckily,
     * I actually don't care!  It's a sacrifice I'm willing to make, and it looks better than trying to interlace a moving picture.  And it's also better than trying to drag around the image and the program responding so slowly
     * you actually can't tell if your movements are being read.  This blurry image is generated anytime refresh is enabled.  If the picture isn't moving or changing in any way, refresh will be disabled, we can draw over and fine
     * tune the blurry image.  THIS IS ACTUALLY THE MOST IMPORTANT PART OF DRAWING A BLURRY IMAGE, so we can fine tune it once refresh is false.  And it won't look that much different, or at the very least it won't be jarringly
     * different, and we can still sort of tell what it's supposed to look like before it's even done rendering.  Admittedly, this aspect can be amplified by carefully choosing the order that each 1/16 of the pixels is chosen,
     * but I didn't really feel like doing that, so I didn't.
     *
    */
  }
  fractal.updatePixels(); //update the set of pixels
  
  if(roots.size()==0) { fractal.background(0); } //SPECIAL CASE: 0 roots, paint the whole thing black
  
  fractal.endDraw(); //stop drawing
  
  ////////////////// DONE DRAWING THE FRACTAL /////////////////
  
  image(fractal, 0, 0); //draw the fractal
  
  ////////////////// DRAWING THE ROOTS //////////////////////
  
  noStroke(); //no stroke
  for(Complex c : roots) { //loop through all roots
    float x = (float)(unitX*(c.re-tlReal)), y = (float)(unitY*(c.im-tlImag)); //compute the x,y coordinates
    
    if(grab==c)                             { fill(255);     } //if we're grabbing & dragging it, draw it opaque white
    else if(sq(mouseX-x)+sq(mouseY-y)<=100) { fill(255,192); } //otherwise, if we're hovering over it, draw it slightly translucent white
    else                                    { fill(255,128); } //otherwise, draw it half-way translucent white
    
    circle(x, y, 20); //draw a circle where the root is
  }
  
  //////////////// MOVING AND REFRESHING ////////////////////
  
  refresh = false; //unless/until I change my mind later, disable refresh mode
  
  if(mouseX!=pmouseX || mouseY!=pmouseY) { //if the mouse moved
    if(grab!=null) {                    //if we're dragging a root
      grab.re+=(mouseX-pmouseX)*scaleX; //move the x-coord of the root
      grab.im+=(mouseY-pmouseY)*scaleY; //move the y-coord of the root
      refresh=true;                     //set refresh to true
    }
    else if(shift) {                    //if we're dragging the whole screen
      tlReal+=(pmouseX-mouseX)*scaleX;  //move the x-coord of the top-left corner
      tlImag+=(pmouseY-mouseY)*scaleY;  //move the y-coord of the top-left corner
      refresh=true;                     //set refresh to true
    }
  }
  //if(frameCount%20==1) { println(frameRate); } //this is just in case you wanna test if the framerate is stable
}

//////////////////// USER INPUTS ////////////////////////

void keyReleased() {
  if(key=='u') {  //if we press 'u', set all roots to be a root of unity
    for(int n=0;n<roots.size();n++) {                     //loop through all roots
      roots.set(n,Cpx.polar(1,2*n*Math.PI/roots.size())); //set it to a root of unity
      refresh = true;                                     //set refresh to true
      grab=null;                                          //if we were dragging something before, we're not anymore
    }
  }
  if(key=='r') { //if we press 'r':
    unitX = 0.25*width; unitY = 0.25*height; //reset zoom
    tlReal = -2D;  tlImag = -2D;             //reset position
    refresh = true;                          //refresh
  }
}

void mousePressed() {
  if(mouseButton == CENTER) { //CENTER BUTTON: place a new root
    roots.add(new Complex(mouseX/unitX+tlReal, mouseY/unitY+tlImag)); //place a new root where the mouse is
    refresh=true;                                                     //set refresh to true
  }
  else for(Complex c : roots) { //otherwise, loop through all roots to check their hitboxes
    float x = (float)(unitX*(c.re-tlReal)), y = (float)(unitY*(c.im-tlImag)); //find the x,y coords of this root
    if(sq(mouseX-x)+sq(mouseY-y)<=100) {                                      //if the mouse lies in it's hitbox
      if(mouseButton == LEFT) { grab = c; }                         //left button: start moving that root around
      if(mouseButton == RIGHT) { roots.remove(c); refresh = true; } //right button: delete that root (set refresh to true)
      break;                                                        //quit the loop
    }
  }
  if(mouseButton == LEFT && grab==null) { shift = true; } //if we left clicked, and we're not dragging any roots, switch to shift mode
}

void mouseReleased() { //every time we release the mouse
  grab = null;    //we're not grabbing anything
  shift=false;    //we're not shifting anything
  refresh = true; //set refresh to true
}

void mouseWheel(MouseEvent event) { //everytime we use the mousewheel
  float ev = event.getCount();      //calculate how much we scrolled it
  
  double zoom = (keyPressed && keyCode==SHIFT) ? 1.20D : 1.04D; //zoom a different amount depending on if we're holding down the shift key
  if     (ev<0) { unitX*=zoom; unitY*=zoom; tlReal+=mouseX*(zoom-1)/unitX; tlImag+=mouseY*(zoom-1)/unitY; refresh=true; } //-: zoom in, move top-left corner so that the point we zoom to doesn't move, set refresh to true
  else if(ev>0) { tlReal-=mouseX*(zoom-1)/unitX; tlImag-=mouseY*(zoom-1)/unitY; unitX/=zoom; unitY/=zoom; refresh=true; } //+: zoom out, move top-left corner so that the point we zoom from doesn't move, set refresh to true
}

////////////////////// MATHEMATICS ///////////////////////

//and now, finally, we move on to the part that ACTUALLY USES complex numbers

Complex newtonRecursion(Complex x, int it) { //performs it iterations of Newton's method with initial seed x
  Complex out = x.copy();          //initialize output to be a copy of the input
  for(int n=0;n<it;n++) {          //loop through all iterations
    out = newtonIteration(out);    //perform one iteration of Newton's method
    if((n&3)==3) {                 //every 4 iterations, check to see if we've hit one of the roots
      for(int k=0;k<roots.size();k++) { if(out.equals(roots.get(k))) { return out; } } //if we have, return result and exit (thus cutting out all the other iterations)
    }
  }
  return out; //return result
}

Complex newtonIteration(Complex x) { //performs one iteration of Newton's method
  /*Complex harmon = new Complex();
  for(int n=0;n<numRoots;n++) {
    harmon.addeq(c.sub(roots.get(n)).inv());
  }
  return c.sub(harmon.inv());*/
  
  return x.sub(harmonic(x)); //subtract 1/(1/(x-r0)+1/(x-r1)+1/(x-r2)+...)   (which is equivalent to y(x)/y'(x), but rewritten in the case that you already know all the roots (and therefore don't even need Newton's method))
}

Complex harmonic(Complex x) { //computes a harmonic sum, using an optimized method such that we only have to perform one division
  Complex numer = Cpx.one();           //initialize numerator to 1
  Complex denom = x.sub(roots.get(0)); //initialize denominator to x-r0
  for(int k=1;k<roots.size();k++) {    //loop through the rest of the roots
    numer.muleq(x.sub(roots.get(k))).addeq(denom); //set the numerator to num*(x-r_k)+den
    denom.muleq(x.sub(roots.get(k)));              //set the denominator to den*(x-r_k)
  }                                                //this works because n/d + 1/(x-r_k) = (n(x-r_k)+d)/(d(x-r_k))
  return denom.diveq(numer);                      //we actually want the reciprocal of this harmonic sum, so return denominator / numerator
}                                                 //while this formula looks like an improvement on paper, in reality, it seems to perform about just as well as just doing the reciprocals manually

int closestRoot(Complex c) { //computes the closest root to the input c (returns the index of that root)
  int root = -1;             //the index of the root
  double minDist = Mafs.INF; //the smallest (square) distance
  for(int n=0;n<roots.size();n++) { //loop through all roots (using numbers instead of iterators, because for some reason that ended up being faster)
    double absq = c.sub(roots.get(n)).absq(); //compute the square distance from this root
    if(absq<minDist) {                        //if it's less than our current record
      minDist = absq;                        //set this to our new record
      root = n;                              //set the new closest root
    }
  }
  if(root == -1) { return 0; } //if, somehow, none of them were closer than infinity (in other words, c is NaN), return 0 (just because it isn't -1)
  return root;                 //otherwise, return the closest root
}
