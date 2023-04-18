package pl.edu.agh.touristsurveys.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Objects;

public final class Building {
    private final String id;
    private final String type;
    private final Map<String, String> tags;
    @SerializedName(value = "lon", alternate = "$.center.lon")
    private final double lon;
    @SerializedName(value = "lat", alternate = "center.lat")
    private final double lat;

    public Building(String id, String type, Map<String, String> tags, double lon, double lat) {
        this.id = id;
        this.type = type;
        this.tags = tags;
        this.lon = lon;
        this.lat = lat;
    }

    public String id() {
        return id;
    }

    public String type() {
        return type;
    }

    public Map<String, String> tags() {
        return tags;
    }

    public double lon() {
        return lon;
    }

    public double lat() {
        return lat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Building) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.tags, that.tags) &&
                Double.doubleToLongBits(this.lon) == Double.doubleToLongBits(that.lon) &&
                Double.doubleToLongBits(this.lat) == Double.doubleToLongBits(that.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, tags, lon, lat);
    }

    @Override
    public String toString() {
        return "Building[" +
                "id=" + id + ", " +
                "type=" + type + ", " +
                "tags=" + tags + ", " +
                "lon=" + lon + ", " +
                "lat=" + lat + ']';
    }
}
