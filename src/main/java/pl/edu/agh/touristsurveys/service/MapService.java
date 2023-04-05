package pl.edu.agh.touristsurveys.service;

import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapService {
    private final OverpassService overpassService;
    private HashMap<City, Double[]> cityBoundaries;

    public MapService(OverpassService overpassService) {
        this.overpassService = overpassService;
        initBoundaries();
    }

    public List<Building> getAllMuseums(City city) {
        var boundaries = cityBoundaries.get(city);

        BuildingQuery buildingQuery = BuildingQuery.singleTagList(
                List.of("museum"),
                boundaries[0], //southernLat
                boundaries[1], //northernLat
                boundaries[2], //westernLon
                boundaries[3]); //easterLon

        return overpassService.getBuildings(buildingQuery);
    }

    public List<Building> getAllAccommodation(City city) {
        var hotels = getTourismBuilding(city, "hotel");
        var hostels = getTourismBuilding(city, "hostel");
        var motels = getTourismBuilding(city, "motel");
        var guest_houses = getTourismBuilding(city, "guest_house");

        var accommodation = new ArrayList<Building>(hotels);
        accommodation.addAll(hostels);
        accommodation.addAll(motels);
        accommodation.addAll(guest_houses);

        return accommodation;
    }

    public List<Building> getAllTransport(City city) {
        var boundaries = cityBoundaries.get(city);

        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("public_transport", "station"), //TODO: airports(which are lines), ferry terminals
                boundaries[0], //southernLat
                boundaries[1], //northernLat
                boundaries[2], //westernLon
                boundaries[3]); //easterLon

        return overpassService.getBuildings(buildingQuery);
    }

    public List<Building> getAllFood(City city) {

        var bars = getSustenanceBuilding(city, "bar");
        var biergartens = getSustenanceBuilding(city, "biergarten");
        var fast_foods = getSustenanceBuilding(city, "fast_food");
        var food_courts = getSustenanceBuilding(city, "food_court");
        var ice_creams = getSustenanceBuilding(city, "ice_cream");
        var pubs = getSustenanceBuilding(city, "pub");
        //var restaurant = getSustenanceBuilding(city, "restaurant"); //TODO: Exceeded limit on max bytes to buffer : 262144

        var food = new ArrayList<Building>(bars);
        food.addAll(biergartens);
        food.addAll(fast_foods);
        food.addAll(food_courts);
        food.addAll(ice_creams);
        food.addAll(pubs);
        //food.addAll(restaurant);

        return food;
    }

    public List<Building> getTourismBuilding(City city, String key) {
        var boundaries = cityBoundaries.get(city);

        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("tourism", key),
                boundaries[0], //southernLat
                boundaries[1], //northernLat
                boundaries[2], //westernLon
                boundaries[3]); //easterLon

        return overpassService.getBuildings(buildingQuery);
    }

    public List<Building> getSustenanceBuilding(City city, String key) {
        var boundaries = cityBoundaries.get(city);

        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("amenity", key),
                boundaries[0], //southernLat
                boundaries[1], //northernLat
                boundaries[2], //westernLon
                boundaries[3]); //easterLon

        return overpassService.getBuildings(buildingQuery);
    }

    private void initBoundaries() {
        cityBoundaries = new HashMap<City, Double[]>();

        cityBoundaries.put(City.KRAKOW, new Double[]{49.967, 50.201, 19.734, 20.17});
        cityBoundaries.put(City.PRAGUE, new Double[]{49.945, 50.178, 14.224, 14.708});
    }
}
