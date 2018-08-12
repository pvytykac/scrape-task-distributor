package pvytykac.net.scrape.model.v1;

import org.hibernate.validator.constraints.NotBlank;
import pvytykac.net.scrape.model.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;

import javax.validation.constraints.NotNull;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class Scrape {

    @NotNull
    private ScrapeType type;

    @NotBlank
    private String target;

    @NotBlank
    private String storeAs;

    // used by jackson
    private Scrape() {
    }

    private Scrape(ScrapeBuilder builder) {
        this.type = builder.getType();
        this.target = builder.getTarget();
        this.storeAs = builder.getStoreAs();
    }

    public ScrapeType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public String getStoreAs() {
        return storeAs;
    }
    
    public static final class ScrapeBuilder implements ModelBuilder<Scrape> {

        private ScrapeType type;
        private String target;
        private String storeAs;

        private ScrapeType getType() {
            return type;
        }

        private String getTarget() {
            return target;
        }

        private String getStoreAs() {
            return storeAs;
        }

        public ScrapeBuilder withType(ScrapeType type) {
            this.type = type;
			return this;
        }

        public ScrapeBuilder withTarget(String target) {
            this.target = target;
			return this;
        }

        public ScrapeBuilder withStoreAs(String storeAs) {
            this.storeAs = storeAs;
			return this;
        }

        @Override
        public Scrape build() {
            return new Scrape(this);
        }
    }
}
