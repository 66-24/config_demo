package com.configuration.config_demo;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@ConfigurationPropertiesBinding
public class StringToInstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String source) {
        return LocalDateTime
                .parse(source, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                .toInstant(ZoneOffset.UTC);
    }
}
