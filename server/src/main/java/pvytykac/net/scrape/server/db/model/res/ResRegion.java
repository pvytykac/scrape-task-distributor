package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "res_region")
public class ResRegion extends ResEnum {

    private ResRegion() {}

    private ResRegion(Builder builder) {
        super(builder);
    }

    public static class Builder extends ResEnum.Builder<Builder, ResRegion> {
        @Override
        public ResRegion build() {
            return new ResRegion(this);
        }
    }
}
