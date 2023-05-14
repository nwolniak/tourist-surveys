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

        surveyResults.add(new SurveyDTO("aaa", "Yes"));
        surveyResults.add(new SurveyDTO("ccc", "No"));
        surveyResults.add(new SurveyDTO("eee", "Yes"));
        return surveyResults;
    }

}
