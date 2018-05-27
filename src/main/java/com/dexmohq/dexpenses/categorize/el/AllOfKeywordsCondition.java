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
public class AllOfKeywordsCondition implements Condition {

    private final List<String> allOfKeywords;

    @JsonCreator
    public AllOfKeywordsCondition(@JsonProperty("allOfKeywords") List<String> allOfKeywords) {
        this.allOfKeywords = allOfKeywords;
    }

    @Override
    public Predicate<CategoryInfo> get() {
        final Set<String> keywords = allOfKeywords.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(toSet());
        return categoryInfo -> categoryInfo.getTokens().containsAll(keywords);
    }
}
