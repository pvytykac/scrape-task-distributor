package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

import java.util.List;
import java.util.Map;

import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableList;
import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableMap;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeStep {

    private Integer sequenceNumber;
    private String uri;
    private String method;
    private String contentType;
    private Map<String, String> queryParameters;
    private Map<String, String> headers;
    private String payload;
    private List<ScrapeExpectation> expectations;
    private List<Scrape> scrape;

    // used by jackson
    private ScrapeStep() {
    }

    private ScrapeStep(ScrapeStepBuilder builder) {
        this.sequenceNumber = builder.getSequenceNumber();
        this.uri = builder.getUri();
        this.method = builder.getMethod();
        this.contentType = builder.getContentType();
        this.queryParameters = builder.getQueryParameters();
        this.headers = builder.getHeaders();
        this.payload = builder.getPayload();
        this.expectations = builder.getExpectations();
        this.scrape = builder.getScrape();
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getPayload() {
        return payload;
    }

    public List<ScrapeExpectation> getExpectations() {
        return expectations;
    }

    public List<Scrape> getScrape() {
        return scrape;
    }

    public static final class ScrapeStepBuilder implements ModelBuilder<ScrapeStep> {

        private Integer sequenceNumber;
        private String uri;
        private String method;
        private String contentType;
        private Map<String, String> queryParameters;
        private Map<String, String> headers;
        private String payload;
        private List<ScrapeExpectation> expectations;
        private List<Scrape> scrape;

        private Integer getSequenceNumber() {
            return sequenceNumber;
        }

        private String getUri() {
            return uri;
        }

        private String getMethod() {
            return method;
        }

        private String getContentType() {
            return contentType;
        }

        private Map<String, String> getQueryParameters() {
            return asImmutableMap(queryParameters);
        }

        private Map<String, String> getHeaders() {
            return asImmutableMap(headers);
        }

        private String getPayload() {
            return payload;
        }

        private List<ScrapeExpectation> getExpectations() {
            return asImmutableList(expectations);
        }

        private List<Scrape> getScrape() {
            return asImmutableList(scrape);
        }

        public ScrapeStepBuilder withSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public ScrapeStepBuilder withUri(String uri) {
            this.uri = uri;
            return this;
        }

        public ScrapeStepBuilder withMethod(String method) {
            this.method = method;
            return this;
        }

        public ScrapeStepBuilder withContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public ScrapeStepBuilder withQueryParameters(Map<String, String> queryParameters) {
            this.queryParameters = queryParameters;
            return this;
        }

        public ScrapeStepBuilder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public ScrapeStepBuilder withPayload(String payload) {
            this.payload = payload;
            return this;
        }

        public ScrapeStepBuilder withExpectations(List<ScrapeExpectation> expectations) {
            this.expectations = expectations;
            return this;
        }

        public ScrapeStepBuilder withScrape(List<Scrape> scrape) {
            this.scrape = scrape;
            return this;
        }

        @Override
        public ScrapeStep build() {
            return new ScrapeStep(this);
        }

    }

}
