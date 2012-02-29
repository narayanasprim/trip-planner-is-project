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
    	ArrayList<Integer> preferedPlaces= new ArrayList<Integer>();
    	preferedPlaces.add(2);
    	preferedPlaces.add(5);
    	preferedPlaces.add(3);
    	int start = 0;
    	(new TripPlanner()).getRoute(preferedPlaces, start);
    }
    
    public ArrayList<Integer> getRoute(ArrayList<Integer> preferedPlaces, int start){

    	ArrayList<Integer> route = new ArrayList<Integer>();
    	Node startingNode = new Node(start);
    	route.add(start);
    	
    	Node[] nodes = new Node[AdjacencyMatrix[0].length];
    	
    	
    	while(!preferedPlaces.isEmpty()){
    		for (int i = 0; i < nodes.length; i++) {
        		nodes[i] = new Node();
    			nodes[i].setId(i);
    			nodes[i].setPre(null);
    			nodes[i].setWeight(0);
    		}
    		ArrayList<Node> discoverd = new ArrayList<Node>();
    		discoverd.addAll(getAllNeighbours(startingNode, nodes));
    		startingNode = isPrefered(preferedPlaces, discoverd);
    		if(startingNode != null){
    		preferedPlaces.remove((Integer.valueOf(startingNode.getId())));
    		route.addAll(subRoute(startingNode, nodes));
    		}
    	}
    	for (int i = 0; i < nodes.length; i++) {
    		nodes[i] = new Node();
			nodes[i].setId(i);
			nodes[i].setPre(null);
			nodes[i].setWeight(0);
		}
    	startingNode = nodes[route.get(route.size()-1)];
    	getAllNeighbours(startingNode, nodes);
    	startingNode = nodes[route.get(0)];
    	route.addAll(subRoute(startingNode, nodes));   	
    	
    	
    	return route;
    }
    
    public ArrayList<Node> getAllNeighbours(Node node, Node[] nodes) {
    	ArrayList<Node> discoverd = new ArrayList<Node>();
    	ArrayList<Node> finished = new ArrayList<Node>();
    	
    	discoverd.add(node);
    	
    	Node curruntNode = node;
    	nodes[curruntNode.getId()].setPre(null);
    	nodes[curruntNode.getId()].setWeight(0);
    	nodes[curruntNode.getId()].setState(1);
    	
    		while(!discoverd.isEmpty()){
    		curruntNode = discoverd.remove(0);
    		for (int i = 0; i < AdjacencyMatrix[curruntNode.getId()].length; i++) {
    			if(AdjacencyMatrix[curruntNode.getId()][i]>0 && AdjacencyMatrix[curruntNode.getId()][i]<INF && nodes[i].getState()==0){
    				nodes[i].setPre(nodes[curruntNode.getId()]);
    				nodes[i].setWeight(curruntNode.getWeight() + AdjacencyMatrix[curruntNode.getId()][i]);
    				nodes[i].setState(1);
    				discoverd.add(nodes[i]);
    			}
    		}
    		nodes[curruntNode.getId()].setState(2);
    		finished.add(nodes[curruntNode.getId()]);
    	}
    		
    	return finished;
	}
    
    public ArrayList<Node> getNeighbours(Node Node, Node[] nodes) {
    	ArrayList<Node> neighbours = new ArrayList<Node>();
    	
    	for (int i = 0; i < AdjacencyMatrix[Node.getId()].length; i++) {
			if(AdjacencyMatrix[Node.getId()][i]>0 && AdjacencyMatrix[Node.getId()][i]<INF){
				nodes[i].setPre(nodes[Node.getId()]);
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
			if(preferedPlaces.contains(nodeList.get(i).getId())){
				prefredNeighbour = nodeList.get(i);
				break;
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
    	Collections.reverse(subRoute);
    	return subRoute;
    }
    
    public void sortNodes(ArrayList<Node> NodeList){
    	Collections.sort(NodeList);
    }
    
    
}


	

