package pl.edu.agh.touristsurveys.model;

import lombok.*;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class Coordinates {

    public final TrajectoryNode trajectoryNode;
    public List<Building> museums;
    public List<Building> accommodation;
    public List<Building> transport;
    public List<Building> food;
    

    public double getLatitude(){
        return trajectoryNode.getLat();
    }

    public double getLongitude(){
        return trajectoryNode.getLon();
    }
}
