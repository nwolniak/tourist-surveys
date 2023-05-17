package pl.edu.agh.touristsurveys.service;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryEdge;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.utils.CalculusUtils;
import pl.edu.agh.touristsurveys.utils.GraphUtils;

import java.util.*;
import java.util.Map.Entry;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.*;

@Service
public class SurveyService {

    public List<Building> filterNearestBuildings(Map<String, TrajectoryNode> nodes, List<Building> allBuildings, double dist) {
        return allBuildings.stream()
                .filter(building -> checkDistanceFromTrajectory(building, nodes, dist))
                .toList();
    }

    private boolean checkDistanceFromTrajectory(Building building, Map<String, TrajectoryNode> nodes, double threshold) {
        return nodes.values().stream()
                .anyMatch(node -> checkDistanceBetween(node, building, threshold));
    }

    public List<Building> filterVisitedBuildings(TrajectoryGraph trajectoryGraph, List<Building> buildings) {
        List<TrajectoryEdge> userMovingSlowlyEdges = trajectoryGraph.trajectoryEdges()
                .values()
                .stream()
                .filter(trajectoryEdge -> trajectoryEdge.getVelocity() < 0.5)
                .toList();

        List<List<TrajectoryEdge>> subsequences = GraphUtils.edgesToSubsequences(userMovingSlowlyEdges);

        return subsequences.stream()
                .map(GraphUtils::edgesToNodes)
                .map(subsequenceNodes -> getBestMatchingBuilding(subsequenceNodes, buildings))
                .flatMap(Optional::stream)
                .toList();
    }

    public List<Building> filterSleepingBuildings(TrajectoryGraph trajectoryGraph, List<Building> buildings) {
        List<Building> sleepingBuildings = new LinkedList<>();

        trajectoryGraph.nodesPerEachDay().values()
                .stream()
                .findFirst()
                .ifPresent(firstDayNodes -> {
                    List<TrajectoryNode> morningNodes = getMorningNodes(firstDayNodes);
                    Optional<Building> morningBuilding = getBestMatchingBuilding(morningNodes, buildings);
                    morningBuilding.ifPresent(sleepingBuildings::add);
                });

        trajectoryGraph.nodesPerEachDay().values()
                .stream()
                .reduce((nodesPerDay1, nodesPerDay2) -> {
                    List<TrajectoryNode> nightNodes = ListUtils.sum(getEveningNodes(nodesPerDay1), getMorningNodes(nodesPerDay2));
                    Optional<Building> nightBuilding = getBestMatchingBuilding(nightNodes, buildings);
                    nightBuilding.ifPresent(sleepingBuildings::add);
                    return nodesPerDay2;
                })
                .ifPresent(lastDayNodes -> {
                    List<TrajectoryNode> eveningNodes = getEveningNodes(lastDayNodes);
                    Optional<Building> eveningBuilding = getBestMatchingBuilding(eveningNodes, buildings);
                    eveningBuilding.ifPresent(sleepingBuildings::add);
                });

        return sleepingBuildings;
    }

    private List<TrajectoryNode> getMorningNodes(List<TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.stream()
                .filter(node -> node.getTimestamp().getHour() < 8)
                .toList();
    }

    private List<TrajectoryNode> getEveningNodes(List<TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.stream()
                .filter(node -> node.getTimestamp().getHour() > 20)
                .toList();
    }

    private Optional<Building> getBestMatchingBuilding(List<TrajectoryNode> nodes, List<Building> buildings) {
        return nodes.stream()
                .map(node -> getNearestBuilding(node, buildings))
                .flatMap(Optional::stream)
                .collect(collectingAndThen(groupingBy(
                                building -> building,
                                counting()
                        ),
                        buildingFrequencies -> buildingFrequencies.entrySet()
                                .stream()
                                .max(comparingByValue())
                                .map(Entry::getKey)));
    }

    private Optional<Building> getNearestBuilding(TrajectoryNode node, List<Building> buildings) {
        return buildings.stream()
                .filter(building -> checkDistanceBetween(node, building, 100))
                .min(Comparator.comparing(building ->
                        CalculusUtils.distance(building.lat(), node.getLat(), building.lon(), node.getLon())));
    }

    private boolean checkDistanceBetween(TrajectoryNode node, Building building, double threshold) {
        double distance = CalculusUtils.distance(node.getLat(), building.lat(), node.getLon(), building.lon());
        return distance < threshold;
    }

}
