package com.dexmohq.dexpenses.extract;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
public class TimeExtractor extends RegexExtractor<LocalTime> {

    private static final Pattern PREFIX_FULL_TIME_REGEX = Pattern.compile("Uhrzeit:?\\s*(\\d{2}:\\d{2}:\\d{2})", Pattern.CASE_INSENSITIVE);
    private static final Pattern PREFIX_TIME_REGEX = Pattern.compile("Uhrzeit:?\\s*(\\d{2}[:;]\\d{2})", Pattern.CASE_INSENSITIVE);
    private static final Pattern FULL_TIME_REGEX = Pattern.compile("(\\d{2}:\\d{2}:\\d{2})");
    private static final Pattern TIME_REGEX = Pattern.compile("(\\d{2}[:;]\\d{2})");

    private static final DateTimeFormatter FULL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static final Function<Matcher, LocalTime> FULL_TIME_FORMATTER_PARSER = matcher -> LocalTime.parse(matcher.group(1), FULL_TIME_FORMATTER);
    private static final Function<Matcher, LocalTime> TIME_FORMATTER_PARSER = matcher -> LocalTime.parse(matcher.group(1).replace(';', ':'), TIME_FORMATTER);

    /**
     * Extracts LocalTime from full date time regex matcher
     *
     * @see DateTimeUtils#DATE_TIME_REGEX
     */
    private static final Function<Matcher, LocalTime> DATE_TIME_PARSER = matcher -> {
        final int hour = Integer.parseInt(matcher.group(4));
        final int minute = Integer.parseInt(matcher.group(5));
        final String maybeSeconds = matcher.group(6);
        if (maybeSeconds != null) {
            final int seconds = Integer.parseInt(maybeSeconds);
            return LocalTime.of(hour, minute, seconds);
        }
        return LocalTime.of(hour, minute);
    };

    @SuppressWarnings("unchecked")
    private static final ExtractMapping<LocalTime>[] EXTRACT_MAPPINGS = new ExtractMapping[]{
            new ExtractMapping<>(PREFIX_FULL_TIME_REGEX, FULL_TIME_FORMATTER_PARSER),
            new ExtractMapping<>(PREFIX_TIME_REGEX, TIME_FORMATTER_PARSER),
            new ExtractMapping(DateTimeUtils.DATE_TIME_REGEX, DATE_TIME_PARSER),
            new ExtractMapping<>(FULL_TIME_REGEX, FULL_TIME_FORMATTER_PARSER),
            new ExtractMapping<>(TIME_REGEX, TIME_FORMATTER_PARSER)
    };

    public TimeExtractor() {
        super(EXTRACT_MAPPINGS);
    }
}
