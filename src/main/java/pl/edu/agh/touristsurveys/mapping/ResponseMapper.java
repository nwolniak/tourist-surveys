package pl.edu.agh.touristsurveys.mapping;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import pl.edu.agh.touristsurveys.model.Building;

import java.util.List;

@Component
public class ResponseMapper {

    private final Gson gson = new Gson();

    public List<Building> parseAndMap(String json) {
        List<JsonElement> jsonElements = JsonParser
                .parseString(json)
                .getAsJsonObject()
                .getAsJsonArray("elements")
                .asList();
        return mapToBuildings(jsonElements);
    }

    private List<Building> mapToBuildings(List<JsonElement> jsonElements) {
        return jsonElements
                .stream()
                .map(jsonElement -> gson.fromJson(jsonElement, Building.class))
                .toList();
    }

}
