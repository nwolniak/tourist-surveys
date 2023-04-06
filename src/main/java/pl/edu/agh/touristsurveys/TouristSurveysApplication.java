package pl.edu.agh.touristsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;
import pl.edu.agh.touristsurveys.service.MapService;
import pl.edu.agh.touristsurveys.service.SurveyService;

import java.util.List;
import java.util.Map;

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
        List<Building> nearestBuildings = surveyService.filterNearestBuildings(trajectoryNodes, allBuildings, 100);

        System.out.println(String.format("============TRAJECTORIES[%s]=============", trajectoryNodes.size()));
        trajectoryNodes.stream()
                .limit(10)
                .forEach(System.out::println);

        System.out.println(String.format("============ALL_BUILDINGS[%s]=============", allBuildings.size()));
        allBuildings.stream()
                .limit(10)
                .forEach(System.out::println);

        System.out.println(String.format("============NEAREST_BUILDINGS[%s]=============", nearestBuildings.size()));
        nearestBuildings.stream()
                .limit(10)
                .forEach(System.out::println);
    }
}
