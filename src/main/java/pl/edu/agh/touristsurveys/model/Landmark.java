package pl.edu.agh.touristsurveys.model;

import lombok.RequiredArgsConstructor;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Landmark {
    private final String name;
    private final Map<String, String> tags;
    private final double lon;
    private final double lat;
    private final List<Position> positionList;
}
