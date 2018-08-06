package pvytykac.net.scrape.model;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Objects;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class EnumMatcher<T extends Enum<T>> extends BaseMatcher<T> {

    private final Enum<T> expected;

    public EnumMatcher(Enum<T> expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object o) {
        return (o == null && expected == null) || (o != null
                && expected != null
                && o.getClass().isAssignableFrom(expected.getClass())
                && o == expected);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(Objects.toString(expected));
    }
}
