package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;
import pvytykac.net.scrape.model.v1.enums.Phase;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class FailedExpectation {

    private ExpectationType expectationType;
    private String expected;
    private String actual;
    private Phase phase;

    // used by jackson
    private FailedExpectation() {
    }

    private FailedExpectation(FailedExpectationBuilder builder) {
        this.expectationType = builder.getExpectationType();
        this.expected = builder.getExpected();
        this.actual = builder.getActual();
        this.phase = builder.getPhase();
    }

    public ExpectationType getExpectationType() {
        return expectationType;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }

    public Phase getPhase() {
        return phase;
    }

    public static final class FailedExpectationBuilder implements ModelBuilder<FailedExpectation> {

        private ExpectationType expectationType;
        private String expected;
        private String actual;
        private Phase phase;

        private ExpectationType getExpectationType() {
            return expectationType;
        }

        private String getExpected() {
            return expected;
        }

        private String getActual() {
            return actual;
        }

        private Phase getPhase() {
            return phase;
        }

        public FailedExpectationBuilder withExpectationType(ExpectationType expectationType) {
            this.expectationType = expectationType;
			return this;
        }

        public FailedExpectationBuilder withExpected(String expected) {
            this.expected = expected;
			return this;
        }

        public FailedExpectationBuilder withActual(String actual) {
            this.actual = actual;
			return this;
        }

        public FailedExpectationBuilder withPhase(Phase phase) {
            this.phase = phase;
			return this;
        }

        @Override
        public FailedExpectation build() {
            return new FailedExpectation(this);
        }

    }

}
