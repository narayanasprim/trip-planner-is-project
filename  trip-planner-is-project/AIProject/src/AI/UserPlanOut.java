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
public class UserPlanOut {
private boolean isPossible;
private  List<Day> dayPlan;
private  ArrayList<City> route;
private  String cities;
public void addDay(Day day)

    {
        getDayPlan().add(day);
        cities="";
}

    public UserPlanOut() {
        this.dayPlan = new ArrayList<Day>();
        isPossible=true;
    }

    /**
     * @return the dayPlan
     */
    public List<Day> getDayPlan() {
        return dayPlan;
    }

    /**
     * @param dayPlan the dayPlan to set
     */
    public void setDayPlan(List<Day> dayPlan) {
        this.dayPlan = dayPlan;
    }

    /**
     * @return the route
     */
    public ArrayList<City> getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(ArrayList<City> route1) {
        this.route = route1;
    }

    /**
     * @return the isPossible
     */
    public boolean isIsPossible() {
        return isPossible;
    }

    /**
     * @param isPossible the isPossible to set
     */
    public void setIsPossible(boolean isPossible) {
        this.isPossible = isPossible;
    }

    /**
     * @return the cities
     */
    public String getCities() {
        return cities;
    }

    /**
     * @param cities the cities to set
     */
    public void setCities(String cities) {
        this.cities = cities;
    }
}
