package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

import java.util.List;

import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableList;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class PostScrapeStatusRepresentation {

    private List<PostScrapeAction> actions;

    // used by jackson
    private PostScrapeStatusRepresentation() {
    }

    private PostScrapeStatusRepresentation(PostScrapeStatusRepresentationBuilder builder) {
        this.actions = builder.getActions();
    }

    public List<PostScrapeAction> getActions() {
        return actions;
    }

    public static final class PostScrapeStatusRepresentationBuilder implements ModelBuilder<PostScrapeStatusRepresentation> {

        private List<PostScrapeAction> actions;

        private List<PostScrapeAction> getActions() {
            return asImmutableList(actions);
        }

        public PostScrapeStatusRepresentationBuilder withActions(List<PostScrapeAction> actions) {
            this.actions = actions;
            return this;
        }

        @Override
        public PostScrapeStatusRepresentation build() {
            return new PostScrapeStatusRepresentation(this);
        }
    }
}
