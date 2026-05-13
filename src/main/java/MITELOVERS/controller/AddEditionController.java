package MITELOVERS.controller;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.time.Year;

/**
 * Controller responsible for creating new editions in the system.
 * <p>
 * This controller delegates the creation of {@link Edition} instances to the {@link IEditionRepo}.
 * </p>
 */

@Component
public class AddEditionController {

    private final IEditionRepo _iRepo;
    private final EditionFactory _editionFactory;


    public AddEditionController(IEditionRepo iRepo, EditionFactory factory) {
        _iRepo = iRepo;
        _editionFactory = factory;
    }


    public Edition addEdition(PublicationTypeId typeId,
                              Identifier identifier,
                              PublicationId publicationId,
                              PublishingCompanyId publishingCompanyId,
                              Year publishingYear,
                              Language editionLanguage,
                              Dimension dimension,
                              Weight weight,
                              NumberOfPages numberOfPages,
                              EditionNumber editionNumber,
                              Binding binding) {

        Edition edition = _editionFactory.createEdition(
                typeId,
                identifier,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage,
                dimension,
                weight,
                numberOfPages,
                editionNumber,
                binding
        );

        for (Edition existingEdition : _iRepo.findAll()) {
            if (identifier != null && existingEdition.getIdentifier() != null) {
                if (existingEdition.getPublicationTypeId().equals(typeId) &&
                        existingEdition.getIdentifier().equals(identifier)) {
                    throw new IllegalStateException("An Edition with this identifier already exists!");
                }
            } else {
                if (existingEdition.sameAs(edition)) {
                    throw new IllegalStateException("Edition already exists!");
                }
            }
        }

        return _iRepo.save(edition);
    }
}
