package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            "public_transport",
            "place=city"
    );

    private final BuildingsService buildingsService;
    private final MapService mapService;

    public List<SurveyDTO> createTouristSurvey(TrajectoryGraph trajectoryGraph) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        Map<String, TrajectoryNode> nodes = trajectoryGraph.trajectoryNodes();
        List<Building> allBuildings = mapService.getAllBuildings(nodes, searchTags);

        List<Building> nearestBuildings = buildingsService.filterNearestBuildings(nodes, allBuildings, 100);
        List<Building> visitedBuildings = buildingsService.filterVisitedBuildings(trajectoryGraph, nearestBuildings, 50);
        List<Building> sleepingBuildings = buildingsService.filterSleepingBuildings(trajectoryGraph, nearestBuildings, 50);
        Map<String, Long> meansOfTransport = buildingsService.getMeansOfTransport(trajectoryGraph, nearestBuildings, 10);
        String arrivalMeanOfTransport = buildingsService.getArrivalMeanOfTransport(trajectoryGraph, nearestBuildings, 10);
        String departureMeanOfTransport = buildingsService.getDepartureMeanOfTransport(trajectoryGraph, nearestBuildings, 10);

        Map<String, LocalDateTime> citiesFirstVisitInOrder = buildingsService.getCitiesFirstVisitInOrder(trajectoryGraph, nearestBuildings, 100);
        Map<String, LocalDateTime> citiesLastVisitInOrder = buildingsService.getCitiesLastVisitInOrder(trajectoryGraph, nearestBuildings, 100);
        Map<String, String> citiesTimeSpent = buildingsService.getCitiesTimeSpentIn(citiesFirstVisitInOrder, citiesLastVisitInOrder);

        var surveyResults = new ArrayList<SurveyDTO>();

        citiesTimeSpent.forEach((key, value) -> {
            surveyResults.add(new SurveyDTO(
                    String.format("When did you come to %s?", key),
                    citiesFirstVisitInOrder.get(key).format(formatter)));
            surveyResults.add(new SurveyDTO(
                    String.format("When did you leave the city of %s?", key),
                    citiesLastVisitInOrder.get(key).format(formatter)));
            surveyResults.add(new SurveyDTO(
                    String.format("How long have you been in %s?", key),
                    value));
        });

        Map.Entry<String, String> mainCity = citiesTimeSpent.entrySet().iterator().next();

        surveyResults.add(new SurveyDTO(
                String.format("Are you staying in %s? ", mainCity.getKey()),
                mainCity.getValue().contains("0 days") ? "No" : "Yes"));
        surveyResults.add(new SurveyDTO(
                String.format("In what type of accommodation in %s do you stay, if any?", mainCity.getKey()),
                mainCity.getValue().contains("0 days") ? "N/A" : sleepingBuildings.get(0).tags().get("tourism")));
        surveyResults.add(new SurveyDTO(
                String.format("What is the name of the accommodation in %s where you stayed, if any?", mainCity.getKey()),
                mainCity.getValue().contains("0 days") ? "N/A" : sleepingBuildings.get(0).tags().get("name")));
        surveyResults.add(new SurveyDTO(
                String.format("What means of transport did you use to get to %s?", mainCity.getKey()),
                arrivalMeanOfTransport != null ? arrivalMeanOfTransport : "car"));

        return surveyResults;
    }

}
