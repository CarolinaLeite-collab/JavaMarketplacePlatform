package TOPSECRET.controller;

import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.repository.IPublishingCompanyRepo;

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

        return _iPublishingCompanyRepo.registerPublishingCompany(publishingCompanyName);

    }

}
