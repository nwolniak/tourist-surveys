package pl.edu.agh.touristsurveys.model;

import java.util.Map;

public record Building(String id,
                       String type,
                       Map<String, String> tags,
                       double lon,
                       double lat) {}
