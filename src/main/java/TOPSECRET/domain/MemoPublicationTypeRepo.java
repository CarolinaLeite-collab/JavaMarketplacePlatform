package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link PublicationType} instances.
 * <p>
 * Provides methods to check if a publication type exists, create and store new types,
 * and retrieve all stored publication types as an unmodifiable list.
 * </p>
 */

public class MemoPublicationTypeRepo implements IPublicationTypeRepo {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();
    private final PublicationTypeFactory _publicationTypeFactory;

    public MemoPublicationTypeRepo(PublicationTypeFactory publicationTypeFactory){

        _publicationTypeFactory = publicationTypeFactory;

    }

    @Override
    public PublicationType addPublicationType(String publicationTypeName) throws IllegalArgumentException {

        if (publicationTypeExists(publicationTypeName)) {

            throw new IllegalArgumentException("This publication type already exists!");

        }

        PublicationType newPublicationType = _publicationTypeFactory.createPublicationType(publicationTypeName);

        _publicationTypes.add(newPublicationType);

        return newPublicationType;

    }

    private boolean publicationTypeExists (String publicationTypeName) {

        for (PublicationType publicationType : _publicationTypes) {

            if (publicationType.isSamePublicationType(publicationTypeName)) {

                return true;

            };

        }

        return false;

    }

    @Override
    public List<PublicationType> getAll() {
        return List.copyOf(_publicationTypes);
    }
}
