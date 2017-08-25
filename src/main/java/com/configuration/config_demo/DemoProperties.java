package com.configuration.config_demo;

import lombok.Data;
import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <ul>
 *     <li> <a href="https://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303">
 *     Cross-field validation</a>
 *     </li>
 *      <li>
 *          <a href="https://stackoverflow.com/questions/4186556/hibernate-validator-custom-resourcebundlelocator-and-spring"
 *          >Using  custom messages from Spring's Resource bundle</a>
 *      </li>
 * </ul>
 */

@ConfigurationProperties(prefix = "demo")
@Component
@Data
@Validated
@ScriptAssert(
        lang = "js",
        script = "_this.password.equals(_this.verifyPassword)",
        message = DemoProperties.PASSWORD_AND_VERIFY_PASSWORD_MUST_MATCH
)
public class DemoProperties {
    public static final String MUST_BE_A_DATE_IN_THE_PAST = "must be a date in the past";
    public static final String PASSWORD_AND_VERIFY_PASSWORD_MUST_MATCH = "Password and verifyPassword must match";
    @NotEmpty
    private String about;
    @NotEmpty
    private List<String> feature;
    @NotEmpty
    private String state;

    @Past(message = MUST_BE_A_DATE_IN_THE_PAST)
    private Instant endDateTime;

    @NotEmpty
    private String password;

    @NotEmpty
    private String verifyPassword;

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
