package pl.edu.agh.touristsurveys.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.agh.touristsurveys.requests.Query;

@Component
@RequiredArgsConstructor
public class OverpassWebClient {

    private final WebClient webClient;

    public String get(Query query) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/interpreter")
                        .queryParam("data", query.getQuery())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
