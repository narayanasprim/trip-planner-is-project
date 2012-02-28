/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AI;

import java.util.ArrayList;
import java.util.List;
import mapitems.City;

/**
 *
 * @author Harsha
 */
public class UserPreference {
private int noOfDays;
private List<City> city;
private boolean breakFast;
private boolean eveningMeal;
private String breakFirstTime;
private String eveningMealTime;
private String startTime;
public UserPreference()
{
    city=new ArrayList<City>();

}
public void addCity(City city)
{
        this.getCity().add(city);
}

    /**
     * @return the noOfDays
     */
    public int getNoOfDays() {
        return noOfDays;
    }

    /**
     * @param noOfDays the noOfDays to set
     */
    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    /**
     * @return the city
     */
    public List<City> getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(List<City> city) {
        this.city = city;
    }

    /**
     * @return the breakFast
     */
    public boolean isBreakFast() {
        return breakFast;
    }

    /**
     * @param breakFast the breakFast to set
     */
    public void setBreakFast(boolean breakFast) {
        this.breakFast = breakFast;
    }

    /**
     * @return the eveningMeal
     */
    public boolean isEveningMeal() {
        return eveningMeal;
    }

    /**
     * @param eveningMeal the eveningMeal to set
     */
    public void setEveningMeal(boolean eveningMeal) {
        this.eveningMeal = eveningMeal;
    }

    /**
     * @return the breakFirstTime
     */
    public String getBreakFirstTime() {
        return breakFirstTime;
    }

    /**
     * @param breakFirstTime the breakFirstTime to set
     */
    public void setBreakFirstTime(String breakFirstTime) {
        this.breakFirstTime = breakFirstTime;
    }

    /**
     * @return the eveningMealTime
     */
    public String getEveningMealTime() {
        return eveningMealTime;
    }

    /**
     * @param eveningMealTime the eveningMealTime to set
     */
    public void setEveningMealTime(String eveningMealTime) {
        this.eveningMealTime = eveningMealTime;
    }

}
