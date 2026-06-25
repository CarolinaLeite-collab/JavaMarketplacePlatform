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
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditionServiceTest {

    // SUT
    @InjectMocks
    EditionService _service;

    @Mock
    IEditionRepo _editionRepoDouble;

    @Mock
    EditionFactory _editionFactoryDouble;

    @Mock
    IPublicationRepo _publicationRepoDouble;

    @Mock
    IPublishingCompanyRepo _publishingCompanyRepoDouble;

    @Mock
    IPublicationTypeRepo _publicationTypeRepoDouble;

    private PublicationTypeId _typeIdDouble;
    private Identifier _identifierDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private Year _yearDouble;
    private Language _languageDouble;
    private Edition _editionDouble;
    private EditionId _editionIdDouble;

    @BeforeEach
    void setUp() {
        _typeIdDouble = mock(PublicationTypeId.class);
        _identifierDouble = mock(Identifier.class);
        _publicationIdDouble = mock(PublicationId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _yearDouble = mock(Year.class);
        _languageDouble = mock(Language.class);
        _editionDouble = mock(Edition.class);
        _editionIdDouble = mock(EditionId.class);
    }

    @Test
    void registerEditionReturnsEditionWhenSuccessful() {
        // Arrange
        when(_publicationTypeRepoDouble.ofIdentity(_typeIdDouble)).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(mock(Publication.class)));
        when(_publishingCompanyRepoDouble.ofIdentity(_publishingCompanyIdDouble)).thenReturn(Optional.of(mock(PublishingCompany.class)));
        when(_editionFactoryDouble.createEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null))
                .thenReturn(_editionDouble);
        when(_editionRepoDouble.findAll()).thenReturn(List.of());
        when(_editionRepoDouble.save(_editionDouble)).thenReturn(_editionDouble);

        // Act
        Edition result = _service.registerEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null);

        // Assert
        assertSame(_editionDouble, result);
    }

    @Test
    void registerEditionThrowsWhenPublicationTypeNotFound() {
        // Arrange
        when(_publicationTypeRepoDouble.ofIdentity(_typeIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                _service.registerEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null));
    }

    @Test
    void registerEditionThrowsWhenPublicationNotFound() {
        // Arrange
        when(_publicationTypeRepoDouble.ofIdentity(_typeIdDouble)).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                _service.registerEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null));
    }

    @Test
    void registerEditionThrowsWhenPublishingCompanyNotFound() {
        // Arrange
        when(_publicationTypeRepoDouble.ofIdentity(_typeIdDouble)).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(mock(Publication.class)));
        when(_publishingCompanyRepoDouble.ofIdentity(_publishingCompanyIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                _service.registerEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null));
    }

    @Test
    void registerEditionReturnsExistingWhenDuplicateIdentifierFound() {
        // Arrange
        Edition existingEditionDouble = mock(Edition.class);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);
        when(existingEditionDouble.getIdentifier()).thenReturn(_identifierDouble);

        when(_publicationTypeRepoDouble.ofIdentity(_typeIdDouble)).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(mock(Publication.class)));
        when(_publishingCompanyRepoDouble.ofIdentity(_publishingCompanyIdDouble)).thenReturn(Optional.of(mock(PublishingCompany.class)));
        when(_editionFactoryDouble.createEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null))
                .thenReturn(_editionDouble);
        when(_editionRepoDouble.findAll()).thenReturn(List.of(existingEditionDouble));

        // Act
        Edition result = _service.registerEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null);

        // Assert
        assertSame(existingEditionDouble, result);
        verify(_editionRepoDouble, never()).save(any());
    }

    @Test
    void registerEditionReturnsExistingWhenSameAsMatchFound() {
        // Arrange
        Edition existingEditionDouble = mock(Edition.class);
        when(existingEditionDouble.getIdentifier()).thenReturn(null);
        when(existingEditionDouble.sameAs(_editionDouble)).thenReturn(true);

        when(_publicationTypeRepoDouble.ofIdentity(_typeIdDouble)).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(mock(Publication.class)));
        when(_publishingCompanyRepoDouble.ofIdentity(_publishingCompanyIdDouble)).thenReturn(Optional.of(mock(PublishingCompany.class)));
        when(_editionFactoryDouble.createEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null))
                .thenReturn(_editionDouble);
        when(_editionRepoDouble.findAll()).thenReturn(List.of(existingEditionDouble));

        // Act
        Edition result = _service.registerEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null);

        // Assert
        assertSame(existingEditionDouble, result);
        verify(_editionRepoDouble, never()).save(any());
    }

    @Test
    void registerEditionSavesWhenNoExistingMatchFound() {
        // Arrange
        Edition nonMatchingEdition = mock(Edition.class);
        when(nonMatchingEdition.getIdentifier()).thenReturn(null);
        when(nonMatchingEdition.sameAs(_editionDouble)).thenReturn(false);

        when(_publicationTypeRepoDouble.ofIdentity(_typeIdDouble)).thenReturn(Optional.of(mock(PublicationType.class)));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(mock(Publication.class)));
        when(_publishingCompanyRepoDouble.ofIdentity(_publishingCompanyIdDouble)).thenReturn(Optional.of(mock(PublishingCompany.class)));
        when(_editionFactoryDouble.createEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null))
                .thenReturn(_editionDouble);
        when(_editionRepoDouble.findAll()).thenReturn(List.of(nonMatchingEdition));
        when(_editionRepoDouble.save(_editionDouble)).thenReturn(_editionDouble);

        // Act
        Edition result = _service.registerEdition(_typeIdDouble, _identifierDouble, _publicationIdDouble, _publishingCompanyIdDouble, _yearDouble, _languageDouble, null, null, null, null, null);

        // Assert
        assertSame(_editionDouble, result);
        verify(_editionRepoDouble).save(_editionDouble);
    }

    @Test
    void getAllEditionsReturnsListOfEditions() {
        // Arrange
        Edition edition1 = mock(Edition.class);
        Edition edition2 = mock(Edition.class);
        when(_editionRepoDouble.findAll()).thenReturn(List.of(edition1, edition2));

        // Act
        List<Edition> result = _service.getAllEditions();

        // Assert
        assertEquals(2, result.size());
        assertSame(edition1, result.get(0));
        assertSame(edition2, result.get(1));
    }

    @Test
    void getAllEditionsReturnsEmptyListWhenNoEditionsExist() {
        // Arrange
        when(_editionRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<Edition> result = _service.getAllEditions();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllEditionsByPublicationReturnsMatchingEditions() {
        // Arrange
        Edition matchingEdition = mock(Edition.class);
        Edition nonMatchingEdition = mock(Edition.class);
        when(matchingEdition.getPublicationId()).thenReturn(_publicationIdDouble);
        when(nonMatchingEdition.getPublicationId()).thenReturn(mock(PublicationId.class));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(mock(Publication.class)));
        when(_editionRepoDouble.findAll()).thenReturn(List.of(matchingEdition, nonMatchingEdition));

        // Act
        List<Edition> result = _service.getAllEditionsByPublication(_publicationIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(matchingEdition, result.get(0));
    }

    @Test
    void getAllEditionsByPublicationThrowsWhenPublicationNotFound() {
        // Arrange
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                _service.getAllEditionsByPublication(_publicationIdDouble));
    }

    @Test
    void getAllEditionsByPublicationReturnsEmptyListWhenNoMatch() {
        // Arrange
        Edition nonMatchingEdition = mock(Edition.class);
        when(nonMatchingEdition.getPublicationId()).thenReturn(mock(PublicationId.class));
        when(_publicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(mock(Publication.class)));
        when(_editionRepoDouble.findAll()).thenReturn(List.of(nonMatchingEdition));

        // Act
        List<Edition> result = _service.getAllEditionsByPublication(_publicationIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getEditionByIdReturnsEdition() {
        // Arrange
        when(_editionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.of(_editionDouble));

        // Act
        Edition result = _service.getEditionById(_editionIdDouble);

        // Assert
        assertSame(_editionDouble, result);
    }

    @Test
    void getEditionByIdThrowsWhenNotFound() {
        // Arrange
        when(_editionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                _service.getEditionById(_editionIdDouble));
    }
}