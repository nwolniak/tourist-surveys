package pl.edu.agh.touristsurveys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;
import pl.edu.agh.touristsurveys.service.MapService;
import pl.edu.agh.touristsurveys.service.SurveyService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pl.edu.agh.touristsurveys.parser.TrajectoryParser.parseTrajectory;

@RestController
public class SurveyController {

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

    @GetMapping("/test")
    public int test() {
        return 1;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getSurveyResults")
    public List<SurveyDTO> getSurveyResults() {
        var surveyResults = new ArrayList<SurveyDTO>();

        surveyResults.add(new SurveyDTO("When did you come to Lisbon and what day of your stay is it today?", "14.05.2023"));
        surveyResults.add(new SurveyDTO("Are you staying in Lisbon? ", "Yes"));
        surveyResults.add(new SurveyDTO("In what type of accommodation in Lisbon do you stay, if any?", "Hotel"));
        surveyResults.add(new SurveyDTO("What means of transport did you use to get to Lisbon?", "Airplane"));
        return surveyResults;
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/surveyResults")
    public List<SurveyDTO> surveyResults(@RequestBody Map<String, String> requestBody) {
        var csv = requestBody.get("file");
        TrajectoryGraph trajectoryGraph = TrajectoryParser.parseAndMapToInternalModel(csv.getBytes());
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

        var surveyResults = new ArrayList<SurveyDTO>();
        surveyResults.add(new SurveyDTO("When did you come to Lisbon and what day of your stay is it today?", "14.05.2023"));
        surveyResults.add(new SurveyDTO("Are you staying in Lisbon? ", "Yes"));
        surveyResults.add(new SurveyDTO("In what type of accommodation in Lisbon do you stay, if any?", "Hotel"));
        surveyResults.add(new SurveyDTO("What means of transport did you use to get to Lisbon?", "Airplane"));
        return surveyResults;
    }

}
