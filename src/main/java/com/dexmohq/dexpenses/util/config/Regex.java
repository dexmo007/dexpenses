package com.dexmohq.dexpenses.util.config;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Henrik Drefs
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegexValidator.class)
@Documented
public @interface Regex {

    int flags() default 0;

    String message() default "com.dexmohq.dexpenses.util.config.Regex.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
