package pl.edu.agh.touristsurveys.requests;

import hu.supercluster.overpasser.library.output.OutputFormat;
import hu.supercluster.overpasser.library.query.OverpassFilterQuery;
import hu.supercluster.overpasser.library.query.OverpassQuery;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class BuildingQuery {

    @Getter
    private final String query;

    private BuildingQuery(String query) {
        this.query = query;
    }

    public static BuildingQuery multipleTagMap(Map<String, String> tagMap, double southernLat, double northernLat, double westernLon, double easternLon) {
        OverpassFilterQuery filterQuery = new OverpassQuery()
                .format(OutputFormat.JSON)
                .timeout(100)
                .boundingBox(southernLat, northernLat, westernLon, easternLon)
                .filterQuery()
                .way();

        tagMap.forEach(filterQuery::tag);

        String query = filterQuery
                .end()
                .build();

        return new BuildingQuery(query);
    }

    public static BuildingQuery singleTagList(List<String> tagList, double southernLat, double northernLat, double westernLon, double easternLon) {
        OverpassFilterQuery filterQuery = new OverpassQuery()
                .format(OutputFormat.JSON)
                .timeout(100)
                .boundingBox(southernLat, northernLat, westernLon, easternLon)
                .filterQuery()
                .way();

        tagList.forEach(filterQuery::tag);

        String query = filterQuery
                .end()
                .build();

        return new BuildingQuery(query);
    }

}
