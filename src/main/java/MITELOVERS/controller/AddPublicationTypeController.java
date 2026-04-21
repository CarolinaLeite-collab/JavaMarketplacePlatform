package MITELOVERS.controller;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.UserId;

/**
 * Controller responsible for managing the creation of new publication types.
 * <p>
 * This controller interacts with the {@link IPublicationTypeRepo} to add new
 * {@link PublicationType} instances, ensuring no duplicates are created.
 * </p>
 */

public class AddPublicationTypeController {

    private final IPublicationTypeRepo _iPublicationTypeRepo;

    public AddPublicationTypeController(IPublicationTypeRepo iPublicationTypeRepo, UserId adminId) {

        _iPublicationTypeRepo = iPublicationTypeRepo;

    }

    public PublicationType addPublicationType(String publicationTypeName) {
        return _iPublicationTypeRepo.addPublicationType(publicationTypeName);

    }
}


