This program simulates what is known as Newton Fractal.  If you'd like to hear about it from someone who's better at explaining things, I recommend the video by 3Blue1Brown, "Newton's Fractal (which Newton knew nothing about)", https://www.youtube.com/watch?v=-RdOwhmqP5s.  If you don't want to know how it works, though, and you just want to play around with pretty fractals, I recommend you skip ahead to the HOW TO USE section.

EXPLANATION:

The fractal is essentially a plot on the complex plane.  We start out with an arbitrary polynomial.  We then perform a well known root-finding algorithm known as "Newton's Method" on that polynomial, which is basically impossible to explain without using pictures, so I'll just assume you already know what that is.  And since you already know what that is, you should know that Newton's method demands starting with an initial seed close to one of the roots.  The closer the initial seed is to one of the roots, the faster the algorithm converges.  Nevertheless, for almost all possible seeds, the algorithm will eventually converge to one of the roots.  Even if the seed is a complex number.

This program looks at each point on the complex plane, sets that as our seed, performs Newton's method, then colors that point a different color based on which root it converged to.  If the polynomial has more than 2 roots, the resulting coloring will always be a fractal.

HOW TO USE:

The white tinted circles are the roots of our polynomial.  To move the roots around, simply click and drag one of them.  Doing so will generate a completely different fractal.  To delete a root, right click that circle.  To add another root, middle click on the spot where you want to add it.  Note that the more roots you add, the slower the program will run, and if you add more than 24 roots, the program will crash because the color array is only 24 long.

To move the image around, simply click anywhere (except on one of the circles) and drag your mouse around.  To zoom in, scroll your mousewheel up.  To zoom out, scroll your mousewheel down.  To zoom even faster, hold shift while scrolling.

If you press 'r', it'll reset the zoom and image position.  If you press 'u', it'll move all the roots into a circular formation.
