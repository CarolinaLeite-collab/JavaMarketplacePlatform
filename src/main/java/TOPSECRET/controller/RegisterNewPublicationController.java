package TOPSECRET.controller;

import TOPSECRET.domain.PublicationInfoRepo;
import TOPSECRET.domain.User;

public class RegisterNewPublicationController {
    private PublicationInfoRepo _publicationInfoRepo;

    public RegisterNewPublicationController(PublicationInfoRepo publicationInfoRepo) {
        _publicationInfoRepo = publicationInfoRepo;
    }
}
