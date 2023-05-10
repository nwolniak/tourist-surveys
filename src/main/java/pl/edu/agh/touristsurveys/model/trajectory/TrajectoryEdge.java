package pl.edu.agh.touristsurveys.model.trajectory;

import lombok.Data;
import pl.edu.agh.touristsurveys.utils.SurveyUtils;

@Data
public class TrajectoryEdge {

    private final TrajectoryNode outgoingTrajectoryNode;
    private final TrajectoryNode incomingTrajectoryNode;

    private final double length;
    private final double timeDifference;
    private final double velocity;

    public TrajectoryEdge(TrajectoryNode outgoingTrajectoryNode, TrajectoryNode incomingTrajectoryNode) {
        this.outgoingTrajectoryNode = outgoingTrajectoryNode;
        this.incomingTrajectoryNode = incomingTrajectoryNode;
        this.length = SurveyUtils.distance(outgoingTrajectoryNode.getLat(), incomingTrajectoryNode.getLat(), outgoingTrajectoryNode.getLon(), incomingTrajectoryNode.getLon());
        this.timeDifference = SurveyUtils.timeDifferenceInMilliseconds(outgoingTrajectoryNode.getTimestamp(), incomingTrajectoryNode.getTimestamp());
        this.velocity = SurveyUtils.velocity(length, timeDifference);
    }

}
