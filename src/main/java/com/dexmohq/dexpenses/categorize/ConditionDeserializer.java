package com.dexmohq.dexpenses.categorize;

import com.dexmohq.dexpenses.categorize.el.*;
import com.dexmohq.dexpenses.util.UniquePropertyPolymorphicDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;

/**
 * @author Henrik Drefs
 */
public class ConditionDeserializer extends UniquePropertyPolymorphicDeserializer<Condition> {

    public ConditionDeserializer() {
        super(Condition.class);
        register("$and", AndCondition.class);
        register("$or", OrCondition.class);
        register("$not", NotCondition.class);
        register("keyword", KeywordCondition.class);
        register("anyOfKeywords", AnyOfKeywordsCondition.class);
        register("allOfKeywords", AllOfKeywordsCondition.class);
        register("regex", RegexCondition.class);
    }

    @Override
    public Condition emptyValue(DeserializationContext ctx) {
        return WildcardCondition.INSTANCE;
    }
}
