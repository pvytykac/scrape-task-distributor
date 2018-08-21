package pvytykac.net.scrape.server.db.repository;

import java.util.Optional;

public interface Repository<ID, ENTITY extends Dbo<ID>> {

	ENTITY find(ID id);

	Optional<ENTITY> findOptional(ID id);

	void save(ENTITY entity);

}
