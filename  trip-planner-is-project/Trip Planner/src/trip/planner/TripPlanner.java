/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.planner;

import trip.dataaccess.MapGenerator;

/**
 *
 * @author Romesh
 */
public class TripPlanner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Map Map = new Map();
        MapGenerator MapGenerator = new MapGenerator(Map);
        
        Map = MapGenerator.Generate();
        System.out.print("hi");
        
    }
}
