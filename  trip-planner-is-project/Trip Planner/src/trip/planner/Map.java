package trip.planner;

import java.util.ArrayList;
import trip.mapitems.City;

/**
 *
 * @author Romesh
 */
public class Map {
    private int mapSize;
    private int[][] AdjacencyMatrix;
    private ArrayList<City> Cities;

    public Map() {
        mapSize = 20;
        AdjacencyMatrix = new int[mapSize][mapSize];
        AdjacencyMatrix[0][0] = 23;
        Cities = new ArrayList<City>();
    }

    /**
     * @return the Cities
     */
    public ArrayList<City> getCities() {
        return Cities;
    }

    /**
     * @param Cities the Cities to set
     */
    public void setCities(ArrayList<City> Cities) {
        this.Cities = Cities;
    }
    
    
    
    
}
