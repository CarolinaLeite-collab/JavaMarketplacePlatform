package MITELOVERS.applicationservices;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.dto.request.EditionRequestDTO;
import MITELOVERS.mapper.EditionRequestDTOMapper;
import MITELOVERS.mapper.EditionResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EditionService {

    private final IEditionRepo _iEditionRepo;
    private final EditionFactory _editionFactory;
    private final IPublicationRepo _iPublicationRepo;
    private final IPublishingCompanyRepo _iPublishingCompanyRepo;
    private final IPublicationTypeRepo _iPublicationTypeRepo;
    private final EditionRequestDTOMapper _editionRequestDTOMapper;
    private final EditionResponseDTOMapper _editionResponseDTOMapper;


    public EditionService(IEditionRepo iEditionRepo, EditionFactory editionFactory,
                          IPublicationRepo iPublicationRepo, IPublishingCompanyRepo iPublishingCompanyRepo,
                          IPublicationTypeRepo iPublicationTypeRepo, EditionRequestDTOMapper editionRequestDTOMapper,
                          EditionResponseDTOMapper editionResponseDTOMapper) {

        _iEditionRepo = iEditionRepo;
        _editionFactory = editionFactory;
        _iPublicationRepo = iPublicationRepo;
        _iPublishingCompanyRepo = iPublishingCompanyRepo;
        _iPublicationTypeRepo = iPublicationTypeRepo;
        _editionRequestDTOMapper = editionRequestDTOMapper;
        _editionResponseDTOMapper = editionResponseDTOMapper;

    }


    public EditionResponseDTO registerEdition(String pubId, EditionRequestDTO dto) {

        PublicationTypeId typeId = new PublicationTypeId(dto.getPublicationTypeId());
        Identifier identifier = _editionRequestDTOMapper.toIdentifier(dto);
        PublicationId publicationId = new PublicationId (pubId);
        PublishingCompanyId publishingCompanyId = new PublishingCompanyId(dto.getPublishingCompanyId());
        Year publishingYear = Year.of(dto.getPublishingYear());
        Language editionLanguage = Language.valueOf(dto.getLanguage());

        // optional fields
        Dimension dimension = _editionRequestDTOMapper.toDimension(dto);
        Weight weight = _editionRequestDTOMapper.toWeight(dto);
        NumberOfPages numberOfPages = _editionRequestDTOMapper.toNumberOfPages(dto);
        EditionNumber editionNumber = _editionRequestDTOMapper.toEditionNumber(dto);
        Binding binding = _editionRequestDTOMapper.toBinding(dto);

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

                    return _editionResponseDTOMapper.toModel(existingEdition);
                }
            } else {
                if (existingEdition.sameAs(edition)) {

                    return _editionResponseDTOMapper.toModel(existingEdition);
                }
            }
        }

        Edition saved = _iEditionRepo.save(edition);

        return _editionResponseDTOMapper.toModel(saved);

    }

    public List<EditionResponseDTO> getAllEditions() {

        Iterable<Edition> editions = _iEditionRepo.findAll();

        List<EditionResponseDTO> response = new ArrayList<>();

        for (Edition edition : editions) {
            response.add(_editionResponseDTOMapper.toModel(edition));
        }

        return response;
    }

    public List<EditionResponseDTO> getAllEditionsByPublication(String publicationId) {

        PublicationId pubId = new PublicationId(publicationId);

        _iPublicationRepo.ofIdentity(pubId)
                .orElseThrow(() -> new NoSuchElementException("Publication not found"));

        Iterable<Edition> editions = _iEditionRepo.findAll();

        List<EditionResponseDTO> response = new ArrayList<>();

        for (Edition edition : editions) {
            if (edition.getPublicationId().equals(pubId)) {
                response.add(_editionResponseDTOMapper.toModel(edition));
            }
        }

        return response;
    }

    public EditionResponseDTO getEditionById(String editionId) {

        EditionId id = new EditionId(editionId);

        Edition edition = _iEditionRepo.ofIdentity(id)
                .orElseThrow(() -> new NoSuchElementException("Edition not found"));

        EditionResponseDTO editionResponseDTO = _editionResponseDTOMapper.toModel(edition);

        return editionResponseDTO;

    }

}
