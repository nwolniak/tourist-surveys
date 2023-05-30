package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SurveyService {

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

    private final BuildingsService buildingsService;
    private final MapService mapService;

    public List<SurveyDTO> createTouristSurvey(TrajectoryGraph trajectoryGraph) {
        Map<String, TrajectoryNode> nodes = trajectoryGraph.trajectoryNodes();
        List<Building> allBuildings = mapService.getAllBuildings(nodes, searchTags);
        List<Building> nearestBuildings = buildingsService.filterNearestBuildings(nodes, allBuildings, 100);
        List<Building> visitedBuildings = buildingsService.filterVisitedBuildings(trajectoryGraph, nearestBuildings, 50);
        List<Building> sleepingBuildings = buildingsService.filterSleepingBuildings(trajectoryGraph, nearestBuildings, 50);
        Map<String, Long> meansOfTransport = buildingsService.getMeansOfTransport(trajectoryGraph, nearestBuildings, 10);
        String arrivalMeanOfTransport = buildingsService.getArrivalMeanOfTransport(trajectoryGraph, nearestBuildings, 10);
        String departureMeanOfTransport = buildingsService.getDepartureMeanOfTransport(trajectoryGraph, nearestBuildings, 10);

        var surveyResults = new ArrayList<SurveyDTO>();
        surveyResults.add(new SurveyDTO("When did you come to Lisbon and what day of your stay is it today?", "14.05.2023"));
        surveyResults.add(new SurveyDTO("Are you staying in Lisbon? ", "Yes"));
        surveyResults.add(new SurveyDTO("In what type of accommodation in Lisbon do you stay, if any?", "Hotel"));
        surveyResults.add(new SurveyDTO("What means of transport did you use to get to Lisbon?", "Airplane"));
        return surveyResults;
    }

}
