package pl.edu.agh.touristsurveys.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Building {

    private final String id;
    private final String type;
    private final BuildingTags tags;
    private final double lon;
    private final double lat;

}
