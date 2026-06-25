package MITELOVERS.applicationservices;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.EditionRequestDTO;
import MITELOVERS.mapper.EditionRequestDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Application service responsible for edition-related operations.
 * Handles edition registration, validation of related domain references,
 * retrieval of all editions, lookup of editions by publication, and
 * retrieval of a single edition by its identifier. Coordinates edition
 * creation through {@link EditionFactory} and persists new editions via
 * {@link IEditionRepo}. All operations are transactional.
 */

@Service
public class EditionService {

    private final IEditionRepo _iEditionRepo;
    private final EditionFactory _editionFactory;
    private final IPublicationRepo _iPublicationRepo;
    private final IPublishingCompanyRepo _iPublishingCompanyRepo;
    private final IPublicationTypeRepo _iPublicationTypeRepo;


    public EditionService(IEditionRepo iEditionRepo, EditionFactory editionFactory,
                          IPublicationRepo iPublicationRepo, IPublishingCompanyRepo iPublishingCompanyRepo,
                          IPublicationTypeRepo iPublicationTypeRepo) {

        _iEditionRepo = iEditionRepo;
        _editionFactory = editionFactory;
        _iPublicationRepo = iPublicationRepo;
        _iPublishingCompanyRepo = iPublishingCompanyRepo;
        _iPublicationTypeRepo = iPublicationTypeRepo;

    }

    @Transactional
    public Edition registerEdition(
            PublicationTypeId typeId,
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

        // doesExist attributes
        _iPublicationTypeRepo.ofIdentity(typeId)
                .orElseThrow(() -> new NoSuchElementException("Publication type not found"));

        _iPublicationRepo.ofIdentity(publicationId)
                .orElseThrow(() -> new NoSuchElementException("Publication not found"));

        _iPublishingCompanyRepo.ofIdentity(publishingCompanyId)
                .orElseThrow(() -> new NoSuchElementException("Publishing company not found"));

        // creation
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

        // doesExist edition
        for (Edition existingEdition : _iEditionRepo.findAll()) {
            if (identifier != null && existingEdition.getIdentifier() != null) {
                if (existingEdition.getPublicationTypeId().equals(typeId) &&
                        existingEdition.getIdentifier().equals(identifier)) {

                    return existingEdition;
                }
            } else {
                if (existingEdition.sameAs(edition)) {

                    return existingEdition;
                }
            }
        }

        return _iEditionRepo.save(edition);

    }

    @Transactional
    public List<Edition> getAllEditions() {

        List<Edition> editions = new ArrayList<>();

        for (Edition edition : _iEditionRepo.findAll()) {
            editions.add(edition);
        }

        return editions;
    }

    @Transactional
    public List<Edition> getAllEditionsByPublication(PublicationId pubId) {

        _iPublicationRepo.ofIdentity(pubId)
                .orElseThrow(() -> new NoSuchElementException("Publication not found"));

        List<Edition> result = new ArrayList<>();

        for (Edition edition : _iEditionRepo.findAll()) {
            if (edition.getPublicationId().equals(pubId)) {
                result.add(edition);
            }
        }

        return result;
    }

    @Transactional
    public Edition getEditionById(EditionId editionId) {

        return _iEditionRepo.ofIdentity(editionId)
                .orElseThrow(() -> new NoSuchElementException("Edition not found"));
    }

}
