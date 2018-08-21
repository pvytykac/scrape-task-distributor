package pvytykac.net.scrape.model.v1;

import net.pvytykac.scrape.util.ModelBuilder;

import java.util.List;

import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableList;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeSessionRepresentation {

    private String sessionUuid;
    private List<ScrapeTask> tasks;

    // used by jackson
    private ScrapeSessionRepresentation() {
    }

    private ScrapeSessionRepresentation(ScrapeTaskRepresentationBuilder builder) {
        this.sessionUuid = builder.getSessionUuid();
        this.tasks = builder.getTasks();
    }

    public String getSessionUuid() {
        return sessionUuid;
    }

    public List<ScrapeTask> getTasks() {
        return tasks;
    }
    
    public static final class ScrapeTaskRepresentationBuilder implements ModelBuilder<ScrapeSessionRepresentation> {
        
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
        public ScrapeSessionRepresentation build() {
            return new ScrapeSessionRepresentation(this);
        }
    }
}
