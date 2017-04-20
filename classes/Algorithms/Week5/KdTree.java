/******************************************************************************
 *  Compilation:  javac KdTree.java
 *  Dependencies: Point2D.java, RectHV.java
 *  
 *  See http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *
 ******************************************************************************/
package Week5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private node root; 
	private RectHV rootRect = new RectHV(0.0,0.0,1.0,1.0);
	private static final boolean VERTICAL = true; 
	private static final boolean HORIZONTAL = false; 
	private static final boolean goLow = true;
	
	/******************************************************************************
	 				* Helpers *
	 For a few of these methods, see http://algs4.cs.princeton.edu/32bst/BST.java.html
	 ******************************************************************************/
	private static class node 
	{
		private Point2D p; 
		private int size;
		private node left; 
		private node right; 
		private boolean orientation;
		
		public node(Point2D p, boolean orientation, int size)
		{
			this.p = p;
			this.orientation = orientation;
			this.size = size;
		}
		
		/** At the root we use x coordinate; at the next level down we use y coordinate. 
		 *  Per the spec: at the root we use the x-coordinate because, if the point to be 
		 *  inserted has a smaller x-coordinate than the point at the root, then we should
		 *  go left; otherwise, we go right. At the next level, we use the y-coordinate 
		 *  because, if the point to be inserted has a smaller y-coordinate than the point in 
		 *  the node, we should go left; otherwise we go right.
		 *  
		 *  - if node is vertical, use the x coordinate to determine order
		 *  - if  node is horizontal, use the y coordinate to determine order
		 *  - watch out for corner case when x value is same as previous x value, use y coordiante
		 */
		public int compareTo(Point2D that) {
			if (this.orientation == VERTICAL && this.p.x() > that.x()) 	return -1; 	
			if (this.orientation == HORIZONTAL && this.p.y() > that.y()) 	return -1; 
			if (this.p.x() == that.x() && this.p.y() == that.y()) 		return  0;
			return +1; 

		}
	}
	private int size(node x)													
	{
		if (x == null) return 0;
        else return x.size;
	}
	private void put(Point2D p)
	{
		if (p == null) throw new NullPointerException();	
		// first node should be vertical, successive will depend on !x.orientation
		root = put(root, p, VERTICAL);											
	}
	private node put(node x, Point2D p, boolean orientation) 
	{
		if (x == null) return new node(p, orientation, 1);			
		int compare = x.compareTo(p);
		// flips orientation per level, when a node is iterated it changes levels
		if (compare < 0) x.left = put(x.left, p, !x.orientation);				
		else if (compare > 0) x.right = put(x.right, p, !x.orientation);
		else x.p = p;
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}
	
	/** 
	 * Used when finding closest neighbor because we want to set the left/right search
	 * 
	 * @param x
	 * @param goLow	<== should we go up or down 
	 * @param rect
	 * @return
	 */
	private RectHV childRect(node x, boolean goLow, RectHV rect)				
	{
		if (x.orientation == VERTICAL) {
			// if this node is lower
			if (goLow == true) {												
				return new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax());
			}		
			// if this node is vertical but higher
			else return new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
		}
		else {
			// if this node is horizontal and lower 
			if (goLow == true) {												
				return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
			}
			// if this node is horizontal but higher 
			else return new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
		}
	}

	private node get(Point2D p)
	{
		return get(root, p);
	}
	private node get(node x, Point2D p)
	{
		if (x == null || x.p.equals(p)) return x; 								
		int compare = x.compareTo(p); 
		if (compare < 0) return get(x.left, p);
		if (compare > 0) return get(x.right, p);
		return null;
	}
	
	/**
	 * Per the spec: To find all points contained in a given query 
	 * rectangle, start at the root and recursively search for points 
	 * in both subtrees using the following pruning rule: if the query 
	 * rectangle does not intersect the rectangle corresponding to a 
	 * node, there is no need to explore that node (or its subtrees). 
	 * A subtree is searched only if it might contain a point contained 
	 * in the query rectangle.
	 * 
	 * @param x		<== the node we are on
	 * @param rectQuery	<== passed in rectangle
	 * @param rectCreated	<== each node's corresponding rectangle
	 * @param points	<== those points within rectQuery 
	 */
	private void range(node x, RectHV rectQuery, RectHV rectCreated, Queue<Point2D> points)
	{
		// base case
		if (x == null)															
			return;
		
		// if the point lies within the rectangle
		if (rectQuery.contains(x.p))											
			points.enqueue(x.p);
		
		// recursively following pruning rule
		if (rectQuery.intersects(rectCreated)) { 
			// inserts rectCreated by calling childRect and creating an appropriately sized rectangle
			range(x.left, rectQuery, childRect(x, true, rectCreated), points);	
			range(x.right, rectQuery, childRect(x, false, rectCreated), points);
		}								
	}

	/**
	 * Per the spec: To find a closest point to a given query point, 
	 *  start at the root and recursively search in both subtrees using 
	 *  the following pruning rule: if the closest point discovered so 
	 *  far is closer than the distance between the query point and 
	 *  the rectangle corresponding to a node, there is no need to explore 
	 *  that node (or its subtrees). That is, a node is searched only if 
	 *  it might contain a point that is closer than the best one found 
	 *  so far. The effectiveness of the pruning rule depends on quickly 
	 *  finding a nearby point. To do this, organize your recursive method 
	 *  so that when there are two possible subtrees to go down, you always 
	 *  choose the subtree that is on the same side of the splitting line 
	 *  as the query point as the first subtree to explore—the closest point 
	 *  found while exploring the first subtree may enable pruning of the 
	 *  second subtree.
	 *  
	 *  1. stop when we hit null or the current node's distance is farther away
	 *     than best distance so far. 
	 *  2. if current node's distance is better, update closest distance
	 *  3. figure out whether to go left or right first
	 *  4. check out "both" paths as long as they adhere to the pruning rule
	 *  
	 * @param current
	 * @param rect
	 * @param p
	 * @param closestPoint
	 * @param closestDistance
	 * @return
	 */
	private Point2D howClose(node current, RectHV rect, Point2D p, Point2D closestPoint, double closestDistance)
	{
		// rect.distance acts as limiter for pruning rule (do not check out nodes farther from closestPoint)
		if (current == null || rect.distanceSquaredTo(p) > closestDistance)	
			return closestPoint; 
		
		double currentDistance = current.p.distanceSquaredTo(p);
        
		// update points to reflect closest distance to point
        if (currentDistance < closestDistance) {
        	
            closestPoint = current.p;										
            closestDistance = currentDistance;
        }
        
        // figure out which way to go first 
        int compare = current.compareTo(p);									

        // go both ways, but go to left first
        if (compare < 0) {													
        	closestPoint = howClose(current.left, childRect(current, true, rect), p, closestPoint, closestDistance);
        	closestPoint = howClose(current.right, childRect(current, false, rect), p, closestPoint, closestPoint.distanceSquaredTo(p));
        }
        if (compare > 0) {
        	closestPoint = howClose(current.right, childRect(current, false, rect), p, closestPoint, closestDistance);
        	closestPoint = howClose(current.left, childRect(current, true, rect), p, closestPoint, closestPoint.distanceSquaredTo(p));
        }
       
        return closestPoint;
	}
	
	/******************************************************************************
	 				* API *
	 ******************************************************************************/
	public KdTree() 		{ }
	public boolean isEmpty() 	{ return size() == 0; 	}
	public int size()		{ return size(root);	}	
	public void insert(Point2D p)				
	{ 
		if (p == null) { 
			throw new NullPointerException("argument or set is null!"); 
		}
		put(p); 					
	}
	public boolean contains(Point2D p)			
	{ 
		if (p == null) { 
			throw new NullPointerException("argument or set is null!"); 
		}
		
		// check if empty, automatically know that set does not contain point
		if (isEmpty()) return false;						
		return (get(p) != null);	
	}
	
	// only useful for visual debugging, not included here
	public void draw() {} 		
	
	public Iterable<Point2D> range(RectHV rect)	
	{ 
		if (rect == null) { 
			throw new NullPointerException("argument or set is null!"); 
		}
		
		Queue<Point2D> points = new Queue<Point2D>();
		// if the set is empty, we know that no points lie within the rectangle
		if (isEmpty()) return points;		
		// places rectangles not during insertion but during this method (heavy lifting put off until now)
		// I originally placed them during insertion, but lost points for the resultant lag
		range(root, rect, rootRect, points);				
		return points;	
	}
	public Point2D nearest(Point2D p)			
	{ 
		if (p == null) { 
			throw new NullPointerException("argument or set is null!");
		}
		
		// if the set is empty, there are no near points
		if (isEmpty()) return null;							
		Point2D closestPoint = root.p; 
		Double closestDistance = Double.POSITIVE_INFINITY;
		return howClose(root, rootRect, p, closestPoint, closestDistance);
	}
	
	/******************************************************************************
	 				* Test client *
	******************************************************************************/
	public static void main (String[] args) throws FileNotFoundException 
	{
		
    	Scanner scanner =  new Scanner(new File("/Users/nathanreitinger/Desktop/circle10k.txt"));
        In in = new In(scanner);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            System.out.println(kdtree.contains(p) + "\t" + kdtree.size());
        }
        RectHV rect = new RectHV(0,0,.4,.4);
        Point2D pp = new Point2D(0.81, 0.30);
        System.out.println("Nearest: \t" + kdtree.nearest(pp));
		System.out.println("Range: \t" +kdtree.range(rect));
		System.out.println("Empty: \t" +kdtree.isEmpty());
		System.out.println("Size: \t" +kdtree.size());
		System.out.println("Contains: \t" +kdtree.contains(pp));
		
	}
}












