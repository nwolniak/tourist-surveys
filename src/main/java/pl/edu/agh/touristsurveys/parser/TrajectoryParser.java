package pl.edu.agh.touristsurveys.parser;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.touristsurveys.model.mapping.CsvNode;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryEdge;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.utils.GraphUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
public class TrajectoryParser {

    public static List<CsvNode> parseTrajectory(byte[] byteArray) {
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(byteArray))) {
            return new CsvToBeanBuilder<CsvNode>(reader)
                    .withType(CsvNode.class)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TrajectoryGraph parseAndMapToInternalModel(byte[] byteArray) {
        List<TrajectoryNode> nodes = parseTrajectory(byteArray).stream()
                .map(csvNode -> new TrajectoryNode(csvNode.getTrajectoryId(), csvNode.getLon(), csvNode.getLat(), csvNode.getTimestamp()))
                .toList();

        return TrajectoryGraph.builder()
                .trajectoryNodes(nodes.stream()
                        .collect(Collectors.toMap(TrajectoryNode::getNodeId, Function.identity())))
                .trajectoryEdges(GraphUtils.nodesToEdges(nodes).stream()
                        .collect(Collectors.toMap(TrajectoryEdge::getEdgeId, Function.identity())))
                .nodesPerEachDay(nodes.stream()
                        .collect(groupingBy(
                                node -> node.getTimestamp().toLocalDate(),
                                TreeMap::new,
                                Collectors.toMap(TrajectoryNode::getNodeId, Function.identity()))))
                .build();
    }

}
