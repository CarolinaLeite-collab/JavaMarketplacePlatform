package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;
import TOPSECRET.domain.User;

/**
 * Controller responsible for managing the creation of new publication types.
 * <p>
 * This controller interacts with the {@link PublicationTypeRepo} to add new
 * {@link PublicationType} instances, ensuring no duplicates are created.
 * </p>
 */

public class AddPublicationTypeController {

    private final PublicationTypeRepo _ptr;

    public AddPublicationTypeController(PublicationTypeRepo _ptr, User _admin) {

        this._ptr = _ptr;

    }

    public PublicationType addPublicationType(String publicationTypeName) throws IllegalArgumentException {

        return _ptr.addPublicationType(publicationTypeName);

    }
}


