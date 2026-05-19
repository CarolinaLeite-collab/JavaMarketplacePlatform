package MITELOVERS.persistence.mem;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.valueobject.EditionId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Represents an in-memory repository of {@link Edition} entities.
 * <p>
 * This class provides management mechanisms for
 * {@link Edition} objects.
 * Each edition is uniquely identified by its {@link EditionId}.
 * <p>
 */

@Repository
@Profile("mem")
public class MemEditionRepo implements IEditionRepo {

    private final Map<EditionId, Edition> DATA = new HashMap<EditionId, Edition>();


    @Override
    public Edition save(Edition editionEntity) {

        DATA.put(editionEntity.identity(), editionEntity);
        return editionEntity;

    }

    @Override
    public Iterable<Edition> findAll() {

        return DATA.values();

    }

    @Override
    public Optional<Edition> ofIdentity(EditionId id) {

        if (!containsOfIdentity(id))
            return Optional.empty();
        else
            return Optional.of(DATA.get(id));
    }

    @Override
    public boolean containsOfIdentity(EditionId id) {

        return DATA.containsKey(id);

    }

    @Override
    public List<EditionId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }

}
