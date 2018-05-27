package com.dexmohq.dexpenses.extract;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
public abstract class RegexExtractor<T> implements Extractor<T> {

    private final ExtractMapping<T>[] extractMappings;

    protected RegexExtractor(ExtractMapping<T>[] extractMappings) {
        this.extractMappings = extractMappings;
    }

    @Override
    public ExtractedResult<T> extract(String[] lines) {
        for (ExtractMapping<T> extractMapping : extractMappings) {
            final ExtractedResult<T> extracted = tryExtract(lines, extractMapping);
            if (extracted != null) {
                return extracted;
            }
        }
        return ExtractedResult.empty();
    }

    private ExtractedResult<T> tryExtract(String[] lines, ExtractMapping<T> extractMapping) {
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            final Matcher matcher = extractMapping.regex.matcher(line);
            if (matcher.find()) {
                final T result = extractMapping.parser.apply(matcher);
                return new ExtractedResult<>(result, i);
            }
        }
        return null;
    }

    @RequiredArgsConstructor
    protected static class ExtractMapping<T> {
        private final Pattern regex;
        private final Function<Matcher, T> parser;

        public ExtractMapping(String regex, Function<Matcher, T> parser) {
            this.regex = Pattern.compile(regex);
            this.parser = parser;
        }
    }

}
