package roadgraph;

import java.util.LinkedList;
import java.util.List;

import geography.GeographicPoint;

public class mapEdge{
	GeographicPoint start; 
	GeographicPoint end; 
	String roadName;
	String roadType; 
	double length;
	
	
	public mapEdge(GeographicPoint start, GeographicPoint end, String raodName, String roadType, double length) {
		this.start = start; 
		this.end = end; 
		this.roadName = raodName;
		this.roadType = roadType; 
		this.length = length;
	}
	
	
	public GeographicPoint getEnd() {			
		return end;
	}
	
	public GeographicPoint getStart() {
		return start;
	}
	
	public String getRoadName() {
		return roadName;
	}
	 
	public String getRoadType() {
		return roadType;
	}
	
	public double getLength() {
		return length; 
	}
	
	public void setEnd(GeographicPoint end) {
		this.end = end;
	}
	
	public void setStart(GeographicPoint start) {
		this.start = start;
	}
	
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	
	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	
}
