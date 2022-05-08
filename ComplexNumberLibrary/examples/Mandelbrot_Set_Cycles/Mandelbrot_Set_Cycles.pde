import complexnumbers.*;

double scaleX, scaleY;
float middle;

PImage mandelbrot;

void setup() {
  size(640,480);
  
  scaleX = 0.325D*width;
  scaleY = 0.4333333333D*height;
  middle = 0.728125*width;
  
  mandelbrot = loadImage("Mandelbrot.png");
}

void draw() {
  image(mandelbrot,0,0,width,height);
  
  createCycles(new Complex((mouseX-middle)/scaleX, (mouseY-height/2)/scaleY));
}

void createCycles(Complex z) {
  Complex z1 = z.copy();
  Complex z2;
  
  fill(255,255,0); noStroke();
  circle((float)(scaleX*z1.re+middle),(float)(scaleY*z1.im+height/2),5);
  
  for(int n=0;n<64;n++) {
    z2 = z1.sq().addeq(z);
    stroke(255,128);
    line((float)(scaleX*z1.re+middle),(float)(scaleY*z1.im+height/2),(float)(scaleX*z2.re+middle),(float)(scaleY*z2.im+height/2));
    noStroke();
    circle((float)(scaleX*z2.re+middle),(float)(scaleY*z2.im+height/2),5);
    
    z1 = z2;
  }
}
