import complexnumbers.*; //import complex numbers

/*
NOTE: Unlike the quadratic solver, which was able to handle every special case, this one is only able to handle a few.
There are simply too many special cases for the cubic equation, and I don't really care enough to go through them all.

So, I just went through a few obvious ones (double root, triple root, a=0, and slight imprecision) and called it a day.
Don't be surprised to find that the equation doesn't work for extremely large or extremely small values

*/

//////////////// ROOT SOLVING PARAMETERS //////////////////////

String[] params = {"","","",""}; //the 4 parameters we type: a, b, c, and d
Complex[] paramc = {new Complex(Double.NaN),new Complex(Double.NaN),new Complex(Double.NaN),new Complex(Double.NaN)}; //the 4 parameters as complex numbers

Complex[] solut={}; //our solutions

////////////////// TYPING PARAMETERS /////////////////////////

int select=-1; //which string we have selected (0=a, 1=b, 2=c, 3=d, -1=N/A)
int cursor=0;  //cursor position

int blink = 0; //a number from 0-63.  It cycles through those numbers each frame, and the cursor shows when it's <32

/*String description = "As you may know, the quadratic equation allows you to solve any and all degree 2 polynomial equations.  This can come in a lot of handy in a lot of scenarios, "+
                     "from factoring a polynomial, to finding when a projectile hits the ground, to computing 2x2 eigenvalues, to solving second order homogenous differential equations!  "+
                     "However, there's a square root in that equation, so whenever the term under the square root is negative, the equation becomes unsolvable with real numbers!  "+
                     "Luckily, you now have complex numbers on your side!\n\nP.S. The coefficients can be complex too! :)";*/
                     
String description = "Just like how there's a quadratic equation, there's also a cubic equation.  It's a bit more complicated, and it might even be more efficient to use Newton's "+
                     "method instead, but it still exists nonetheless.  However, unlike the quadratic equation, where the only time you needed complex numbers was when the answers were "+
                     "complex, that's not the case for cubics.  When there's only one real root, it's possible to solve for that root using only real numbers.  However, when there are 3 "+
                     "real roots, each of them has to be solved by first constructing a complex number, cube rooting that, then taking the real part (along with some other algebra mumbo jumbo).  "+
                     "This is what is known as casus irreducibilus, Latin for irreducible case, as there is no way to compute this using only arithmetic and radicals without also using complex numbers.";
                     


void setup() {
  size(400,460);
  
  Complex.omit_Option=true; //use less precision (since this formula is more prone to roundoff)
  textSize(20);             //draw most things at size 20
}

void draw() {
  background(255); //white background
  
  fill(0); textAlign(CENTER,BOTTOM); text("ax³+bx²+cx+d = 0",width/2,30); //display the cubic polynomial at the top
  textAlign(LEFT,BOTTOM);
  
  drawTextboxes(); //draw the textboxes
  
  drawButton();    //draw the solve button
  
  displayRoots();  //display the roots
  
  giveDescription(); //give description on why complex numbers are useful for the quadratic equation
}

void mousePressed() {
  
  boolean found = false; //whether or not our mouse is in a text box
  
  if(mouseX>=45 && mouseX<=345) { //if the mosue is horizontally aligned
    for(int n=0;n<4;n++) {        //loop through all textboxes
      if(mouseY>=40+30*n && mouseY<=66+30*n) { //if vertically aligned
        swapSelect(n);
        
        cursor = cursorBinarySearch(params[n],mouseX-50); //find where to put the cursor
        blink = 0;                                        //make cursor visible
        found = true;                                     //we're in a text box
        break;
      }
    }
  }
  
  if(!found) {      //if our mouse isn't in any of the hitboxes:
    swapSelect(-1); //swap to -1
  }
  
  if(mouseY>=162 && mouseY<=192 && mouseX>=137 && mouseX<=263) { //if our mouse is in the button:
    solve();                                                     //solve the equation
  }
}

void keyPressed() {
  keyResponse(key, keyCode, params); //use another function to determine how to read our key inputs (i.e. backspace, delete, left, right, some normal key, etc.)
}

void drawTextboxes() { //draws the textboxes (and determines what the cursor should look like)
  boolean hitbox = false; //set to true if our mouse is on any of the textboxes' hitbox
  
  stroke(192); //(each text box has a light-gray border)
  for(int n=0;n<4;n++) {                       //loop through all 3 text boxes
    fill(0); text(char('a'+n)+"=",10,65+30*n); //show where we enter each parameter
    noFill(); rect(45,40+30*n,300,26);         //draw the rectangle where we type the parameter
    
    if(params[n].equals("") && select!=n) { //if empty text:
      fill(192); textAlign(CENTER,BOTTOM); text("Click To Enter Text",195,65+30*n); textAlign(LEFT,BOTTOM); //tell us to enter text
    }
    else {                        //otherwise:
      text(params[n],50,65+30*n); //display what we've typed
    }
    
    if(mouseY>=40+30*n && mouseY<=66+30*n && mouseX>=45 && mouseX<=345) { //if our mouse is in the text box
      hitbox=true;                                                        //hitbox is now true
    }
  }
  
  if(select!=-1) {        //if we have a text box selected
    blink = (blink+1)&63; //cycle the blink counter
    
    if((blink&32)==0) {                                           //if it's less than 32:
      float x = 50+textWidth(params[select].substring(0,cursor)); //find the position of our cursor
      stroke(0);  line(x,42+30*select,x,64+30*select);            //draw our cursor
    }
  }
  
  if(hitbox) { cursor(TEXT);  } //if our mouse is on a textbox, set our cursor to a capital I
  else       { cursor(ARROW); } //otherwise, set it to the default arrow
}

void drawButton() { //draws the solve button
  fill(0,128,255); noStroke(); //set fill & stroke
  if(mouseY>=162 && mouseY<=192 && mouseX>=137 && mouseX<=263) { //if mouse is in hitbox
    if(mousePressed) { fill(170,213,255); }                      //if mouse pressed, make it much lighter
    else             { fill( 85,170,255); }                      //if not pressed, make it a little lighter
  }
  rect(137,162,126,30,10);                                                             //draw the rectangle
  fill(255); textAlign(CENTER,CENTER); text("Solve!",200,175); textAlign(LEFT,BOTTOM); //draw the text
}

void displayRoots() { //displays the roots
  textSize(14);          //change text size, since the results can be quite large
  for(int n=0;n<3;n++) { //loop through the two roots
    String disp = "x"+(n+1)+"="; //initialize the string with the x= part
    if(n<solut.length) {         //if the root exists:
      disp+=solut[n];            //add the solution to the string
      fill(0);                   //fill black
    }
    else { fill(128); }          //if the root doesn't exist, fill gray
    text(disp,10,215+25*n);      //draw the root display
  }
  textSize(20); //reset text size
}

void giveDescription() {
  textSize(12); textLeading(13);
  fill(0);
  
  text(description,5,263,390,190);
  textSize(20);
  
  stroke(0); line(0,280,400,280);
}
