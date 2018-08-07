package pvytykac.net.scrape.server;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import java.util.List;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeTaskDistributorConfiguration extends Configuration {

	@Valid
	@NotNull
	private DataSourceFactory database;

	private List<ScrapeTaskConfiguration> scrapeTaskConfiguration;

	public DataSourceFactory getDatabase() {
		return database;
	}

	public List<ScrapeTaskConfiguration> getScrapeTaskConfiguration() {
		return scrapeTaskConfiguration;
	}
}
