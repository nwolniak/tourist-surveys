package pl.edu.agh.touristsurveys.model;

import lombok.RequiredArgsConstructor;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class Landmark {
    private final String name;
    private final Map<String, String> tags;
    private final double lon;
    private final double lat;
    private final List<Position> positionList;

    private Duration timeSpend;

    public Duration getTimeSpend() {
        return timeSpend;
    }

    public LocalDateTime getEnterTime(){
        return positionList.stream()
                .min(Comparator.comparing(Position::getTimestamp))
                .map(Position::getTimestamp)
                .orElse(null);
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void calculateTimeSpend() {

        List<LocalDateTime> timestamps = positionList.stream()
                .map(Position::getTimestamp)
                .toList();

        if (timestamps.size() > 1) {
            LocalDateTime earliest = Collections.max(timestamps, Comparator.naturalOrder());
            LocalDateTime latest = Collections.min(timestamps, Comparator.naturalOrder());

            timeSpend = Duration.between(latest, earliest);
        }else{
            timeSpend = Duration.ofSeconds(9999);
        }
    }

    @Override
    public String toString() {
        return name + " --- time spend: " + timeSpend + " | " + tags;
    }
}
