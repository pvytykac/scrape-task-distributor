package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

import java.util.List;

import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableList;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeTaskRepresentation {

    private String sessionUuid;
    private List<ScrapeTask> tasks;

    // used by jackson
    private ScrapeTaskRepresentation() {
    }

    private ScrapeTaskRepresentation(ScrapeTaskRepresentationBuilder builder) {
        this.sessionUuid = builder.getSessionUuid();
        this.tasks = builder.getTasks();
    }

    public String getSessionUuid() {
        return sessionUuid;
    }

    public List<ScrapeTask> getTasks() {
        return tasks;
    }
    
    public static final class ScrapeTaskRepresentationBuilder implements ModelBuilder<ScrapeTaskRepresentation> {
        
        private String sessionUuid;
        private List<ScrapeTask> tasks;

        private String getSessionUuid() {
            return sessionUuid;
        }

        private List<ScrapeTask> getTasks() {
            return asImmutableList(tasks);
        }

        public ScrapeTaskRepresentationBuilder withSessionUuid(String sessionUuid) {
            this.sessionUuid = sessionUuid;
			return this;
        }

        public ScrapeTaskRepresentationBuilder withTasks(List<ScrapeTask> tasks) {
            this.tasks = tasks;
			return this;
        }

        @Override
        public ScrapeTaskRepresentation build() {
            return new ScrapeTaskRepresentation(this);
        }
    }
}
