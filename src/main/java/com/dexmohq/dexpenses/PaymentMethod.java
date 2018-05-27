package com.dexmohq.dexpenses;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
@Getter
public enum PaymentMethod {

    DEBIT(
            Pattern.compile("gegeben\\s*girocard", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^\\s*girocard\\s*$", Pattern.CASE_INSENSITIVE)
    ),
    CREDIT(
            Pattern.compile("gegeben\\s*VISA", Pattern.CASE_INSENSITIVE)
    ), CASH(
            Pattern.compile("gegeben\\s*bar", Pattern.CASE_INSENSITIVE)
    );

    private final List<Pattern> patterns;

    PaymentMethod(Pattern firstPattern, Pattern... patterns) {
        final List<Pattern> patternList = new ArrayList<>(patterns.length + 1);
        patternList.add(firstPattern);
        patternList.addAll(Arrays.asList(patterns));
        this.patterns = Collections.unmodifiableList(patternList);
    }

}
