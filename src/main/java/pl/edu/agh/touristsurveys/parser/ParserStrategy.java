package pl.edu.agh.touristsurveys.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.bean.CsvToBeanBuilder;
import pl.edu.agh.touristsurveys.model.mapping.CsvNode;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public enum ParserStrategy {
    csv {
        @Override
        List<TrajectoryNode> parseTrajectory(byte[] byteArray) {
            try (Reader reader = new InputStreamReader(new ByteArrayInputStream(byteArray))) {
                List<CsvNode> csvNodes = new CsvToBeanBuilder<CsvNode>(reader)
                        .withType(CsvNode.class)
                        .withSeparator(';')
                        .build()
                        .parse();
                return csvNodes.stream()
                        .map(csvNode -> new TrajectoryNode(csvNode.getTrajectoryId(), csvNode.getLon(), csvNode.getLat(), csvNode.getTimestamp()))
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    },
    json {
        @Override
        List<TrajectoryNode> parseTrajectory(byte[] byteArray) {
            String jsonString = new String(byteArray, StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(jsonString);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject trajectory = jsonObject.getAsJsonObject("trajectory");
            String trajectoryId = trajectory.getAsJsonPrimitive("trajectoryId").getAsString();
            List<TrajectoryNode> nodes = new LinkedList<>();
            trajectory.getAsJsonArray("poisWithTimestamps")
                    .forEach(element -> {
                        JsonObject date = element
                                .getAsJsonObject()
                                .getAsJsonObject("first")
                                .getAsJsonObject("date");
                        JsonObject time = element
                                .getAsJsonObject()
                                .getAsJsonObject("first")
                                .getAsJsonObject("time");
                        LocalDateTime timestamp = LocalDateTime.of(
                                date.getAsJsonPrimitive("year").getAsInt(),
                                date.getAsJsonPrimitive("month").getAsInt(),
                                date.getAsJsonPrimitive("day").getAsInt(),
                                time.getAsJsonPrimitive("hour").getAsInt(),
                                time.getAsJsonPrimitive("minute").getAsInt(),
                                time.getAsJsonPrimitive("second").getAsInt(),
                                time.getAsJsonPrimitive("nano").getAsInt());
                        double lat = Optional.ofNullable(element
                                        .getAsJsonObject()
                                        .getAsJsonObject("second")
                                        .getAsJsonPrimitive("lat"))
                                .or(() -> Optional.ofNullable(element
                                        .getAsJsonObject()
                                        .getAsJsonObject("second")
                                        .getAsJsonObject("osmModel")
                                        .getAsJsonPrimitive("lat")))
                                .get()
                                .getAsDouble();
                        double lon = Optional.ofNullable(element
                                        .getAsJsonObject()
                                        .getAsJsonObject("second")
                                        .getAsJsonPrimitive("lon"))
                                .or(() -> Optional.ofNullable(element
                                        .getAsJsonObject()
                                        .getAsJsonObject("second")
                                        .getAsJsonObject("osmModel")
                                        .getAsJsonPrimitive("lon")))
                                .get()
                                .getAsDouble();
                        nodes.add(new TrajectoryNode(trajectoryId, lon, lat, timestamp));
                    });
            return nodes;
        }
    };

    abstract List<TrajectoryNode> parseTrajectory(byte[] byteArray);
}
