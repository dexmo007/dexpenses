package com.dexmohq.dexpenses.util.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
@Getter
public class InvalidConfigurationException extends Exception {
    private final Set<ConstraintViolation<?>> constraintViolations;
}
