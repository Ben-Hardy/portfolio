This program was one of my favourite solutions to any problem in all of university.
The purpose of the program is to look at the curvature of a shape in a 
black and white (binary) image at various scale sizes and create a histogram of it.

The thing with this problem, was that the solution will always take a looooooong time
to run. My original solution was about average for speed, so I came up with the one here.

I basically took the solution matrix, subtracted one from it, and then converted it to
unsigned and it cut runtime down by over two thirds. It seemed like such a dumb at the
time but it worked.


To run this, you need to open it in MATLAB with an image that either already is binary, or you have converted to binary yourself. The image must have just one big area on
it (we were using leaves in the class). It will output a histogram of the result.
