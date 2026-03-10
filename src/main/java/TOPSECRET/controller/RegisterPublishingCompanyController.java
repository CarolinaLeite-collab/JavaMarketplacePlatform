package TOPSECRET.controller;

import TOPSECRET.domain.PublishingCompany;
import TOPSECRET.domain.PublishingCompanyRepo;

/**
 * Controller responsible for handling the registration of a {@link PublishingCompany}.
 * Delegates the operation to {@link PublishingCompanyRepo}.
 */

public class RegisterPublishingCompanyController {

    private final PublishingCompanyRepo _publishingCompanyRepo;

    public RegisterPublishingCompanyController(PublishingCompanyRepo publishingCompanyRepo) {

        _publishingCompanyRepo = publishingCompanyRepo;

    }

    public PublishingCompany registerPublishingCompany(String publishingCompanyName) {

        PublishingCompany publishingCompany = _publishingCompanyRepo.registerPublishingCompany(publishingCompanyName);

        return publishingCompany;

    }

}
