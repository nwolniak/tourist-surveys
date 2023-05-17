package pl.edu.agh.touristsurveys.service;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryEdge;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.utils.CalculusUtils;
import pl.edu.agh.touristsurveys.utils.GraphUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

    public List<Building> filterVisitedBuildings(TrajectoryGraph trajectoryGraph, List<Building> buildings, int threshold) {
        List<TrajectoryEdge> userMovingSlowlyEdges = trajectoryGraph.trajectoryEdges()
                .values()
                .stream()
                .filter(trajectoryEdge -> trajectoryEdge.getVelocity() < 0.5)
                .toList();

        List<List<TrajectoryEdge>> subsequences = GraphUtils.edgesToSubsequences(userMovingSlowlyEdges);

        return subsequences.stream()
                .map(GraphUtils::edgesToNodes)
                .map(subsequenceNodes -> getBestMatchingBuilding(subsequenceNodes, buildings, threshold))
                .flatMap(Optional::stream)
                .toList();
    }

    public List<Building> filterSleepingBuildings(TrajectoryGraph trajectoryGraph, List<Building> buildings, int threshold) {
        List<Building> sleepingBuildings = new LinkedList<>();

        trajectoryGraph.nodesPerEachDay().values()
                .stream()
                .findFirst()
                .ifPresent(firstDayNodes -> {
                    List<TrajectoryNode> morningNodes = getMorningNodes(firstDayNodes);
                    Optional<Building> morningBuilding = getBestMatchingBuilding(morningNodes, buildings, threshold);
                    morningBuilding.ifPresent(sleepingBuildings::add);
                });

        trajectoryGraph.nodesPerEachDay().values()
                .stream()
                .reduce((nodesPerDay1, nodesPerDay2) -> {
                    List<TrajectoryNode> nightNodes = ListUtils.sum(getEveningNodes(nodesPerDay1), getMorningNodes(nodesPerDay2));
                    Optional<Building> nightBuilding = getBestMatchingBuilding(nightNodes, buildings, threshold);
                    nightBuilding.ifPresent(sleepingBuildings::add);
                    return nodesPerDay2;
                })
                .ifPresent(lastDayNodes -> {
                    List<TrajectoryNode> eveningNodes = getEveningNodes(lastDayNodes);
                    Optional<Building> eveningBuilding = getBestMatchingBuilding(eveningNodes, buildings, threshold);
                    eveningBuilding.ifPresent(sleepingBuildings::add);
                });

        return sleepingBuildings;
    }

    private List<TrajectoryNode> getMorningNodes(Map<String, TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.values().stream()
                .filter(node -> node.getTimestamp().getHour() < 8)
                .toList();
    }

    private List<TrajectoryNode> getEveningNodes(Map<String, TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.values().stream()
                .filter(node -> node.getTimestamp().getHour() > 20)
                .toList();
    }

    private Optional<Building> getBestMatchingBuilding(List<TrajectoryNode> nodes, List<Building> buildings, int threshold) {
        return nodes.stream()
                .map(node -> getNearestBuilding(node, buildings, threshold))
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

    private Optional<Building> getNearestBuilding(TrajectoryNode node, List<Building> buildings, int threshold) {
        return buildings.stream()
                .filter(building -> checkDistanceBetween(node, building, threshold))
                .min(Comparator.comparing(building ->
                        CalculusUtils.distance(building.lat(), node.getLat(), building.lon(), node.getLon())));
    }

    private boolean checkDistanceBetween(TrajectoryNode node, Building building, double threshold) {
        double distance = CalculusUtils.distance(node.getLat(), building.lat(), node.getLon(), building.lon());
        return distance < threshold;
    }

    private List<Building> getMeansOfTransportSorted(TrajectoryGraph trajectoryGraph, List<Building> buildings, int threshold) {
        List<Building> publicTransportNodes = buildings.stream()
                .filter(building -> building.type().equals("node"))
                .filter(building -> building.tags().containsKey("public_transport"))
                .toList();

        return publicTransportNodes.stream()
                .filter(publicTransportNode -> checkDistanceFromTrajectory(publicTransportNode, trajectoryGraph.trajectoryNodes(), threshold))
                .sorted(Comparator.comparingInt(node -> GraphUtils.buildingPositionOnTrajectory(trajectoryGraph.trajectoryNodes(), node)))
                .toList();
    }

    public String getArrivalMeanOfTransport(TrajectoryGraph trajectoryGraph, List<Building> buildings, int threshold) {
        return getMeansOfTransportSorted(trajectoryGraph, buildings, threshold)
                .stream()
                .findFirst()
                .filter(building -> {
                    String nodeId = GraphUtils.buildingCorrespondingTrajectoryNodeIdOnTrajectory(trajectoryGraph.trajectoryNodes(), building);
                    return trajectoryGraph.nodesPerEachDay()
                            .values()
                            .stream()
                            .findFirst()
                            .filter(firstDayNodes -> firstDayNodes.containsKey(nodeId))
                            .isPresent();
                })
                .flatMap(this::getBuildingPublicTransportType)
                .orElse(null);
    }

    public String getDepartureMeanOfTransport(TrajectoryGraph trajectoryGraph, List<Building> buildings, int threshold) {
        List<Building> publicTransportNodesSorted = getMeansOfTransportSorted(trajectoryGraph, buildings, threshold);
        return publicTransportNodesSorted
                .stream()
                .skip(publicTransportNodesSorted.size() - 1)
                .findFirst()
                .filter(building -> {
                    String nodeId = GraphUtils.buildingCorrespondingTrajectoryNodeIdOnTrajectory(trajectoryGraph.trajectoryNodes(), building);
                    SortedMap<LocalDate, Map<String, TrajectoryNode>> nodesPerEachDay = trajectoryGraph.nodesPerEachDay();
                    return nodesPerEachDay
                            .values()
                            .stream()
                            .skip(nodesPerEachDay.size() - 1)
                            .findFirst()
                            .filter(lastDayNodes -> lastDayNodes.containsKey(nodeId))
                            .isPresent();
                })
                .flatMap(this::getBuildingPublicTransportType)
                .orElse(null);
    }

    public Map<String, Long> getMeansOfTransport(TrajectoryGraph trajectoryGraph, List<Building> buildings, int threshold) {
        List<Building> publicTransportNodesSorted = getMeansOfTransportSorted(trajectoryGraph, buildings, threshold);

        List<List<Building>> subsequences = new LinkedList<>();
        ListIterator<Building> it = publicTransportNodesSorted.listIterator();
        boolean firstItem = true;
        LinkedList<Building> subsequence;
        while (it.hasNext()) {
            subsequence = new LinkedList<>();
            Building currentPublicTransport = it.next();
            while (it.hasNext()) {
                Building nextPublicTransport = it.next();

                Building lastPublicTransport;
                if (subsequence.isEmpty()) {
                    lastPublicTransport = currentPublicTransport;
                } else {
                    lastPublicTransport = subsequence.getLast();
                }
                Optional<String> lastPublicTransportType = getBuildingPublicTransportType(lastPublicTransport);
                Optional<String> nextPublicTransportType = getBuildingPublicTransportType(nextPublicTransport);

                if (lastPublicTransportType.isEmpty()
                        || nextPublicTransportType.isEmpty()
                        || ObjectUtils.notEqual(lastPublicTransportType.get(), nextPublicTransportType.get())) {
                    it.previous();
                    firstItem = true;
                    break;
                }
                if (firstItem) {
                    subsequence.add(currentPublicTransport);
                }
                subsequence.add(nextPublicTransport);
                firstItem = false;
            }
            if (!subsequence.isEmpty()) {
                subsequences.add(subsequence);
            }
        }
        return subsequences.stream()
                .collect(Collectors.groupingBy(
                        subseq -> subseq.stream().findFirst()
                                .flatMap(this::getBuildingPublicTransportType)
                                .orElseThrow(),
                        Collectors.counting()));
    }

    private Optional<String> getBuildingPublicTransportType(Building building) {
        return building.tags()
                .entrySet().stream()
                .filter(entry -> StringUtils.equalsAny(entry.getKey(), "bus", "tram", "railway"))
                .map(Entry::getKey)
                .findFirst();
    }

}
