package net.pvytykac.scrape.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ModelBuilderUtil {

    // prevent instantiation
    ModelBuilderUtil() {}

    public static <T, U extends ModelBuilder<T>> T buildOptional(Optional<U> optional) {
        return optional
                .map(ModelBuilder::build)
                .orElse(null);
    }

    public static <T> Set<T> asImmutableSet(Set<T> original) {
        return original == null
                ? null
                : ImmutableSet.copyOf(original);
    }

    public static <K, V> Map<K, V> asImmutableMap(Map<K, V> original) {
        return original == null
                ? null
                : ImmutableMap.copyOf(original);
    }

    public static <T> List<T> asImmutableList(List<T> original) {
        return original == null
                ? null
                : ImmutableList.copyOf(original);
    }

}
