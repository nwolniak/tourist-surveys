package pl.edu.agh.touristsurveys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurveyController {

    @GetMapping("/test")
    public int test() {
        return 1;
    }

}
