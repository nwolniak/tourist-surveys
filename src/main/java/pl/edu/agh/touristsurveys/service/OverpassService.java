package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.mapping.ResponseMapper;
import pl.edu.agh.touristsurveys.model.Building;
import pl.edu.agh.touristsurveys.requests.Query;
import pl.edu.agh.touristsurveys.webclient.OverpassWebClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverpassService {

    private final OverpassWebClient overpassWebClient;
    private final ResponseMapper responseMapper;

    public List<Building> getBuildings(Query query) {
        log.info("Query: {}", query.getQuery());
        String response = overpassWebClient.get(query);
        log.info("Response: {}", response);
        return responseMapper.parseAndMap(response);
    }

}
