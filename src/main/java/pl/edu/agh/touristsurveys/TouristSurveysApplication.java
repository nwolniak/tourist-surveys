package pl.edu.agh.touristsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;
import pl.edu.agh.touristsurveys.service.MapService;
import pl.edu.agh.touristsurveys.service.SurveyService;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TouristSurveysApplication implements ApplicationRunner {

    private static final List<String> searchTags = List.of(
            "museum",
            "tourism=hotel",
            "tourism=hostel",
            "tourism=motel",
            "tourism=guest_house",
            "public_transport=station",
            "amenity=bar",
            "amenity=biergarten",
            "amenity=fast_food",
            "amenity=food_court",
            "amenity=ice_cream",
            "amenity=pub",
            "amenity=restaurant",
            "public_transport"
    );

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private MapService mapService;

    @Autowired
    private TrajectoryParser trajectoryParser;

    public static void main(String[] args) {
        SpringApplication.run(TouristSurveysApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        TrajectoryGraph trajectoryGraph = trajectoryParser.parseAndMapToInternalModel();
        Map<String, TrajectoryNode> trajectoryNodes = trajectoryGraph.trajectoryNodes();

        System.out.println(String.format("============TRAJECTORIES[%s]=============", trajectoryNodes.size()));
        trajectoryNodes.values().stream()
                .limit(10)
                .forEach(System.out::println);

        List<Building> allBuildings = mapService.getAllBuildings(trajectoryNodes, searchTags);
        System.out.println(String.format("============ALL_BUILDINGS[%s]=============", allBuildings.size()));
        allBuildings.stream()
                .limit(10)
                .forEach(System.out::println);

        List<Building> nearestBuildings = surveyService.filterNearestBuildings(trajectoryNodes, allBuildings, 100);
        System.out.println(String.format("============NEAREST_BUILDINGS[%s]=============", nearestBuildings.size()));
        nearestBuildings.stream()
                .limit(10)
                .forEach(System.out::println);

        List<Building> visitedBuildings = surveyService.filterVisitedBuildings(trajectoryGraph, nearestBuildings, 50);
        System.out.println(String.format("============VISITED_BUILDINGS[%s]=============", visitedBuildings.size()));
        visitedBuildings.stream()
                .limit(10)
                .forEach(System.out::println);

        List<Building> sleepingBuildings = surveyService.filterSleepingBuildings(trajectoryGraph, nearestBuildings, 50);
        System.out.println(String.format("============SLEEPING_BUILDINGS[%s]=============", sleepingBuildings.size()));
        sleepingBuildings.stream()
                .limit(10)
                .forEach(System.out::println);

        Map<String, Long> meansOfTransport = surveyService.getMeansOfTransport(trajectoryGraph, nearestBuildings, 10);
        System.out.println(String.format("============MEANS_OF_TRANSPORT[%s]=============", meansOfTransport.size()));
        meansOfTransport.
                forEach((k, v) -> System.out.println(k + " -> " + v));

        String arrivalMeanOfTransport = surveyService.getArrivalMeanOfTransport(trajectoryGraph, nearestBuildings, 10);
        System.out.println("============ARRIVAL_MEAN_OF_TRANSPORT=============");
        System.out.println(arrivalMeanOfTransport);

        String departureMeanOfTransport = surveyService.getDepartureMeanOfTransport(trajectoryGraph, nearestBuildings, 10);
        System.out.println("============DEPARTURE_MEAN_OF_TRANSPORT=============");
        System.out.println(departureMeanOfTransport);
    }
}
