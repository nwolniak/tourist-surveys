package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.requests.Query;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparingDouble;

@Service
@RequiredArgsConstructor
public class MapService {

    private final OverpassService overpassService;

    public List<Building> getAllBuildings(Map<String, TrajectoryNode> nodes, List<String> tags) {
        Query query = Query.builder()
                .timeout(100)
                .southernLat(getBottomBound(nodes) - 0.001)
                .northernLat(getTopBound(nodes) + 0.001)
                .westernLon(getLeftBound(nodes) - 0.001)
                .easterLon(getRightBound(nodes) + 0.001)
                .tags(tags)
                .build();

        return overpassService.getBuildings(query);
    }

    private static double getTopBound(Map<String, TrajectoryNode> nodes) {
        return nodes.values().stream()
                .map(TrajectoryNode::getLat)
                .max(comparingDouble(value -> value))
                .orElseThrow();
    }

    private static double getLeftBound(Map<String, TrajectoryNode> nodes) {
        return nodes.values().stream()
                .map(TrajectoryNode::getLon)
                .min(comparingDouble(value -> value))
                .orElseThrow();
    }

    private static double getBottomBound(Map<String, TrajectoryNode> nodes) {
        return nodes.values().stream()
                .map(TrajectoryNode::getLat)
                .min(comparingDouble(value -> value))
                .orElseThrow();
    }

    private static double getRightBound(Map<String, TrajectoryNode> nodes) {
        return nodes.values().stream()
                .map(TrajectoryNode::getLon)
                .max(comparingDouble(value -> value))
                .orElseThrow();
    }

}
