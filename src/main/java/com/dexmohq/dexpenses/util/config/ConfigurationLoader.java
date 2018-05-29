package com.dexmohq.dexpenses.util.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Henrik Drefs
 */
public class ConfigurationLoader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @SuppressWarnings("unchecked")
    public <T> T load(File configFile, Class<T> configClass) throws IOException, InvalidConfigurationException {
        final T config = OBJECT_MAPPER.readValue(configFile, configClass);
        final Set<ConstraintViolation<T>> violations = VALIDATOR.validate(config);
        if (violations.isEmpty()) {
            return config;
        }
        throw new InvalidConfigurationException((Set<ConstraintViolation<?>>)(Set<?>) violations);
    }

}
