package com.configuration.config_demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import java.util.List;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties
public class ConfigDemoApplication implements CommandLineRunner {
    private DemoProperties demoProperties;
    private Environment environment;

    private List<String> featureStates;
    @SuppressWarnings("unchecked")
    public ConfigDemoApplication(DemoProperties demoProperties, Environment environment) {
        this.demoProperties = demoProperties;
        this.environment = environment;
        featureStates = environment.getProperty("demo.feature@state", List.class);
    }


    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder()
                .addCommandLineProperties(true)
                .bannerMode(Banner.Mode.OFF)
                .sources(ConfigDemoApplication.class)
                .logStartupInfo(true)
                .registerShutdownHook(true)
                .run(args);    }

    @Override
    public void run(String... args) throws Exception {
        log.info("feature states: [{}]",featureStates);
        log.info("Demo: [{}]", demoProperties);
//        log.info("Demo endDateTime: {}", demoProperties.getEndDateTime().getDate());
        log.info("Demo endDateTime: {}", demoProperties.getEndDateTime());
    }

}
