package com.dexmohq.dexpenses.extract;

import com.dexmohq.dexpenses.util.CollectorUtils;
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
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
public class ConfigurableTimeExtractor extends AbstractConfigurableRegexExtractor<LocalTime> {

    private ConfigurableTimeExtractor(LinkedHashMap<Pattern, Function<Matcher, LocalTime>> patternMappings) {
        super(patternMappings);
    }

    public static ConfigurableTimeExtractor load(File configFile) throws IOException, InvalidConfigurationException {
        final TimeExtractorConfig timeExtractorConfig = new ConfigurationLoader().load(configFile, TimeExtractorConfig.class);
        final LinkedHashMap<Pattern, Function<Matcher, LocalTime>> patternMappings = timeExtractorConfig.getParsers().entrySet().stream()
                .collect(CollectorUtils.toLinkedMap(e -> Pattern.compile(e.getKey(), Pattern.CASE_INSENSITIVE), e -> e.getValue().getParser()));
        return new ConfigurableTimeExtractor(patternMappings);
    }

    @Data
    public static class TimeExtractorConfig {

        @JsonIgnore
        @Valid
        private LinkedHashMap<@Regex String, TimeParserProvider> parsers = new LinkedHashMap<>();

        @JsonAnySetter
        public void set(String key, TimeParserProvider timeParserProvider) {
            parsers.put(key, timeParserProvider);
        }

    }

    @JsonDeserialize(using = TimeParserProviderDeserializer.class)
    public interface TimeParserProvider {
        Function<Matcher, LocalTime> getParser();
    }

    public static class TimeParserProviderDeserializer extends UniquePropertyPolymorphicDeserializer<TimeParserProvider> {
        public TimeParserProviderDeserializer() {
            super(TimeParserProvider.class);
            register("format", FormatterParser.class);
            register("hour", IndividualTimeParser.class);
        }
    }

    @JsonDeserialize
    public static class FormatterParser implements TimeParserProvider {

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
        public Function<Matcher, LocalTime> getParser() {
            final int group = this.group == null ? 1 : this.group;
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return m -> LocalTime.parse(m.group(group), formatter);
        }

    }

    @JsonDeserialize
    public static class IndividualTimeParser implements TimeParserProvider {

        @Min(1)
        @NotNull
        private final Integer hour;
        @Min(1)
        @NotNull
        private final Integer minute;
        @Min(1)
        private final Integer seconds;

        @JsonCreator
        public IndividualTimeParser(@JsonProperty("hour") Integer hour,
                                    @JsonProperty("minute") Integer minute,
                                    @JsonProperty("seconds") Integer seconds) {
            this.hour = hour;
            this.minute = minute;
            this.seconds = seconds;
        }

        @Override
        public Function<Matcher, LocalTime> getParser() {
            return matcher -> {
                final int hour = Integer.parseInt(matcher.group(this.hour));
                final int minute = Integer.parseInt(matcher.group(this.minute));
                if (this.seconds != null) {
                    final String secondsString = matcher.group(this.seconds);
                    if (secondsString != null) {
                        final int seconds = Integer.parseInt(secondsString);
                        return LocalTime.of(hour, minute, seconds);
                    }
                }
                return LocalTime.of(hour, minute);
            };
        }
    }
}
