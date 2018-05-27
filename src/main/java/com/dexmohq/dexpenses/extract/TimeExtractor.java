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
    private static final Pattern PREFIX_TIME_REGEX = Pattern.compile("Uhrzeit:?\\s*(\\d{2}:\\d{2})", Pattern.CASE_INSENSITIVE);
    private static final Pattern FULL_TIME_REGEX = Pattern.compile("(\\d{2}:\\d{2}:\\d{2})");
    private static final Pattern TIME_REGEX = Pattern.compile("(\\d{2}:\\d{2})");

    private static final DateTimeFormatter FULL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final Function<Matcher, LocalTime> FULL_TIME_FORMATTER_PARSER = matcher -> LocalTime.parse(matcher.group(1), FULL_TIME_FORMATTER);
    private static final Function<Matcher, LocalTime> TIME_FORMATTER_PARSER = matcher -> LocalTime.parse(matcher.group(1), TIME_FORMATTER);

    @SuppressWarnings("unchecked")
    private static final ExtractMapping<LocalTime>[] EXTRACT_MAPPINGS = new ExtractMapping[]{
            new ExtractMapping<>(PREFIX_FULL_TIME_REGEX, FULL_TIME_FORMATTER_PARSER),
            new ExtractMapping<>(PREFIX_TIME_REGEX, TIME_FORMATTER_PARSER),
            new ExtractMapping<>(FULL_TIME_REGEX, FULL_TIME_FORMATTER_PARSER),
            new ExtractMapping<>(TIME_REGEX, TIME_FORMATTER_PARSER)
    };

    public TimeExtractor() {
        super(EXTRACT_MAPPINGS);
    }
}
