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

    private void initBoundaries() {
        cityBoundaries = new HashMap<City, Double[]>();

        cityBoundaries.put(City.KRAKOW, new Double[]{50.0462364, 50.0707757, 19.9248071, 19.954148});
        cityBoundaries.put(City.KYIV, new Double[]{50.0462364, 50.0707757, 19.9248071, 19.954148});
    }
}
