package TOPSECRET.controller;

import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.PublisherRepo;

/**
 * Controller responsible for handling the registration of a {@link Publisher}.
 * Delegates creation/persistence to {@link PublisherRepo}.
 */

public class RegisterPublisherController {

    private PublisherRepo _publisherRepo;

    public RegisterPublisherController(PublisherRepo publisherRepo) {
        _publisherRepo = publisherRepo;
    }

    public Publisher registerPublisher(String publisherName) {
        Publisher publisher = _publisherRepo.registerPublisher(publisherName);
        return publisher;
    }

}
