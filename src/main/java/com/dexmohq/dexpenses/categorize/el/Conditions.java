package com.dexmohq.dexpenses.categorize.el;

import com.dexmohq.dexpenses.categorize.ConditionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Henrik Drefs
 */
@JsonDeserialize(contentUsing = ConditionDeserializer.class)
public class Conditions extends ArrayList<Condition> {

    public static Conditions singleton(Condition condition) {
        final Conditions conditions = new Conditions();
        conditions.add(condition);
        return conditions;
    }

    public static AndCondition and(Condition... conditions) {
        return new AndCondition(Arrays.asList(conditions));
    }

    public static OrCondition or(Condition... conditions) {
        return new OrCondition(Arrays.asList(conditions));
    }

    public static NotCondition not(Condition condition) {
        return new NotCondition(condition);
    }
}
