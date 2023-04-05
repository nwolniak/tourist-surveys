package pl.edu.agh.touristsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.model.Coordinates;
import pl.edu.agh.touristsurveys.service.SurveyService;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;
import pl.edu.agh.touristsurveys.service.OverpassService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TouristSurveysApplication implements ApplicationRunner {

    @Autowired
    private OverpassService overpassService;

    @Autowired
    private TrajectoryParser trajectoryParser;

    public static void main(String[] args) {
        SpringApplication.run(TouristSurveysApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<TrajectoryNode> trajectoryNodes = trajectoryParser.parseTrajectory();

        trajectoryNodes.stream()
                .limit(10)
                .forEach(System.out::println);

        System.out.println("\n\n-------------------------------------------\n\n");

        List<Coordinates> coordinates = new ArrayList<>();

        for (var tn : trajectoryNodes) {
            coordinates.add(new Coordinates(tn));
        }

        SurveyService surveyService = new SurveyService(overpassService);
        surveyService.setCity(City.PRAGUE);
        surveyService.setCoordinates(coordinates);
        surveyService.getAllCityData();
        surveyService.seekForPlaces(100); //seek for the places in distance of 100m for that desired point
        var poiList = surveyService.getPOIs();
        poiList.getMuseum().stream()
                .limit(10)
                .forEach(System.out::println);

        System.out.println("\n\n-------------------------------------------\n\n");

        poiList.getFood().stream()
                .limit(10)
                .forEach(System.out::println);

        System.out.println("\n\n-------------------------------------------\n\n");

        var coordinatePOIS = surveyService.getCoordinatesList();
        coordinatePOIS.stream()
                .limit(20)
                .map(coord -> coord.accommodation)
                .forEach(System.out::println);
    }
}
