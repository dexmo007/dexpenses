package com.dexmohq.dexpenses.categorize;

import com.dexmohq.dexpenses.categorize.el.CategoryInfo;
import com.dexmohq.dexpenses.categorize.el.Condition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

/**
 * @author Henrik Drefs
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WildcardCondition implements Condition {

    public static final WildcardCondition INSTANCE = new WildcardCondition();

    @Override
    public Predicate<CategoryInfo> get() {
        return categoryInfo -> true;
    }
}
