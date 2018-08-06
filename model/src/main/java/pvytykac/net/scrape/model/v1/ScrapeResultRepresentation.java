package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static pvytykac.net.scrape.model.ModelBuilderUtil.buildOptional;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeResultRepresentation {

    private String sessionUuid;
    private String taskUuid;
    private ScrapeResult result;
    private ScrapeError error;

    // used by jackson
    public ScrapeResultRepresentation() {
    }

    public ScrapeResultRepresentation(ScrapeResultRepresentationBuilder builder) {
        this.sessionUuid = builder.getSessionUuid();
        this.taskUuid = builder.getTaskUuid();
        this.result = builder.getResult();
        this.error = builder.getError();
    }

    public String getSessionUuid() {
        return sessionUuid;
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public ScrapeResult getResult() {
        return result;
    }

    public ScrapeError getError() {
        return error;
    }

    public static final class ScrapeResultRepresentationBuilder implements ModelBuilder<ScrapeResultRepresentation> {

        private String sessionUuid;
        private String taskUuid;
        private Optional<ModelBuilder<ScrapeResult>> resultBuilder = Optional.empty();
        private Optional<ModelBuilder<ScrapeError>> errorBuilder = Optional.empty();

        private String getSessionUuid() {
            return sessionUuid;
        }

        private String getTaskUuid() {
            return taskUuid;
        }

        private ScrapeResult getResult() {
            return buildOptional(resultBuilder);
        }

        private ScrapeError getError() {
            return buildOptional(errorBuilder);
        }

        public ScrapeResultRepresentationBuilder withSessionId(String sessionId) {
            this.sessionUuid = sessionId;
			return this;
        }

        public ScrapeResultRepresentationBuilder withTaskId(String taskId) {
            this.taskUuid = taskId;
			return this;
        }

        public ScrapeResultRepresentationBuilder withResult(ModelBuilder<ScrapeResult> resultBuilder) {
            this.resultBuilder = ofNullable(resultBuilder);
			return this;
        }

        public ScrapeResultRepresentationBuilder withError(ModelBuilder<ScrapeError> errorBuilder) {
            this.errorBuilder = ofNullable(errorBuilder);
			return this;
        }

        @Override
        public ScrapeResultRepresentation build() {
            return new ScrapeResultRepresentation(this);
        }

    }

}
