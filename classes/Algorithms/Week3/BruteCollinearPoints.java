/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Dependencies: Point.java, LineSegent.java 
 *  
 *  Uses four for loops to iterate over points and determine whether there are
 *  at least 4 collinear points. Not efficient, but returns correct results.
 *
 ******************************************************************************/
package Week3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class BruteCollinearPoints {
   
	private int count;
	private LineSegment[] bruteSegments;

	public BruteCollinearPoints(Point[] points) 
	{
		if (points == null) { throw new NullPointerException("Please do not input a null array!"); }
		Point[] copy = points.clone();
		this.bruteSegments = new LineSegment[copy.length/2];
		Arrays.sort(copy);
		
		for (int i = 0; i < copy.length-1; i++) {
			if (copy[i].slopeTo(copy[i+1]) == Double.NEGATIVE_INFINITY) {
				throw new IllegalArgumentException("Please, no duplicates!");
			}
		}
		
		// traverse through array 
		for (int p = 0; p < copy.length; p++) {								
			for (int q = p + 1; q < copy.length; q++) {
				for (int r = q + 1; r < copy.length; r++) {
					for (int s = r + 1; s < copy.length; s++) {
						
						// per spec, if one of these points is null throw error
						if (copy[p] == null || copy[q] == null || copy[r] == null || copy[s] == null) {
							throw new NullPointerException("Please do not input a null array!");
						}
						
						// if slopes are collinear 
						if (copy[p].slopeTo(copy[q]) == copy[p].slopeTo(copy[r]) && copy[p].slopeTo(copy[r]) == copy[p].slopeTo(copy[s])) {
							LineSegment segment = new LineSegment(copy[p],copy[s]);
							// add line segments to list to return
							bruteSegments[count] = segment;						
							//System.out.println(points[p] + "-->" + points[s]);
							count++;
						}
					}
				}
			}
		}
	}
	
	public int numberOfSegments() { 
		return count; 
	}
	// returning only filled array 
	public LineSegment[] segments() { 
		return Arrays.copyOf(bruteSegments, count); 
	}
 
	
    /**
     * Unit tests the Brute Force.
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
  
    	Scanner scanner =  new Scanner(new File("/Users/nathanreitinger/Desktop/input48.txt"));
    	// read the N points from a file
    	// In in = new In(args[0]);
        int N = scanner.nextInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points[i] = new Point(x, y);
        }
    	BruteCollinearPoints test = new BruteCollinearPoints(points);
    	
    	System.out.println(test.numberOfSegments());
    }
   
    
 
}
