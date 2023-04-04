package pl.edu.agh.touristsurveys.service;

import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.model.CityPOIs;
import pl.edu.agh.touristsurveys.model.Coordinates;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;

import java.util.ArrayList;
import java.util.List;

public class BtsService {

    private City city;
    private List<Coordinates> coordinatesList;
    private OverpassService overpassService;
    private CityPOIs POIs;

    public BtsService(OverpassService overpassService) {
        this.overpassService = overpassService;
    }

    public void setCity(City city) {
        this.city = city;
        this.POIs = new CityPOIs();
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinatesList = coordinates;
    }

    public void getAllCityData() throws Exception {
        MapService mp = new MapService(overpassService);
        var museums = mp.getAllMuseums(city);
        var accommodation = mp.getAllAccommodation(city);
        var transport = mp.getAllTransport(city);
        var food = mp.getAllFood(city);
    }

    public void seekForMuseums(double dist) {
        for (var c : coordinatesList) {
            List<Building> buildings = new ArrayList<Building>();
            for (var m : POIs.museum) {
                var dis = distance(c.latitude, m.getLat(), c.longitude, m.getLon());
                if (dis <= dist) {
                    buildings.add(m);
                }
            }
            c.setMuseums(buildings);
        }
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

}
