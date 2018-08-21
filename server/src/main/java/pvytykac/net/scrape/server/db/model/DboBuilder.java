package pvytykac.net.scrape.server.db.model;

import net.pvytykac.scrape.util.ModelBuilder;
import pvytykac.net.scrape.server.db.repository.Dbo;

/**
 * @author Paly
 * @since 2018-08-21
 */
public abstract class DboBuilder<BUILDER extends DboBuilder, ID, ENTITY extends Dbo<ID>> implements ModelBuilder<ENTITY> {

    private ID id;

    public ID getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    public BUILDER withId(ID id) {
        this.id = id;
        return (BUILDER) this;
    }
}
