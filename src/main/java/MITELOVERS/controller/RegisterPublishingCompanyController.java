package MITELOVERS.controller;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;

/**
 * Controller responsible for handling the registration of a {@link PublishingCompany}.
 * Delegates the operation to {@link IPublishingCompanyRepo}.
 */

public class RegisterPublishingCompanyController {

    private final IPublishingCompanyRepo _iPublishingCompanyRepo;
    private final PublishingCompanyFactory _publishingCompanyFactory;

    public RegisterPublishingCompanyController(IPublishingCompanyRepo iPublishingCompanyRepo,  PublishingCompanyFactory publishingCompanyFactory) {

        _iPublishingCompanyRepo = iPublishingCompanyRepo;
        _publishingCompanyFactory = publishingCompanyFactory;

    }

    public PublishingCompany registerPublishingCompany(String publishingCompanyName) {

        PublishingCompany newPublishingCompany = _publishingCompanyFactory.createPublishingCompany(publishingCompanyName);

        if (_iPublishingCompanyRepo.containsOfIdentity(newPublishingCompany.identity())) {

            throw new IllegalArgumentException("Publishing Company with name " + publishingCompanyName + " already exists");

        }

        return _iPublishingCompanyRepo.save(newPublishingCompany);

    }

}
