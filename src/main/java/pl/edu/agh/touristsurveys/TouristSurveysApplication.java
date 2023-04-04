package pl.edu.agh.touristsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.model.Coordinates;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;
import pl.edu.agh.touristsurveys.service.BtsService;
import pl.edu.agh.touristsurveys.service.OverpassService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TouristSurveysApplication implements ApplicationRunner {

    @Autowired
    private OverpassService overpassService;

    public static void main(String[] args) {
        SpringApplication.run(TouristSurveysApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
//                Map.of("tourism", "museum"),
//                50.0462364,
//                50.0707757,
//                19.9248071,
//                19.9541481);
//        overpassService.getBuildings(buildingQuery);
//
//        BuildingQuery buildingQuery2 = BuildingQuery.singleTagList(
//                List.of("museum"),
//                50.0462364,
//                50.0707757,
//                19.9248071,
//                19.9541481);
//        overpassService.getBuildings(buildingQuery2);

        var c1 = new Coordinates(19.9399170, 50.0601301);
        var c2 = new Coordinates(19.9999170, 50.0601301);
        var c3 = new Coordinates(19.9415137, 50.0554747);
        var c4 = new Coordinates(1, 50.0554747);

        var coordinates = new ArrayList<Coordinates>();
        coordinates.add(c1);
        coordinates.add(c2);
        coordinates.add(c3);
        coordinates.add(c4);

        BtsService btsService = new BtsService(overpassService, coordinates);
        btsService.setCity(City.KRAKOW);
        btsService.getAllCityData();
        btsService.seekForMuseums(10);

    }
}
