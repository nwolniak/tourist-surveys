package pl.edu.agh.touristsurveys.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;

@Configuration
public class AppConfiguration {

    @Value("${overpass-api.url}")
    private String overpassApiUrl;

    @Value("${trajectory.path}")
    private String trajectoryPath;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .baseUrl(overpassApiUrl)
                .build();
    }

    @Bean
    public TrajectoryParser trajectoryParser() {
        return new TrajectoryParser(trajectoryPath);
    }

}
