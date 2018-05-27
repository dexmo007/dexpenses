package com.dexmohq.dexpenses.categorize;

import com.dexmohq.dexpenses.categorize.el.CategoryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Henrik Drefs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private String identifier;

    private List<Predicate<CategoryInfo>> predicates;

    @Override
    public String toString() {
        return identifier;
    }
}
