package pvytykac.net.scrape.model.v1;

import net.pvytykac.scrape.util.ModelBuilder;
import pvytykac.net.scrape.model.v1.enums.Type;

public class ParameterType {

	private Type rootType;
	private ParameterType elementType;

	private ParameterType() {}

	private ParameterType(Builder builder) {
		this.rootType = builder.getRootType();
		this.elementType = builder.getElementType();
	}
	
	public Type getRootType() {
		return rootType;
	}

	public ParameterType getElementType() {
		return elementType;
	}

	public static final class Builder implements ModelBuilder<ParameterType> {

		private Type rootType;
		private ParameterType elementType;

		private Type getRootType() {
			return rootType;
		}

		private ParameterType getElementType() {
			return elementType;
		}

		public Builder withRootType(Type rootType) {
			this.rootType = rootType;
			return this;
		}

		public Builder withElementType(ParameterType elementType) {
			this.elementType = elementType;
			return this;
		}

		@Override
		public ParameterType build() {
			return new ParameterType(this);
		}
	}
}
