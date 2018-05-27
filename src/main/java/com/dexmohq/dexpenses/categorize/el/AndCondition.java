package com.dexmohq.dexpenses.categorize.el;

import com.dexmohq.dexpenses.categorize.ConditionDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @author Henrik Drefs
 */
@Value
public class AndCondition implements Condition {

    @JsonProperty("$and")
    private final List<Condition> conditions;

    @JsonCreator
    public AndCondition(@JsonProperty("$and")
                        @JsonDeserialize(contentUsing = ConditionDeserializer.class) List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public Predicate<CategoryInfo> get() {
        final List<Predicate<CategoryInfo>> predicates = conditions.stream()
                .map(Condition::get)
                .collect(toList());
        return categoryInfo -> predicates.stream().allMatch(p -> p.test(categoryInfo));
    }
}
