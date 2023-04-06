package pl.edu.agh.touristsurveys.model;

public record Building(String id, String type, BuildingTags tags, double lon, double lat) {}

record BuildingTags(String name) {}