package pl.edu.agh.touristsurveys.model;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.util.List;

@Setter
@Getter
public class Coordinates {
    //    public final double longitude;
//    public final double latitude;
    public TrajectoryNode trajectoryNode;
    public List<Building> museums;
    public List<Building> accommodation;
    public List<Building> transport;
    public List<Building> food;

    public Coordinates(TrajectoryNode trajectoryNode) {
        this.trajectoryNode = trajectoryNode;
    }

    public double getLatitude(){
        return trajectoryNode.getLat();
    }

    public double getLongitude(){
        return trajectoryNode.getLon();
    }

    public Coordinates(double longitude, double latitude) {
//    public Coordinates(double longitude, double latitude) {
//        this.longitude = longitude;
//        this.latitude = latitude;
    }
}
