package pl.edu.agh.touristsurveys.model.trajectory;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
import pl.edu.agh.touristsurveys.mapping.DoubleConverter;
import pl.edu.agh.touristsurveys.mapping.TimestampConverter;

import java.time.LocalDateTime;

@Data
public class TrajectoryNode {

    @CsvBindByName(column = "trajectory id")
    private String trajectoryId;

    @CsvCustomBindByName(column = "longitude", converter = DoubleConverter.class)
    private double lon;

    @CsvCustomBindByName(column = "latitude", converter = DoubleConverter.class)
    private double lat;

    @CsvCustomBindByName(column = "timestamp", converter = TimestampConverter.class)
    private LocalDateTime timestamp;

}
