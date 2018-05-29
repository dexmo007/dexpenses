package com.dexmohq.dexpenses.extract;

import com.dexmohq.dexpenses.util.UniquePropertyPolymorphicDeserializer;
import com.dexmohq.dexpenses.util.config.ConfigurationLoader;
import com.dexmohq.dexpenses.util.config.InvalidConfigurationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dexmohq.dexpenses.util.CollectorUtils.toLinkedMap;

/**
 * @author Henrik Drefs
 */
public class ConfigurableDateExtractor extends AbstractConfigurableRegexExtractor<LocalDate> {

    private ConfigurableDateExtractor(LinkedHashMap<Pattern, Function<Matcher, LocalDate>> patternMappings) {
        super(patternMappings);
    }

    public static ConfigurableDateExtractor load(File dateConfigFile) throws IOException, InvalidConfigurationException {
        final Config config = new ConfigurationLoader().load(dateConfigFile, Config.class);
        return new ConfigurableDateExtractor(
                config.entrySet().stream()
                        .collect(toLinkedMap(e -> Pattern.compile(e.getKey()), e -> e.getValue().getParser())));
    }

    public static class DateParseConfigDeserializer extends UniquePropertyPolymorphicDeserializer<DateParserProvider> {

        public DateParseConfigDeserializer() {
            super(DateParserProvider.class);
            register("day", IndividualDateParser.class);
            register("format", FormatterParser.class);
        }
    }

    @JsonDeserialize(contentUsing = DateParseConfigDeserializer.class)
    public static class Config extends LinkedHashMap<String, DateParserProvider> {

    }

    public interface DateParserProvider {
        Function<Matcher, LocalDate> getParser();
    }

    public static class FormatterParser implements DateParserProvider {

        private final String format;
        private final Integer group;

        @JsonCreator
        public FormatterParser(@JsonProperty("format") String format,
                               @JsonProperty(value = "group") Integer group) {
            this.format = format;
            this.group = group == null ? 1 : group;
        }

        @Override
        public Function<Matcher, LocalDate> getParser() {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            if (group < 0) {
                throw new IllegalArgumentException("regex capture group index cannot be < 0");
            }
            return m -> LocalDate.parse(m.group(group), formatter);
        }

    }

    public static class IndividualDateParser implements DateParserProvider {

        private final int day;
        private final int month;
        private final int year;

        @JsonCreator
        private IndividualDateParser(@JsonProperty("day") int day,
                                     @JsonProperty("month") int month,
                                     @JsonProperty("year") int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        @Override
        public Function<Matcher, LocalDate> getParser() {
            return matcher -> {
                final int year = Integer.parseInt(matcher.group(this.year));
                final int month = Integer.parseInt(matcher.group(this.month));
                final int day = Integer.parseInt(matcher.group(this.day));
                return LocalDate.of(year, month, day);
            };
        }
    }
}
