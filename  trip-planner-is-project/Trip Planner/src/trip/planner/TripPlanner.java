/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.planner;

import java.util.ArrayList;

import trip.dataaccess.MapGenerator;
import trip.mapitems.City;

/**
 *
 * @author Romesh
 */
public class TripPlanner {
	
	private class Node{
    	City City;
    	int Weight;
    	Node predecessor;
    }
	
	
	private Map Map;
	private MapGenerator MapGenerator;
    private int[][] AdjacencyMatrix;
    private ArrayList<City> CityList;
    private final int INF = 999999;
    private Node[] nodes;

    
    
    public TripPlanner() {
    	Map = new Map();
    	MapGenerator = new MapGenerator(Map);
    	
    	Map = MapGenerator.Generate();
    	AdjacencyMatrix = Map.getAdjacencyMatrix();
    	CityList = Map.getCities();
    	
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
        
    }
    
    public ArrayList<Node> makeRoute(Node Source, Node Target) {
    	ArrayList<Node> discovered = new ArrayList<Node>();
    	ArrayList<Node> finished = new ArrayList<Node>();
    	ArrayList<Node> route = new ArrayList<Node>();
    	nodes = new Node[CityList.size()];
    	
    	for (int i = 0; i < nodes.length; i++) {
			nodes[i].City = CityList.get(i);
			nodes[i].Weight = INF;
			nodes[i].predecessor = null;
		}
    	
    	Source.Weight = 0;
    	discovered.add(Source);
    
    	while (!discovered.isEmpty()) {
    		Node MinDistance = findMinimumDistance(discovered);
    		finished.add(MinDistance);
    		ArrayList<Node> neighbours = getNeighbours(MinDistance, nodes);
    		if(isTargetFound(neighbours, Target)!=-1){
    			Target = neighbours.get(isTargetFound(neighbours, Target));
    			break;
    		}
    		discovered.addAll(neighbours);
    		discovered.remove(MinDistance);
    		
		}
    	
    	Node currNode = Target;
    	
    	while (currNode.predecessor != null) {
			route.add(currNode);
			currNode = currNode.predecessor;
		}
    	return route;
	}
    
    public int isTargetFound(ArrayList<Node> neighbours, Node Target) {
		return neighbours.indexOf(Target);
	}
    
    public Node findMinimumDistance(ArrayList<Node> discovered){
    	Node MinDistance = discovered.get(0);
    	for (int i = 1; i < discovered.size(); i++) {
			if(discovered.get(i).Weight<MinDistance.Weight){
				MinDistance = discovered.get(i);
			}
		}
    	return MinDistance;
    } 
    
    public ArrayList<Node> getNeighbours(Node Node, Node[] nodes) {
    	ArrayList<Node> neighbours = new ArrayList<Node>();
    	
    	for (int i = 0; i < AdjacencyMatrix[Node.City.getId()].length; i++) {
			if(AdjacencyMatrix[Node.City.getId()][i]<INF){
				nodes[i].predecessor = Node;
				neighbours.add(nodes[i]);
			}
		}
    	return neighbours;
	}
    
}

