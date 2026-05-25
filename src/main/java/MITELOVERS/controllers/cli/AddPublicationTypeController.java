package MITELOVERS.controllers.cli;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import org.springframework.stereotype.Controller;

/**
 * Controller responsible for handling the addition of a new {@link PublicationType}.
 * <p>
 * Delegates the creation to {@link PublicationTypeFactory}
 * and storage to {@link IPublicationTypeRepo}. Validates uniqueness.
 * </p>
 */

@Controller
public class AddPublicationTypeController {

    private final IPublicationTypeRepo _iPublicationTypeRepo;
    private final PublicationTypeFactory _publicationTypeFactory;

    public AddPublicationTypeController(IPublicationTypeRepo iPublicationTypeRepo, PublicationTypeFactory publicationTypeFactory) {

        _iPublicationTypeRepo = iPublicationTypeRepo;
        _publicationTypeFactory = publicationTypeFactory;

    }

    public PublicationType addPublicationType(String publicationTypeName) {

        PublicationType newPublicationType = _publicationTypeFactory.createPublicationType(publicationTypeName);

        if (_iPublicationTypeRepo.containsOfIdentity(newPublicationType.identity())) {

            throw new IllegalArgumentException("The publication type " + publicationTypeName + " already exists.");

        }

        return _iPublicationTypeRepo.save(newPublicationType);

    }

}


