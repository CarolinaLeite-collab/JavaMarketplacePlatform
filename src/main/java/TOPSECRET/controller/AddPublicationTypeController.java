package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.repository.IPublicationTypeRepo;
import TOPSECRET.domain.valueobject.Role;

/**
 * Controller responsible for managing the creation of new publication types.
 * <p>
 * This controller interacts with the {@link IPublicationTypeRepo} to add new
 * {@link PublicationType} instances, ensuring no duplicates are created.
 * </p>
 */

public class AddPublicationTypeController {

    private final IPublicationTypeRepo _iPublicationTypeRepo;

    public AddPublicationTypeController(IPublicationTypeRepo iPublicationTypeRepo, User admin) {
        if(!admin.hasRole(Role.ADMIN)){
            throw new SecurityException("User is not allowed to add publication type");
        }

        _iPublicationTypeRepo = iPublicationTypeRepo;

    }

    public PublicationType addPublicationType(String publicationTypeName) {
        return _iPublicationTypeRepo.addPublicationType(publicationTypeName);

    }
}


