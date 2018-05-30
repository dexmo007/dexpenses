package com.dexmohq.dexpenses.extract;

import com.dexmohq.dexpenses.util.UniquePropertyPolymorphicDeserializer;
import com.dexmohq.dexpenses.util.config.ConfigurationLoader;
import com.dexmohq.dexpenses.util.config.DateTimeFormat;
import com.dexmohq.dexpenses.util.config.InvalidConfigurationException;
import com.dexmohq.dexpenses.util.config.Regex;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
                config.parsers.entrySet().stream()
                        .collect(toLinkedMap(e -> Pattern.compile(e.getKey()), e -> e.getValue().getParser())));
    }

    public static class DateParseConfigDeserializer extends UniquePropertyPolymorphicDeserializer<DateParserProvider> {

        public DateParseConfigDeserializer() {
            super(DateParserProvider.class);
            register("day", IndividualDateParser.class);
            register("format", FormatterParser.class);
        }
    }


    public static class Config {

        @JsonIgnore
        @Valid
        private final LinkedHashMap<@Regex String, DateParserProvider> parsers = new LinkedHashMap<>();

        @JsonAnySetter
        public void add(String regex, DateParserProvider dateParserProvider) {
            parsers.put(regex, dateParserProvider);
        }
    }

    @JsonDeserialize(using = DateParseConfigDeserializer.class)
    public interface DateParserProvider {
        Function<Matcher, LocalDate> getParser();
    }

    @JsonDeserialize
    public static class FormatterParser implements DateParserProvider {

        @DateTimeFormat
        private final String format;
        @Min(1)
        private final Integer group;

        @JsonCreator
        public FormatterParser(@JsonProperty("format") String format,
                               @JsonProperty(value = "group") Integer group) {
            this.format = format;
            this.group = group;
        }

        @Override
        public Function<Matcher, LocalDate> getParser() {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            final int group = this.group == null ? 1 : this.group;
            return m -> LocalDate.parse(m.group(group), formatter);
        }

    }

    @JsonDeserialize
    public static class IndividualDateParser implements DateParserProvider {
        @Min(1)
        @NotNull
        private final Integer day;
        @Min(1)
        @NotNull
        private final Integer month;
        @Min(1)
        @NotNull
        private final Integer year;

        @JsonCreator
        private IndividualDateParser(@JsonProperty("day") Integer day,
                                     @JsonProperty("month") Integer month,
                                     @JsonProperty("year") Integer year) {
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
