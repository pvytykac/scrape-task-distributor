package pvytykac.net.scrape.model.v1;

import javax.validation.constraints.NotNull;

import net.pvytykac.scrape.util.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;
import pvytykac.net.scrape.model.v1.enums.Operator;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeExpectation {

    @NotNull
    private Integer id;

    @NotNull
    private ExpectationType type;

    private String target;

    private String expectedValue;

    @NotNull
    private Operator operator;

    @NotNull
    private Boolean expected;

    // used by jackson
    private ScrapeExpectation() {
    }

    private ScrapeExpectation(ScrapeExpectationBuilder builder) {
        this.id = builder.getId();
        this.type = builder.getType();
        this.target = builder.getTarget();
        this.expectedValue = builder.getExpectedValue();
        this.operator = builder.getOperator();
        this.expected = builder.getExpected();
    }

    public Integer getId() {
        return id;
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

        private Integer id;
        private ExpectationType type;
        private String target;
        private String expectedValue;
        private Operator operator;
        private Boolean expected;

        private Integer getId() {
            return id;
        }

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

        public ScrapeExpectationBuilder withId(Integer id) {
            this.id = id;
            return this;
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
