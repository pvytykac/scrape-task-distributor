package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class TimeoutAction {

    private String taskType;
    private Long timeout;

    // used by jackson
    private TimeoutAction() {
    }

    private TimeoutAction(TimeoutActionBuilder builder) {
        this.taskType = builder.getTaskType();
        this.timeout = builder.getTimeout();
    }

    public String getTaskType() {
        return taskType;
    }

    public Long getTimeout() {
        return timeout;
    }

    public static final class TimeoutActionBuilder implements ModelBuilder<TimeoutAction> {

        private String taskType;
        private Long timeout;

        private String getTaskType() {
            return taskType;
        }

        private Long getTimeout() {
            return timeout;
        }

        public TimeoutActionBuilder withTaskType(String taskType) {
            this.taskType = taskType;
            return this;
        }

        public TimeoutActionBuilder withTimeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        @Override
        public TimeoutAction build() {
            return new TimeoutAction(this);
        }
    }
}
