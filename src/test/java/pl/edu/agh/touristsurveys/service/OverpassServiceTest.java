package pl.edu.agh.touristsurveys.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.edu.agh.touristsurveys.mapping.ResponseMapper;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;
import pl.edu.agh.touristsurveys.utils.Constants;
import pl.edu.agh.touristsurveys.utils.JsonUtil;
import pl.edu.agh.touristsurveys.webclient.OverpassWebClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OverpassServiceTest {

    private final OverpassWebClient overpassWebClient = Mockito.mock(OverpassWebClient.class);
    private final ResponseMapper responseMapper = new ResponseMapper();
    private final OverpassService overpassService = new OverpassService(overpassWebClient, responseMapper);

    @Test
    public void getBuildingsTest() {
        // init
        String museumsJson = JsonUtil.getJsonAsString(Constants.MUSEUM_RESPONSE_JSON_PATH);
        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("tourism", "museum"),
                50.0462364,
                50.0707757,
                19.9248071,
                19.9541481);

        // mock
        Mockito.when(overpassWebClient.get(buildingQuery))
                .thenReturn(museumsJson);

        // result
        List<Building> buildings = overpassService.getBuildings(buildingQuery);

        // check
        assertNotNull(buildings);
        assertEquals(21, buildings.size());
    }

}
