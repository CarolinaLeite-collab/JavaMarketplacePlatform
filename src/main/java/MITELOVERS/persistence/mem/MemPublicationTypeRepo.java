package MITELOVERS.persistence.mem;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-Memory Repository implementation of {@link IPublicationTypeRepo} for storing and
 * managing {@link PublicationType} instances.
 * <p>
 * Provides methods to save new publication types, list all stored publication types,
 * retrieve stored types by their {@link PublicationTypeId}, and check if a publication
 * type already exists for a given identity.
 * </p>
 */

@Repository
@Profile("mem")
public class MemPublicationTypeRepo implements IPublicationTypeRepo {

    private final Map<PublicationTypeId, PublicationType> DATA = new HashMap<PublicationTypeId, PublicationType>();

    @Override
    public PublicationType save(PublicationType publicationType) {

        DATA.put(publicationType.identity(), publicationType);

        return publicationType;

    }

    @Override
    public Iterable<PublicationType> findAll() {

        return DATA.values();

    }

    @Override
    public Optional<PublicationType> ofIdentity(PublicationTypeId publicationTypeId) {

        if(!containsOfIdentity(publicationTypeId)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(publicationTypeId));

        }

    }

    @Override
    public boolean containsOfIdentity(PublicationTypeId publicationTypeId) {

        return DATA.containsKey(publicationTypeId);

    }

    @Override
    public List<PublicationTypeId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }

}
