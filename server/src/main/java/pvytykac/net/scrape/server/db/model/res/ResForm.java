package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "res_form")
public class ResForm extends ResEnum {

    private ResForm() {
        super();
    }

    private ResForm(Builder builder) {
        super(builder);
    }

    public static class Builder extends ResEnum.Builder<Builder, ResForm> {
        @Override
        public ResForm build() {
            return new ResForm(this);
        }
    }

}
