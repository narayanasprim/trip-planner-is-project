/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mapitems.View;
import mapitems.City;
import planner.TripPlanner;

/**
 *
 * @author Harsha
 */
public class PlaceRoute {

    private int noOfDays;
    private double hoursForDay;
    private double defaultStartTime;
    private ArrayList<City> city;
    private UserPreference preference;
    private double currentRemainingTime;
    private Map map;
    private ArrayList<Integer> visited;
    private TripPlanner planner;
    private int[][] AdjacencyMatrix;
    private ArrayList<Integer> route;
    private double CurrentTime;
    private double justBeforeCurrentTime;
    private ArrayList<City> routePlaces;
     private ArrayList<City> routePlacesClone;
    private ArrayList<City> userPreference;
    private ArrayList<City> userPreferenceClone;
     private ArrayList<City> city1;
    private UserPlanOut planOut;
    private Day day;

    public PlaceRoute(UserPreference preference) {
        this.preference = preference;
        planOut = new UserPlanOut();
        setDefaultStartTime(7);
        noOfDays = preference.getNoOfDays();
        setHoursForDay(18);
    }

    private ArrayList<Integer> getRouteCities(ArrayList<Integer> cityId, int startId) {
        ArrayList<Integer> temp;
        setPlanner(new TripPlanner());
        temp = getPlanner().getRoute(cityId, startId);
        setCity(getPlanner().getMap().getCities());
        city1=(ArrayList<City>) getPlanner().getMap().getCities().clone();
        setAdjacencyMatrix(getPlanner().getMap().getAdjacencyMatrix());
        return temp;
    }
    public UserPlanOut planRoute(UserPreference preference1) {
        route = getRouteCities(preference1.getSelectedCityId(), preference1.getStartCityId());
        if(route==null)
        {
            planOut.setIsPossible(false);
            return planOut;
        }
        day = new Day();
        routePlaces =  makeRouteCities();
        addSuggestions();
        userPreference = userPreference();
        userPreferenceClone=(ArrayList<City>) userPreference.clone();
        ArrayList<City> temp=(ArrayList<City>) routePlaces.clone();
        planOut.setRoute(temp);
        routePlacesClone=(ArrayList<City>) routePlaces.clone();
        CurrentTime=0.00;
        while (noOfDays > 0) {  
            if (noOfDays == preference.getNoOfDays()) {
                CurrentTime += preference.getStartTime();
                System.out.println("Day 1");
            } else {
                CurrentTime = getDefaultStartTime()+0.00;
                 System.out.println("Next Day");
            }
            planRouteForDay();
            planOut.addDay(day);
            day = new Day();
            userPreferenceClone=(ArrayList<City>) userPreference.clone();
            routePlacesClone=(ArrayList<City>) routePlaces.clone();
            noOfDays--;
        }
        if(!userPreference.isEmpty())
        {
            planOut.setIsPossible(false);
        }
        if(CurrentTime>preference.getArrivalTime())
        {
            planOut.setIsPossible(false);
        }
        return planOut;
    }

    public void planRouteForDay() {
        double cityTravelTime;
        
        for (int i = 0; i < routePlacesClone.size() - 1; i++) {
            for (int j = 0; j < userPreferenceClone.size(); j++) {
                System.out.println(userPreferenceClone.get(j).getId()+"and"+routePlacesClone.get(i).getId());
                if (userPreferenceClone.get(j).getId() == routePlacesClone.get(i).getId()) {
                    System.out.println("CIty"+userPreferenceClone.get(j).getName());
                    String s = planVisitingPlaces(j);
                    if (s.equalsIgnoreCase("end")) {
                        System.out.println("end");
                        return;
                    } else {
                        userPreference.remove(userPreferenceClone.get(j));
                        System.out.println(userPreferenceClone.size());
                        System.out.println(userPreference.size());
                        System.out.println("YeppiRemoved");
                    }
                }
            }
            cityTravelTime = AdjacencyMatrix[routePlacesClone.get(i).getId()][routePlacesClone.get(i + 1).getId()];
            if (isDayEnd(CurrentTime, cityTravelTime)) {
                if (noOfDays > 1) {
                    for (int a = 0; a < routePlaces.get(i).getBeds().size(); a++) {
                        Location loc = new Location();
                        loc.setCity(routePlaces.get(i).getName());
                        loc.setName(routePlaces.get(i).getBeds().get(a).getName());
                        day.addRestaurant(loc);
                    }
                } else if (noOfDays == 1 && preference.isDinner()) {
                    for (int b = 0; b < routePlacesClone.get(i).getBeds().size(); b++) {
                        Location loc = new Location();
                        loc.setCity(routePlacesClone.get(i).getName());
                        loc.setName(routePlacesClone.get(i).getMeals().get(b).getName());
                        day.addDinner(loc);
                    }
                }
                return;
            } else if (!isDayEnd(CurrentTime, cityTravelTime)) {
                justBeforeCurrentTime = CurrentTime;
                CurrentTime += cityTravelTime;
                if (chekForBreakFirst(CurrentTime, justBeforeCurrentTime)) {
                    if (preference.isBreakFast()) {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime = +1.00;
                        Location loc = new Location();
                        loc.setCity(routePlacesClone.get(i).getName());
                        loc.setApproximateTime(justBeforeCurrentTime);
                        loc.setName("Cannot get meal at a visiting place.Changing between cities");
                        day.addMorningMeal(loc);
                    } else {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.00;
                        for (int j = 0; j < routePlacesClone.get(i).getMeals().size(); j++) {
                            Location loc = new Location();
                            loc.setCity(routePlacesClone.get(i).getName());
                            loc.setApproximateTime(justBeforeCurrentTime);
                            loc.setName(routePlacesClone.get(i).getMeals().get(j).getName());
                            day.addMorningMeal(loc);
                        }
                    }
                } else if (checkForLunch(CurrentTime, justBeforeCurrentTime)) {
                    if (preference.isLunch()) {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.30;
                        Location loc = new Location();
                        loc.setCity(routePlacesClone.get(i).getName());
                        loc.setApproximateTime(justBeforeCurrentTime);
                        loc.setName("Cannot get meal at a visiting place.Changing between cities");
                        day.addLunch(loc);
                    } else {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.30;
                        for (int j = 0; j < routePlacesClone.get(i).getMeals().size(); j++) {
                            Location loc = new Location();
                            loc.setCity(routePlacesClone.get(i).getName());
                            loc.setApproximateTime(justBeforeCurrentTime);
                            loc.setName(routePlacesClone.get(i).getMeals().get(j).getName());
                            day.addLunch(loc);
                        }
                    }
                } else if (checkForEveningTea(CurrentTime, justBeforeCurrentTime)) {
                    if (preference.isEveningMeal()) {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.00;
                        Location loc = new Location();
                        loc.setCity(routePlacesClone.get(i).getName());
                        loc.setApproximateTime(justBeforeCurrentTime);
                        loc.setName("Cannot get meal at a visiting place.Changing between cities");
                        day.addeveningMealPlace(loc);
                    } else {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.00;
                        for (int j = 0; j < routePlacesClone.get(i).getMeals().size(); j++) {
                            Location loc = new Location();
                            loc.setCity(routePlacesClone.get(i).getName());
                            loc.setApproximateTime(justBeforeCurrentTime);
                            loc.setName(routePlacesClone.get(i).getMeals().get(j).getName());
                            day.addeveningMealPlace(loc);
                        }
                    }
                }
                routePlaces.remove(routePlacesClone.get(i));
            }
               System.out.println(routePlacesClone.size());
               System.out.println(routePlaces.size());
            System.out.println(CurrentTime + "After a City");
        }
    }

    public void addSuggestions() {
        boolean isContains=true;
                        for(int i=0;i<routePlaces.size()-1;i++)
                        {
                        for(int j=0;j<routePlaces.get(i).getViews().size();j++)
                        {
                            isContains=false;
                        for(int k=0;k<preference.getData().size();k++)
                        {
                            for(int l=0;l<preference.getData().get(k).getLocations().size();l++)
                            {
                                  if(routePlaces.get(i).getViews().get(j).getName().equalsIgnoreCase(preference.getData().get(k).getLocations().get(l)))
                                  {
                                      isContains=true;
                                  }

                            }
                        }
                         if(!isContains)
                         {
                             Location location=new Location();
                             location.setCity(routePlaces.get(i).getName());
                             location.setName(routePlaces.get(i).getViews().get(j).getName());
                             day.addSuggetion(location);
                         }
                      
                        }
                        }
    }

    public boolean isSuggestionExist(String name) {
        for (int i = 0; i < day.getSuggestions().size(); i++) {
            if (day.getSuggestions().get(i).getCity().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean chekForBreakFirst(double currentTime, double timebefore) {
        if (preference.getBreakFirstTime() <= currentTime && preference.getBreakFirstTime() >= timebefore) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForLunch(double currentTime, double timebefore) {
        if (preference.getLunchTime() <= currentTime && preference.getLunchTime() >= timebefore) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForEveningTea(double currentTime, double timebefore) {
        if (preference.getEveningMealTime() <= currentTime && preference.getEveningMealTime() >= timebefore) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Planning trip within visiting city
     * @param x
     * @return
     */
    public String planVisitingPlaces(int x) {
        System.out.println("Callededdd");
        System.out.println(CurrentTime + "See");
        double visitingTime = 0;
        System.out.println("Oooopz" + userPreferenceClone.get(x).getViews().size());
        for(int i=0;i<userPreferenceClone.get(x).getViews().size();i++)
        {
            visitingTime = userPreferenceClone.get(x).getViews().get(i).getTime() / 60;
            System.out.println("VistingTIme" + visitingTime);
            if (isDayEnd(CurrentTime, visitingTime)) {
                System.out.println("DayEndwwwww");
                if (noOfDays > 1) {
                    for (int a = 0; a < userPreferenceClone.get(x).getBeds().size(); a++) {
                        Location loc = new Location();
                        loc.setCity(userPreferenceClone.get(x).getName());
                        loc.setName(userPreferenceClone.get(x).getBeds().get(a).getName());
                        day.addRestaurant(loc);
                    }
                } else if (noOfDays == 1 && preference.isDinner()) {
                    for (int b = 0; b < userPreferenceClone.get(x).getBeds().size(); b++) {
                        Location loc = new Location();
                        loc.setCity(userPreferenceClone.get(x).getName());
                        loc.setName(userPreferenceClone.get(x).getMeals().get(b).getName());
                        day.addDinner(loc);
                    }
                }
                return "end";
            } else {
                justBeforeCurrentTime = CurrentTime;
                CurrentTime += visitingTime;
                if (chekForBreakFirst(CurrentTime, justBeforeCurrentTime)) {
                    if (preference.isBreakFast()) {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 0.15;
                        Location loc = new Location();
                        loc.setCity(userPreferenceClone.get(x).getName());
                        loc.setApproximateTime(justBeforeCurrentTime);
                        loc.setName(userPreferenceClone.get(x).getViews().get(i).getName());
                        day.addMorningMeal(loc);
                    } else {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.00;
                        for (int j = 0; j < userPreferenceClone.get(x).getMeals().size(); j++) {
                            Location loc = new Location();
                            loc.setCity(userPreferenceClone.get(x).getName());
                            loc.setApproximateTime(justBeforeCurrentTime);
                            loc.setName(userPreferenceClone.get(x).getMeals().get(j).getName());
                            day.addMorningMeal(loc);
                        }
                    }
                } else if (checkForLunch(CurrentTime, justBeforeCurrentTime)) {
                    if (preference.isLunch()) {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.00;
                        Location loc = new Location();
                        loc.setCity(userPreferenceClone.get(x).getName());
                        loc.setApproximateTime(justBeforeCurrentTime);
                        loc.setName(userPreferenceClone.get(x).getViews().get(i).getName());
                        day.addLunch(loc);
                    } else {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.30;
                        for (int j = 0; j < userPreferenceClone.get(x).getMeals().size(); j++) {
                            Location loc = new Location();
                            loc.setCity(userPreferenceClone.get(x).getName());
                            loc.setApproximateTime(justBeforeCurrentTime);
                            loc.setName(userPreferenceClone.get(x).getMeals().get(j).getName());
                            day.addLunch(loc);
                        }
                    }
                } else if (checkForEveningTea(CurrentTime, justBeforeCurrentTime)) {
                    if (preference.isEveningMeal()) {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 0.15;
                        Location loc = new Location();
                        loc.setCity(userPreferenceClone.get(x).getName());
                        loc.setApproximateTime(justBeforeCurrentTime);
                        loc.setName(userPreferenceClone.get(x).getViews().get(i).getName());
                        day.addeveningMealPlace(loc);
                    } else {
                        justBeforeCurrentTime = CurrentTime;
                        CurrentTime += 1.00;
                        for (int j = 0; j < userPreferenceClone.get(x).getMeals().size(); j++) {
                            Location loc = new Location();
                            loc.setCity(userPreferenceClone.get(x).getName());
                            loc.setApproximateTime(justBeforeCurrentTime);
                            loc.setName(userPreferenceClone.get(x).getMeals().get(j).getName());
                            day.addeveningMealPlace(loc);
                        }
                    }
                }
            }
            Location location = new Location();
            location.setCity(userPreferenceClone.get(x).getName());
            location.setApproximateTime(justBeforeCurrentTime);
            location.setName(userPreferenceClone.get(x).getViews().get(i).getName());
            day.addVisitingPlace(location);
            for(int aa=0;aa<userPreference.size();aa++)
            {
                if(userPreference.get(aa).getId()==userPreferenceClone.get(x).getId())
                {
                     userPreference.get(aa).removeView(userPreferenceClone.get(x).getViews().get(i).getName());
                }
            }
            i=-1;
            System.out.println(i+"AfterLocation"+userPreferenceClone.get(x).getViews().size());

        }
        return "ok";
    }

    public boolean isDayEnd(double currenTime, double otherTime) {
        if (getHoursForDay() > currenTime + otherTime) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<City> makeRouteCities() {
        ArrayList<City> city = new ArrayList<City>();

        for (int i = 0; i < route.size(); i++) {
            city.add(getCityById(route.get(i)));
            System.out.println(getCityById(route.get(i)).getName()+"ROUTE");
            System.out.println(getCityById(route.get(i)).getViews().size()+"ROUTE");      
        }
        return  city;
    }

    public ArrayList<City> userPreference() {
        ArrayList<UserData> data = (ArrayList<UserData>) preference.getData();
        ArrayList<City> city = new ArrayList<City>();
        for (int i = 0; i < data.size(); i++) {
            ArrayList<View> view = new ArrayList<View>();
            City temp = getCityById(data.get(i).getCityId());
            for (int j = 0; j < data.get(i).getLocations().size(); j++) {
                View temp1 = temp.getLocationByViewName(data.get(i).getLocations().get(j));
                view.add(temp1);
            }

            temp.setViews(view);
            city.add(temp);
        }
        return  city;
    }

    public boolean isVisited(int id) {
        for (int i = 0; i < visited.size(); i++) {
            if (visited.get(i) == id) {
                return true;
            }
        }
        return false;
    }

    public City getCityById(int id) {
        City temp = null;
        for (int i = 0; i < city.size(); i++) {
            if (city.get(i).getId() == id) {
                temp = city.get(i);
                System.out.println("Viewssss"+temp.getViews().size());
            }
        }
        return temp;
    }
     public City getCityById2(int id) {
        City temp = null;
        for (int i = 0; i < city1.size(); i++) {
            if (city1.get(i).getId() == id) {
                temp = city1.get(i);
                System.out.println("Viewssss"+temp.getViews().size());
            }
        }
        return temp;
    }
    private void remainingTime() {
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
     * @return the hoursForDay
     */
    public double getHoursForDay() {
        return hoursForDay;
    }

    /**
     * @param hoursForDay the hoursForDay to set
     */
    public void setHoursForDay(double hoursForDay) {
        this.hoursForDay = hoursForDay;
    }

    /**
     * @param city the city to set
     */
    public void setCity(ArrayList<City> city) {
        this.city = city;
    }

    /**
     * @return the preference
     */
    public UserPreference getPreference() {
        return preference;
    }

    /**
     * @param preference the preference to set
     */
    public void setPreference(UserPreference preference) {
        this.preference = preference;
    }

    /**
     * @return the currentRemainingTime
     */
    public double getCurrentRemainingTime() {
        return currentRemainingTime;
    }

    /**
     * @param currentRemainingTime the currentRemainingTime to set
     */
    public void setCurrentRemainingTime(double currentRemainingTime) {
        this.currentRemainingTime = currentRemainingTime;
    }

    /**
     * @return the defaultStartTime
     */
    public double getDefaultStartTime() {
        return defaultStartTime;
    }

    /**
     * @param defaultStartTime the defaultStartTime to set
     */
    public void setDefaultStartTime(double defaultStartTime) {
        this.defaultStartTime = defaultStartTime;
    }

    /**
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * @return the planner
     */
    public TripPlanner getPlanner() {
        return planner;
    }

    /**
     * @param planner the planner to set
     */
    public void setPlanner(TripPlanner planner) {
        this.planner = planner;
    }

    /**
     * @return the AdjacencyMatrix
     */
    public int[][] getAdjacencyMatrix() {
        return AdjacencyMatrix;
    }

    /**
     * @param AdjacencyMatrix the AdjacencyMatrix to set
     */
    public void setAdjacencyMatrix(int[][] AdjacencyMatrix) {
        this.AdjacencyMatrix = AdjacencyMatrix;
    }

    /**
     * @return the route
     */
    public ArrayList<Integer> getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(ArrayList<Integer> route) {
        this.route = route;
    }

    /**
     * @return the routePlaces
     */
    public List<City> getRoutePlaces() {
        return routePlaces;
    }

    /**
     * @param routePlaces the routePlaces to set
     */
    public void setRoutePlaces(ArrayList<City> routePlaces) {
        this.routePlaces = routePlaces;
    }

    /**
     * @return the userPererence
     */
    public List<City> getUserPererence() {
        return userPreference;
    }

    /**
     * @param userPererence the userPererence to set
     */
    public void setUserPererence(ArrayList<City> userPererence) {
        this.userPreference = userPererence;
    }
}
