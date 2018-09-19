package pvytykac.net.scrape.model.v1;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;

import net.pvytykac.scrape.util.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.Type;

public class Parameter {

	private ParameterType type;
	private Object value;

	private Parameter() {}

	private Parameter(Builder builder) {
		this.type = builder.getType();
		this.value = builder.getValue();
	}

	public ParameterType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public static final class Builder implements ModelBuilder<Parameter> {

		private ParameterType type;
		private Object value;

		public ParameterType getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}

		public Builder withType(ParameterType type) {
			this.type = type;
			return this;
		}

		public Builder withValue(Object value) {
			this.value = value;
			return this;
		}

		@Override
		public Parameter build() {
			validate();
			return new Parameter(this);
		}

		private void validate() {
			Type rootType = getType().getRootType();
			Preconditions.checkState(getValue() == null
					|| (rootType == Type.LIST && getValue() instanceof Collection)
					|| (rootType == Type.LONG && getValue() instanceof Long || getValue() instanceof Integer)
					|| (rootType == Type.BOOLEAN && getValue() instanceof Boolean)
					|| (rootType == Type.DOUBLE && (getValue() instanceof Double || getValue() instanceof Float))
					|| (rootType == Type.STRING && (getValue() instanceof String))
					|| (rootType == Type.OBJECT && (getValue() instanceof Map)),
					"value '" + value + "' doesn't match the specified type '" + rootType + "'");
		}
	}
}
