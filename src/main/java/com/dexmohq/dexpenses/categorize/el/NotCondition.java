package com.dexmohq.dexpenses.categorize.el;

import com.dexmohq.dexpenses.categorize.ConditionDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.util.function.Predicate;

/**
 * @author Henrik Drefs
 */
@Value
public class NotCondition implements Condition {

    @JsonProperty("$not")
    private final Condition condition;

    @JsonCreator
    public NotCondition(@JsonProperty("$not")
                        @JsonDeserialize(using = ConditionDeserializer.class) Condition condition) {
        this.condition = condition;
    }

    @Override
    public Predicate<CategoryInfo> get() {
        return condition.get().negate();
    }

}
