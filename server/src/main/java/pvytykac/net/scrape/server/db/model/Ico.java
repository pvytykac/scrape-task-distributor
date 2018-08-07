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
    private DateTime lastUpdated;

    private Ico() {}

    public Ico(String ico, DateTime lastUpdated) {
        this.ico = ico;
        this.lastUpdated = lastUpdated;
    }

    public String getIco() {
        return ico;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }
}
