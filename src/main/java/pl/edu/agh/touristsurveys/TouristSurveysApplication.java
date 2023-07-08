package pl.edu.agh.touristsurveys;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.service.DirectoryProcessingService;

import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class TouristSurveysApplication implements CommandLineRunner {

    private final DirectoryProcessingService directoryProcessingService;

    public static void main(String[] args) {
        SpringApplication.run(TouristSurveysApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(args)
                .findFirst()
                .ifPresent(directoryProcessingService::processSurveysFromDirectory);
    }

}
