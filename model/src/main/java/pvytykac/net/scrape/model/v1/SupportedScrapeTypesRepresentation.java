package pvytykac.net.scrape.model.v1;

import net.pvytykac.scrape.util.ModelBuilder;

import java.util.Set;

import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableSet;

public class SupportedScrapeTypesRepresentation {

	private Set<String> supportedScrapeTypes;

	// used by jackson
	private SupportedScrapeTypesRepresentation() {}

	private SupportedScrapeTypesRepresentation(SupportedScrapeTypesRepresentationBuilder builder) {
		this.supportedScrapeTypes = builder.getSupportedScrapeTypes();
	}

	public Set<String> getSupportedScrapeTypes() {
		return supportedScrapeTypes;
	}

	public static final class SupportedScrapeTypesRepresentationBuilder implements ModelBuilder<SupportedScrapeTypesRepresentation> {

		private Set<String> supportedScrapeTypes;

		private Set<String> getSupportedScrapeTypes() {
			return asImmutableSet(supportedScrapeTypes);
		}

		public SupportedScrapeTypesRepresentationBuilder withSupportedScrapeTypes(Set<String> supportedScrapeTypes) {
			this.supportedScrapeTypes = supportedScrapeTypes;
			return this;
		}

		@Override
		public SupportedScrapeTypesRepresentation build() {
			return new SupportedScrapeTypesRepresentation(this);
		}
	}
}
