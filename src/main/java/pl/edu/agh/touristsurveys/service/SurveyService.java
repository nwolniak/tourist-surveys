package pl.edu.agh.touristsurveys.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryEdge;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.utils.SurveyUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    public List<Building> filterNearestBuildings(List<TrajectoryNode> trajectoryNodes, List<Building> allBuildings, double dist) {
        return allBuildings.stream()
                .filter(building -> checkDistanceFromTrajectory(building, trajectoryNodes, dist))
                .toList();
    }

    private static boolean checkDistanceFromTrajectory(Building building, List<TrajectoryNode> trajectoryNodes, double threshold) {
        return trajectoryNodes.stream()
                .map(trajectoryNode -> SurveyUtils.distance(trajectoryNode.getLat(), building.lat(), trajectoryNode.getLon(), building.lon()))
                .anyMatch(distance -> distance < threshold);
    }

    public List<Building> filterVisitedBuildings(List<TrajectoryNode> trajectoryNodes, List<Building> buildings) {
        List<TrajectoryEdge> trajectoryEdges = nodesToEdges(trajectoryNodes);

        List<TrajectoryEdge> userMovingSlowlyEdges = trajectoryEdges.stream()
                .filter(trajectoryEdge -> trajectoryEdge.getVelocity() < 0.5)
                .toList();

        List<List<TrajectoryEdge>> subsequences = edgesToSubsequences(userMovingSlowlyEdges);

        return subsequences.stream()
                .map(this::edgesToNodes)
                .map(subsequenceNodes -> getBestMatchingBuilding(subsequenceNodes, buildings))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<Building> getBestMatchingBuilding(List<TrajectoryNode> nodes, List<Building> buildings) {
        Map<Building, Long> buildingFrequencies = nodes.stream()
                .map(node -> getNearestBuilding(node, buildings))
                .flatMap(Optional::stream)
                .collect(Collectors.groupingBy(
                        building -> building,
                        Collectors.counting()
                ));
        return buildingFrequencies.entrySet()
                .stream()
                .max(Entry.comparingByValue())
                .map(Entry::getKey);
    }

    private Optional<Building> getNearestBuilding(TrajectoryNode node, List<Building> buildings) {
        return buildings.stream()
                .filter(building -> checkDistanceBetween(node, building, 100))
                .min(Comparator.comparing(building ->
                        SurveyUtils.distance(building.lat(), node.getLat(), building.lon(), node.getLon())));
    }

    private boolean checkDistanceBetween(TrajectoryNode node, Building building, double threshold) {
        double distance = SurveyUtils.distance(node.getLat(), building.lat(), node.getLon(), building.lon());
        return distance < threshold;
    }

    /**
     * @param nodes list of trajectory nodes
     * @return list of trajectory edges
     */
    public List<TrajectoryEdge> nodesToEdges(List<TrajectoryNode> nodes) {
        List<TrajectoryEdge> edges = new LinkedList<>();
        nodes.stream()
                .reduce((outgoingNode, incomingNode) -> {
                    TrajectoryEdge trajectoryEdge = new TrajectoryEdge(outgoingNode, incomingNode);
                    edges.add(trajectoryEdge);
                    return incomingNode;
                });
        return edges;
    }

    /**
     * @param edges list of trajectory edges
     * @return list of trajectory nodes
     */
    public List<TrajectoryNode> edgesToNodes(List<TrajectoryEdge> edges) {
        List<TrajectoryNode> nodes = new LinkedList<>();
        edges.stream()
                .reduce((edge1, edge2) -> {
                    nodes.add(edge1.getOutgoingTrajectoryNode());
                    return edge2;
                })
                .ifPresent(lastEdge -> {
                    nodes.add(lastEdge.getOutgoingTrajectoryNode());
                    nodes.add(lastEdge.getIncomingTrajectoryNode());
                });
        return nodes;
    }

    /**
     * @param edges list of trajectory edges
     * @return subsequences of connected edges
     */
    private List<List<TrajectoryEdge>> edgesToSubsequences(List<TrajectoryEdge> edges) {
        List<List<TrajectoryEdge>> subsequences = new LinkedList<>();
        ListIterator<TrajectoryEdge> edgeIterator = edges.listIterator();
        while (edgeIterator.hasNext()) {
            LinkedList<TrajectoryEdge> subsequence = new LinkedList<>();
            subsequence.add(edgeIterator.next());
            while (edgeIterator.hasNext()) {
                TrajectoryEdge nextEdge = edgeIterator.next();
                if (!subsequence.getLast()
                        .getIncomingTrajectoryNode()
                        .equals(nextEdge.getOutgoingTrajectoryNode())) {
                    edgeIterator.previous();
                    break;
                }
                subsequence.add(nextEdge);
            }
            subsequences.add(subsequence);
        }
        return subsequences;
    }

}
