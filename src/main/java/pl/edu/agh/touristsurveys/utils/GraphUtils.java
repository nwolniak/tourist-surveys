package pl.edu.agh.touristsurveys.utils;

import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryEdge;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class GraphUtils {

    /**
     * @param nodes list of trajectory nodes
     * @return list of trajectory edges
     */
    public static List<TrajectoryEdge> nodesToEdges(List<TrajectoryNode> nodes) {
        List<TrajectoryEdge> edges = new LinkedList<>();
        nodes.stream()
                .reduce((incomingNode, outgoingNode) -> {
                    TrajectoryEdge trajectoryEdge = new TrajectoryEdge(incomingNode, outgoingNode);
                    edges.add(trajectoryEdge);
                    return outgoingNode;
                });
        return edges;
    }

    /**
     * @param edges list of trajectory edges
     * @return list of trajectory nodes
     */
    public static List<TrajectoryNode> edgesToNodes(List<TrajectoryEdge> edges) {
        List<TrajectoryNode> nodes = new LinkedList<>();
        edges.stream()
                .reduce((edge1, edge2) -> {
                    nodes.add(edge1.getIncomingTrajectoryNode());
                    return edge2;
                })
                .ifPresent(lastEdge -> {
                    nodes.add(lastEdge.getIncomingTrajectoryNode());
                    nodes.add(lastEdge.getOutgoingTrajectoryNode());
                });
        return nodes;
    }

    /**
     * @param edges list of trajectory edges
     * @return subsequences of connected edges
     */
    public static List<List<TrajectoryEdge>> edgesToSubsequences(List<TrajectoryEdge> edges) {
        List<List<TrajectoryEdge>> subsequences = new LinkedList<>();
        ListIterator<TrajectoryEdge> edgeIterator = edges.listIterator();
        while (edgeIterator.hasNext()) {
            LinkedList<TrajectoryEdge> subsequence = new LinkedList<>();
            subsequence.add(edgeIterator.next());
            while (edgeIterator.hasNext()) {
                TrajectoryEdge nextEdge = edgeIterator.next();
                if (!subsequence.getLast().getOutgoingTrajectoryNode().equals(nextEdge.getIncomingTrajectoryNode())) {
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
