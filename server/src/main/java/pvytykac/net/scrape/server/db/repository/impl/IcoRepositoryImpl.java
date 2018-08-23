package pvytykac.net.scrape.server.db.repository.impl;

import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import pvytykac.net.scrape.server.db.model.ico.Ico;
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
    public Optional<Ico> findNext(String offsetId, Set<Integer> formIds) {
        return query("FROM Ico i WHERE i.id > :offsetId AND i.form IN (:formIds) ORDER BY i.id ASC")
                .setParameter("offsetId", Optional.ofNullable(offsetId).orElse("0"))
                .setParameter("formIds", formIds)
                .list()
                .stream()
                .findFirst();
    }

    @Override
    public boolean updateFromRes(String id, Integer form, Integer resId) {
        return untypedQuery("UPDATE Ico i SET i.resId = :resId, i.form = :form, i.lastUpdated = :now WHERE i.id = :id")
                .setParameter("resId", resId)
                .setParameter("form", form)
				.setParameter("now", new DateTime(DateTimeZone.UTC))
                .setParameter("id", id)
                .executeUpdate() == 1;
    }

    @Override
    protected Class<Ico> getEntityClass() {
        return Ico.class;
    }
}
