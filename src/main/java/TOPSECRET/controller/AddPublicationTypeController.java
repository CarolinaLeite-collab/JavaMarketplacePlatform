package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;

/**
 * Controller responsible for managing the creation of new publication types.
 * <p>
 * This controller interacts with the {@link PublicationTypeRepo} to add new
 * {@link PublicationType} instances, ensuring no duplicates are created.
 * </p>
 */

public class AddPublicationTypeController {

    private final PublicationTypeRepo repo;

    public AddPublicationTypeController(PublicationTypeRepo repo) {
        this.repo = repo;
    }

    public PublicationType addPublicationType(String typeName) {
        if (repo.existsPublicationType(typeName)) {
            throw new IllegalArgumentException("Publication type already exists!");
        }
        return repo.createPublicationType(typeName);
    }
}


