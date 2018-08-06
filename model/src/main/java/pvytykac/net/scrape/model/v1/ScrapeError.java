package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static pvytykac.net.scrape.model.ModelBuilderUtil.buildOptional;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeError {

    private ScrapeStep step;
    private ClientException clientException;
    private FailedExpectation failedExpectation;

    // used by jackson
    private ScrapeError() {
    }

    private ScrapeError(ScrapeErrorBuilder builder) {
        this.step = builder.getScrapeStep();
        this.clientException = builder.getClientException();
        this.failedExpectation = builder.getFailedExpectationBuilder();
    }

    public ScrapeStep getStep() {
        return step;
    }

    public ClientException getClientException() {
        return clientException;
    }

    public FailedExpectation getFailedExpectation() {
        return failedExpectation;
    }

    public static final class ScrapeErrorBuilder implements ModelBuilder<ScrapeError> {

        private Optional<ModelBuilder<ScrapeStep>> scrapeStepBuilder = Optional.empty();
        private Optional<ModelBuilder<ClientException>> clientExceptionBuilder = Optional.empty();
        private Optional<ModelBuilder<FailedExpectation>> failedExpectationBuilder = Optional.empty();

        private ScrapeStep getScrapeStep() {
            return buildOptional(scrapeStepBuilder);
        }

        private ClientException getClientException() {
            return buildOptional(clientExceptionBuilder);
        }

        private FailedExpectation getFailedExpectationBuilder() {
            return buildOptional(failedExpectationBuilder);
        }

        public ScrapeErrorBuilder withScrapeStep(ModelBuilder<ScrapeStep> scrapeStepBuilder) {
            this.scrapeStepBuilder = ofNullable(scrapeStepBuilder);
            return this;
        }

        public ScrapeErrorBuilder withClientException(ModelBuilder<ClientException> clientExceptionBuilder) {
            this.clientExceptionBuilder = ofNullable(clientExceptionBuilder);
            return this;
        }

        public ScrapeErrorBuilder withFailedExpectation(ModelBuilder<FailedExpectation> failedExpectationBuilder) {
            this.failedExpectationBuilder = ofNullable(failedExpectationBuilder);
            return this;
        }

        @Override
        public ScrapeError build() {
            return new ScrapeError(this);
        }

    }

}
