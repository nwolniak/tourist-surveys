package pl.edu.agh.touristsurveys.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;

@Component
@RequiredArgsConstructor
public class OverpassWebClient {

    private final WebClient webClient;

    public String get(BuildingQuery buildingQuery) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/interpreter")
                        .queryParam("data", buildingQuery.getQuery())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
