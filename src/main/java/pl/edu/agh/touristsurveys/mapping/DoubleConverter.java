package pl.edu.agh.touristsurveys.mapping;

import com.opencsv.bean.AbstractBeanField;

public class DoubleConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Double convert(String doubleString) {
        return Double.parseDouble(doubleString.replace(",", "."));
    }

}
