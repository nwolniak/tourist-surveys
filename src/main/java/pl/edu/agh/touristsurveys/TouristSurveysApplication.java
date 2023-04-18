package pl.edu.agh.touristsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.model.Landmark;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;
import pl.edu.agh.touristsurveys.service.MapService;
import pl.edu.agh.touristsurveys.service.SurveyService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class TouristSurveysApplication implements ApplicationRunner {

    private static final Map<City, Double[]> cityMap = Map.of(
            City.KRAKOW, new Double[]{49.967, 50.201, 19.734, 20.17},
            City.PRAGUE, new Double[]{49.945, 50.178, 14.224, 14.708}
    );

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
            "amenity=restaurant"
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
        List<TrajectoryNode> trajectoryNodes = trajectoryParser.parseTrajectory();
        List<Building> allBuildings = mapService.getAllBuildings(cityMap.get(City.PRAGUE), searchTags);
        List<Landmark> landmarksWithVisitHistory = surveyService.getVisitHistory(trajectoryNodes, allBuildings, 5);

        System.out.println(String.format("\n============TRAJECTORIES[%s]=============", trajectoryNodes.size()));
        trajectoryNodes.stream()
                .limit(10)
                .forEach(System.out::println);

        System.out.println(String.format("\n============ALL_BUILDINGS[%s]=============", allBuildings.size()));
        allBuildings.stream()
                .filter(x -> !x.type().equals("node"))
                .limit(10)
                .forEach(System.out::println);

        System.out.println(String.format("\n============LANDMARKS_WITH_TIME_SPEND=============", landmarksWithVisitHistory.size()));

        landmarksWithVisitHistory
                .forEach(Landmark::calculateTimeSpend);

        landmarksWithVisitHistory.stream()
                .sorted(Comparator.comparing(Landmark::getTimeSpend).reversed())
                .limit(10)
                .forEach(System.out::println);

        Optional<Landmark> earliestLandmark = landmarksWithVisitHistory.stream()
                .filter(landmark -> landmark.getTags().containsKey("public_transport"))
                .min(Comparator.comparing(Landmark::getEnterTime));

        System.out.println("\n============HOW_DID_HE_GET_TO_THE_CITY============="); //TODO: MAKE CLASS FOR ALL OF THAT FUNCTIONS
        earliestLandmark.ifPresent(System.out::println);

    }
}
