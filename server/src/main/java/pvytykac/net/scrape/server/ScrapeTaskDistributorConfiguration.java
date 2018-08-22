package pvytykac.net.scrape.server;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeTaskDistributorConfiguration extends Configuration {

	@Valid
	@NotNull
	private DataSourceFactory database;

	@Valid
	@NotEmpty
	@NotNull
	private List<TaskTypeConfiguration> taskTypes;

	public DataSourceFactory getDatabase() {
		return database;
	}

	public List<TaskTypeConfiguration> getTaskTypes() {
		return taskTypes;
	}
}
