package pl.edu.agh.touristsurveys.parser;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class TrajectoryParser {

    private final String trajectoryPath;

    public List<TrajectoryNode> parseTrajectory() {
        try (Reader reader = Files.newBufferedReader(Path.of(trajectoryPath))) {
            return new CsvToBeanBuilder<TrajectoryNode>(reader)
                    .withType(TrajectoryNode.class)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
