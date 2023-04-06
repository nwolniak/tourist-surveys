package pl.edu.agh.touristsurveys.service;

import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.requests.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapService {
    private final OverpassService overpassService;
    private HashMap<City, Double[]> cityBoundaries;

    public MapService(OverpassService overpassService) {
        this.overpassService = overpassService;
        initBoundaries();
    }

    public List<Building> getAllMuseums(City city) {
        var boundaries = cityBoundaries.get(city);

        Query query = Query.builder()
                .timeout(100)
                .southernLat(boundaries[0])
                .northernLat(boundaries[1])
                .westernLon(boundaries[2])
                .easterLon(boundaries[3])
                .tags(List.of("museum"))
                .build();

        return overpassService.getBuildings(query);
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

        Query query = Query.builder()
                .timeout(100)
                .southernLat(boundaries[0])
                .northernLat(boundaries[1])
                .westernLon(boundaries[2])
                .easterLon(boundaries[3])
                .tags(List.of("public_transport=station")) //TODO: airports(which are lines), ferry terminals
                .build();

        return overpassService.getBuildings(query);
    }

    public List<Building> getAllFood(City city) {

        var bars = getSustenanceBuilding(city, "bar");
        var biergartens = getSustenanceBuilding(city, "biergarten");
        var fast_foods = getSustenanceBuilding(city, "fast_food");
        var food_courts = getSustenanceBuilding(city, "food_court");
        var ice_creams = getSustenanceBuilding(city, "ice_cream");
        var pubs = getSustenanceBuilding(city, "pub");
        var restaurant = getSustenanceBuilding(city, "restaurant");

        var food = new ArrayList<Building>(bars);
        food.addAll(biergartens);
        food.addAll(fast_foods);
        food.addAll(food_courts);
        food.addAll(ice_creams);
        food.addAll(pubs);
        food.addAll(restaurant);

        return food;
    }

    public List<Building> getTourismBuilding(City city, String key) {
        var boundaries = cityBoundaries.get(city);

        Query query = Query.builder()
                .timeout(100)
                .southernLat(boundaries[0])
                .northernLat(boundaries[1])
                .westernLon(boundaries[2])
                .easterLon(boundaries[3])
                .tags(List.of(String.format("tourism=%s", key)))
                .build();

        return overpassService.getBuildings(query);
    }

    public List<Building> getSustenanceBuilding(City city, String key) {
        var boundaries = cityBoundaries.get(city);

        Query query = Query.builder()
                .timeout(100)
                .southernLat(boundaries[0])
                .northernLat(boundaries[1])
                .westernLon(boundaries[2])
                .easterLon(boundaries[3])
                .tags(List.of(String.format("amenity=%s", key)))
                .build();

        return overpassService.getBuildings(query);
    }

    private void initBoundaries() {
        cityBoundaries = new HashMap<City, Double[]>();

        cityBoundaries.put(City.KRAKOW, new Double[]{49.967, 50.201, 19.734, 20.17});
        cityBoundaries.put(City.PRAGUE, new Double[]{49.945, 50.178, 14.224, 14.708});
    }
}
