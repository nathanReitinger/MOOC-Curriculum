package roadgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;



import geography.GeographicPoint;
//import week2example.MazeNode;

//follows support video closely 
public class myNode {										
	private GeographicPoint location; 
	private List<mapEdge> edges;  
	
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


}
