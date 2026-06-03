package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.domain.publicationtype.PublicationType;
import org.springframework.stereotype.Controller;

/**
 * Controller responsible for registering new {@link PublicationType} instances.
 * <p>
 * Delegates publication type creation and validation to the application service
 * layer, keeping the controller focused on coordinating the registration flow.
 * </p>
 */

@Controller
public class AddPublicationTypeController {

    private final PublicationTypeService _publicationTypeService;

    public AddPublicationTypeController(PublicationTypeService publicationTypeService) {

        _publicationTypeService = publicationTypeService;

    }

    public PublicationType addPublicationType(String publicationTypeName) {

        return _publicationTypeService.addPublicationType(publicationTypeName);

    }

}


