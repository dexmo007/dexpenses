package com.dexmohq.dexpenses.extract;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public abstract class AbstractConfigurableRegexExtractor<T> implements Extractor<T> {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    protected final LinkedHashMap<Pattern, Function<Matcher, T>> patternMappings;

    @Override
    public ExtractedResult<T> extract(String[] lines) {
        for (Map.Entry<Pattern, Function<Matcher, T>> mapping : patternMappings.entrySet()) {
            final Pattern pattern = mapping.getKey();
            for (int i = 0; i < lines.length; i++) {
                final String line = lines[i];
                final Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return new ExtractedResult<>(mapping.getValue().apply(matcher), i);
                }
            }
        }
        return ExtractedResult.empty();
    }
}
