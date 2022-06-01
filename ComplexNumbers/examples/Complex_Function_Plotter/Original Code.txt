/*

          Complex Function Plotter
        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~


--------------------INTRO-----------------

It's easy enough to plot one real number as a function of another real number.  All it takes is an x and y axis.

But what about plotting a complex number as a function of another complex number?  Well, we'd need one axis for the real part of
the input, one axis for the imaginary part of the input, one axis for the real part of the output, and one axis for the imaginary part
of the output.  Unfortunately, we just don't have enough dimensions for that.

Never fear, though.  One of many ways to get around this is to make two graphs: one where the z axis represents the real part of the output,
one where the z axis represents the imaginary part of the output.  In both graphs, the x and y represent the real and imaginary parts of the input,
respectively.  And that is precisely what this program does!

--------------------HOW TO USE-----------------------

To rotate the graph, click and drag your mouse around.  To alternate between plotting the real part and plotting the imaginary part, simply press the
'r' key.

There are 3 graphing modes: dot mode, wireframe mode, and surface mode.  To switch between them, press the 'm' key.  To cycle between them in the opposite
direction, hit capital 'M'.  If doing that wasn't enough to see the difference between each mode, allow me to explain:

In dot mode, each individual point on the graph is plotted out as a single point in 3D space.  In wireframe mode, these dots are connected by lines, creating
a series of cross sections normal to the x and y axes.  In surface mode, these lines are connected to form a surface, composed entirely of polygons (triangles,
to be precise).  The third one can be laggy for especially complicated functions.

To change the complex function that gets graphed out, simply edit line 63 to have it output whatever function you want.  inp is the input, the return statement is the
output.  If you're familiar enough with Processing, you can turn this function into a multiline function and create as complicated an output function as you want!

Finally, the graph parameters can be edited as follows: Edit lines 51 and 52 to change the boundaries in the x and y directions.  Edit line 54 to change how many
samples to take in each direction (technically, it's actually the number of samples minus 1, but that's not important).

You can also edit lines 56 and 58 if you want the program to start out in a specific mode right off the bat.  (0 = dot, 1 = wireframe, 2 = surface)

If you accidentally change this program too much and want to revert it back to default, there's a copy of this program saved as a txt file in the program folder.

*/



import complexnumbers.*;

/////////////// GRAPHING PARAMETERS //////////

float xPos, yPos, zPos; //camera position

float xMin=-4, xMax=4, xChg; //x axis minimum, maximum, and gridline change
float yMin=-4, yMax=4, yChg; //y axis minimum, maximum, and gridline change

int xStep=128, yStep=128;    //the number of steps we take along the grid in each direction (the number of grids in each direction -1)

boolean real=true; //(for if we want to plot complex functions)

int mode = 0;      //graphing mode: 0=points, 1=wireframe, 2=surface

//////////// FUNCTION TO PLOT //////////////////////

Complex out(Complex inp) { //Here, we specify our complex output given the complex input
  return Cpx.sq(inp);      // <<<----------------- Feel free to edit that line to your heart's content!  By default, we plot the square, but you can plot whatever you want!
                           //                      You can even come up with some ridiculous function which takes up several lines!
}
                           //                      Small note: If the function you want returns a double instead of a complex, the compiler will yell at you.  EASY FIX: return new Complex(function_name(inp));



double zCalc(float x, float y) { //here's the function we plot out
  Complex z = out(new Complex(x,y)); //turn our point on the x-y plane into a point on the complex plane, then perform our complex function on it
  return real ? z.re : z.im;         //since we only have 3 spatial dimensions, we have to either output the real part or the imaginary part.  Luckily, you can change which one by hitting the 'r' key!
}

//Bonus: if you want to, you can modify this program slightly to plot out any function of x and y your heart desires!  It can be something completely unrelated to complex numbers!
//all you have to do is comment out / remove all the lines in zCalc, then replace them with whatever function of x and y you want.
//If you have trouble, try changing the return type of the function to "float" rather than "double".







///////// ROTATION PARAMETERS ///////////////////

PMatrix3D reference =new PMatrix3D(1,0,0,0, 0,0,-1,0, 0,1,0,0, 0,0,0,1); //matrix represents our reference frame
PMatrix3D referenceT=new PMatrix3D(1,0,0,0, 0,0,1,0, 0,-1,0,0, 0,0,0,1); //transpose of our reference frame (also equals its inverse)

boolean mouseRot, pmouseRot; //whether we're rotating the graph, whether we were rotating the graph last frame (respectively)

///////////// SETUP FUNCTION ////////////////////

void setup() {
  size(600,600,P3D); //initialize size and renderer
  
  xChg=(xMax-xMin)/xStep; //compute the change in the x direction
  yChg=(yMax-yMin)/yStep; //compute the change in the y direction
  
  xPos = 0.5*(xMin+xMax); yPos = 0.5*(yMin+yMax); //compute the camera coordinates so that we're looking straight at the function from a specific distance
  zPos = -sqrt(sq(xMax-xMin)+sq(yMax-yMin));
}

//////////// DRAW FUNCTION //////////////////////

void draw() {
  background(0); //black background
  
  perspective(PI/3.0, float(width)/float(height), 0.01, 5.0*sqrt(3)*height); //orient camera
  
  if(mouseRot && pmouseRot) { //if we're rotating (and were rotating last frame)
    PVector amt=new PVector(mouseX-pmouseX,mouseY-pmouseY,0); //compute the change in the mouse position
    referenceT.rotate(amt.mag()/100,amt.y,-amt.x,0);          //rotate the transpose matrix by however much the mouse moved
    reference=referenceT.get(); reference.transpose();        //set the reference frame matrix equal to its transpose's transpose
  }
  
  /////////////////////RENDERING//////////////////////////
  
  translate(width/2+50*xPos,width/2+50*yPos,width*sqrt(3)/2+50*zPos); //translate to the center of the graph
  applyMatrix(reference);                                             //apply the reference matrix
  
  stroke(255); noFill();
  
  if(mode==0) { // DOT MODE:
    plotDots(); //plot the graph in dot mode
  }
  else if(mode==1) { // WIRE FRAME MODE:
    plotWireframe(); //plot the graph in wireframe mode
  }
  else if(mode==2) { // SURFACE MODE:
    plotSurface();   //plot the graph in surface mode
  }
  
  pmouseRot=mouseRot; //set this parameter to record what we did this frame
  
  //if(frameCount%30==1) { println(frameRate); } //DEBUG: for stability, or to test the complexity of a function, uncomment this so it displays the frame rate periodically
}

void mousePressed() { //mouse pressed:
  if(!mouseRot) { mouseRot=true; } //we're rotating the graph
}

void mouseReleased() { //mouse released:
  if(mouseRot) { mouseRot=false; } //we're not rotating the graph
}

void keyPressed() {    //key pressed:
  if(key=='r') { real^=true; } //'r' (real) means to switch between graphing the real & imaginary parts (if we're plotting complex functions)
  
  if(key=='m') { mode=(mode+1)%3; } //'m' (mode) means to switch between dot, wireframe, or surface mode
  if(key=='M') { mode=(mode+2)%3; } //'M' (mode) means to switch in the opposite direction (since there are only two options right now, though, it's the same as 'm')
}



void plotDots() { //plots out the graph in dot mode
  stroke(255);    //set stroke to white
  for(float x=xMin; x<=xMax; x+=xChg) {       //loop through all x coordinates
    for(float y=yMin; y<=yMax; y+=yChg) {     //loop through all y coordinates
      point(50*x,-50*y,50*(float)zCalc(x,y)); //plot out each point on those coordinates
    }
  }
}

void plotWireframe() {   //plots out the graph in wireframe mode
  
  stroke(255); noFill(); //set stroke to white, don't fill in the curves
  //start with the y-axis gridlines
  for(float x=xMin; x<=xMax; x+=xChg) { //loop through all x-values of the gridlines
    beginShape();                       //start drawing a curve
    for(float y=yMin; y<=yMax; y+=yChg) {      //loop through all y coordinates along the gridline
      vertex(50*x,-50*y,50*(float)zCalc(x,y)); //connect the dots along each point
    }
    endShape();                         //finish drawing the curve
  }
  //next, draw the x-axis gridlines
  for(float y=yMin; y<=yMax; y+=yChg) { //loop through all y-values of the gridlines
    beginShape();                       //start drawing curve
    for(float x=xMin; x<=xMax; x+=xChg) {      //loop through all x coordinates along the gridline
      vertex(50*x,-50*y,50*(float)zCalc(x,y)); //connect the dots along each point
    }
    endShape();                         //finish drawing the curve
  }
}

void plotSurface() { //plots out the graph in surface mode
  
  
  fill(255); noStroke(); //set the fill to white and stroke to none
  lights();              //enable lighting
  
  for(float y=yMin; y<yMax;y+=yChg) { //loop through the y-values
    double z1, z2=0, z3=0, z4;        //the z-values for the 4 neighboring points
    for(float x=xMin; x<xMax;x+=xChg) { //loop through the x-values
      if(x==xMin) { z1=zCalc(x,y); z4=zCalc(x,y+yChg); } //if at the beginning of the x loop, we have to compute these two manually
      else        { z1=z2; z4=z3;                      } //otherwise, we can just steal the calculations from the previous x coordinate
      z2=zCalc(x+xChg,y); z3=zCalc(x+xChg,y+yChg);       //compute the remaining two points
      
      beginShape();
      vertex(50*x,-50*y,50*(float)z1); vertex(50*(x+xChg),-50*y,50*(float)z2); vertex(50*(x+xChg),-50*(y+yChg),50*(float)z3); //draw one triangle
      endShape(CLOSE); beginShape();
      vertex(50*x,-50*y,50*(float)z1); vertex(50*x,-50*(y+yChg),50*(float)z4); vertex(50*(x+xChg),-50*(y+yChg),50*(float)z3); //draw another triangle
      endShape(CLOSE);
    }
  }
}
