package TOPSECRET.controller;

import TOPSECRET.domain.repository.IPublishingCompanyRepo;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.User.User;

/**
 * Controller responsible for handling the registration of a {@link PublishingCompany}.
 * Delegates the operation to {@link IPublishingCompanyRepo}.
 */

public class RegisterPublishingCompanyController {

    private final IPublishingCompanyRepo _iPublishingCompanyRepo;

    public RegisterPublishingCompanyController(IPublishingCompanyRepo iPublishingCompanyRepo) {

        _iPublishingCompanyRepo = iPublishingCompanyRepo;

    }

    public PublishingCompany registerPublishingCompany(User user, String publishingCompanyName) {

        if (!user.hasRole(Role.ADMIN)) {

            throw new SecurityException("User is not authorized to register publishing companies");

        }

        return _iPublishingCompanyRepo.registerPublishingCompany(publishingCompanyName);

    }

}
