package pvytykac.net.scrape.server.db.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import pvytykac.net.scrape.server.db.repository.IcoRepository;
import pvytykac.net.scrape.server.db.model.Ico;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class IcoRepositoryImpl extends AbstractRepository<String, Ico> implements IcoRepository {

    public IcoRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Ico> list(int limit, String offsetIco) {
        Query<Ico> query = offsetIco != null
                ? query("FROM Ico i WHERE i.ico > :offsetIco ORDER BY i.ico ASC")
                        .setParameter("offsetIco", offsetIco)
                : query("FROM Ico i ORDER BY i.ico ASC");

        query = query.setMaxResults(limit);

        return list(query);
    }

    @Override
    public boolean updateLastUpdated(String ico) {
        return 1 == getSession()
                .createQuery("UPDATE Ico i SET i.lastUpdated = :lastUpdate WHERE i.ico = :ico")
                .setParameter("ico", ico)
                .setParameter("lastUpdate", DateTime.now(DateTimeZone.UTC))
                .executeUpdate();
    }

    @Override
    protected Class<Ico> getEntityClass() {
        return Ico.class;
    }
}
