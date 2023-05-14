package pl.edu.agh.touristsurveys.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.touristsurveys.model.SurveyDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SurveyController {

    @GetMapping("/test")
    public int test() {
        return 1;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getSurveyResults")
    public List<SurveyDTO> getSurveyResults() {
        var surveyResults = new ArrayList<SurveyDTO>();

        surveyResults.add(new SurveyDTO("When did you come to Lisbon and what day of your stay is it today?", "14.05.2023"));
        surveyResults.add(new SurveyDTO("Are you staying in Lisbon? ", "Yes"));
        surveyResults.add(new SurveyDTO("In what type of accommodation in Lisbon do you stay, if any?", "Hotel"));
        surveyResults.add(new SurveyDTO("What means of transport did you use to get to Lisbon?", "Airplane"));
        return surveyResults;
    }

}
