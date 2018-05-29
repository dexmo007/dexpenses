package com.dexmohq.dexpenses.util.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Henrik Drefs
 */
public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormat, String> {

    private Locale locale;

    public void initialize(DateTimeFormat constraint) {
        this.locale = constraint.locale().isEmpty() ? Locale.getDefault() : new Locale(constraint.locale());
    }

    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) {
            return true;
        }
        try {
            DateTimeFormatter.ofPattern(s, locale);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
