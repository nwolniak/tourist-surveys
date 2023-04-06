package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.requests.Query;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final OverpassService overpassService;

    public List<Building> getAllBuildings(Double[] boundaries, List<String> tags) {
        Query query = Query.builder()
                .timeout(100)
                .southernLat(boundaries[0])
                .northernLat(boundaries[1])
                .westernLon(boundaries[2])
                .easterLon(boundaries[3])
                .tags(tags)
                .build();

        return overpassService.getBuildings(query);
    }
}
