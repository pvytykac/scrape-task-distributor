package net.pvytykac.scrape.util;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Paly
 * @since 2018-08-12
 */
public class CollectionsUtil {

    public static <T> void removeAndProcessAllAboveLimit(List<T> list, int limit, Consumer<T> consumer) {
        while (limit <= list.size()) {
            consumer.accept(list.remove(limit));
        }
    }

}
