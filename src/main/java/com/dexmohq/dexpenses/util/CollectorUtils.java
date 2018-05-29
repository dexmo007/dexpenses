package com.dexmohq.dexpenses.util;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Henrik Drefs
 */
@UtilityClass
public class CollectorUtils {

    public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> toLinkedMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(keyMapper, valueMapper,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new);
    }

}
