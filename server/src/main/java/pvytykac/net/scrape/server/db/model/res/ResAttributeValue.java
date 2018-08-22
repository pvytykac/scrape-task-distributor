package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Paly
 * @since 2018-08-21
 */
@Entity
@Table(name = "res_attribute_value")
public class ResAttributeValue extends ResEnum {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attribute_id", referencedColumnName = "code")
    private ResAttribute attribute;

    private ResAttributeValue() {}

    private ResAttributeValue(Builder builder) {
        super(builder);
        this.attribute = builder.getAttribute();
    }

    public static final class Builder extends ResEnum.Builder<Builder, ResAttributeValue> {

        private ResAttribute attribute;

        public ResAttribute getAttribute() {
            return attribute;
        }

        public Builder withAttribute(ResAttribute attribute) {
            this.attribute = attribute;
            return this;
        }

        @Override
        public ResAttributeValue build() {
            return new ResAttributeValue(this);
        }
    }
}
