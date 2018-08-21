package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "res_unit")
public class ResUnit extends ResEnum {

    private ResUnit() {
    }

    private ResUnit(Builder builder) {
        super(builder);
    }

    public static class Builder extends ResEnum.Builder<Builder, ResUnit> {
        @Override
        public ResUnit build() {
            return new ResUnit(this);
        }
    }
}
