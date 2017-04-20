/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Dependencies: Point.java, LineSegent.java 
 *  
 *  Sorts points first by natural order and then by slope order to acheive
 *  efficiency.  
 *
 ******************************************************************************/

package Week3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;


public class FastCollinearPoints {

	// where collinear points (maximal first and minimal last) are stored
	private ArrayList<LineSegment> fastSegments;											
	
	public FastCollinearPoints(Point[] points) 
	{
		if (points == null) { throw new NullPointerException("Please do not input a null array!"); }
		
		Point[] copy = points.clone();
		this.fastSegments = new ArrayList<LineSegment>();
		
		Arrays.sort(copy);
		for (int i = 0; i < copy.length-1; i++) {
			if (copy[i].slopeTo(copy[i+1]) == Double.NEGATIVE_INFINITY) {
				throw new IllegalArgumentException("Please, no duplicates!");
			}
		}
		
		for (int i = 0; i < copy.length; i++) {	
			// must sort the array each time 
			Arrays.sort(copy);																
			Arrays.sort(copy, copy[i].slopeOrder());
			
			// check each point in array for 3 or more collinear points
			// "second" will count up how many collinear points there are
			// since second started at 2, reduce it for accurate count
			// if the new point (copy[first]) is -1 (see Point.java compareTo method)
			// then we know that this is a new line, see https://plus.google.com/communities/103599045766660139776
			// add the new segment --> "top", is always copy[r] and "bottom" is always highest second counted -1 since it started at 2
			for (int r = 0, first = 1, second = 2; second < copy.length; second++) {
				while (second < copy.length && copy[r].slopeTo(copy[first]) == copy[r].slopeTo(copy[second])) {	
					second++;																
				}																			
				if (second-first >= 3) {													
					if (copy[r].compareTo(copy[first]) == -1) {								
							System.out.println(copy[r].compareTo(copy[first]));				
							LineSegment segment = new LineSegment(copy[r],copy[second-1]);	
							fastSegments.add(segment);
							//System.out.println(segment);
					}
				}
				// continue searching for segments
				first = second;
			}
		}
	}
	
	
	public int numberOfSegments() 	{ return fastSegments.size(); }
	public LineSegment[] segments() 
	{ 
		LineSegment[] toReturn = fastSegments.toArray(new LineSegment[fastSegments.size()]);
		return toReturn;
	}
	
	
	 public static void main(String[] args) throws FileNotFoundException 
	 {
		 
		 Scanner scanner =  new Scanner(new File("/Users/nathanreitinger/Desktop/input8.txt"));
		 // read the N points from a file
		 // In in = new In(args[0]);
		 	int N = scanner.nextInt();
	        Point[] points = new Point[N];
	        for (int i = 0; i < N; i++) {
	            int x = scanner.nextInt();
	            int y = scanner.nextInt();
	            points[i] = new Point(x, y);
	        }
	    	FastCollinearPoints test = new FastCollinearPoints(points);
	    	
	    	System.out.println(test.numberOfSegments());
	    	
	    }
	   
}
