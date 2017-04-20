/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

import javax.swing.text.html.HTMLDocument.Iterator;

import geography.GeographicPoint;
import util.GraphLoader;
//import week2example.MazeNode;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
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
		
		//corner case
		if (location == null) {									
			System.out.println("Please provide non-null locations");
			return false;
		}
		
		//new node at the passed in lat/lang
		myNode temp = new myNode(location);						
		
		//only add new keys
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
		
		//corner case
		if (!myMap.containsKey(to) || !myMap.containsKey(from)) {	
			throw new IllegalArgumentException();
		}
		
		
		myNode node = myMap.get(from);
		//copy all passed in values into the mapEdge class type
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
		
		//where I will put return values
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();		
		//I used geopoint and myNode here because it was easier to traverse through the nodes (spec uses geopoint,geopoint)
		HashMap<GeographicPoint, myNode> parentMap = new HashMap<GeographicPoint, myNode>();	
		HashSet<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Queue<myNode> to_explore = new LinkedList<myNode>();
		boolean found = false; 
	
		//start at the beginning
		myNode beginning = myMap.get(start);	
		//using nodes because (1) they are easier to stack in the queue, (2) easier to traverse, and
		// (3) because my "myNode" class creates a list of type mapEdge-->Private List<mapEdge> edges<--
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
			//I did attempt a for-each loop here, but it returned a null-pointer (any ideas???)
			for (int i = 0; i < neighbors_node.size(); i++) {									
				mapEdge e = neighbors_node.get(i);												
				GeographicPoint p = e.getEnd();
				neighbor_locations.add(p);
			}
			
			//now loop through the geopoint locations of the neighbor nodes
			for (GeographicPoint to_check : neighbor_locations) {								
				if (!visited.contains(to_check)) {
					//this variable is only used for ensuring we don't double-add geopoints
					visited.add(to_check); 	
					//add our neighbor's location to the parentMap for access later for building our path -- link to current node
					parentMap.put(to_check, curr);	
					//put another location in the queue, so we can check it out in the next loop
					to_explore.add(myMap.get(to_check));										
				}
			}
		}
		//corner case
		if (!found) {																			
			System.out.println("No path exists!!!!");
			return null;
		}
		
		//using some refactoring here
		path = buildPath(goal, start, parentMap, path);											
		return path;																					
																										
	}																									
	
	private LinkedList<GeographicPoint> buildPath(GeographicPoint goal, GeographicPoint start, HashMap<GeographicPoint, myNode> parentMap, LinkedList<GeographicPoint> path) {

		myNode current = new myNode(goal); 
		//until we hit the beginning of the route
		while (!current.getLocation().equals(start)) {											
			//add the end to the beginning of the queue
			path.addFirst(current.getLocation());		
			//replace the end with the next node, which will be taken from the parentMap list and identified by the stored geopoint
			current = parentMap.get(current.getLocation());										
		}
		//don't forget to start the list with the "start" position 
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
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		return null;
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
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3
		
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		return null;
	}
	
	public static void main(String[] args)
	{
		//System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		//System.out.print("DONE. \nLoading the map..." + "\n");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		//System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		/*
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		*/
		
		
		/* Use this code in Week 3 End of Week Quiz */
		/*MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		*/
		
	}
	
}
