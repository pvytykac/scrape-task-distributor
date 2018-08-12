package net.pvytykac.scrape.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author Paly
 * @since 2018-08-12
 */
public class CollectionsUtil {

    public static <T> void removeAndProcessAllAboveLimit(List<T> list, int limit, Consumer<T> consumer) {
        while (limit < list.size()) {
            consumer.accept(list.remove(limit));
        }
    }

    public static <T> boolean containsDuplicates(Collection<T> collection) {
        return collection != null
                && collection.size() != streamNonNullDistinct(collection)
                        .count();
    }

    public static <T> boolean containsNulls(Collection<T> collection) {
        return collection != null
                && collection.size() != streamNonNull(collection).count();
    }

    public static boolean isSequential(Collection<Integer> collection) {
        long size = streamNonNullDistinct(collection).count();

        long sum = streamNonNullDistinct(collection)
                .reduce(0, (acc, cur) -> acc + cur);

        long expectedSum = LongStream.range(1, size + 1)
                .sum();

        return expectedSum == sum;
    }

    private static <T> Stream<T> streamNonNull(Collection<T> collection) {
        return collection.stream().filter(Objects::nonNull);
    }

    private static <T> Stream<T> streamNonNullDistinct(Collection<T> collection) {
        return streamNonNull(collection)
                .distinct();
    }

}
