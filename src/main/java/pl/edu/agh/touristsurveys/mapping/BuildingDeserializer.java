package pl.edu.agh.touristsurveys.mapping;

import com.google.gson.*;
import org.apache.commons.lang3.math.NumberUtils;
import pl.edu.agh.touristsurveys.model.Building;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BuildingDeserializer implements JsonDeserializer<Building> {

    @Override
    public Building deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = jsonObject.getAsJsonPrimitive("id").getAsString();
        String typeName = jsonObject.getAsJsonPrimitive("type").getAsString();
        Map<String, String> tags = jsonObject.getAsJsonObject("tags")
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().getAsJsonPrimitive().getAsString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        double lon = Optional.ofNullable(jsonObject.getAsJsonPrimitive("lon"))
                .or(() -> Optional.ofNullable(jsonObject.getAsJsonObject("center").getAsJsonPrimitive("lon")))
                .map(JsonPrimitive::getAsDouble)
                .orElse(NumberUtils.DOUBLE_ZERO);
        double lat = Optional.ofNullable(jsonObject.getAsJsonPrimitive("lat"))
                .or(() -> Optional.ofNullable(jsonObject.getAsJsonObject("center")
                        .getAsJsonPrimitive("lat")))
                .map(JsonPrimitive::getAsDouble)
                .orElse(NumberUtils.DOUBLE_ZERO);
        return new Building(id, typeName, tags, lon, lat);
    }

}
