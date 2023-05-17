package pl.edu.agh.touristsurveys.model.trajectory;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Map;
import java.util.SortedMap;

@Builder
public record TrajectoryGraph(
        Map<String, TrajectoryNode> trajectoryNodes,
        Map<String, TrajectoryEdge> trajectoryEdges,
        SortedMap<LocalDate, Map<String, TrajectoryNode>> nodesPerEachDay) {
}
