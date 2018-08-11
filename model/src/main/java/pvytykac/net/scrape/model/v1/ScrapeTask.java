package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.TaskType;

import java.util.List;
import java.util.Map;

import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableList;
import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableMap;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeTask {

    private String taskUuid;
    private TaskType taskType;
    private Map<String, String> parameters;
    private List<ScrapeStep> steps;

    // used by jackson
    private ScrapeTask() {
    }

    private ScrapeTask(ScrapeTaskBuilder builder) {
        this.taskUuid = builder.getTaskUuid();
        this.parameters = builder.getParameters();
        this.steps = builder.getSteps();
        this.taskType = builder.getTaskType();
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Map<String, String> getParameters() {
        return asImmutableMap(parameters);
    }

    public List<ScrapeStep> getSteps() {
        return asImmutableList(steps);
    }
    
    public static final class ScrapeTaskBuilder implements ModelBuilder<ScrapeTask> {
        
        private String taskUuid;
        private TaskType taskType;
        private Map<String, String> parameters;
        private List<ScrapeStep> steps;

        private String getTaskUuid() {
            return taskUuid;
        }

        public TaskType getTaskType() {
            return taskType;
        }

        private Map<String, String> getParameters() {
            return parameters;
        }

        private List<ScrapeStep> getSteps() {
            return steps;
        }

        public ScrapeTaskBuilder withTaskType(TaskType taskType) {
            this.taskType = taskType;
            return this;
        }

        public ScrapeTaskBuilder withTaskUuid(String taskUuid) {
            this.taskUuid = taskUuid;
			return this;
        }

        public ScrapeTaskBuilder withParameters(Map<String, String> params) {
            this.parameters = params;
			return this;
        }

        public ScrapeTaskBuilder withSteps(List<ScrapeStep> steps) {
            this.steps = steps;
			return this;
        }

        @Override
        public ScrapeTask build() {
            return new ScrapeTask(this);
        }
    }
}
