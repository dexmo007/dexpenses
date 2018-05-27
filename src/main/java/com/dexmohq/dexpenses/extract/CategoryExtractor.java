package com.dexmohq.dexpenses.extract;

import com.dexmohq.dexpenses.categorize.Category;
import com.dexmohq.dexpenses.categorize.el.CategoryInfo;
import com.dexmohq.dexpenses.util.Tokenizer;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class CategoryExtractor implements Extractor<Category> {

    private final String[] header;
    private final List<Category> categories;

    @Override
    public ExtractedResult<Category> extract(String[] lines) {

        for (Category category : categories) {
            final List<String> tokens = Tokenizer.tokenize(header);
            final CategoryInfo categoryInfo = new CategoryInfo(tokens, header, String.join("\n", header));
            for (Predicate<CategoryInfo> predicate : category.getPredicates()) {
                if (predicate.test(categoryInfo)) {
                    return new ExtractedResult<>(category, -1);//todo what line here
                }
            }
        }
        return ExtractedResult.empty();
    }
}
