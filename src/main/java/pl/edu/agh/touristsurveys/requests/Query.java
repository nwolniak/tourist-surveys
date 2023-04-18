package pl.edu.agh.touristsurveys.requests;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Query {

    private static final String JSON_FORMAT = "[out:json]";
    private static final String TIMEOUT_FORMAT = "[timeout:%s]";
    private static final String BBOX_FORMAT = "[bbox:%s,%s,%s,%s]";
    private static final String FILTER_GROUP_FORMAT = "(%s)";
    private static final String SINGLE_FILTER_FORMAT = "nw[%s];";
    private static final String OUTPUT_FORMAT = "out body center qt";

    @Getter
    private final String query;

    @Builder
    private Query(int timeout, double southernLat, double westernLon, double northernLat, double easterLon, List<String> tags) {
        String timeoutFormatted = String.format(TIMEOUT_FORMAT, timeout);
        String bbox = String.format(BBOX_FORMAT, southernLat, westernLon, northernLat, easterLon);
        String filters = tags.stream()
                .map(tag -> String.format(SINGLE_FILTER_FORMAT, tag))
                .collect(Collectors.joining());
        String filtersGroup = String.format(FILTER_GROUP_FORMAT, filters);

        String parameters = JSON_FORMAT + timeoutFormatted + bbox;
        this.query = Stream.of(parameters, filtersGroup, OUTPUT_FORMAT)
                .map(s -> s.concat(";"))
                .collect(Collectors.joining());
    }

}
