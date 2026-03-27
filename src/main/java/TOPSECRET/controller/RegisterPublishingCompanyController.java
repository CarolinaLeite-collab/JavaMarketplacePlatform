package TOPSECRET.controller;

import TOPSECRET.domain.IPublishingCompanyRepo;
import TOPSECRET.domain.PublishingCompany;

/**
 * Controller responsible for handling the registration of a {@link PublishingCompany}.
 * Delegates the operation to {@link IPublishingCompanyRepo}.
 */

public class RegisterPublishingCompanyController {

    private final IPublishingCompanyRepo _iPublishingCompanyRepo;

    public RegisterPublishingCompanyController(IPublishingCompanyRepo iPublishingCompanyRepo) {

        _iPublishingCompanyRepo = iPublishingCompanyRepo;

    }

    public PublishingCompany registerPublishingCompany(String publishingCompanyName) {

        PublishingCompany publishingCompany = _iPublishingCompanyRepo.registerPublishingCompany(publishingCompanyName);

        return publishingCompany;

    }

}
