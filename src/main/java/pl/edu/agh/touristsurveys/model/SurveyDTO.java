package pl.edu.agh.touristsurveys.model;

public class SurveyDTO {
    public String question;
    public String answer;

    public SurveyDTO(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
