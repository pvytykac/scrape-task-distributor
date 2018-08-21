package pvytykac.net.scrape.server.db.repository.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.db.repository.IcoRepository;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class IcoRepositoryImpl extends AbstractRepository<String, Ico> implements IcoRepository {

    public IcoRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Ico> list(int limit, String offsetId) {
        Query<Ico> query = offsetId != null
                ? query("FROM Ico i WHERE i.id > :offsetId ORDER BY i.id ASC")
                        .setParameter("offsetId", offsetId)
                : query("FROM Ico i ORDER BY i.id ASC");

        query = query.setMaxResults(limit);

        return list(query);
    }

    @Override
    public boolean updateLastUpdated(String id) {
        return 1 == getSession()
                .createQuery("UPDATE Ico i SET i.lastUpdated = :lastUpdate WHERE i.id = :id")
                .setParameter("id", id)
                .setParameter("lastUpdate", DateTime.now(DateTimeZone.UTC))
                .executeUpdate();
    }

    @Override
    protected Class<Ico> getEntityClass() {
        return Ico.class;
    }
}
