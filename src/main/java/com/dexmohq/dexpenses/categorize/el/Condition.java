package com.dexmohq.dexpenses.categorize.el;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Henrik Drefs
 */
public interface Condition extends Supplier<Predicate<CategoryInfo>> {

}
