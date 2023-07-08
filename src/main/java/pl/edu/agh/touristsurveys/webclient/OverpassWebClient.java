package pl.edu.agh.touristsurveys.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.agh.touristsurveys.requests.Query;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class OverpassWebClient {

    private final RestTemplate restTemplate;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 200))
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

    @Recover
    private void recover(Query query) {
        log.info("Omitting query: {}", query.getQuery());
    }

}
