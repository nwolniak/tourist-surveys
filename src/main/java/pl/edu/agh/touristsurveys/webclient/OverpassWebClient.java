package pl.edu.agh.touristsurveys.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.agh.touristsurveys.requests.Query;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class OverpassWebClient {

    private final RestTemplate restTemplate;

    public String get(Query query) {
        String url = "https://www.overpass-api.de";
        URI uri = UriComponentsBuilder.fromPath(url)
                .path("/api/interpreter")
                .queryParam("data", query.getQuery())
                .build()
                .toUri();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

}
