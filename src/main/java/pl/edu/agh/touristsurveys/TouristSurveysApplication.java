package pl.edu.agh.touristsurveys;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.requests.BuildingQuery;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TouristSurveysApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(TouristSurveysApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        BuildingQuery buildingQuery = BuildingQuery.multipleTagMap(
                Map.of("building", "dormitory",
                        "tourism", "museum"),
                50.0673297,
                19.9007186,
                50.0704123,
                19.9065637);
        System.out.println(buildingQuery.getQuery());

        BuildingQuery buildingQuery2 = BuildingQuery.singleTagList(
                List.of("building", "tourism"),
                50.0673297,
                19.9007186,
                50.0704123,
                19.9065637);
        System.out.println(buildingQuery2.getQuery());
    }
}
