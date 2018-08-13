package pvytykac.net.scrape.model.v1;

import static java.util.Optional.ofNullable;
import static pvytykac.net.scrape.model.ModelBuilderUtil.buildOptional;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import pvytykac.net.scrape.model.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeResultRepresentation {

    @NotNull
    private String sessionUuid;

    @NotNull
    private String taskUuid;

    @NotNull
    private String taskType;

    @Valid
    private ScrapeResult result;

    @Valid
    private ScrapeError error;

    // used by jackson
    public ScrapeResultRepresentation() {}

    public ScrapeResultRepresentation(ScrapeResultRepresentationBuilder builder) {
        this.sessionUuid = builder.getSessionUuid();
        this.taskUuid = builder.getTaskUuid();
        this.taskType = builder.getTaskType();
        this.result = builder.getResult();
        this.error = builder.getError();
    }

    public String getSessionUuid() {
        return sessionUuid;
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public String getTaskType() {
        return taskType;
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
        private String taskType;
        private Optional<ModelBuilder<ScrapeResult>> resultBuilder = Optional.empty();
        private Optional<ModelBuilder<ScrapeError>> errorBuilder = Optional.empty();

        private String getSessionUuid() {
            return sessionUuid;
        }

        private String getTaskUuid() {
            return taskUuid;
        }

        private String getTaskType() {
            return taskType;
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

        public ScrapeResultRepresentationBuilder withTaskType(String taskType) {
            this.taskType = taskType;
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
