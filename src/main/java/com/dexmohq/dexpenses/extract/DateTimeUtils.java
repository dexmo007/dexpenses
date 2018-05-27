package com.dexmohq.dexpenses.extract;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
@UtilityClass
public class DateTimeUtils {

    static final Pattern DATE_TIME_REGEX = Pattern.compile("(\\d{2})[.,\\s](\\d{2})[.,\\s](\\d{4})\\s+(\\d{2})[:;](\\d{2})([:;]\\d{2})?");

}
