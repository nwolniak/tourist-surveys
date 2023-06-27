package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private static final List<String> searchTags = List.of(
            "museum",
            "tourism",
/*            "tourism=hotel",
            "tourism=hostel",
            "tourism=motel",
            "tourism=guest_house",*/
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

        Building favouriteRestaurant = buildingsService.getFavouriteRestaurant(trajectoryGraph, nearestBuildings, 50);
        Building mostCommonAttraction = buildingsService.getMostCommonAttraction(trajectoryGraph, allBuildings, 150);
        String arrivalMeanOfTransport = buildingsService.getArrivalMeanOfTransport(trajectoryGraph, nearestBuildings, 20);
        String departureMeanOfTransport = buildingsService.getDepartureMeanOfTransport(trajectoryGraph, nearestBuildings, 20);

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

        Map.Entry<String, String> mainCity = null;
        Iterator<Map.Entry<String, String>> iterator = citiesTimeSpent.entrySet().iterator();
        if (iterator.hasNext()) {
            mainCity = iterator.next();

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
            surveyResults.add(new SurveyDTO(
                    String.format("What kind of attraction in %s have you spent the most time on?", mainCity.getKey()),
                    mostCommonAttraction.tags().getOrDefault("tourism", "N/A")));
            surveyResults.add(new SurveyDTO(
                    String.format("What is the name of the attraction in %s you spent the most time on?", mainCity.getKey()),
                    mostCommonAttraction.tags().getOrDefault("name", "N/A")));
            surveyResults.add(new SurveyDTO(
                    String.format("What is the name of the restaurant in %s where you spent the most time?", mainCity.getKey()),
                    favouriteRestaurant.tags().getOrDefault("name", "N/A")));
            surveyResults.add(new SurveyDTO(
                    String.format("What is the type of the restaurant in %s where you have spent the most time?", mainCity.getKey()),
                    favouriteRestaurant.tags().getOrDefault("amenity", "N/A")));
            surveyResults.add(new SurveyDTO(
                    String.format("What cuisine is served in the restaurant in %s where you spent the most time?", mainCity.getKey()),
                    favouriteRestaurant.tags().getOrDefault("cuisine", "N/A")));
        } else {
            System.out.println("City not found");
        }

        return surveyResults;
    }

}
