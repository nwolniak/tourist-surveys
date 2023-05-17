package pl.edu.agh.touristsurveys.model.trajectory;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import pl.edu.agh.touristsurveys.utils.CalculusUtils;

@Getter
public class TrajectoryEdge {

    private final String edgeId;
    private final TrajectoryNode incomingTrajectoryNode;
    private final TrajectoryNode outgoingTrajectoryNode;

    private final double length;
    private final double timeDifference;
    private final double velocity;

    public TrajectoryEdge(TrajectoryNode incomingTrajectoryNode, TrajectoryNode outgoingTrajectoryNode) {
        this.edgeId = outgoingTrajectoryNode.getNodeId() + "->" + incomingTrajectoryNode.getNodeId();
        this.incomingTrajectoryNode = incomingTrajectoryNode;
        this.outgoingTrajectoryNode = outgoingTrajectoryNode;
        this.length = CalculusUtils.distance(outgoingTrajectoryNode.getLat(), incomingTrajectoryNode.getLat(), outgoingTrajectoryNode.getLon(), incomingTrajectoryNode.getLon());
        this.timeDifference = CalculusUtils.timeDifferenceInMilliseconds(outgoingTrajectoryNode.getTimestamp(), incomingTrajectoryNode.getTimestamp());
        this.velocity = CalculusUtils.velocity(length, timeDifference);
    }

    @PostConstruct
    public void post() {
        this.incomingTrajectoryNode.setOutgoingTrajectoryEdge(this);
        this.outgoingTrajectoryNode.setIncomingTrajectoryEdge(this);
    }

}
