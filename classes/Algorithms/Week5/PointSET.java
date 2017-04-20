/******************************************************************************
 *  Compilation:  javac KdTree.java
 *  Dependencies: Point2D.java, RectHV.java
 *  
 *  See http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *
 ******************************************************************************/
package Week5;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
	
	private SET<Point2D> set; 
	
	public PointSET() 
	{
		set = new SET<Point2D>();
	}
	public boolean isEmpty() { 
		return set.isEmpty(); 
	}
	public int size() { 
		return set.size(); 	
	}
	public void insert(Point2D p)					
	{ 
		if (p == null) { 
			throw new NullPointerException("argument or set is null!"); 
		}
		set.add(p);
		
	}
	public boolean contains(Point2D p) 				
	{ 	
		if (p == null) { 
			throw new NullPointerException("argument or set is null!");
		}
		return set.contains(p);

	}
	public void draw()               				
	{ 
		/** only for debugging
		StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : set)
			p.draw();
		*/
	}
	public Iterable<Point2D> range(RectHV rect)		
	{ 
		Queue<Point2D> withinRect = new Queue<Point2D>();

		// iterate over each of they keys (nodes) in set
		// if rectangle overlaps with this point
		// include it within the returnable queue 
		for (Point2D p : set) 	{						
			if (rect.contains(p)) {						
				withinRect.enqueue(p);					
			}
		}
		return withinRect;  
	}
	
	// returns the closest Point2D to p	
	public Point2D nearest(Point2D p)					
	{ 
		if (set.isEmpty())		{ return null; }
		
		Point2D neighbor = new Point2D(0,0);
		double distance1 = Double.POSITIVE_INFINITY;
		double distance2;
		
		// compare end-point to current-point (not vice-versa) 
		for (Point2D pp : set) {
			distance2 = p.distanceTo(pp);				
			if (distance2 < distance1) {
				neighbor = pp;
				distance1 = distance2;
			}
		}
		
		return neighbor; 
	}

	public static void main(String[] args)
	{
		
		PointSET test = new PointSET(); 
		Point2D testPoint1 = new Point2D(1.0, 1.0);
		Point2D testPoint2 = new Point2D(2.0, 2.0);
		Point2D testPoint3 = new Point2D(3.0, 3.0);
		Point2D testPoint4 = new Point2D(2.1, 2.1);
		Point2D testPoint5 = new Point2D(1.9, 1.9);
		Point2D testPoint6 = new Point2D(0.5, 0.5);
		Point2D testPoint7 = new Point2D(0.0, 0.9);
		Point2D testPoint8 = new Point2D(4.0, 4.0);

		test.insert(testPoint1);
		test.insert(testPoint2);
		test.insert(testPoint3);
		test.insert(testPoint4);
		test.insert(testPoint5);
		test.insert(testPoint6);
		test.insert(testPoint7);
		test.insert(testPoint8);
		
		RectHV rect = new RectHV(0.1, 0.1, 0.2, 0.2);	
		System.out.println("Rectangle: \t" + rect);
		System.out.println("Points Within: \t" +test.range(rect));
		System.out.println("Nearest Point: \t" +test.nearest(testPoint1));
		System.out.println("Contains: \t" + test.contains(testPoint8));
		System.out.println("Size \t\t" + test.size());
		
	}
}
