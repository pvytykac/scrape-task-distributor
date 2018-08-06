package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeExpectation {

    private ExpectationType type;
    private String expected;

    // used by jackson
    private ScrapeExpectation() {
    }

    private ScrapeExpectation(ScrapeExpectationBuilder builder) {
        this.type = builder.getType();
        this.expected = builder.getExpected();
    }

    public ExpectationType getType() {
        return type;
    }

    public String getExpected() {
        return expected;
    }

    public static final class ScrapeExpectationBuilder implements ModelBuilder<ScrapeExpectation> {

        private ExpectationType type;
        private String expected;

        private ExpectationType getType() {
            return type;
        }

        private String getExpected() {
            return expected;
        }

        public ScrapeExpectationBuilder withType(ExpectationType type) {
            this.type = type;
			return this;
        }

        public ScrapeExpectationBuilder withExpected(String expected) {
            this.expected = expected;
			return this;
        }

        @Override
        public ScrapeExpectation build() {
            return new ScrapeExpectation(this);
        }
    }
}
