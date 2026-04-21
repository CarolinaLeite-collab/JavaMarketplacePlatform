package MITELOVERS.controller;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;

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
