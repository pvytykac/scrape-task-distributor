package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;

/**
 * @author Paly
 * @since 2018-08-19
 */
public interface ScrapeResultHandler {

    ErrorStatus processError(ScrapeError scrapeError);
    SuccessStatus processSuccess(ScrapeResult scrapeError);

    final class ErrorStatus {
        private final Long timeout;
        private final boolean retriable;

        public ErrorStatus(Long timeout, boolean retriable) {
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

    final class SuccessStatus {
    }

}
