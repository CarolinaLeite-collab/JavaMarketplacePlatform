package TOPSECRET.controller;

import TOPSECRET.domain.PublishingCompany;
import TOPSECRET.domain.PublisherRepo;

/**
 * Controller responsible for handling the registration of a {@link PublishingCompany}.
 * Delegates creation/persistence to {@link PublisherRepo}.
 */

public class RegisterPublishingCompanyController {

    private PublisherRepo _publisherRepo;

    public RegisterPublishingCompanyController(PublisherRepo publisherRepo) {
        _publisherRepo = publisherRepo;
    }

    public PublishingCompany registerPublisher(String publisherName) {
        PublishingCompany publisher = _publisherRepo.registerPublisher(publisherName);
        return publisher;
    }

}
