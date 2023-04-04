package pl.edu.agh.touristsurveys.model;

import java.util.List;

public class Coordinates {
    public final double longitude;
    public final double latitude;
    public List<Building> museums;

    public Coordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setMuseums(List<Building> museums) {
        this.museums = museums;
    }
}
