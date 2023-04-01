package pl.edu.agh.touristsurveys.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BuildingTags {

    private final String fee;
    private final String museum;
    private final String name;
    private final String operator;
    private final String tourism;
    private final String opening_hours;
}
