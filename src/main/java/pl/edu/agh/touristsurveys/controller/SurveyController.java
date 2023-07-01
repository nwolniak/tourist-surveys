package pl.edu.agh.touristsurveys.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.parser.TrajectoryMapper;
import pl.edu.agh.touristsurveys.service.SurveyService;

import java.io.FileWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SurveyController {

    private final TrajectoryMapper trajectoryMapper;
    private final SurveyService surveyService;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SneakyThrows
    @PostMapping("/surveyResults")
    public List<SurveyDTO> surveyResults(@RequestBody Map<String, String> requestBody) {
        String csv = requestBody.get("file");
        String type = requestBody.get("type");
        TrajectoryGraph trajectoryGraph = trajectoryMapper.parseAndMapToInternalModel(csv.getBytes(), type);
        List<SurveyDTO> touristSurveyList = surveyService.createTouristSurvey(trajectoryGraph);
        gson.toJson(touristSurveyList, new FileWriter("survey.json"));
        return touristSurveyList;
    }

}
