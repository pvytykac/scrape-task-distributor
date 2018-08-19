package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class PostScrapeStatusRepresentation {

    private TimeoutAction timeoutAction;

    // used by jackson
    private PostScrapeStatusRepresentation() {
    }

    private PostScrapeStatusRepresentation(PostScrapeStatusRepresentationBuilder builder) {
        this.timeoutAction = builder.getTimeoutAction();
    }

    public TimeoutAction getTimeoutAction() {
        return timeoutAction;
    }

    public static final class PostScrapeStatusRepresentationBuilder implements ModelBuilder<PostScrapeStatusRepresentation> {

        private TimeoutAction timeoutAction;

        private TimeoutAction getTimeoutAction() {
            return timeoutAction;
        }

        public PostScrapeStatusRepresentationBuilder withTimeoutAction(TimeoutAction timeoutAction) {
            this.timeoutAction = timeoutAction;
            return this;
        }

        @Override
        public PostScrapeStatusRepresentation build() {
            return new PostScrapeStatusRepresentation(this);
        }
    }
}
