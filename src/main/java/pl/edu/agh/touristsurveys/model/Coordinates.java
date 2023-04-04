package pl.edu.agh.touristsurveys.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Coordinates {
    public final double longitude;
    public final double latitude;
    public List<Building> museums;
    public List<Building> accommodation;
    public List<Building> transport;
    public List<Building> food;

    public Coordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
