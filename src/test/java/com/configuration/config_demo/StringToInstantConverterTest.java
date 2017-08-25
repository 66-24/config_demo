package com.configuration.config_demo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class StringToInstantConverterTest {

    private StringToInstantConverter stringToInstantConverter;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void setUp() throws Exception {
        stringToInstantConverter = new StringToInstantConverter();
    }

    @Test
    public void should_return_instant_for_valid_iso8601_date_string() {
        Instant expected = Instant.ofEpochSecond(1000);
        String dateString = expected.toString();
        Instant actual = stringToInstantConverter.convert(dateString);
        assertEquals("The 2 instants are not the same",expected,actual);
    }

    @Test
    public void should_throw_exception_on_invalid_date_string() {
        expectedException.expect(DateTimeParseException.class);
        stringToInstantConverter.convert("20080915T155300Z");
    }
}