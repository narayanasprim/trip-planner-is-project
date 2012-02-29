/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.planner;

import java.util.ArrayList;
import java.util.Collections;


import trip.dataaccess.MapGenerator;


/**
 *
 * @author Romesh
 */
public class TripPlanner {

	private int[][] AdjacencyMatrix;
	private MapGenerator MapGenerator;
	private Map Map;
	private final int INF = 999999;
	
	
    public TripPlanner() {
    	Map = new Map();
    	MapGenerator = new MapGenerator(Map);
    	Map = MapGenerator.Generate();
    	AdjacencyMatrix = Map.getAdjacencyMatrix();
	}
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
    }
    
    public ArrayList<Integer> getRoute(ArrayList<Integer> preferedPlaces, int start){

    	ArrayList<Integer> route = new ArrayList<Integer>();
    	Node startingNode = new Node(start);
    	
    	Node[] nodes = new Node[AdjacencyMatrix[0].length];
    	for (int i = 0; i < nodes.length; i++) {
			nodes[i].setId(i);
		}
    	
    	while(!preferedPlaces.isEmpty()){
    		ArrayList<Node> discoverd = new ArrayList<Node>();
    		discoverd.addAll(getNeighbours(startingNode, nodes));
    		startingNode = isPrefered(preferedPlaces, discoverd);
    		preferedPlaces.remove(startingNode);
    		route.addAll(subRoute(startingNode, nodes));
    	}
    	
    	
    	
    	return route;
    }
    
    public ArrayList<Node> getNeighbours(Node Node, Node[] nodes) {
    	ArrayList<Node> neighbours = new ArrayList<Node>();
    	
    	for (int i = 0; i < AdjacencyMatrix[Node.getId()].length; i++) {
			if(AdjacencyMatrix[Node.getId()][i]<INF){
				nodes[i].setPre(Node);
				nodes[i].setWeight(Node.getWeight() + AdjacencyMatrix[Node.getId()][i]);
				neighbours.add(nodes[i]);
			}
		}
    	return neighbours;
	}
    
    public Node isPrefered(ArrayList<Integer> preferedPlaces, ArrayList<Node> nodeList){
    	sortNodes(nodeList);
    	Node prefredNeighbour = null;
    	for (int i = 0; i < nodeList.size(); i++) {
			if(preferedPlaces.contains(nodeList.get(i))){
				prefredNeighbour = nodeList.get(i);
			}
		}
    	return prefredNeighbour;
	}
    
    public ArrayList<Integer> subRoute(Node Node, Node[] nodes){
    	ArrayList<Integer> subRoute = new ArrayList<Integer>();
    	while(Node.getPre()!=null){
    		subRoute.add(Node.getId());
    		Node = Node.getPre();
    	}
    	return subRoute;
    }
    
    public void sortNodes(ArrayList<Node> NodeList){
    	Collections.sort(NodeList);
    }
    
    
}


	

