package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.requests.Query;

import java.util.List;

import static java.util.Comparator.comparingDouble;

@Service
@RequiredArgsConstructor
public class MapService {

    private final OverpassService overpassService;

    public List<Building> getAllBuildings(List<TrajectoryNode> trajectoryNodes, List<String> tags) {
        Query query = Query.builder()
                .timeout(100)
                .southernLat(getBottomBound(trajectoryNodes) - 0.001)
                .northernLat(getTopBound(trajectoryNodes) + 0.001)
                .westernLon(getLeftBound(trajectoryNodes) - 0.001)
                .easterLon(getRightBound(trajectoryNodes) + 0.001)
                .tags(tags)
                .build();

        return overpassService.getBuildings(query);
    }

    public double getTopBound(List<TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.stream()
                .map(TrajectoryNode::getLat)
                .max(comparingDouble(value -> value))
                .orElseThrow();
    }

    public double getLeftBound(List<TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.stream()
                .map(TrajectoryNode::getLon)
                .min(comparingDouble(value -> value))
                .orElseThrow();
    }

    public double getBottomBound(List<TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.stream()
                .map(TrajectoryNode::getLat)
                .min(comparingDouble(value -> value))
                .orElseThrow();
    }

    public double getRightBound(List<TrajectoryNode> trajectoryNodes) {
        return trajectoryNodes.stream()
                .map(TrajectoryNode::getLon)
                .max(comparingDouble(value -> value))
                .orElseThrow();
    }

}
