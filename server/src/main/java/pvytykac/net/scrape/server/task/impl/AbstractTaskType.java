package pvytykac.net.scrape.server.task.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pvytykac.net.scrape.model.v1.ClientException;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.impl.RepositoryFacade;
import pvytykac.net.scrape.server.task.TaskType;

abstract class AbstractTaskType implements TaskType {

	private static final Logger LOG = LoggerFactory.getLogger(TaskType.class);

	private final String id;
	protected final RepositoryFacade facade;

	private String offsetIco;

	protected AbstractTaskType(String id, RepositoryFacade facade) {
		this.id = id;
		this.facade = facade;
	}

	@Override
	public final String getId() {
		return id;
	}

	@Override
	public final String getOffsetIco() {
		return offsetIco;
	}

	@Override
	public final ScrapeTask createScrapeTask(Ico ico) {
		boolean known = isKnownIco(ico);
		LOG.debug("creating task of type '{}' for ico '{}' with form '{}', known '{}'", id, ico.getId(), ico.getForm(),
				known);

		this.offsetIco = ico.getId();
		return known
				? createTaskForKnownIco(ico)
				: createTaskForNewIco(ico.getId());
	}

	@Override
	public final Status processClientError(ClientException error, ScrapeStep step) {
		LOG.debug("client error during step '{}' when processing task of type '{}': {}\n{}", step.getSequenceNumber(),
				error.getMessage(), error.getStackTrace());

		return onClientError(error, step);
	}

	@Override
	public final Status processFailedExpectations(List<FailedExpectation> errors, ScrapeStep step) {
		LOG.debug("found '{}' failed expectations during step '{}' when processing task of type '{}': {}",
				errors.size(), step.getSequenceNumber(), id, errors.stream()
						.map(fe -> String.format("{id:'%d', value: '%s')", fe.getExpectation().getId(), fe.getActual()))
						.toArray());

		return onExpectationsFailed(errors, step);
	}

	@Override
	public final Status processSuccess(ScrapeResult result) {
		boolean valid = isResultResponseValid(result);
		LOG.debug("processing scrape result for type '{}': valid '{}'", id, valid);

		return valid
				? onSuccess(result)
				: onInvalidResult(result);
	}

	protected abstract boolean isKnownIco(Ico ico);
	protected abstract boolean isResultResponseValid(ScrapeResult result);
	protected abstract ScrapeTask createTaskForKnownIco(Ico ico);
	protected abstract ScrapeTask createTaskForNewIco(String ico);
	protected abstract Status onClientError(ClientException error, ScrapeStep step);
	protected abstract Status onExpectationsFailed(List<FailedExpectation> errors, ScrapeStep step);
	protected abstract Status onSuccess(ScrapeResult result);
	protected abstract Status onInvalidResult(ScrapeResult result);
}
