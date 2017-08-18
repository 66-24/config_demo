package com.configuration.config_demo.property_sources;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.DefaultExpressionEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class DemoListener implements SpringApplicationRunListener {
    public DemoListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        String filename = environment.getProperty("demo.configuration");
        try {
            createConfigureAndSetDefaultExpressionEngine();
            XMLConfiguration xmlConfiguration = new XMLConfiguration(filename);
            DemoXmlConfigurationSource.addToEnvironment(environment, xmlConfiguration);
        } catch (ConfigurationException e) {
            log.error("Unable to load configuration from file: {}", filename, e);
            throw new RuntimeException(e);
        }
    }

    private void createConfigureAndSetDefaultExpressionEngine() {
        DefaultExpressionEngine engine = new DefaultExpressionEngine();

// Use a slash as property delimiter
//        engine.setPropertyDelimiter("/");
// Indices should be provided in curly brackets
        engine.setIndexStart("{");
        engine.setIndexEnd("}");
// For attributes use simply a @
        engine.setAttributeStart("@");
        engine.setAttributeEnd(null);
// A Backslash is used for escaping property delimiters
        engine.setEscapedDelimiter("\\/");

// Now install this engine as the global engine
        HierarchicalConfiguration.setDefaultExpressionEngine(engine);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }
}
