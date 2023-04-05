package pl.edu.agh.touristsurveys.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class CityPOIs {
    private final City city;
    private List<Building> museum;
    private List<Building> accommodation;
    private List<Building> transport;
    private List<Building> food;

}
