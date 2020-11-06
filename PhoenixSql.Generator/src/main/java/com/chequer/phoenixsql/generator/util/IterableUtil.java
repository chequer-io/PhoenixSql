package com.chequer.phoenixsql.generator.util;

import java.util.function.Predicate;

public class IterableUtil {
    public static <T> boolean any(Iterable<T> iterable, Predicate<T> predicate) {
        for (final var element : iterable) {
            if (predicate.test(element)) {
                return true;
            }
        }

        return false;
    }
}
