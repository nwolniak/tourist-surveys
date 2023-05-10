package pl.edu.agh.touristsurveys.parser;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.touristsurveys.model.mapping.CsvNode;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryEdge;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.utils.GraphUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
public class TrajectoryParser {

    private final String trajectoryPath;

    public List<CsvNode> parseTrajectory() {
        try (Reader reader = Files.newBufferedReader(Path.of(trajectoryPath))) {
            return new CsvToBeanBuilder<CsvNode>(reader)
                    .withType(CsvNode.class)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TrajectoryGraph parseAndMapToInternalModel() {
        List<TrajectoryNode> nodes = parseTrajectory().stream()
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
                                Collectors.toList())))
                .build();
    }

}
