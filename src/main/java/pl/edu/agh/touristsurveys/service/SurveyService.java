package pl.edu.agh.touristsurveys.service;

import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.model.CityPOIs;
import pl.edu.agh.touristsurveys.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class SurveyService {

    private City city;
    private List<Coordinates> coordinatesList;
    private final OverpassService overpassService;
    private CityPOIs POIs;

    public SurveyService(OverpassService overpassService) {
        this.overpassService = overpassService;
    }

    public void setCity(City city) {
        this.city = city;
        this.POIs = new CityPOIs(city);
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinatesList = coordinates;
    }

    public List<Coordinates> getCoordinatesList() {
        return coordinatesList;
    }

    public void getAllCityData() throws Exception {
        MapService mp = new MapService(overpassService);
        POIs.setMuseum(mp.getAllMuseums(city));
        POIs.setAccommodation(mp.getAllAccommodation(city));
        POIs.setTransport(mp.getAllTransport(city));
        POIs.setFood(mp.getAllFood(city));
    }

    public void seekForPlaces(double dist) {
        //TODO: change it to foreach for every defined type of place
        for (var c : coordinatesList) {
            List<Building> buildings = new ArrayList<Building>();
            for (var m : POIs.museum) {
                var dis = distance(c.getLatitude(), m.getLat(), c.getLongitude(), m.getLon());
                if (dis <= dist) {
                    buildings.add(m);
                }
            }
            c.setMuseums(buildings);
        }

        for (var c : coordinatesList) {
            List<Building> buildings = new ArrayList<Building>();
            for (var m : POIs.transport) {
                var dis = distance(c.getLatitude(), m.getLat(), c.getLongitude(), m.getLon());
                if (dis <= dist) {
                    buildings.add(m);
                }
            }
            c.setTransport(buildings);
        }

        for (var c : coordinatesList) {
            List<Building> buildings = new ArrayList<Building>();
            for (var m : POIs.food) {
                var dis = distance(c.getLatitude(), m.getLat(), c.getLongitude(), m.getLon());
                if (dis <= dist) {
                    buildings.add(m);
                }
            }
            c.setFood(buildings);
        }

        for (var c : coordinatesList) {
            List<Building> buildings = new ArrayList<Building>();
            for (var m : POIs.accommodation) {
                var dis = distance(c.getLatitude(), m.getLat(), c.getLongitude(), m.getLon());
                if (dis <= dist) {
                    buildings.add(m);
                }
            }
            c.setAccommodation(buildings);
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
