package com.dexmohq.dexpenses.categorize;

import com.dexmohq.dexpenses.categorize.el.*;
import com.dexmohq.dexpenses.extract.CategoryExtractor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Categorizer {

    private final List<Category> categories;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static Categorizer load(File categorizerFile) throws IOException, InvalidCategorizerConfigurationException {
        final CategorizerJson categorizerJson = OBJECT_MAPPER.readValue(categorizerFile, CategorizerJson.class);
        final ArrayList<Category> categories = new ArrayList<>(categorizerJson.size());
        final Iterator<Map.Entry<String, Conditions>> iterator = categorizerJson.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Conditions> categoryDefinitionEntry = iterator.next();
            final String categoryIdentifier = categoryDefinitionEntry.getKey();
            final Conditions conditions = categoryDefinitionEntry.getValue();
            if (conditions.contains(WildcardCondition.INSTANCE)) {
                if (iterator.hasNext()) {
                    throw new InvalidCategorizerConfigurationException("wildcard category '" + categoryIdentifier + "' must be last");
                }
                if (conditions.size() > 1) {
                    throw new InvalidCategorizerConfigurationException("wild card condition must be the only condition in config");
                }
            }
            final Category category = new Category(categoryIdentifier, conditions.stream().map(Condition::get).collect(toList()));
            categories.add(category);
        }
        return new Categorizer(categories);
    }

    public CategoryExtractor getCategoryExtractor(String[] header) {
        return new CategoryExtractor(header, categories);
    }

    public static class CategorizerJson extends LinkedHashMap<String, Conditions> {

    }

}
