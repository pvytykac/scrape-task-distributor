package pvytykac.net.scrape.server.db.model.res;

import pvytykac.net.scrape.server.db.model.DboBuilder;
import pvytykac.net.scrape.server.db.repository.Dbo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Paly
 * @since 2018-08-21
 */
@Entity
@Table(name = "res_attribute")
public class ResAttribute implements Dbo<Integer> {

    @Id
    private Integer id;

    @Column
    private String name;

    private ResAttribute() {}

    private ResAttribute(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder extends DboBuilder<Builder, Integer, ResAttribute> {

        private String name;

        public String getName() {
            return name;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public ResAttribute build() {
            return new ResAttribute(this);
        }
    }
}
