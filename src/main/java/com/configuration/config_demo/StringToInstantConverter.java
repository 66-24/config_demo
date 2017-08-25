package com.configuration.config_demo;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@ConfigurationPropertiesBinding
public class StringToInstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String source) {
        return Instant.parse(source);
    }
}
