package pvytykac.net.scrape.server.db.model.ico;

import org.joda.time.DateTime;
import pvytykac.net.scrape.server.db.model.DboBuilder;
import pvytykac.net.scrape.server.db.repository.Dbo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Paly
 * @since 2018-08-11
 */
@Entity
@Table(name = "icos")
public class Ico implements Dbo<String> {

    @Id
    @Column(name = "ico")
    private String id;

    @Column
    private Integer form;

    @Column(name = "last_updated")
    private DateTime lastUpdated;

    @Column(name = "res_id")
    private Integer resId;

    private Ico() {}

    private Ico(Builder builder) {
        this.id = builder.getId();
        this.form = builder.getForm();
        this.lastUpdated = builder.getLastUpdated();
        this.resId = builder.getResId();
    }

    @Override
    public String getId() {
        return id;
    }

    public Integer getForm() {
        return form;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public Integer getResId() {
        return resId;
    }

    public static class Builder extends DboBuilder<Builder, String, Ico> {

        private Integer form;
        private DateTime lastUpdated;
        private Integer resId;

        public Integer getForm() {
            return form;
        }

        public DateTime getLastUpdated() {
            return lastUpdated;
        }

        public Integer getResId() {
            return resId;
        }

        public Builder withForm(Integer form) {
            this.form = form;
            return this;
        }

        public Builder withLastUpdated(DateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder withResId(Integer resId) {
            this.resId = resId;
            return this;
        }

        @Override
        public Ico build() {
            return new Ico(this);
        }
    }
}
