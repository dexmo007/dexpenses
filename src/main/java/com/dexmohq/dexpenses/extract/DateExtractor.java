package com.dexmohq.dexpenses.extract;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
public class DateExtractor extends RegexExtractor<LocalDate> {

    private static final Pattern PREFIX_DATE_REGEX = Pattern.compile("Datum:?\\s*(\\d{2}\\.\\d{2}\\.\\d{4})", Pattern.CASE_INSENSITIVE);
    private static final Pattern DATE_REGEX = Pattern.compile("(\\d{2}[.,\\s]\\d{2}[.,\\s]\\d{4})");

    private static final DateTimeFormatter GERMAN_DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final Function<Matcher, LocalDate> GERMAN_DEFAULT_FORMATTER_PARSER = matcher -> LocalDate.parse(matcher.group(1), GERMAN_DEFAULT_FORMATTER);

    private static final Function<Matcher, LocalDate> DATE_TIME_PARSER = matcher -> {
        final int day = Integer.parseInt(matcher.group(1));
        final int month = Integer.parseInt(matcher.group(2));
        final int year = Integer.parseInt(matcher.group(3));
        return LocalDate.of(year, month, day);
    };

    @SuppressWarnings("unchecked")
    private static final ExtractMapping<LocalDate>[] EXTRACT_MAPPINGS = new ExtractMapping[]{
            new ExtractMapping<>(PREFIX_DATE_REGEX, GERMAN_DEFAULT_FORMATTER_PARSER),
            new ExtractMapping(DateTimeUtils.DATE_TIME_REGEX, DATE_TIME_PARSER),
            new ExtractMapping<>(DATE_REGEX, GERMAN_DEFAULT_FORMATTER_PARSER)
    };

    public DateExtractor() {
        super(EXTRACT_MAPPINGS);
    }

}
