package MITELOVERS.persistence.mem;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;

import java.util.*;

/**
 * Repository for managing {@link PublicationType} instances.
 * <p>
 * Provides methods to check if a publication type exists, delegates creation to
 * {@link PublicationTypeFactory}, stores new types, and retrieves all stored
 * publication types as an unmodifiable list.
 * </p>
 */

public class MemoPublicationTypeRepo implements IPublicationTypeRepo {

    private final Map<PublicationTypeId, PublicationType> DATA = new HashMap<PublicationTypeId, PublicationType>();
    private final PublicationTypeFactory _publicationTypeFactory;

    public MemoPublicationTypeRepo(PublicationTypeFactory publicationTypeFactory){

        _publicationTypeFactory = publicationTypeFactory;

    }

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

    @Override
    public PublicationType addPublicationType(String publicationTypeName) {

        PublicationType newPublicationType = _publicationTypeFactory.createPublicationType(publicationTypeName);

        if (containsOfIdentity(newPublicationType.identity())) {

            throw new IllegalArgumentException("This publication type already exists!");

        }

        return save(newPublicationType);

    }

}
