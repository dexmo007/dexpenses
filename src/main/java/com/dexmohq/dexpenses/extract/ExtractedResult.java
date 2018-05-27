package com.dexmohq.dexpenses.extract;

import lombok.Value;

/**
 * @author Henrik Drefs
 */
@Value
public class ExtractedResult<T> {
    private final T value;
    private final int line;

    public static <T> ExtractedResult<T> empty() {
        return new ExtractedResult<>(null, Integer.MAX_VALUE);
    }
}
