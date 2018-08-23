package pvytykac.net.scrape.server.db.model.res;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import net.pvytykac.scrape.util.ModelBuilder;

@Embeddable
public class ResAttributeValueId implements Serializable {

	@Column(name = "code")
	private String id;

	@Column(name = "attribute_id")
	private String attributeId;

	private ResAttributeValueId() {}

	private ResAttributeValueId(Builder builder) {
		this.id = builder.getId();
		this.attributeId = builder.getAttributeId();
	}

	public String getId() {
		return id;
	}

	public String getAttributeId() {
		return attributeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ResAttributeValueId that = (ResAttributeValueId) o;
		return Objects.equals(id, that.id) && Objects.equals(attributeId, that.attributeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, attributeId);
	}

	public static class Builder implements ModelBuilder<ResAttributeValueId> {

		private String id;
		private String attributeId;

		public String getId() {
			return id;
		}

		public String getAttributeId() {
			return attributeId;
		}

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder withAttribute(String attributeId) {
			this.attributeId = attributeId;
			return this;
		}

		@Override
		public ResAttributeValueId build() {
			return new ResAttributeValueId(this);
		}
	}

}