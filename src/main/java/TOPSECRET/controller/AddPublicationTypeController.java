package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;

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


