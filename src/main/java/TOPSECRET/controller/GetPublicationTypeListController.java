package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;

import java.util.List;

public class GetPublicationTypeListController {

    private final PublicationTypeRepo publicationTypeRepo;

    public GetPublicationTypeListController(PublicationTypeRepo publicationTypeRepo) {
        this.publicationTypeRepo = publicationTypeRepo;
    }

    public List<PublicationType> getListOfPublicationTypes() {
        return publicationTypeRepo.getAll();
    }
}