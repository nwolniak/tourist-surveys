package pl.edu.agh.touristsurveys.webclient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;
import pl.edu.agh.touristsurveys.utils.Constants;
import pl.edu.agh.touristsurveys.utils.JsonUtil;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OverpassWebClientTest {

    private static MockWebServer mockWebServer;
    private OverpassWebClient overpassWebClient;

    @BeforeAll
    public static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    public void init() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.create(baseUrl);
        overpassWebClient = new OverpassWebClient(webClient);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.close();
    }

    @Test
    public void webClientTest() throws InterruptedException {
        // init
        String museumsJson = JsonUtil.getJsonAsString(Constants.MUSEUM_RESPONSE_JSON_PATH);
        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("tourism", "museum"),
                50.0462364,
                50.0707757,
                19.9248071,
                19.9541481);

        // mock
        mockWebServer.enqueue(new MockResponse().setBody(museumsJson));

        // get
        String response = overpassWebClient.get(buildingQuery);

        // check
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertNotNull(request.getPath());
        assertTrue(request.getPath().startsWith("/api/interpreter?data="));
        assertNotNull(response);
        assertEquals(museumsJson, response);
    }
}
