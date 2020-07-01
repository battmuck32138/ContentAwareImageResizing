
Seam Carver is a content aware image resizing program that reduces the size of an image by one row or column of pixels at a time.  The program will seek out the seems with the lowest energy and remove them first leaving the most important parts of the image unchanged.  

A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row. (A horizontal seam is a path of pixels connected from the left to the right with one pixel in each column.) Below is the original 505-by-287 pixel image; further below we see the result after removing 150 vertical seams, resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (e.g. cropping and scaling), the most interesting features (aspect ratio, set of objects present, etc.) of the image are preserved.
https://sp18.datastructur.es/materials/hw/hw5/hw5
1/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
   In this assignment, you will create a data type that resizes a W-by-H image using the seam-carving technique.
Finding and removing a seam involves three parts and a tiny bit of notation:
Notation. In image processing, pixel (x, y) refers to the pixel in column x and row y, with pixel (0, 0) at the upper left corner and pixel (W − 1, H − 1) at the bottom right corner. This is consistent with
https://sp18.datastructur.es/materials/hw/hw5/hw5
2/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
 the Picture data type in stdlib.jar. Warning: this is the opposite of the standard mathematical notation used in linear algebra where (i, j) refers to row i and column j and with Cartesian coordinates where (0, 0) is at the lower left corner.
   (0, 0) (0, 1)
(0, 2)
(0, 3)
a 3-by-4 image (1, 0)
(1, 1)
(1, 2)
(1, 3)
(2, 0) (2, 1)
(2, 2)
(2, 3)
      We also assume that the color of a pixel is represented in RGB space, using three integers between 0 and 255. This is consistent with the data type.
1. Energy calculation. The first step is to calculate the energy of each pixel, which is a measure of the importance of each pixel—the higher the energy, the less likely that the pixel will be included as part of a seam (as we’ll see in the next step). In this assignment, you will implement the dual gradient energy function, which is described below. Here is the dual gradient of the surfing image above:
 java.awt.Color
 https://sp18.datastructur.es/materials/hw/hw5/hw5
3/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
 A high-energy pixel corresponds to a pixel where there is a sudden change in color (such as the boundary between the sea and sky or the boundary between the surfer on the left and the ocean behind him). In the image above, pixels with higher energy values have whiter values. The seam- carving technique avoids removing such high-energy pixels.
2. Seam identification. The next step is to find a vertical seam of minimum total energy. This is similar to the classic shortest path problem in an edge-weighted digraph except for the following:
The weights are on the vertices instead of the edges.
We want to find the shortest path from any of W pixels in the top row to any of the W pixels in the bottom row.
The digraph is acyclic, where there is a downward edge from pixel (x, y) to pixels (x − 1, y + 1), (x, y + 1), and (x + 1, y + 1), assuming that the coordinates are in the prescribed range.
3. Seam Removal. The final step is remove from the image all of the pixels along the seam. The logic for this method has been implemented for you in the supplementary SeamRemover class, provided in SeamRemover.java.
   public class SeamRemover {
 // These methods are NOT destructive
 public static Picture removeHorizontalSeam(Picture picture, int[] seam)  // returns
 public static Picture removeVerticalSeam(Picture picture, int[] seam)    // returns
}
         SeamCarver
https://sp18.datastructur.es/materials/hw/hw5/hw5
4/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
 The SeamCarver API. Your task is to implement the following mutable data type:
  public class SeamCarver {
    public SeamCarver(Picture picture)
    public Picture picture()
    public     int width()
    public     int height()
    public  double energy(int x, int y)
// current picture
// width of current picture
// height of current picture
// energy of pixel at column x and
// sequence of indices for horizont
// sequence of indices for vertical
}
public
public
public
public
int[] findHorizontalSeam()
int[] findVerticalSeam()
void removeHorizontalSeam(int[] seam)   // remove horizontal seam from p
void removeVerticalSeam(int[] seam)     // remove vertical seam from pic
   energy(): Computing the Energy of a Pixel
We will use the dual gradient energy function: The energy of pixel (x, y) is , where the square of the x-gradient , and where the central differences , , and are the absolute value in differences of red, green, and blue components between pixel (x + 1, y) and pixel (x − 1, y). The square of the y- gradient is defined in an analogous manner. We define the energy of pixels at the border of the image to use the same pixels but to replace the non-existant pixel with the pixel from the opposite edge.
As an example, consider the 3-by-4 image with RGB values (each component is an integer between 0 and 255) as shown in the table below.
   (255, 101, 51) (255,153,51)
(255,203,51)
(255,255,51)
(255, 101, 153) (255,153,153)
(255,204,153)
(255,255,153)
(255, 101, 255) (255,153,255)
(255,205,255)
(255,255,255)
      https://sp18.datastructur.es/materials/hw/hw5/hw5
5/13
)y,x(xB )y,x(xG )y,x(xR 2)y ,x(xB + 2)y ,x(xG + 2)y ,x(xR = )y ,x(x2Δ
)y ,x(y2Δ + )y ,x(x2Δ
) y , x ( y2 Δ

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
 Example 1: We calculate the energy of pixel (1, 2) in detail:
, ,
,
yielding .
,
,
yielding
.
, ,
,
,
yielding . Thus, the energy of pixel (1, 2) is Test your understanding: The energy of pixel (1, 1) is
Example 2: We calculate the energy of the border pixel (1, 0) in detail:
.
Since there is no pixel (x, y - 1) we wrap around and use the corresponding pixel from the bottom row the image, thus performing calculations based on pixel (x, y + 1) and pixel (x, height − 1).
,
,
,
yielding
Thus, the energy of pixel (1, 2) is
Examples Summary: The energies for each pixel is given in the table below:
.
.
.
https://sp18.datastructur.es/materials/hw/hw5/hw5
6/13
52225 = 2301 + 2402
42025 = 40401 + 02614 40401
= 2201 = )2 ,1(y2Δ
02025 = 40401 + 61614
40401 = 2201 = )2 ,1(y2Δ
0 2 6 1 4 = 2 4
0 2 + 2 2 = ) 2 , 1 ( x2 Δ
0 = 351 − 351 = )0 ,1(yB 201 = 351 − 552 = )0 ,1(yG 0 = 552 − 552 = )0 ,1(yR
61614 = 2402 = )0 ,1(x2Δ
402 = 15 − 552 = )0 ,1(xB 0 = 101 − 101 = )0 ,1(xG 0 = 552 − 552 = )0 ,1(xR
201
0 = 351 − 351 = )2 ,1(yB = 351 − 552 = )2 ,1(yG 0 = 552 − 552 = )2 ,1(yR
402 = 15 − 552 = )2 ,1(xB 2 = 302 − 502 = )2 ,1(xG 0 = 552 − 552 = )2 ,1(xR

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
    20808.0 52020.0 20808.0 52225.0
20809.0 52024.0
20808.0 52225.0
20808.0 21220.0
20809.0
21220.0
       findVerticalSeam(): Finding a Minimum Energy Path
The   method should return an array of length H such that entry x is the column number of the pixel to be removed from row x of the image. For example, consider the 6- by-5 image below (supplied as 6x5.png).
Finding a vertical seam. The   method returns an array of length H such that entry i is the column number of the pixel to be removed from row i of the image. For example, consider the 6-by-5 image below (supplied as 6x5.png).
findVerticalSeam()
findVerticalSeam()
      ( 78,209, 79) (224,191,182)
(117,189,149)
(163,222,132)
(211,120,173)
( 63,118,247) (108, 89, 82)
(171,231,153)
(187,117,183)
(188,218,244)
( 92,175, 95) ( 80,196,230)
(149,164,168)
( 92,145, 69)
(214,103, 68)
(243, 73,183) (112,156,180)
(107,119, 71)
(158,143, 79)
(163,166,246)
(210,109,104) (176,178,120)
(120,105,138)
(220, 75,222)
( 79,125,246)
(252,101,119) (142,151,142)
(163,174,196)
(189, 73,214)
(211,201, 98)
          The corresponding pixel energies are shown below, with a minimum energy vertical seam highlighted in pink. In this case, the method   returns the array { 3, 4, 3, 2, 2 } .
57685.0 50893.0 91370.0 25418.0 33055.0 37246.0 15421.0 56334.0 22808.0 54796.0 11641.0 25496.0
12344.0 19236.0 52030.0 17708.0 44735.0 20663.0
17074.0 23678.0 30279.0 80663.0 37831.0 45595.0
 findVerticalSeam()
              https://sp18.datastructur.es/materials/hw/hw5/hw5
7/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
       32337.0 30796.0 4909.0 73334.0 40613.0 36556.0
 When there are multiple vertical seams with minimal total energy, your method can return any such seam.
Your   method should work by first solving a base case subproblem, and then using the results of previous subproblems to solve later subproblems. Suppose we have the following definitions:
- cost of minimum cost path ending at (i, j) - energy cost of pixel at location (i, j)
Then each subproblem is the calculation of for some and . The top row is trivial, is just for all . For lower rows, we can find simply by adding the
to the minimum cost path ending at its top left, top middle, and top right pixels, or more formally:
In short, we start from one side of the 2D image array and process row-by-row or column-by- column (for vertical and horizontal seam carving respectively).
findVerticalSeam
 https://sp18.datastructur.es/materials/hw/hw5/hw5
8/13
))1 − j ,1 + i( M ,)1 − j ,i( M ,)1 − j ,1 − i( M(nim + )j ,i(e = )j ,i( M
)j,i(e )j,i(M i )0,i(e )0,i(M j i )j,i(M
)j ,i(e )j,i(M

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
  Addendum: The Java language does not deal well with deep recursion, and thus a recursive approach will almost certainly not be able to handle images of largish size (say 500x500). We recommend writing your code iteratively.
An equivalent (but slower approach) is to build an explicit Graph object and run some sort of shortest paths algorithm. You are welcome to try this approach, but be warned it is slower, and it may not be possible to sufficiently optimize your code so that it passes the autograder timing tests.
findHorizontalSeam(): Avoiding Redundancy
The behavior of   as analogous to that of   except that it should return an array of W such that entry y is the row number of the pixel to be removed from column y of the image. Your   method should NOT be a copy and paste of your   method! Instead, considering transposing the image, running
, and then transposing it back. The autograder will not test this, but a similar idea could easily appear on the final exam.
Other Program Requirements
 findHorizontalSeam()
findVerticalSeam()
findHorizontalSeam
findVerticalSeam
 findVerticalSeam
 https://sp18.datastructur.es/materials/hw/hw5/hw5
9/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
  Performance requirements. The , , and   methods should take constant time in the worst case. All other methods should run in time at most proportional to W H in the worst case.
Exceptions. Your code should throw an exception when called with invalid arguments.
By convention, the indices x and y are integers between 0 and W − 1 and between 0 and H − 1 respectively. Throw a if either x or y is outside its prescribed range.
Throw a if   or
is called with an array of the wrong length or if the array is not a valid
seam (i.e., two consecutive entries differ by more than 1).
Some Useful Files
PrintEnergy.java: For printing the energy calculations per pixel for an input image.
PrintSeams.java: Prints the energies and computed horizontal and vertical seams for an input image.
ShowEnergy.java: Shows the grayscale image corresponding to the energy computed by pixel.
ShowSeams.java: Displays the vertical and horizontal minimum energy seams for a given image.
SanityCheckTest.java: Basic JUnit tests consisting of the energy and path examples given in this spec.
SCUtility.java: Some utilies for testing SeamCarver.
width()
height()
energy()
 java.lang.IndexOutOfBoundsException
 java.lang.IllegalArgumentException
removeVerticalSeam()
 removeHorizontalSeam()
 https://sp18.datastructur.es/materials/hw/hw5/hw5
10/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
SeamRemover.java: Contains a SeamRemover class file with   and methods to use in your SeamCarver.
SeamCarverVisualizer.java: For the purposes of visualizing the frame-by-frame actions of your SeamCarver, we’ve provided you with a   class which takes three command line arguments: [filename] [numPixels to remove] [y (if horizontal carving) | N (otherwise)]
That is, the first is the name of the file you’d like to seam carve, the second is how many rows (or columns) to remove, and the third is “y” if you want horizontal carving or “n” if you want vertical carving.
For example, the arguments would carve 50 horizontal seams from the provided file.
In IntelliJ, set the command line arguments up using the Edit Configurations option (similar to HW3, in the spirit of building independence, we don’t provide explicit instructions, and instead turn you towards Google).
Or if you’re using the command line you’d run the command as follows:
Example:
 removeVerticalSeam()
SeamCarverVisualizer
     images/HJoceanSmall.png 50 y
 HJoceanSmall.png
  java SeamCarverVisualizer [filename] [numPixels to remove] [y (if horizontal carving) | N
    java SeamCarverVisualizer images/HJoceanSmall.png 50 y
removeHorizontalSeam()
https://sp18.datastructur.es/materials/hw/hw5/hw5
11/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
  Extra Fun
Fun #1: Try out your SeamCarver on various real world images. I recommend human faces.
Fun #2: Try to implement a version of the   class that avoids the need to recompute the entire energy matrix every time a seam is removed. This will require getting fancy with your data structures. If you do this, email Josh and let him know. This should make your SeamCarver class extremely fast.
SeamCarver
 Submission
Submit
need to submit
FAQ
How do I debug this?
and any supporting classes that you created, if applicable. You do not .
 SeamCarver.java
 SeamRemover.java
 Make sure to try out the “Useful Files” above, especially the PrintEnergy and PrintSeams classes.
My code is slow (failing timing tests), what can I do to speed it up?
Some possible optimizations include (in decreasing order of likely impact):
Avoiding recalculation of energies for the same pixel over and over (e.g. through creation of an explicit energy matrix of type   ). Essentially you want to memoize energy calculations.
double[][]
https://sp18.datastructur.es/materials/hw/hw5/hw5
12/13

 4/22/2018 HW 5: Seam Carving | CS 61B Spring 2018
 Don’t use a HashMap for looking up data by row and column. Instead, use a 2D array. They are much faster. HashMaps are constant time, but the constant factor is significant.
Not using   or .
Not storing an explicit   data structure. It is possible to rebuild the seam ONLY from the values for   ! That is, you don’t need to actually record the predecessor like you did in the 8puzzle assignment.
Using a more clever approach than transposing your images (though this is not required to pass the autograder).
Credits
This assignment was originally developed by Josh Hug, with supporting development work by Maia Ginsburg and Kevin Wayne at Princeton University.
 Math.pow
Math.abs
edgeTo
M(i, j)
 https://sp18.datastructur.es/materials/hw/hw5/hw5
13/13
