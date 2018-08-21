package net.pvytykac.scrape.util;

import java.util.Optional;

/**
 * @author Paly
 * @since 2018-08-06
 */
public interface ModelBuilder<T> {

    T build();

    default Optional<T> buildAsOptional() {
        return Optional.of(build());
    }

}
