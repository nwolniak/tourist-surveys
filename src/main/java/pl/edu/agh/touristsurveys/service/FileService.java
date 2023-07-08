package pl.edu.agh.touristsurveys.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.edu.agh.touristsurveys.model.SurveyDTO;
import pl.edu.agh.touristsurveys.model.SurveyListDTO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class FileService {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    @SneakyThrows
    public void saveSurveyToJson(List<SurveyDTO> surveys) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String json = gson.toJson(surveys);
        Files.createDirectories(Paths.get("results"));
        Files.write(Paths.get(String.format("results/%s.json", DATE_FORMAT.format(timestamp))), json.getBytes());
    }

    @SneakyThrows
    public void saveSurveysToJson(List<SurveyListDTO> surveys) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String json = gson.toJson(surveys);
        Files.createDirectories(Paths.get("results"));
        Files.write(Paths.get(String.format("results/%s.json", DATE_FORMAT.format(timestamp))), json.getBytes());
    }

}
