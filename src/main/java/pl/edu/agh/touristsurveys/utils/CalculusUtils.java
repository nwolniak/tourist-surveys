package pl.edu.agh.touristsurveys.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CalculusUtils {

    private CalculusUtils() {}

    public static double timeDifferenceInMilliseconds(LocalDateTime time1, LocalDateTime time2) {
        return ChronoUnit.MILLIS.between(time1, time2);
    }

    public static double velocity(double meters, double milliseconds) {
        return meters / ((milliseconds / 1000) % 60);
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

}
