package pvytykac.net.scrape.model.v1;

import net.pvytykac.scrape.util.ModelBuilder;

public class Parameter {

	private ParameterType type;
	private String value;

	private Parameter() {}

	private Parameter(Builder builder) {
		this.type = builder.getType();
		this.value = builder.getValue();
	}

	public ParameterType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public static final class Builder implements ModelBuilder<Parameter> {

		private ParameterType type;
		private String value;

		public ParameterType getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		public Builder withType(ParameterType type) {
			this.type = type;
			return this;
		}

		public Builder withValue(String value) {
			this.value = value;
			return this;
		}

		@Override
		public Parameter build() {
			return new Parameter(this);
		}
	}
}
