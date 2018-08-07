package pvytykac.net.scrape.server.db.impl;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import pvytykac.net.scrape.server.db.IcoRepository;
import pvytykac.net.scrape.server.db.model.Ico;

import java.util.List;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class IcoRepositoryImpl extends AbstractDAO<Ico> implements IcoRepository {

    public IcoRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Ico> list(int limit, int offset) {
        Query<Ico> query = query("FROM Ico ico")
                .setFirstResult(offset)
                .setMaxResults(limit);

        return super.list(query);
    }

    @Override
    public void save(Ico ico) {
        super.persist(ico);
    }
}
