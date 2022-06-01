# Complex Numbers
This library allows you to use double floating point precision complex numbers in an easy and computationally efficient manner.
It was built specifically for Processing.js.

The source code is heavily commented, and if you wanna look at it yourself, by all means! :)
If you see something that doesn't quite make sense, you might be able to find more information on the wiki I wrote.

This library has been tested and proven functional on Windows 10, on Ubuntu 20.04/Mint 20.x, and on ArchLinux 21.2.  I see no reason why it shouldn't run on other versions of Windows and Linux, or on Mac OS, but I cannot guarantee it will.

It has been tested and proven functional on Processing 3.5.4 and on Processing 4.0 Beta 8.

This library does not depend on any other libraries, excluding of course the built-in java packages (such as java.lang and java.util).

The most recent version of this library is 1.0.2.

# How to Download
Some of you probably already know this....I didn't.  So, I'm putting this down for those of you who also struggle with Github.  Also, just cuz the README file is supposed to tell you how to use the code...and if you can't download it, you can't use it.

1. Make sure you have Processing downloaded on your system.
2. Press the green dropdown "Code" button at the top of the Github page.  Press "Download ZIP".
3. The ZIP file should now be downloaded (presumably, it's in your Downloads folder.  If not, you'll have to look through your browser's settings to see where it sends downloads).  Now, you have to unzip the file.  For Windows, you do that by right clicking and pressing "extract all".  For Linux, you do that by right clicking and pressing "extract here".  For Mac, you do that by double clicking the .zip file.
4. Enter the UNCOMPRESSED folder (the one that DOESN'T end in the .zip extension) named "Complex-Numbers-master".  If you're downloading another version, it'll say "Complex-Numbers-" followed by that version.
5. Right click the Folder labeled "ComplexNumberLibrary", hit "copy".  (In some cases, you might have to first enter another, nested folder also named "Complex-Numbers-master" before completing this step.  You might also possibly have to left click the folder first just so the computer knows what you're selecting).
6. If you have Processing open, close it.
7. Go to your Processing sketchbook folder (the folder containing all of your current processing sketches).  Go to the "libraries" folder.
8. In the libraries folder, hit paste (CTRL+V, or right-click + paste).
9. Open Processing.  Click sketch (at the top), Import Library, then Complex Numbers.
10. Make sure it works properly.  Try some of the example code at the bottom.

11. Once you've made sure your library is fully functional, feel free to delete the ZIP file from your downloads folder.  As well as any other copies of this library that haven't been moved to the Processing sketchbook (if you completed the steps above to a T, that should just be the copy in your Downloads folder).  This step is optional, of course.

# Details

The Mafs class allows for several key math functionalities and variables that aren't provided by default.  It contains functions for converting doubles to strings, a function
for modular exponentiation for doubles, the signum function, fast computation of sinh and cosh, round-off proof computation of sin and cos, and several fundamental math constants (such as ∞, π/2, and the Euler-Mascheroni constant).  If you think I'm missing anything, please tell me.

The Complex class, which extends the Mafs class, contains all the Complex Number functionality.  All instances of the Complex class have two attributes: re and im (real and imaginary
parts, both doubles).  There are many functions to this class, anywhere from converting to a string, to creating a deep copy, to addition and subtraction, to taking the natural logarithm.
There's a lot to see, and if you're curious, I recommend you take a look at the heavily commented src java files to learn more.

The Cpx class, which extends the Complex class, contains several other Complex number functions not included in the default class.  Many of these functions either made no sense to
put in the original class (such as the double-minus-Complex function), while others just seemed to make more sense outside the class (such as Cpx.gd(x)).  Ideally, any large
mathematical algorithms using complex numbers would be built inside a user-created class which extends Cpx.  That way, throughout the functions, the programmer can just casually
call "sin(number)" or "exp(number)" without having to prefix it with a class name.  However, if the programmer is unable to implement their function inside a class which extends
Cpx, the Cpx class still has a short name, so it's not too much of a hassle to use it as a prefix.


My email is mathmachine4@gmail.com.  If you experience any issues with this library, feel free to contact me at that email.  Just make sure to use a descriptive subject line so I know it's not spam.  Also, be patient with me.  That's not my main email address, so I don't use it too frequently.


# Example Code:

```
Complex a=new Complex(2,1);  //declares a as the number 2+i
Complex b=new Complex(-1,1); //declares b as the number -1+i

Complex c=a.sq();  //declares c as the square of 2+i (which is 3+4i)
println(c.add(b)); //prints out their sum, which is 2+5i
println(b, c);     //the add function did not alter b or c, so it should print out "-1+i 3+4i"
println(c.addeq(b)); //once again prints out their sum, which is 2+5i
println(b, c);     //the addeq function DID alter c, but not b, so it should print out "-1+i 2+5i"
```

```
Complex a=Cpx.iTimes(Math.PI); //declares a as the number πi (note that this is slightly shorter than saying new Complex(0,Math.PI))
println(a.exp().add(1));       //due to Euler's identity, e^(πi)+1=0.  So, if you're running the latest version, it should say 0.
```
