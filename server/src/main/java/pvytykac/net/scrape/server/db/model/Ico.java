package pvytykac.net.scrape.server.db.model;

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import pvytykac.net.scrape.server.db.repository.Dbo;

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

    @Column
    private DateTime lastUpdated;

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
}
