package pl.edu.agh.touristsurveys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.parser.TrajectoryMapper;
import pl.edu.agh.touristsurveys.service.SurveyService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SurveyController {

    private final TrajectoryMapper trajectoryMapper;
    private final SurveyService surveyService;

    @PostMapping("/surveyResults")
    public List<SurveyDTO> surveyResults(@RequestBody Map<String, String> requestBody) {
        String csv = requestBody.get("file");
        String type = requestBody.get("type");
        TrajectoryGraph trajectoryGraph = trajectoryMapper.parseAndMapToInternalModel(csv.getBytes(), type);
        return surveyService.createTouristSurvey(trajectoryGraph);
    }

}
