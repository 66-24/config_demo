package com.configuration.config_demo;

import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * <a href="http://hibernate.org/validator/documentation/getting-started/">
 * See hibernate validator docs</a>
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DemoPropertiesTest.TestConfiguration.class})
public class DemoPropertiesTest {

    @Configuration
    public static class TestConfiguration {
        @Bean
        Environment environment() {
            return new StandardEnvironment();
        }

    }

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Test
    public void should_not_allow_future_dates() {
        DemoProperties demoProperties = getDemoProperties(
                Lists.newArrayList("feature1"),
                Instant.now().plus(Duration.ofDays(100)),
                "password");

        Set<ConstraintViolation<DemoProperties>> constraintViolations =
                validator.validate(demoProperties);

        assertEquals(1, constraintViolations.size());
        assertEquals(
                DemoProperties.MUST_BE_A_DATE_IN_THE_PAST,
                constraintViolations.iterator().next().getMessage()
        );

    }

    @Test
    public void should_fail_when_password_and_verify_password_do_not_match() {
        DemoProperties demoProperties = getDemoProperties(
                Lists.newArrayList("feature1"),
                Instant.now().minus(Duration.ofDays(100)),
                "password1");

        Set<ConstraintViolation<DemoProperties>> constraintViolations =
                validator.validate(demoProperties);

        assertEquals(1, constraintViolations.size());
        assertEquals(
                DemoProperties.PASSWORD_AND_VERIFY_PASSWORD_MUST_MATCH,
                constraintViolations.iterator().next().getMessage()
        );

    }


    private DemoProperties getDemoProperties(ArrayList<String> featureList,
                                             Instant endDateTime,
                                             String verifyPassword) {
        DemoProperties demoProperties = new DemoProperties(environment);
        demoProperties.setAbout("Some about");
        demoProperties.setFeature(featureList);
        demoProperties.setState("Some state");
        demoProperties.setEndDateTime(endDateTime);
        demoProperties.setPassword("password");
        demoProperties.setVerifyPassword(verifyPassword);
        return demoProperties;
    }

}