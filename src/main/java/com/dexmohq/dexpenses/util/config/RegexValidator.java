package com.dexmohq.dexpenses.util.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Henrik Drefs
 */
public class RegexValidator implements ConstraintValidator<Regex, String> {

    private int flags;

    public void initialize(Regex constraint) {
        this.flags = constraint.flags();
    }

    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) {
            return true;
        }
        try {
            Pattern.compile(s, flags);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}
