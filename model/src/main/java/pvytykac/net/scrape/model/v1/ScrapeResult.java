package pvytykac.net.scrape.model.v1;

import pvytykac.net.scrape.model.ModelBuilder;

import java.util.Map;

import static pvytykac.net.scrape.model.ModelBuilderUtil.asImmutableMap;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ScrapeResult {

    private Integer statusCode;
    private String payload;
    private Map<String, String> headers;
    private String contentType;

    // used by jackson
    private ScrapeResult() {
    }

    private ScrapeResult(ScrapeResultBuilder builder) {
        this.statusCode = builder.getStatusCode();
        this.payload = builder.getPayload();
        this.headers = builder.getHeaders();
        this.contentType = builder.getContentType();
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getPayload() {
        return payload;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getContentType() {
        return contentType;
    }

    public static final class ScrapeResultBuilder implements ModelBuilder<ScrapeResult> {

        private Integer statusCode;
        private String payload;
        private Map<String, String> headers;
        private String contentType;

        private Integer getStatusCode() {
            return statusCode;
        }

        private String getPayload() {
            return payload;
        }

        private Map<String, String> getHeaders() {
            return asImmutableMap(headers);
        }

        private String getContentType() {
            return contentType;
        }

        public ScrapeResultBuilder withStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
			return  this;
        }

        public ScrapeResultBuilder withPayload(String payload) {
            this.payload = payload;
			return  this;
        }

        public ScrapeResultBuilder withHeaders(Map<String, String> headers) {
            this.headers = headers;
			return  this;
        }

        public ScrapeResultBuilder withContentType(String contentType) {
            this.contentType = contentType;
			return  this;
        }

        @Override
        public ScrapeResult build() {
            return new ScrapeResult(this);
        }

    }
}
