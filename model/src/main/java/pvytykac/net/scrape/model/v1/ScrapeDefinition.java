package pvytykac.net.scrape.model.v1;

import java.util.List;

import net.pvytykac.scrape.util.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;

public class ScrapeDefinition {

	private ScrapeType scrapeType;
	private String storeAs;
	private String selector;
	private List<ScrapeDefinition> subDefinitions;

	private ScrapeDefinition() {}
	private ScrapeDefinition(Builder builder) {

	}

	public ScrapeType getScrapeType() {
		return scrapeType;
	}

	public String getStoreAs() {
		return storeAs;
	}

	public String getSelector() {
		return selector;
	}

	public List<ScrapeDefinition> getSubDefinitions() {
		return subDefinitions;
	}

	public static final class Builder implements ModelBuilder<ScrapeDefinition> {

		private ScrapeType scrapeType;
		private String storeAs;
		private String selector;
		private List<ScrapeDefinition> subDefinitions;

		private ScrapeType getScrapeType() {
			return scrapeType;
		}

		private String getStoreAs() {
			return storeAs;
		}

		private String getSelector() {
			return selector;
		}

		private List<ScrapeDefinition> getSubDefinitions() {
			return subDefinitions;
		}

		public Builder withScrapeType(ScrapeType scrapeType) {
			this.scrapeType = scrapeType;
			return this;
		}

		public Builder withStoreAs(String storeAs) {
			this.storeAs = storeAs;
			return this;
		}

		public Builder withSelector(String selector) {
			this.selector = selector;
			return this;
		}

		public Builder withSubDefinitions(List<ScrapeDefinition> subDefinitions) {
			this.subDefinitions = subDefinitions;
			return this;
		}

		@Override
		public ScrapeDefinition build() {
			return new ScrapeDefinition(this);
		}
	}
}
