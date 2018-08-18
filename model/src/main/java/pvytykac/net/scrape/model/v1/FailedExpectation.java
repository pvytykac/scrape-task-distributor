package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class FailedExpectation {

    private ScrapeExpectation expectation;
    private String actual;

    // used by jackson
    private FailedExpectation() {
    }

    private FailedExpectation(FailedExpectationBuilder builder) {
        this.expectation = builder.getExpectation();
        this.actual = builder.getActual();
    }

    public ScrapeExpectation getExpectation() {
        return expectation;
    }

    public String getActual() {
        return actual;
    }

    public static final class FailedExpectationBuilder implements ModelBuilder<FailedExpectation> {

        private ScrapeExpectation expectation;
        private String actual;

        private ScrapeExpectation getExpectation() {
            return this.expectation;
        }

        private String getActual() {
            return actual;
        }

        public FailedExpectationBuilder withExpectation(ScrapeExpectation expectation) {
            this.expectation = expectation;
			return this;
        }

        public FailedExpectationBuilder withActual(String actual) {
            this.actual = actual;
			return this;
        }

        @Override
        public FailedExpectation build() {
            return new FailedExpectation(this);
        }

    }

}
