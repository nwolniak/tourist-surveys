package pl.edu.agh.touristsurveys.parser;

import org.junit.jupiter.api.Test;
import pl.edu.agh.touristsurveys.model.mapping.CsvNode;
import pl.edu.agh.touristsurveys.model.trajectory.TrajectoryNode;
import pl.edu.agh.touristsurveys.utils.Constants;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class TrajectoryParserTest {

//    private final TrajectoryParser trajectoryParser = new TrajectoryParser(Constants.TRAJECTORY_CSV_PATH);
//
//    @Test
//    public void parseTrajectoryTest() {
//        AtomicReference<List<CsvNode>> listAtomicReference = new AtomicReference<>();
//        assertDoesNotThrow(() -> listAtomicReference.set(trajectoryParser.parseTrajectory()));
//        List<CsvNode> nodes = listAtomicReference.get();
//        assertNotNull(nodes);
//        assertEquals(30, nodes.size());
//    }
}
