package pl.edu.agh.touristsurveys.model.trajectory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TrajectoryNode {

    private final String nodeId;
    private final String trajectoryId;
    private final double lon;
    private final double lat;
    private final LocalDateTime timestamp;

    private TrajectoryEdge incomingTrajectoryEdge;
    private TrajectoryEdge outgoingTrajectoryEdge;

    public TrajectoryNode(String trajectoryId, double lon, double lat, LocalDateTime timestamp) {
        this.nodeId = trajectoryId + timestamp.toString();
        this.trajectoryId = trajectoryId;
        this.lon = lon;
        this.lat = lat;
        this.timestamp = timestamp;
    }

}
