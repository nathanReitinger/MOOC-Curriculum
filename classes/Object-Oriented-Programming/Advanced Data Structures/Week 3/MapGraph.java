/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;


import javax.swing.text.html.HTMLDocument.Iterator;

import geography.GeographicPoint;
import util.GraphLoader;
//import week2example.MazeNode;

public class MapGraph {
	//TODO: Add your member variables here in WEEK 2
	//instantiation (master map is called "myMap")
	//including the list for edges, where we will keep all of our edge objects
	private HashMap<GeographicPoint, myNode> myMap; 			
	private List<mapEdge> edges; 								
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()				
	{
		// TODO: Implement in this constructor in WEEK 2
		myMap = new HashMap<GeographicPoint, myNode>();
		edges = new LinkedList<mapEdge>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 2
		return myMap.size();			
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//TODO: Implement this method in WEEK 2
		HashSet<GeographicPoint> intersections = new HashSet<GeographicPoint>();
		
		//keySet returns all keys in the HashMap
		//looping through all keys (intersections) and placing them in a new HashSet called Hashset
		for (GeographicPoint p : myMap.keySet()) {				 
			intersections.add(p);								
		}
		
		return intersections;
	}

	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method in WEEK 2
		//since all edges are added with method addEdge, we only need to size of that list
		return edges.size();									
	}

	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// TODO: Implement this method in WEEK 2
		//adding the nodes from myNode (because those are vertices) 
		
		if (location == null) {									
			System.out.println("Please provide non-null locations");
			return false;
		}
		
		//new node at the passed in lat/lang
		myNode temp = new myNode(location);																		
		
		if (!myMap.containsKey(temp)) {							
			myMap.put(location, temp);
			return true;
		}
		System.out.println("This value is already on the map!");
		return false;
	}

	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//TODO: Implement this method in WEEK 2
		
		if (!myMap.containsKey(to) || !myMap.containsKey(from)) {	
			throw new IllegalArgumentException();
		}
		
		//copy all passed in values into the mapEdge class type	
		myNode node = myMap.get(from);
		mapEdge temp = new mapEdge(from, to, roadName, roadType, length);			
		edges.add(temp);
		//links each of the edges to their respective nodes (crucial step)	
		node.addEdges(temp);																										
	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 2
		// Hook for visualization.  See writeup. nodeSearched.accept(next.getLocation());

		if (start == null || goal == null) {
			System.out.println("Start or goal node is null!  No path exists.");
			return new LinkedList<GeographicPoint>();
		}
		
		//linked list is where return values will be put
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();		
		HashMap<GeographicPoint, myNode> parentMap = new HashMap<GeographicPoint, myNode>();	
		HashSet<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Queue<myNode> to_explore = new LinkedList<myNode>();
		boolean found = false; 
	
		//start at the beginning
		//using nodes because 
			// (1) they are easier to stack in the queue
			// (2) easier to traverse, and
			// (3) because my "myNode" class creates a list of type mapEdge-->Private List<mapEdge> edges<--
		myNode beginning = myMap.get(start);									
																				
		to_explore.add(beginning);																 
																								
		while (!to_explore.isEmpty()) {
			myNode curr = to_explore.remove(); 										
			if (curr.getLocation().equals(goal)) {
				found = true;
				break;
			}
			//returns a list of type mapEdge that my node connects to
			List<mapEdge> neighbors_node = curr.getEdges(); 	
			//use this to store only geopoint variables (which we need to add to visited and parentMap) from neighbor's edges
			List<GeographicPoint> neighbor_locations = new LinkedList<GeographicPoint>();	
																				
			//because I used nodes in my queue, I can access their geopoints by looping through the neighbors_node
			for (int i = 0; i < neighbors_node.size(); i++) {									
				mapEdge e = neighbors_node.get(i);							
				GeographicPoint p = e.getEnd();
				neighbor_locations.add(p);
			}
			
			//now loop through the geopoint locations of the neighbor nodes	
			//do not double-add geopoints
			//add our neighbor's location to the parentMap for access later for building our path -- link to current node
			//put another location in the queue, so we can check it out in the next loop
			for (GeographicPoint to_check : neighbor_locations) {								
				if (!visited.contains(to_check)) {								
					visited.add(to_check); 										
					parentMap.put(to_check, curr);								
					to_explore.add(myMap.get(to_check));										
				}
			}
		}
		if (!found) {																			
			System.out.println("No path exists!!!!");
			return null;
		}
		
		path = buildPath(goal, start, parentMap, path);											
		return path;																					
				
	}																									
	/** Arrange the path from start to goal
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param parentMap The map holding each of the intersections and nodes
	 * @return The LinkedList holding shortest path
	 */
	private LinkedList<GeographicPoint> buildPath
	(GeographicPoint goal, GeographicPoint start, HashMap<GeographicPoint, myNode> parentMap, LinkedList<GeographicPoint> path) {

		//until we hit the beginning of the route
		//add the end to the beginning of the queue
		//replace the end with the next node, which will be taken from the parentMap list and identified by the stored geopoint
		//don't forget to start the list with the "start" position	
		myNode current = new myNode(goal); 										
		while (!current.getLocation().equals(start)) {							
			path.addFirst(current.getLocation());								
			current = parentMap.get(current.getLocation());										
		} 
		path.addFirst(start);																											
		return path;
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
		
	/** Arrange the path from start to goal (used for Dijkstra and A*)
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The ArrayList holding the shortest path
	 */
	private ArrayList<GeographicPoint> buildPath(GeographicPoint goal, GeographicPoint start) {
		ArrayList<GeographicPoint> path = new ArrayList<GeographicPoint>();
        for (myNode current = myMap.get(goal); current != null; current = current.previous) {
            path.add(current.getLocation());
        }

        Collections.reverse(path);
		return path;
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		//http://stackoverflow.com/questions/17480022/java-find-shortest-path-between-2-points-in-a-distance-weighted-map

		
		HashSet<myNode> visited = new HashSet<myNode>();
		boolean found = false; 
		
		//start will always be 0
		myNode beginning = myMap.get(start); 									
		beginning.minDistance = 0.0;											
		beginning.minDistance_heuristic = 0.0;
		PriorityQueue<myNode> PQ_nodes = new PriorityQueue<myNode>();
		//add first node to beginning
		PQ_nodes.add(beginning); 												
		while (!PQ_nodes.isEmpty()) {
			//grabbing first element (ranked) in priority queue
			myNode curr = PQ_nodes.poll();										
			if (!visited.contains(curr)) {									
				visited.add(curr);
				if (curr.getLocation().equals(goal)) {
					found = true;
					break;
				}
				//get all the edges of current node	
				for (mapEdge e : curr.getEdges()) {								
					myNode to_check = myMap.get(e.getEnd()); 	
					//grabbing edge length that was already part of edges features
					double weight = e.length; 	
					//current node (added up since start) + edge length
					double distanceThroughCurr = curr.minDistance + weight; 	
					
					if (distanceThroughCurr < to_check.minDistance) {
						to_check.minDistance = distanceThroughCurr; 
						//added to simplify Dijkstra and A*
						//dijkstra is a corner case of A* whereby heuristic is always 0
						to_check.minDistance_heuristic = distanceThroughCurr;	
						//starting from the new, moved location
						to_check.previous = curr; 		
						//only add those edges to PQ that are smaller than our current total distance
						PQ_nodes.add(to_check); 								
					}
				}
				//useful for debugging
				//System.out.println("PQ_Nodes contains: ");					
				//for (myNode n : PQ_nodes) {
				//	System.out.println(n.getLocation() + " " + n.minDistance);
				//}
			}
		}
		if (!found) {																			
			return null;
		}
		ArrayList<GeographicPoint>path = buildPath (goal, start);	
		return path;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
		
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
	
		if (start == null || goal == null) {
			System.out.println("Start or goal node is null!  No path exists.");
			return new LinkedList<GeographicPoint>();
		}
	
		PriorityQueue<myNode> PQ_nodes = new PriorityQueue<myNode>(); 		
		HashSet<myNode> visited = new HashSet<myNode>();
		boolean found = false; 
		
		myNode beginning = myMap.get(start); 

		beginning.minDistance = 0.0;
		beginning.minDistance_heuristic = 0.0;
				
		PQ_nodes.add(beginning); 		
		while (!PQ_nodes.isEmpty()) {
			//retrieves and removes first node
			myNode curr = PQ_nodes.poll();											
			if (!visited.contains(curr)) {											
				visited.add(curr);
				//if we've hit our goal
				if (curr.getLocation().equals(goal)) {								
					found = true;
					break;
				}
				//gather all roads flowing from current node
				for (mapEdge e : curr.getEdges()) {									
					if (!visited.contains(e)) {
						myNode to_check = myMap.get(e.getEnd()); 				

						double weight = e.length; 
						//this is our totaled distance + edge's length 
						double distanceThroughCurr = curr.minDistance + weight; 	
														
						//heuristic is totaled distance + edge's length + guessed distance from this node to goal
						//.distance(GeographicPoint) is contained in GeographicPoint.java
						double heuristic_distance = distanceThroughCurr + (e.getEnd().distance(goal));	
				
						if (distanceThroughCurr < to_check.minDistance) {
							to_check.minDistance = distanceThroughCurr; 
							to_check.minDistance_heuristic = heuristic_distance;		
							to_check.previous = curr;
							PQ_nodes.add(to_check); 
						}
					}
				}
				//System.out.println("Count # " + count + " PQ_Nodes contains: ");
				//count++;
				//for (myNode n : PQ_nodes) {
				//	System.out.println(n.getLocation() + "\t" + n.minDistance_heuristic + "\t" + n.minDistance);
				//}
			}
		}
		if (!found) {																			
			return null;
		}
		ArrayList<GeographicPoint>path = buildPath (goal, start);	
		return path;

	}

	/** Find the path from start to goal using Dijkstra with a Fibonacci Heap 
	 * @param <T>
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra_fibonacci_search(GeographicPoint start, GeographicPoint goal) 
	{
		FibonacciHeap<myNode> FH_nodes = new FibonacciHeap<myNode>();
		if (start == null || goal == null) {
			System.out.println("Start or goal node is null!  No path exists.");
			return new LinkedList<GeographicPoint>();
		}
			
		HashSet<myNode> visited = new HashSet<myNode>();
		boolean found = false; 
		
		myNode beginning = myMap.get(start); 

		beginning.minDistance = 0.0;
		beginning.minDistance_heuristic = 0.0;
				
		FH_nodes.enqueue(beginning, beginning.minDistance);
		while (!FH_nodes.isEmpty()) {
			myNode curr = FH_nodes.dequeueMin().getValue();
			if (!visited.contains(curr)) {									
				visited.add(curr);
				if (curr.getLocation().equals(goal)) {
					found = true;
					break;
				}
				//get all the edges of current node	
				for (mapEdge e : curr.getEdges()) {								
					myNode to_check = myMap.get(e.getEnd()); 											
					double weight = e.length; 									
					double distanceThroughCurr = curr.minDistance + weight; 	
					
					if (distanceThroughCurr < to_check.minDistance) {
						to_check.minDistance = distanceThroughCurr; 
						to_check.minDistance_heuristic = distanceThroughCurr;
						to_check.previous = curr; 				
						FH_nodes.enqueue(to_check, to_check.minDistance);
					}
				}
			}
		}
		if (!found) {																			
			return null;
		}
		ArrayList<GeographicPoint>path = buildPath (goal, start);	
		System.out.println(path);
		return path;

	}
		
	/** Find the path from start to goal using A* with a Fibonacci Heap 
	 * @param <T>
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> astar_fibonacci_search(GeographicPoint start, GeographicPoint goal) 
	{
		// TODO: Implement this method in WEEK 3
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
	
		if (start == null || goal == null) {
			System.out.println("Start or goal node is null!  No path exists.");
			return new LinkedList<GeographicPoint>();
		}
	
		FibonacciHeap<myNode> FH_nodes = new FibonacciHeap<myNode>();	
		HashSet<myNode> visited = new HashSet<myNode>();
		boolean found = false; 
		
		myNode beginning = myMap.get(start); 

		beginning.minDistance = 0.0;
		beginning.minDistance_heuristic = 0.0;
	
		FH_nodes.enqueue(beginning, beginning.minDistance);		
		while (!FH_nodes.isEmpty()) {
			myNode curr = FH_nodes.dequeueMin().getValue();	
			if (!visited.contains(curr)) {											
				visited.add(curr);
				if (curr.getLocation().equals(goal)) {	
					found = true;
					break;
				}
				for (mapEdge e : curr.getEdges()) {		
					if (!visited.contains(e)) {
						myNode to_check = myMap.get(e.getEnd()); 				

						double weight = e.length; 	
						double distanceThroughCurr = curr.minDistance + weight; 
						double heuristic_distance = distanceThroughCurr + (e.getEnd().distance(goal));	
					
						if (distanceThroughCurr < to_check.minDistance) {
							to_check.minDistance = distanceThroughCurr; 
							to_check.minDistance_heuristic = heuristic_distance;	
							to_check.previous = curr;
							FH_nodes.enqueue(to_check, to_check.minDistance); 
						}
					}
				}
				//System.out.println("Count # " + count + " PQ_Nodes contains: ");
				//count++;
				//for (myNode n : PQ_nodes) {
				//	System.out.println(n.getLocation() + "\t" + n.minDistance_heuristic + "\t" + n.minDistance);
				//}
			}
		}
		if (!found) {																			
			return null;
		}
		ArrayList<GeographicPoint>path = buildPath (goal, start);	
		return path;
	}
	
	public static void main(String[] args)
	{
	for (int q = 0; q < 1; q++) {
		for (int i = 0; i < 50; i++) {
			System.out.print("UTC-small" + "\t\t");
			
			MapGraph theMap = new MapGraph();
			GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
	
			GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
			GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
			
	
				long startTime_dijkstra = System.nanoTime(); 
				List<GeographicPoint> route = theMap.dijkstra(start,end);
				long endTime_dijkstra = System.nanoTime(); 
				double estTime = (endTime_dijkstra - startTime_dijkstra) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_astar = System.nanoTime(); 
				List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
				long endTime_astar = System.nanoTime();
				estTime = (endTime_astar - startTime_astar) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_fibonacci = System.nanoTime(); 
				List<GeographicPoint> route3 = theMap.dijkstra_fibonacci_search(start,end);
				long endTime_fibonacci = System.nanoTime();
				estTime = (endTime_fibonacci - startTime_fibonacci) / 1000000000.0;
				System.out.print(estTime + "\t");
				
				long startTime_fibonacci_astar = System.nanoTime(); 
				List<GeographicPoint> route4 = theMap.astar_fibonacci_search(start,end);
				long endTime_fibonacci_astar = System.nanoTime();
				estTime = (endTime_fibonacci_astar - startTime_fibonacci_astar) / 1000000000.0;
				System.out.print(estTime + "\n");
		}
		
		for (int i = 0; i < 1; i++) {
			System.out.print("UTC-large" + "\t\t");
			MapGraph theMap = new MapGraph();
			GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
			GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
			GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
			
				long startTime_dijkstra = System.nanoTime(); 
				List<GeographicPoint> route = theMap.dijkstra(start,end);
				long endTime_dijkstra = System.nanoTime(); 
				double estTime = (endTime_dijkstra - startTime_dijkstra) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_astar = System.nanoTime(); 
				List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
				long endTime_astar = System.nanoTime();
				estTime = (endTime_astar - startTime_astar) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_fibonacci = System.nanoTime(); 
				List<GeographicPoint> route3 = theMap.dijkstra_fibonacci_search(start,end);
				long endTime_fibonacci = System.nanoTime();
				estTime = (endTime_fibonacci - startTime_fibonacci) / 1000000000.0;
				System.out.print(estTime + "\t");
				
				long startTime_fibonacci_astar = System.nanoTime(); 
				List<GeographicPoint> route4 = theMap.astar_fibonacci_search(start,end);
				long endTime_fibonacci_astar = System.nanoTime();
				estTime = (endTime_fibonacci_astar - startTime_fibonacci_astar) / 1000000000.0;
				System.out.print(estTime + "\n");
		}
		
		for (int i = 0; i < 1; i++) {
			System.out.print("NY-small" + "\t\t");
			MapGraph theMap = new MapGraph();
			GraphLoader.loadRoadMap("data/maps/new_york.map", theMap);
			GeographicPoint start = new GeographicPoint(40.745624, -73.9726034);
			GeographicPoint end = new GeographicPoint(40.7569287, -73.9955967);
			
				long startTime_dijkstra = System.nanoTime(); 
				List<GeographicPoint> route = theMap.dijkstra(start,end);
				long endTime_dijkstra = System.nanoTime(); 
				double estTime = (endTime_dijkstra - startTime_dijkstra) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_astar = System.nanoTime(); 
				List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
				long endTime_astar = System.nanoTime();
				estTime = (endTime_astar - startTime_astar) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_fibonacci = System.nanoTime(); 
				List<GeographicPoint> route3 = theMap.dijkstra_fibonacci_search(start,end);
				long endTime_fibonacci = System.nanoTime();
				estTime = (endTime_fibonacci - startTime_fibonacci) / 1000000000.0;
				System.out.print(estTime + "\t");
				
				long startTime_fibonacci_astar = System.nanoTime(); 
				List<GeographicPoint> route4 = theMap.astar_fibonacci_search(start,end);
				long endTime_fibonacci_astar = System.nanoTime();
				estTime = (endTime_fibonacci_astar - startTime_fibonacci_astar) / 1000000000.0;
				System.out.print(estTime + "\n");
		}
		for (int i = 0; i < 1; i++) {
			System.out.print("NY-large" + "\t\t");
			MapGraph theMap = new MapGraph();
			GraphLoader.loadRoadMap("data/maps/new_york.map", theMap);
			GeographicPoint start = new GeographicPoint(40.745624, -73.9726034);
			GeographicPoint end = new GeographicPoint(40.7646309, -73.9955449);
			
				long startTime_dijkstra = System.nanoTime(); 
				List<GeographicPoint> route = theMap.dijkstra(start,end);
				long endTime_dijkstra = System.nanoTime(); 
				double estTime = (endTime_dijkstra - startTime_dijkstra) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_astar = System.nanoTime(); 
				List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
				long endTime_astar = System.nanoTime();
				estTime = (endTime_astar - startTime_astar) / 1000000000.0;
				System.out.print(estTime + "\t\t");
				
				long startTime_fibonacci = System.nanoTime(); 
				List<GeographicPoint> route3 = theMap.dijkstra_fibonacci_search(start,end);
				long endTime_fibonacci = System.nanoTime();
				estTime = (endTime_fibonacci - startTime_fibonacci) / 1000000000.0;
				System.out.print(estTime + "\t");
				
				long startTime_fibonacci_astar = System.nanoTime(); 
				List<GeographicPoint> route4 = theMap.astar_fibonacci_search(start,end);
				long endTime_fibonacci_astar = System.nanoTime();
				estTime = (endTime_fibonacci_astar - startTime_fibonacci_astar) / 1000000000.0;
				System.out.print(estTime + "\n");
		}
	}
	}
}
