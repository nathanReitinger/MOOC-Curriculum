package roadgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;



import geography.GeographicPoint;
//import week2example.MazeNode;


public class myNode implements Comparable<myNode> {										
	private GeographicPoint location;
	private List<mapEdge> edges;
	public double minDistance = Double.POSITIVE_INFINITY;
	public double minDistance_heuristic = Double.POSITIVE_INFINITY;
	public myNode previous; 												
	
	
	public myNode(GeographicPoint location) {
		this.location = location;
		edges = new LinkedList<mapEdge>();	
	}

	public GeographicPoint getLocation() {
		return location;
	}

	public void setLocation(GeographicPoint location) {
		this.location = location;
	}
 
	//links the edges to the nodes
	public void addEdges (mapEdge edge) {					
		edges.add(edge);
	}
	
	//returns the edges linked to this node
	public List<mapEdge> getEdges() {						
		return edges;
	}
	
	//per A*, we need to compare heuristic distance in order to pick least heuristic distance from PQ																
	@Override																
	public int compareTo(myNode other) {
		return Double.compare(this.minDistance_heuristic, other.minDistance_heuristic);
	}
	


}
