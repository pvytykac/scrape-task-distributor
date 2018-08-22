package pvytykac.net.scrape.server.db.repository.impl;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.IcoRepository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class IcoRepositoryImpl extends AbstractRepository<String, Ico> implements IcoRepository {

    public IcoRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Ico> findNext(String offsetId, Set<Integer> formIds) {
        return query("FROM Ico i WHERE i.id > :offsetId AND i.formId IN (:formIds) ORDER BY i.id ASC")
                .setParameter("offsetId", Optional.ofNullable(offsetId).orElse("0"))
                .setParameter("formIds", formIds)
                .list()
                .stream()
                .findFirst();
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
