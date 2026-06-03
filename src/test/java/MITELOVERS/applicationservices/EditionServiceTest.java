package MITELOVERS.applicationservices;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.ISBN;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.dto.request.EditionRequestDTO;
import MITELOVERS.mapper.EditionRequestDTOMapper;
import MITELOVERS.mapper.EditionResponseDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionServiceTest {

    private IEditionRepo _iEditionRepoDouble;
    private EditionFactory _editionFactoryDouble;
    private IPublicationRepo _iPublicationRepoDouble;
    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;
    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
    private EditionRequestDTOMapper _requestMapperDouble;
    private EditionResponseDTOMapper _responseMapperDouble;

    @BeforeEach
    void setUp() {

        _iEditionRepoDouble = mock(IEditionRepo.class);
        _editionFactoryDouble = mock(EditionFactory.class);
        _iPublicationRepoDouble = mock(IPublicationRepo.class);
        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);
        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        _requestMapperDouble = mock(EditionRequestDTOMapper.class);
        _responseMapperDouble = mock(EditionResponseDTOMapper.class);

    }

    @Test
    void registerEditionReturnsEditionResponseDTO() {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .identifier("9780747532743")
                .build();

        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);

        when(_iPublicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(Publication.class)));
        when(_iPublishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(PublishingCompany.class)));
        when(_requestMapperDouble.toIdentifier(dto)).thenReturn(new ISBN("9780747532743"));
        when(_requestMapperDouble.toDimension(dto)).thenReturn(null);
        when(_requestMapperDouble.toWeight(dto)).thenReturn(null);
        when(_requestMapperDouble.toNumberOfPages(dto)).thenReturn(null);
        when(_requestMapperDouble.toEditionNumber(dto)).thenReturn(null);
        when(_requestMapperDouble.toBinding(dto)).thenReturn(null);
        when(_iEditionRepoDouble.findAll()).thenReturn(List.of());
        when(_editionFactoryDouble.createEdition(any(), any(), any(), any(), any(), any(),
                isNull(), isNull(), isNull(), isNull(), isNull())).thenReturn(editionDouble);
        when(_iEditionRepoDouble.save(editionDouble)).thenReturn(editionDouble);
        when(_responseMapperDouble.toModel(editionDouble)).thenReturn(responseDouble);

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act
        EditionResponseDTO result = service.registerEdition("1984-Orwell-G--F43DD6(1949)", dto);

        // Assert
        assertNotNull(result);

    }

    @Test
    void registerEditionPublicationTypeNotFoundThrowsException() {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .build();

        when(_iPublicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.registerEdition("1984-Orwell-G--F43DD6(1949)", dto));
    }

    @Test
    void registerEditionPublicationNotFoundThrowsException() {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .build();

        when(_iPublicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.registerEdition("1984-Orwell-G--F43DD6(1949)", dto));
    }

    @Test
    void registerEditionPublishingCompanyNotFoundThrowsException() {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .build();

        when(_iPublicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(Publication.class)));
        when(_iPublishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.registerEdition("1984-Orwell-G--F43DD6(1949)", dto));
    }

    @Test
    void registerEditionDuplicateIdentifierReturnsExistingEdition() {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .identifier("9780747532743")
                .build();

        Edition existingEditionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);

        when(_iPublicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(Publication.class)));
        when(_iPublishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(PublishingCompany.class)));
        when(_requestMapperDouble.toIdentifier(dto)).thenReturn(new ISBN("9780747532743"));
        when(_requestMapperDouble.toDimension(dto)).thenReturn(null);
        when(_requestMapperDouble.toWeight(dto)).thenReturn(null);
        when(_requestMapperDouble.toNumberOfPages(dto)).thenReturn(null);
        when(_requestMapperDouble.toEditionNumber(dto)).thenReturn(null);
        when(_requestMapperDouble.toBinding(dto)).thenReturn(null);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(new PublicationTypeId("BOOK"));
        when(existingEditionDouble.getIdentifier()).thenReturn(new ISBN("9780747532743"));
        when(_iEditionRepoDouble.findAll()).thenReturn(List.of(existingEditionDouble));
        when(_responseMapperDouble.toModel(existingEditionDouble)).thenReturn(responseDouble);

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act
        EditionResponseDTO result = service.registerEdition("1984-Orwell-G--F43DD6(1949)", dto);

        // Assert
        assertNotNull(result);
    }




    @Test
    void getAllEditionsByPublicationReturnsListOfDTOs() {
        // Arrange
        String publicationId = "1984-Orwell-G--F43DD6(1949)";
        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);

        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(Publication.class)));
        when(_iEditionRepoDouble.findAll()).thenReturn(List.of(editionDouble));
        when(editionDouble.getPublicationId()).thenReturn(new PublicationId(publicationId));
        when(_responseMapperDouble.toModel(editionDouble)).thenReturn(responseDouble);

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act
        List<EditionResponseDTO> result = service.getAllEditionsByPublication(publicationId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllEditionsByPublicationPublicationNotFoundThrowsException() {
        // Arrange
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.getAllEditionsByPublication("1984-Orwell-G--F43DD6(1949)"));
    }

    @Test
    void getAllEditionsByPublicationNoMatchReturnsEmptyList() {
        // Arrange
        Edition editionDouble = mock(Edition.class);

        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(mock(Publication.class)));
        when(_iEditionRepoDouble.findAll()).thenReturn(List.of(editionDouble));
        when(editionDouble.getPublicationId()).thenReturn(new PublicationId("Foundation-Asimov-I--D60AD1(1951)"));

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act
        List<EditionResponseDTO> result = service.getAllEditionsByPublication("1984-Orwell-G--F43DD6(1949)");

        // Assert
        assertTrue(result.isEmpty());
    }



    @Test
    void getEditionByIdReturnsEditionResponseDTO() {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);

        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(_responseMapperDouble.toModel(editionDouble)).thenReturn(responseDouble);

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act
        EditionResponseDTO result = service.getEditionById("E-ABC12345");

        // Assert
        assertNotNull(result);
    }

    @Test
    void getEditionByIdNotFoundThrowsException() {
        // Arrange
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        EditionService service = new EditionService(
                _iEditionRepoDouble, _editionFactoryDouble,
                _iPublicationRepoDouble, _iPublishingCompanyRepoDouble,
                _iPublicationTypeRepoDouble, _requestMapperDouble, _responseMapperDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.getEditionById("E-ABC12345"));
    }

}