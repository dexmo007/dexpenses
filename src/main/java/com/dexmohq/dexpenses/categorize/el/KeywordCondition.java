package com.dexmohq.dexpenses.categorize.el;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.function.Predicate;

/**
 * @author Henrik Drefs
 */
@Value
public class KeywordCondition implements Condition {

    private final String keyword;

    @JsonCreator
    public KeywordCondition(@JsonProperty("keyword") String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Predicate<CategoryInfo> get() {
        return categoryInfo -> categoryInfo.getTokens().contains(keyword);
    }
}
