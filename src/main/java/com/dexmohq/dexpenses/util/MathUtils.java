package com.dexmohq.dexpenses.util;

import lombok.experimental.UtilityClass;

/**
 * @author Henrik Drefs
 */
@UtilityClass
public class MathUtils {

    public static int min(int i, int... ints) {
        int min = i;
        for (int j : ints) {
            if (j < min) {
                min = j;
            }
        }
        return min;
    }

}
