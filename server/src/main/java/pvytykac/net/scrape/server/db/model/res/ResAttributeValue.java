package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.pvytykac.scrape.util.ModelBuilder;
import pvytykac.net.scrape.server.db.repository.Dbo;

/**
 * @author Paly
 * @since 2018-08-21
 */
@Entity
@Table(name = "res_attribute_value")
public class ResAttributeValue implements Dbo<ResAttributeValueId> {

    @EmbeddedId
    private ResAttributeValueId id;

    @Column
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "attribute_id", referencedColumnName = "code", insertable = false, updatable = false)
	private ResAttribute attribute;

    private ResAttributeValue() {}

    private ResAttributeValue(Builder builder) {
        this.id = builder.getId();
        this.text = builder.getText();
        this.attribute = builder.getAttribute();
    }

    @Override
    public ResAttributeValueId getId() {
        return id;
    }

    public String getText() {
        return text;
    }

	public ResAttribute getAttribute() {
		return attribute;
	}

	public static final class Builder implements ModelBuilder<ResAttributeValue> {

        private String code;
        private ResAttribute attribute;
        private String text;

        public ResAttributeValueId getId() {
            return new ResAttributeValueId.Builder()
                    .withId(code)
                    .withAttribute(attribute.getId())
                    .build();
        }

        public String getText() {
            return text;
        }

        public Builder withCode(String code) {
            this.code = code;
            return this;
        }

		public ResAttribute getAttribute() {
			return attribute;
		}

		public Builder withAttribute(ResAttribute attribute) {
            this.attribute = attribute;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        @Override
        public ResAttributeValue build() {
            return new ResAttributeValue(this);
        }
    }
}
