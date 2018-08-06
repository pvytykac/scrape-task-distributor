package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.ActionType;

import java.util.Map;

import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableMap;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class PostScrapeAction {

    private ActionType actionType;
    private Map<String, String> parameters;

    // used by jackson
    private PostScrapeAction() {
    }

    private PostScrapeAction(PostScrapeActionBuilder builder) {
        this.actionType = builder.getActionType();
        this.parameters = builder.getParameters();
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public static final class PostScrapeActionBuilder implements ModelBuilder<PostScrapeAction> {

        private ActionType actionType;
        private Map<String, String> parameters;

        private ActionType getActionType() {
            return actionType;
        }

        private Map<String, String> getParameters() {
            return asImmutableMap(parameters);
        }

        public PostScrapeActionBuilder withActionType(ActionType actionType) {
            this.actionType = actionType;
            return this;
        }

        public PostScrapeActionBuilder withParameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        @Override
        public PostScrapeAction build() {
            return new PostScrapeAction(this);
        }
    }
}
