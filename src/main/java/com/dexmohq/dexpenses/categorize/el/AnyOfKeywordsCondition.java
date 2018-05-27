package com.dexmohq.dexpenses.categorize.el;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

/**
 * @author Henrik Drefs
 */
@Value
public class AnyOfKeywordsCondition implements Condition {

    private final List<String> anyOfKeywords;

    @JsonCreator
    public AnyOfKeywordsCondition(@JsonProperty("anyOfKeywords") List<String> anyOfKeywords) {
        this.anyOfKeywords = anyOfKeywords;
    }

    @Override
    public Predicate<CategoryInfo> get() {
        final Set<String> keywords = anyOfKeywords.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(toSet());
        return categoryInfo -> categoryInfo.getTokens().stream().anyMatch(keywords::contains);
    }
}
