package com.configuration.config_demo.property_sources;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;

import java.util.ArrayList;

@Slf4j
public class DemoXmlConfigurationSource extends EnumerablePropertySource<XMLConfiguration> {
    private DemoXmlConfigurationSource(String name, XMLConfiguration source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        ArrayList<String> keys = Lists.newArrayList(this.source.getKeys());
        return keys.toArray(new String[keys.size()]);
    }

    @Override
    public Object getProperty(String s) {
        return this.source.getProperty(s);
    }

    static void addToEnvironment(final ConfigurableEnvironment environment, final XMLConfiguration xmlConfiguration) {
        environment.getPropertySources().addFirst(
                new DemoXmlConfigurationSource("DemoAppConfiguration", xmlConfiguration));
        log.info("Demo.about: {}", environment.getProperty("demo.about"));
        log.info("Placeholder expanded: {}", environment.getProperty("demo.feature{1}"));
        log.info("BUG - Placeholder not expanded: {}", environment.getProperty("demo.feature",String.class));
        log.info("Demo Xml Configuration [{}] added to Environment", xmlConfiguration.getFile().getAbsolutePath());
    }

}
