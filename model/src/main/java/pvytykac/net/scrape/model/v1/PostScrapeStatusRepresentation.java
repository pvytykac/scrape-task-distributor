package pvytykac.net.scrape.model.v1;

import net.pvytykac.scrape.util.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class PostScrapeStatusRepresentation {

    private TimeoutAction timeoutAction;
    private Boolean retry;

    // used by jackson
    private PostScrapeStatusRepresentation() {}

    private PostScrapeStatusRepresentation(PostScrapeStatusRepresentationBuilder builder) {
        this.timeoutAction = builder.getTimeoutAction();
        this.retry = builder.isRetry();
    }

    public TimeoutAction getTimeoutAction() {
        return timeoutAction;
    }

    public Boolean getRetry() {
        return retry;
    }

    public static final class PostScrapeStatusRepresentationBuilder implements ModelBuilder<PostScrapeStatusRepresentation> {

        private TimeoutAction timeoutAction;
        private boolean retry = false;

        private TimeoutAction getTimeoutAction() {
            return timeoutAction;
        }

        public boolean isRetry() {
            return retry;
        }

        public PostScrapeStatusRepresentationBuilder withTimeoutAction(TimeoutAction timeoutAction) {
            this.timeoutAction = timeoutAction;
            return this;
        }

        public PostScrapeStatusRepresentationBuilder withRetry(boolean retry) {
            this.retry = retry;
            return this;
        }

        @Override
        public PostScrapeStatusRepresentation build() {
            return new PostScrapeStatusRepresentation(this);
        }
    }
}
