package pl.edu.agh.touristsurveys.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.SurveyListDTO;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryGraph;
import pl.edu.agh.touristsurveys.parser.TrajectoryMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectoryProcessingService {

    private final TrajectoryMapper trajectoryMapper;
    private final SurveyService surveyService;
    private final FileService fileService;

    @SneakyThrows
    public void processSurveysFromDirectory(String path) {
        File[] files = Optional.ofNullable(new File(path).listFiles())
                .orElseThrow(() -> new FileNotFoundException(path));
        List<SurveyListDTO> surveys = Arrays.stream(files)
                .parallel()
                .filter(File::isFile)
                .map(this::processTrajectoryFile)
                .toList();
        fileService.saveSurveysToJson(surveys);
    }

    @SneakyThrows
    private SurveyListDTO processTrajectoryFile(File trajectoryFile) {
        log.info("Processing {}", trajectoryFile.getName());
        String filePath = trajectoryFile.getAbsolutePath();
        String type = filePath.substring(filePath.lastIndexOf(".") + 1);
        byte[] fileBytes = Files.readAllBytes(Path.of(filePath));
        TrajectoryGraph trajectoryGraph = trajectoryMapper.parseAndMapToInternalModel(fileBytes, type);
        List<SurveyDTO> touristSurveyList = surveyService.createTouristSurvey(trajectoryGraph);
        return new SurveyListDTO(trajectoryFile.getName(), touristSurveyList);
    }

}
