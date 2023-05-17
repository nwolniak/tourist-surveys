package pl.edu.agh.touristsurveys.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.parser.TrajectoryParser;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pl.edu.agh.touristsurveys.parser.TrajectoryParser.parseTrajectory;

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


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/surveyResults")
    public List<SurveyDTO> surveyResults(@RequestBody Map<String, String> requestBody) {
        var csv = requestBody.get("file");
        var trajectory = parseTrajectory(csv.getBytes());

        var surveyResults = new ArrayList<SurveyDTO>();

        surveyResults.add(new SurveyDTO("When did you come to Lisbon and what day of your stay is it today?", "14.05.2023"));
        surveyResults.add(new SurveyDTO("Are you staying in Lisbon? ", "Yes"));
        surveyResults.add(new SurveyDTO("In what type of accommodation in Lisbon do you stay, if any?", "Hotel"));
        surveyResults.add(new SurveyDTO("What means of transport did you use to get to Lisbon?", "Airplane"));
        return surveyResults;
    }

}
