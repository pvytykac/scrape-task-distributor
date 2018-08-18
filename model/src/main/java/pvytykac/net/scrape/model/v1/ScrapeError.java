package pvytykac.net.scrape.model.v1;

import static java.util.Optional.ofNullable;
import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableList;
import static pvytykac.net.scrape.model.ModelBuilderUtil.buildOptional;

import java.util.List;
import java.util.Optional;

import pvytykac.net.scrape.model.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeError {

    private ScrapeStep step;
    private ClientException clientException;
    private List<FailedExpectation> failedExpectations;

    // used by jackson
    private ScrapeError() {
    }

    private ScrapeError(ScrapeErrorBuilder builder) {
        this.step = builder.getScrapeStep();
        this.clientException = builder.getClientException();
        this.failedExpectations = builder.getFailedExpectations();
    }

    public ScrapeStep getStep() {
        return step;
    }

    public ClientException getClientException() {
        return clientException;
    }

    public List<FailedExpectation> getFailedExpectations() {
        return failedExpectations;
    }

    public static final class ScrapeErrorBuilder implements ModelBuilder<ScrapeError> {

        private Optional<ModelBuilder<ScrapeStep>> scrapeStepBuilder = Optional.empty();
        private Optional<ModelBuilder<ClientException>> clientExceptionBuilder = Optional.empty();
        private List<FailedExpectation> failedExpectations;
        private ScrapeStep step;

        private ScrapeStep getScrapeStep() {
            return scrapeStepBuilder.map(ModelBuilder::build)
                    .orElse(step);
        }

        private ClientException getClientException() {
            return buildOptional(clientExceptionBuilder);
        }

        private List<FailedExpectation> getFailedExpectations() {
            return asImmutableList(failedExpectations);
        }

        public ScrapeErrorBuilder withScrapeStep(ModelBuilder<ScrapeStep> scrapeStepBuilder) {
            this.scrapeStepBuilder = ofNullable(scrapeStepBuilder);
            return this;
        }

        public ScrapeErrorBuilder withScrapeStep(ScrapeStep step) {
            this.step = step;
            return this;
        }

        public ScrapeErrorBuilder withFailedExpectations(List<FailedExpectation> failedExpectations) {
            this.failedExpectations = failedExpectations;
            return this;
        }

        public ScrapeErrorBuilder withClientException(ModelBuilder<ClientException> clientExceptionBuilder) {
            this.clientExceptionBuilder = ofNullable(clientExceptionBuilder);
            return this;
        }

        @Override
        public ScrapeError build() {
            return new ScrapeError(this);
        }

	}

}
