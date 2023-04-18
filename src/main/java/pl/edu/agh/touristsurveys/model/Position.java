package pl.edu.agh.touristsurveys.model;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class Position {
    private final LocalDateTime timestamp;
    private final double distance;
    private final double lon;
    private final double lat;
}
