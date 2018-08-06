package pvytykac.net.scrape.model;

import org.hamcrest.Matcher;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class MatcherUtil {

    public static <T extends Enum<T>> Matcher<String> isEnum(T constant) {
        return is(constant == null
                ? null
                : constant.toString());
    }

    public static <T extends Enum<T>> Matcher<T> isEnum(String key, Class<T> enumClass) {
        T expected = Arrays.stream(enumClass.getEnumConstants())
                .filter(constant -> constant.toString().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown enum constant: '" + key + "'"));

        return new EnumMatcher<>(expected);
    }

}
