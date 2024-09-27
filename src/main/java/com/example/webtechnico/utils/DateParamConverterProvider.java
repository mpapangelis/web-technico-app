package com.example.webtechnico.utils;

import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Provider
public class DateParamConverterProvider implements ParamConverterProvider {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.equals(Date.class)) {
            return new ParamConverter<T>() {

                @Override
                public T fromString(String value) {
                    try {
                        return rawType.cast(dateFormat.parse(value));
                    } catch (ParseException e) {
                        throw new RuntimeException("Invalid date format, expected yyyy-MM-dd");
                    }
                }

                @Override
                public String toString(T value) {
                    return dateFormat.format((Date) value);
                }
            };
        }
        return null;
    }
}
