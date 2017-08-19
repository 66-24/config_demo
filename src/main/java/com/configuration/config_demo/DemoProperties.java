package com.configuration.config_demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "demo")
@Component
@Data
public class DemoProperties {
    private String about;
    private List<String> feature;
    private String state;
    private Instant endDateTime;

    private Environment environment;

    public DemoProperties(Environment environment) {
        this.environment = environment;
    }

    public List<String> getFeature() {
        return feature
                .parallelStream()
                .map(feature -> environment.resolvePlaceholders(feature))
                .collect(Collectors.toList());
    }
}
