package pvytykac.net.scrape.server.db.model;

import org.joda.time.DateTime;

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
public class Ico {

    @Id
    private String ico;

    @Column
    private Integer form;

    @Column
    private DateTime lastUpdated;

    public String getIco() {
        return ico;
    }

    public Integer getForm() {
        return form;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }
}
