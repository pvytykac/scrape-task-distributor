package pvytykac.net.scrape.server.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.ScrapeTaskConfiguration;
import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.service.ScrapeTypeService;

public class ScrapeTypeServiceImpl implements ScrapeTypeService {

	private final Map<String, ScrapeMapper> mappers;

	public ScrapeTypeServiceImpl(List<ScrapeTaskConfiguration> configurations) {
		this.mappers = Collections.emptyMap();
	}

	@Override
	public Set<String> getScrapeTypes() {
		return mappers.keySet();
	}

	@Override
	public Optional<ScrapeTask> createScrapeTask(Ico ico, String scrapeType) {
		return Optional.ofNullable(mappers.get(scrapeType))
				.filter(mapper -> mapper.isApplicableTo(ico.getForm()))
				.map(mapper -> mapper.createTask(ico.getIco()));
	}

	@Override
	public Stream<ScrapeTask> createScrapeTasks(Ico ico) {
		return mappers.keySet().stream()
			.map(scrapeType -> createScrapeTask(ico, scrapeType))
			.filter(Optional::isPresent)
			.map(Optional::get);
	}

	private static final class ScrapeMapper {

		private final Set<Integer> supportedForms;

		public ScrapeMapper(Set<Integer> supportedForms) {
			this.supportedForms = supportedForms;
		}

		public boolean isApplicableTo(Integer form) {
			return supportedForms.contains(form);
		}

		public ScrapeTask createTask(String ico) {
			return new ScrapeTask.ScrapeTaskBuilder()
					.build();
		}
	}
}
