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

public class PublicationTypeRepo {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();
    private final PublicationTypeFactory _publicationTypeFactory;

    public PublicationTypeRepo(PublicationTypeFactory publicationTypeFactory){

        _publicationTypeFactory = publicationTypeFactory;

    }

    public PublicationType addPublicationType(String publicationTypeName) throws IllegalArgumentException, InstantiationException {

        // Verifies if PublicationType already exists in the repo
        if (publicationTypeExists(publicationTypeName)) {

            throw new IllegalArgumentException("This publication type already exists!");

        }

        // Uses factory to create a new instance of PublicationType
        PublicationType newPublicationType = _publicationTypeFactory.createPublicationType(publicationTypeName);

        //Adds to repo
        _publicationTypes.add(newPublicationType);

        // Returns instantiated object
        return newPublicationType;

    }

    // Verifies if a Publication Type already exists in the repo
    private boolean publicationTypeExists (String publicationTypeName) {

        for (PublicationType publicationType : _publicationTypes) {

            if (publicationType.isSamePublicationType(publicationTypeName)) {

                return true;

            };

        }

        return false;

    }

    // Devolve uma cópia da lista (não quebra encapsulamento)
    public List<PublicationType> getAll() {
        return List.copyOf(_publicationTypes);
    }
}
