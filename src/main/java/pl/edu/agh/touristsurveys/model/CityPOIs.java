package pl.edu.agh.touristsurveys.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CityPOIs {
    private final City city;
    public List<Building> museum;
    public List<Building> accommodation;
    public List<Building> transport;
    public List<Building> food;

    public CityPOIs(City city) {
        this.city = city;
    }
}
