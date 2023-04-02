package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private OverpassService overpassService;
    private double southernLat;
    private double northernLat;
    private double westernLon;
    private double easternLon;

    public BuildingService(double latitude, double longitude, double range, OverpassService overpassService) {
        westernLon = longitude - range / 2;
        easternLon = longitude + range / 2;
        southernLat = latitude - range / 2;
        northernLat = latitude + range / 2;
        this.overpassService = overpassService;
    }

    public List<Building> getMuseums() {
        BuildingQuery buildingQuery = BuildingQuery.singleTagList(
                List.of("museum"),
                southernLat,
                northernLat,
                westernLon,
                easternLon);
        return overpassService.getBuildings(buildingQuery);
    }

    public List<Building> getHotels() {
        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("tourism", "hotel"),
                southernLat,
                northernLat,
                westernLon,
                easternLon);
        return overpassService.getBuildings(buildingQuery);
    }


}
