package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Controller;

import java.time.Year;

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

    public Edition addEdition(PublicationTypeId typeId,
                              Identifier identifier,
                              PublicationId publicationId,
                              PublishingCompanyId publishingCompanyId,
                              Year publishingYear,
                              Language language,
                              Dimension dimension,
                              Weight weight,
                              NumberOfPages numberOfPages,
                              EditionNumber editionNumber,
                              Binding binding) {

        return _editionService.registerEdition(
                typeId, identifier, publicationId, publishingCompanyId,
                publishingYear, language, dimension, weight,
                numberOfPages, editionNumber, binding);
    }
}