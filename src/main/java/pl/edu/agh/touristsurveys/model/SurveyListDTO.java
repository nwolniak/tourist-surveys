package pl.edu.agh.touristsurveys.model;

import java.util.List;

public record SurveyListDTO(String fileName, List<SurveyDTO> surveyList) {}
