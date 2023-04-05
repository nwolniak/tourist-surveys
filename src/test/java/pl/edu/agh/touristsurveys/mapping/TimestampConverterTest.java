package pl.edu.agh.touristsurveys.mapping;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TimestampConverterTest {

    private final TimestampConverter<LocalDateTime, Integer> timestampConverter = new TimestampConverter<>();

    @ParameterizedTest
    @CsvSource({
            "2022-06-21T07:04",
            "2022-06-21T07:04:04.9",
            "2022-06-21T07:04:04.96",
            "2022-06-21T07:04:04.967",
            "2022-06-21T07:04:04.9676",
            "2022-06-21T07:04:04.96765",
            "2022-06-21T07:04:04.967654",
            "2022-06-21T07:04:04.9676540",
            "2022-06-21T07:04:04.96765403",
            "2022-06-21T07:04:04.967654035"
    })
    public void convertTest(String dateString) {
        assertDoesNotThrow(() -> timestampConverter.convert(dateString));
        assertNotNull(timestampConverter.convert(dateString));
    }

}
