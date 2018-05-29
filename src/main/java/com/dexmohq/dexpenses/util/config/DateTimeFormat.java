package com.dexmohq.dexpenses.util.config;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Henrik Drefs
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeFormatValidator.class)
@Documented
public @interface DateTimeFormat {

    String locale() default "";

    String message() default "com.dexmohq.dexpenses.util.config.DateTimeFormat.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
