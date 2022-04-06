# Complex-Numbers
This library allows you to use double floating point complex numbers in an easy and computationally efficient manner.
It was built specifically for Processing.js.

The source code is heavily commented, and if you wanna look at it yourself, by all means! :)
If you see something that doesn't quite make sense, you might be able to find more information on the wiki I wrote.

This library has been successfully been tested on Windows 10 and Linux Mint (an Ubuntu based distro).  I see no reason why it shouldn't run on Mac OS or on earlier (or later) versions of Windows, but cannot guarantee it will.

# How to Download
Some of you probably already know this....I didn't.  So, I'm putting this down for those of you who also struggle with Github.  Also, just cuz the README file is supposed to tell you how to use the code...and if you can't download it, you can't use it.

1. Make sure you have Processing downloaded on your system.
2. Press the green dropdown "Code" button at the top of the Github page.  Press "Download ZIP".
3. Once the ZIP is downloaded (presumably, it's in your Downloads folder.  If not, you'll have to look through your browser's settings to see where it sends downloads).  Now, you have to unzip the file.  For Windows, you do that by right clicking and pressing "extract all".  For Linux, you do that by right clicking and pressing "extract here".
4. Enter the UNCOMPRESSED folder (the one that DOESN'T end in the .zip extension) named "Complex-Numbers-master".
5. Right click the Folder labeled "ComplexNumberLibrary", hit "copy".  (On Windows, you might have to first enter another, nested folder also named "Complex-Numbers-master" before completing this step).
6. If you have Processing open, close it.
7. Go to your Processing sketchbook folder (the folder containing all of your current processing sketches).  Go to the libraries folder.
8. In the libraries folder, hit paste (CTRL+V, or right-click + paste).
9. Open Processing.  Hit import (at the top), libraries, then Complex Numbers.
10. Make sure it works properly.  Try some of the example code below.

11. Once you've made sure your library is fully functional, feel free to delete the ZIP file from your downloads folder.  As well as any other copies of this library that haven't been moved to the Processing sketchbook.  This step is optional, of course.

Just to note, this library is currently going through a transition, and will soon be "Github released", whatever that means.  After that, the download process might be a little different.  Or it might be exactly the same.  I honestly have no idea.

# Details

The Mafs class allows for several key math functionalities and variables that aren't provided by default.  It contains functions for converting doubles to strings, a function
for modular exponentiation for doubles, the signum function, and several fundamental math constants (such as ∞, π/2, e, and the Euler-Mascheroni constant).  If you think I'm missing anything, please tell me.

The Complex class, which extends the Mafs class, contains all the Complex Number functionality.  All instances of the Complex class have two attributes: re and im (real and imaginary
parts, both doubles).  There are many functions to this class, anywhere from converting to a string, to creating a deep copy, to adding instances, to taking the natural logarithm.
There's a lot to see, and if you're curious, I strongly recommend you take a look at the heavily commented src java files to learn more.

The Cpx class, which extends the Complex class, contains several other Complex number functions not included in the default class.  Many of these functions either made no sense to
put in the original class (such as the double-minus-Complex function), while others just seemed to make more sense outside the class (such as Cpx.sin(x)).  Ideally, any large
mathematical algorithms using complex numbers would be built inside a user-created class which extends Cpx.  That way, throughout the functions, the programmer can just casually
call "sin(number)" or "exp(number)" without having to prefix it with a class name.  However, if the programmer is unable to implement their function inside a class which extends
Cpx, the Cpx class still has a short name, so it's not too much of a hassle to use it as a prefix.


My email is mathmachine4@gmail.com.  If you experience any issues with this library, feel free to contact me at that email.  Just make sure to use a descriptive subject line so I know it's not spam.  Also, be patient with me.  That's not my main email address, so I don't use it too frequently.


# Example Code:

```
Complex A=new Complex(2,1);  //declares A as the number 2+i
Complex B=new Complex(-1,1); //declares B as the number -1+i

Complex C=A.sq();  //declares C as the square of 2+i (which is 3+4i)
println(C.add(B)); //prints out their sum, which is 2+5i
println(B, C);     //the add function did not alter B or C, so it should print out "-1+i 3+4i"
println(C.addeq(B)); //once again prints out their sum, which is 2+5i
println(B, C);     //the addeq function DID alter C, but not B, so it should print out "-1+i 2+5i"
```

```
Complex A=Cpx.iTimes(Math.PI); //declares A as the number πi (note that this is slightly shorter than saying new Complex(0,Math.PI))
println(A.exp().add(1));       //due to Euler's identity, e^(πi)+1=0.  Due to roundoff, this will not print out that, but will instead print out 1.224646799147E-16i
```
