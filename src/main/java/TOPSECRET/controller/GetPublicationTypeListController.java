package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;

import java.util.Collection;

public class GetPublicationTypeListController {

    private final PublicationTypeRepo publicationTypeRepo;

    public GetPublicationTypeListController(PublicationTypeRepo publicationTypeRepo) {
        this.publicationTypeRepo = publicationTypeRepo;
    }

    public Collection<PublicationType> getListOfPublicationTypes() {
        return publicationTypeRepo.getAll();
    }
}