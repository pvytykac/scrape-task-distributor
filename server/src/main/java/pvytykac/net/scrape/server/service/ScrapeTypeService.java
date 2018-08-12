package pvytykac.net.scrape.server.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.db.model.Ico;

public interface ScrapeTypeService {

	Set<String> getScrapeTypes();

	Optional<ScrapeTask> createScrapeTask(Ico ico, String scrapeType);

	Stream<ScrapeTask> createScrapeTasks(Ico ico);

}
