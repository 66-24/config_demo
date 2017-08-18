package com.configuration.config_demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "demo")
@Component
@Data
public class DemoProperties {
    private String about;
    private List<String> feature;
    private String state;
}
