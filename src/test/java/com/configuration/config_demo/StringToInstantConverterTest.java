package com.configuration.config_demo;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

public class StringToInstantConverterTest {

    private StringToInstantConverter stringToInstantConverter;

    @Before
    public void setUp() throws Exception {
        stringToInstantConverter = new StringToInstantConverter();
    }

    @Test
    public void should_return_instant_for_valid_iso8601_date_string() {
        Instant instant = stringToInstantConverter.convert("2008-09-15T15:53:00Z");
    }
}