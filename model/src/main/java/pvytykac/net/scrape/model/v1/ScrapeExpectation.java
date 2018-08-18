package pvytykac.net.scrape.model.v1;

import org.hibernate.validator.constraints.NotBlank;
import pvytykac.net.scrape.model.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;
import pvytykac.net.scrape.model.v1.enums.Operator;

import javax.validation.constraints.NotNull;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeExpectation {

    @NotNull
    private ExpectationType type;

    private String target;

    @NotBlank
    private String expectedValue;

    @NotNull
    private Operator operator;

    @NotNull
    private Boolean expected;

    // used by jackson
    private ScrapeExpectation() {
    }

    private ScrapeExpectation(ScrapeExpectationBuilder builder) {
        this.type = builder.getType();
        this.target = builder.getTarget();
        this.expectedValue = builder.getExpectedValue();
        this.operator = builder.getOperator();
        this.expected = builder.getExpected();
    }

    public ExpectationType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public Operator getOperator() {
        return operator;
    }

    public Boolean getExpected() {
        return expected;
    }

    public static final class ScrapeExpectationBuilder implements ModelBuilder<ScrapeExpectation> {

        private ExpectationType type;
        private String target;
        private String expectedValue;
        private Operator operator;
        private Boolean expected;

        private ExpectationType getType() {
            return type;
        }

        private String getExpectedValue() {
            return expectedValue;
        }

        private Operator getOperator() {
            return operator;
        }

        private Boolean getExpected() {
            return expected;
        }

        private String getTarget() {
            return target;
        }

        public ScrapeExpectationBuilder withType(ExpectationType type) {
            this.type = type;
			return this;
        }

        public ScrapeExpectationBuilder withTarget(String target) {
            this.target = target;
            return this;
        }

        public ScrapeExpectationBuilder withExpectedValue(String expectedValue) {
            this.expectedValue = expectedValue;
			return this;
        }

        public ScrapeExpectationBuilder withOperator(Operator operator) {
            this.operator = operator;
            return this;
        }

        public ScrapeExpectationBuilder withExpected(Boolean expected) {
            this.expected = expected;
            return this;
        }

        @Override
        public ScrapeExpectation build() {
            return new ScrapeExpectation(this);
        }
    }
}
