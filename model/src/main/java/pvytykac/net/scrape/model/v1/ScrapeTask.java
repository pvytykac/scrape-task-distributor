package pvytykac.net.scrape.model.v1;

import net.pvytykac.scrape.util.ModelBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableList;
import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableMap;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeTask {

    private String taskUuid;
    private String taskType;
    private Map<String, Parameter> parameters;
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

    public String getTaskType() {
        return taskType;
    }

    public Map<String, Parameter> getParameters() {
        return asImmutableMap(parameters);
    }

    public List<ScrapeStep> getSteps() {
        return asImmutableList(steps);
    }
    
    public static final class ScrapeTaskBuilder implements ModelBuilder<ScrapeTask> {
        
        private String taskUuid;
        private String taskType;
        private Map<String, Parameter> parameters;
        private List<ScrapeStep> steps;

        private String getTaskUuid() {
            return taskUuid;
        }

        public String getTaskType() {
            return taskType;
        }

        private Map<String, Parameter> getParameters() {
            return parameters;
        }

        private List<ScrapeStep> getSteps() {
            return steps;
        }

        public ScrapeTaskBuilder withTaskType(String taskType) {
            this.taskType = taskType;
            return this;
        }

        public ScrapeTaskBuilder withTaskUuid(String taskUuid) {
            this.taskUuid = taskUuid;
			return this;
        }

        public ScrapeTaskBuilder withParameters(Map<String, Parameter> params) {
            this.parameters = params;
			return this;
        }

        public ScrapeTaskBuilder addParameter(String key, Parameter parameter) {
            if (parameters == null) {
                parameters = new HashMap<>();
            }
            parameters.put(key, parameter);
            return this;
        }

        public ScrapeTaskBuilder withSteps(List<ScrapeStep> steps) {
            this.steps = steps;
			return this;
        }

        public ScrapeTaskBuilder addStep(ScrapeStep step) {
            if(this.steps == null) {
                this.steps = new ArrayList<>();
            }
            steps.add(step);
            return this;
        }

        @Override
        public ScrapeTask build() {
            return new ScrapeTask(this);
        }
    }
}
