package pl.edu.agh.touristsurveys.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.util.List;

@Service
public class SurveyService {

    public List<Building> filterNearestBuildings(List<TrajectoryNode> trajectoryNodes, List<Building> allBuildings, double dist) {
        return allBuildings.stream()
                .filter(building -> checkDistanceFromTrajectory(building, trajectoryNodes, dist))
                .toList();
    }

    private static boolean checkDistanceFromTrajectory(Building building, List<TrajectoryNode> trajectoryNodes, double threshold) {
        return trajectoryNodes.stream()
                .map(trajectoryNode -> distance(trajectoryNode.getLat(), building.lat(), trajectoryNode.getLon(), building.lon()))
                .anyMatch(distance -> distance < threshold);
    }

    private static double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

}
