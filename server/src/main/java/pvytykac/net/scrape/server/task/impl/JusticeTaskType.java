package pvytykac.net.scrape.server.task.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import pvytykac.net.scrape.model.v1.ClientException;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.impl.RepositoryFacade;

public class JusticeTaskType extends AbstractTaskType {

	public JusticeTaskType(String id, RepositoryFacade facade, String offsetIco) {
		super(id, facade, offsetIco);
	}

	@Override
	protected boolean isKnownIco(Ico ico) {
		return false;
	}

	@Override
	protected boolean isResultResponseValid(ScrapeResult result) {
		return false;
	}

	@Override
	protected ScrapeTask createTaskForKnownIco(Ico ico) {
		return null;
	}

	@Override
	protected ScrapeTask createTaskForNewIco(String ico) {
		return null;
	}

	@Override
	protected Status onClientError(ClientException error, ScrapeStep step) {
		return null;
	}

	@Override
	protected Status onExpectationsFailed(List<FailedExpectation> errors, ScrapeStep step) {
		return null;
	}

	@Override
	protected Status onSuccess(ScrapeResult result) {
		return null;
	}

	@Override
	protected Status onInvalidResult(ScrapeResult result) {
		return null;
	}

	@Override
	public Set<Integer> getApplicableFormIds() {
		return Collections.emptySet();
	}
}
