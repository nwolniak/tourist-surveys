package pl.edu.agh.touristsurveys.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryEdge;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.utils.GraphUtils;

import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
public class TrajectoryMapper {

    public TrajectoryGraph parseAndMapToInternalModel(byte[] byteArray, String type) {
        List<TrajectoryNode> nodes = ParserStrategy
                .valueOf(type)
                .parseTrajectory(byteArray);

        return TrajectoryGraph.builder()
                .trajectoryNodes(nodes.stream()
                        .collect(Collectors.toMap(TrajectoryNode::getNodeId, Function.identity(), (o1, o2) -> o1, TreeMap::new)))
                .trajectoryEdges(GraphUtils.nodesToEdges(nodes).stream()
                        .collect(Collectors.toMap(TrajectoryEdge::getEdgeId, Function.identity(), (o1, o2) -> o1, TreeMap::new)))
                .nodesPerEachDay(nodes.stream()
                        .collect(groupingBy(
                                node -> node.getTimestamp().toLocalDate(),
                                TreeMap::new,
                                Collectors.toMap(TrajectoryNode::getNodeId, Function.identity(), (o1, o2) -> o1, TreeMap::new))))
                .build();
    }

}
