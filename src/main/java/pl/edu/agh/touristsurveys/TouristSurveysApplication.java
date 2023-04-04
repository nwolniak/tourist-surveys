package pl.edu.agh.touristsurveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.touristsurveys.model.City;
import pl.edu.agh.touristsurveys.model.Coordinates;
import pl.edu.agh.touristsurveys.service.SurveyService;
import pl.edu.agh.touristsurveys.service.OverpassService;

import java.util.ArrayList;

@SpringBootApplication
public class TouristSurveysApplication implements ApplicationRunner {

    @Autowired
    private OverpassService overpassService;

    public static void main(String[] args) {
        SpringApplication.run(TouristSurveysApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var c1 = new Coordinates(19.94037083309971, 50.06351962152442); //Floriańska street
        var c2 = new Coordinates(19.9999170, 50.0601301);
        var c3 = new Coordinates(19.9415137, 50.0554747);
        var c4 = new Coordinates(19.4415137, 50.0554747);

        var coordinates = new ArrayList<Coordinates>();
        coordinates.add(c1);
        coordinates.add(c2);
        coordinates.add(c3);
        coordinates.add(c4);

        SurveyService surveyService = new SurveyService(overpassService);
        surveyService.setCity(City.KRAKOW);
        surveyService.setCoordinates(coordinates);
        surveyService.getAllCityData();
        surveyService.seekForPlaces(100); //seek for the places in distance of 100m for that desired point
        var a = surveyService.getCoordinatesList();
    }
}
