package pl.edu.agh.touristsurveys.mapping;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DoubleConverterTest {

    private final DoubleConverter<Double, Integer> doubleConverter = new DoubleConverter<>();

    @ParameterizedTest
    @CsvSource(value = {
            "50.090496",
            "14.090496",
            "50,090496",
            "14,090496",
    }, delimiter = ' ')
    public void convertTest(String doubleString) {
        assertDoesNotThrow(() -> doubleConverter.convert(doubleString));
        assertNotNull(doubleConverter.convert(doubleString));
    }
}
