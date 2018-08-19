package pvytykac.net.scrape.server.service;

import java.util.List;

import pvytykac.net.scrape.model.v1.ClientException;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeStep;

/**
 * @author Paly
 * @since 2018-08-19
 */
public interface ScrapeResultHandler {

    Status processClientError(ClientException error, ScrapeStep step);
    Status processFailedExpectations(List<FailedExpectation> errors, ScrapeStep step);
    Status processSuccess(ScrapeResult result);

    final class Status {
        private final Long timeout;
        private final boolean retriable;

        public Status(Long timeout, boolean retriable) {
            this.timeout = timeout;
            this.retriable = retriable;
        }

        public Long getTimeout() {
            return timeout;
        }

        public boolean isRetriable() {
            return retriable;
        }
    }

}
