package pl.edu.agh.touristsurveys.mapping;

import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDateTime;

public final class TimestampConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected LocalDateTime convert(String timestamp) {
        return LocalDateTime.parse(timestamp);
    }

}
