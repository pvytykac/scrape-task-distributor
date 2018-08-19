package pvytykac.net.scrape.server.service.impl;

import com.google.common.collect.ImmutableMap;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.ScrapeTaskConfiguration;
import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.service.ScrapeTypeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScrapeTypeServiceImpl implements ScrapeTypeService {

	private final Map<String, ScrapeMapper> mappers;

	public ScrapeTypeServiceImpl(List<ScrapeTaskConfiguration> configurations) {
		this.mappers = configurations.stream()
				.map(ScrapeMapper::new)
				.collect(Collectors.toMap(ScrapeMapper::getScrapeType, mapper -> mapper));
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

		private final String scrapeType;
		private final Set<Integer> supportedForms;
		private final List<ScrapeStep> steps;

		public ScrapeMapper(ScrapeTaskConfiguration configuration) {
			this.scrapeType = configuration.getScrapeType();
			this.supportedForms = configuration.getSupportedForms();
			this.steps = configuration.getStepDefinitions();
		}

		public String getScrapeType() {
			return scrapeType;
		}

		public boolean isApplicableTo(Integer form) {
			return supportedForms == null
					|| supportedForms.isEmpty()
					|| supportedForms.contains(form);
		}

		public ScrapeTask createTask(String ico) {
			return new ScrapeTask.ScrapeTaskBuilder()
					.withTaskUuid(UUID.randomUUID().toString())
					.withParameters(ImmutableMap.of("ico", ico))
					.withTaskType(scrapeType)
					.withSteps(steps)
					.build();
		}
	}
}
