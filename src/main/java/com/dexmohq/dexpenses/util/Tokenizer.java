package com.dexmohq.dexpenses.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Henrik Drefs
 */
@UtilityClass
public class Tokenizer {

    public static String[] tokenize(String s) {
        return Arrays.stream(StringUtils.split(s, "\t\n\r ,.:;-\\/?!&()[]{}"))
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }

    public static List<String> tokenize(String[] lines) {
        final List<String> tokens = new ArrayList<>();
        for (String line : lines) {
            final String[] lineTokens = tokenize(line);
            tokens.addAll(Arrays.asList(lineTokens));
        }
        return tokens;
    }

}
