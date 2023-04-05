package pl.edu.agh.touristsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;
import pl.edu.agh.touristsurveys.service.OverpassService;

import java.util.List;
import java.util.Map;

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
    public void run(ApplicationArguments args) {
        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("tourism", "museum"),
                50.0462364,
                50.0707757,
                19.9248071,
                19.9541481);
        overpassService.getBuildings(buildingQuery).stream()
                .limit(5)
                .forEach(System.out::println);

        List<TrajectoryNode> trajectoryNodes = trajectoryParser.parseTrajectory();
        trajectoryNodes.stream()
                .limit(20)
                .forEach(System.out::println);
    }
}
