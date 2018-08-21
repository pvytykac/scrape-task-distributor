package pvytykac.net.scrape.model.v1;


import net.pvytykac.scrape.util.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public final class ClientException {

    private String message;
    private String stackTrace;
    private Integer statusCode;
    private String payload;

    // used by jackson
    private ClientException() {
    }
    
    private ClientException(ClientExceptionBuilder builder) {
        this.message = builder.getMessage();
        this.stackTrace = builder.getStackTrace();
        this.statusCode = builder.getStatusCode();
        this.payload = builder.getPayload();
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getPayload() {
        return payload;
    }

    public static final class ClientExceptionBuilder implements ModelBuilder<ClientException> {
        
        private String message;
        private String stackTrace;
        private Integer statusCode;
        private String payload;

        private String getMessage() {
            return message;
        }

        private String getStackTrace() {
            return stackTrace;
        }

        private Integer getStatusCode() {
            return statusCode;
        }

        private String getPayload() {
            return payload;
        }

        public ClientExceptionBuilder withMessage(String message) {
            this.message = message;
			return this;
        }

        public ClientExceptionBuilder withStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
			return this;
        }

        public ClientExceptionBuilder withStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
			return this;
        }

        public ClientExceptionBuilder withPayload(String payload) {
            this.payload = payload;
			return this;
        }

        @Override
        public ClientException build() {
            return new ClientException(this);
        }

    }
    
}
