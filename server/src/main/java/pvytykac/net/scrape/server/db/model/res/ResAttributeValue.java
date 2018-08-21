package pvytykac.net.scrape.server.db.model.res;

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

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private ResAttribute attribute;

    private ResAttributeValue() {}

    private ResAttributeValue(Builder builder) {
        super(builder);
    }

    public static final class Builder extends ResEnum.Builder<Builder, ResAttributeValue> {
        @Override
        public ResAttributeValue build() {
            return new ResAttributeValue(this);
        }
    }
}
