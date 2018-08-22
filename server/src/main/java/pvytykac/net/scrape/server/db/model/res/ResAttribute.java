package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Paly
 * @since 2018-08-21
 */
@Entity
@Table(name = "res_attribute")
public class ResAttribute extends ResEnum {

    private ResAttribute() {}

    private ResAttribute(Builder builder) {
        super(builder);
    }

    public static class Builder extends ResEnum.Builder<Builder, ResAttribute> {
        @Override
        public ResAttribute build() {
            return new ResAttribute(this);
        }
    }
}
