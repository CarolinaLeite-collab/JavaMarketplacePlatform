package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.dto.request.EditionRequestDTO;
import org.springframework.stereotype.Controller;

/**
 * CLI controller responsible for creating new editions in the system.
 * <p>
 * This controller is used for testing purposes and delegates
 * the creation of {@link Edition} instances to the {@link EditionService}.
 * </p>
 */

@Controller
public class AddEditionController {

    private final EditionService _editionService;

    public AddEditionController(EditionService editionService) {
        _editionService = editionService;
    }

    public EditionResponseDTO addEdition(String publicationId, EditionRequestDTO dto) {
        return _editionService.registerEdition(publicationId, dto);
    }
}
