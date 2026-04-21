package MITELOVERS.persistence.mem;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoEditionRepoTest {

    private EditionFactory _editionFactoryDouble;
    private Edition _editionDouble;
    private EditionId _editionIdDouble;

    private PublicationTypeId _typeIdDouble;
    private Identifier _identifierDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private Language _languageDouble;
    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _numberOfPagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    private static final String expectedMessageIdentifierAlreadyExists =
            "An Edition with this identifier already exists!";
    private static final String expectedMessageEditionAlreadyExists =
            "Edition already exists!";

    @BeforeEach
    void setUp() {
        // Arrange
        _editionFactoryDouble = mock(EditionFactory.class);

        _editionDouble = mock(Edition.class);
        _editionIdDouble = mock(EditionId.class);
        when(_editionDouble.identity()).thenReturn(_editionIdDouble);

        _typeIdDouble = mock(PublicationTypeId.class);
        _identifierDouble = mock(Identifier.class);
        _publicationIdDouble = mock(PublicationId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _languageDouble = mock(Language.class);
        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
        _numberOfPagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble = mock(EditionNumber.class);
        _bindingDouble = mock(Binding.class);
    }

    @Test
    void saveShouldStoreEditionAndReturnIt() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        // Act
        Edition result = memoRepo.save(_editionDouble);

        // Assert
        assertEquals(_editionDouble, result);
    }

    @Test
    void findAllShouldReturnSavedEditions() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        Edition anotherEditionDouble = mock(Edition.class);
        EditionId anotherEditionIdDouble = mock(EditionId.class);
        when(anotherEditionDouble.identity()).thenReturn(anotherEditionIdDouble);

        memoRepo.save(_editionDouble);
        memoRepo.save(anotherEditionDouble);

        // Act
        Iterable<Edition> result = memoRepo.findAll();

        // Assert
        List<Edition> editions = new ArrayList<>();
        result.forEach(editions::add);

        assertEquals(2, editions.size());
        assertTrue(editions.contains(_editionDouble));
        assertTrue(editions.contains(anotherEditionDouble));
    }

    @Test
    void ofIdentityShouldReturnEditionWhenItExists() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        memoRepo.save(_editionDouble);

        // Act
        Optional<Edition> result = memoRepo.ofIdentity(_editionIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_editionDouble, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyWhenEditionDoesNotExist() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        // Act
        Optional<Edition> result = memoRepo.ofIdentity(_editionIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenEditionExists() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        memoRepo.save(_editionDouble);

        // Act
        boolean result = memoRepo.containsOfIdentity(_editionIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenEditionDoesNotExist() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        // Act
        boolean result = memoRepo.containsOfIdentity(_editionIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void addEditionShouldCreateSaveAndReturnEditionWhenItDoesNotExist() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        // Act
        Edition result = memoRepo.addEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
    }

    @Test
    void addEditionShouldThrowExceptionWhenIdentifierAlreadyExists() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        Edition existingEditionDouble = mock(Edition.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);
        when(existingEditionDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionDouble.getIdentifier()).thenReturn(_identifierDouble);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEditionDouble);

        // Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                memoRepo.addEdition(
                        _typeIdDouble,
                        _identifierDouble,
                        _publicationIdDouble,
                        _publishingCompanyIdDouble,
                        publishingYear,
                        _languageDouble,
                        _dimensionDouble,
                        _weightDouble,
                        _numberOfPagesDouble,
                        _editionNumberDouble,
                        _bindingDouble
                )
        );

        // Assert
        assertEquals(expectedMessageIdentifierAlreadyExists, exception.getMessage());
    }

    @Test
    void addEditionShouldThrowExceptionWhenEquivalentEditionAlreadyExistsAndIdentifierIsNull() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(1960);

        Edition existingEditionDouble = mock(Edition.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);

        when(existingEditionDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionDouble.getIdentifier()).thenReturn(null);
        when(existingEditionDouble.sameAs(_editionDouble)).thenReturn(true);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEditionDouble);

        // Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                memoRepo.addEdition(
                        _typeIdDouble,
                        null,
                        _publicationIdDouble,
                        _publishingCompanyIdDouble,
                        publishingYear,
                        _languageDouble,
                        _dimensionDouble,
                        _weightDouble,
                        _numberOfPagesDouble,
                        _editionNumberDouble,
                        _bindingDouble
                )
        );

        // Assert
        assertEquals(expectedMessageEditionAlreadyExists, exception.getMessage());
    }

    @Test
    void addEditionShouldSaveAndReturnEditionWhenEquivalentEditionDoesNotExistAndIdentifierIsNull() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(1960);

        Edition existingEditionDouble = mock(Edition.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);

        when(existingEditionDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionDouble.getIdentifier()).thenReturn(null);
        when(existingEditionDouble.sameAs(_editionDouble)).thenReturn(false);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEditionDouble);

        // Act
        Edition result = memoRepo.addEdition(
                _typeIdDouble,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
    }

    @Test
    void addEditionShouldThrowWhenExistingEditionHasSameTypeAndSameIdentifier() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        Edition existingEditionDouble = mock(Edition.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);
        when(existingEditionDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionDouble.getIdentifier()).thenReturn(_identifierDouble);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEditionDouble);

        // Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                memoRepo.addEdition(
                        _typeIdDouble,
                        _identifierDouble,
                        _publicationIdDouble,
                        _publishingCompanyIdDouble,
                        publishingYear,
                        _languageDouble,
                        _dimensionDouble,
                        _weightDouble,
                        _numberOfPagesDouble,
                        _editionNumberDouble,
                        _bindingDouble
                )
        );

        // Assert
        assertEquals(expectedMessageIdentifierAlreadyExists, exception.getMessage());
    }

    @Test
    void addEditionShouldNotThrowWhenExistingEditionHasDifferentTypeButSameIdentifier() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        PublicationTypeId otherTypeIdDouble = mock(PublicationTypeId.class);

        Edition existingEditionDouble = mock(Edition.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);
        when(existingEditionDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionDouble.getIdentifier()).thenReturn(_identifierDouble);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(otherTypeIdDouble);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEditionDouble);

        // Act
        Edition result = memoRepo.addEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
    }

    @Test
    void addEditionShouldEvaluateIdentifierComparisonWhenBothIdentifiersAreNotNull() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        Identifier existingIdentifier = mock(Identifier.class);

        Edition existingEdition = mock(Edition.class);
        EditionId existingEditionId = mock(EditionId.class);
        PublicationTypeId otherTypeId = mock(PublicationTypeId.class);

        when(existingEdition.identity()).thenReturn(existingEditionId);
        when(existingEdition.getIdentifier()).thenReturn(existingIdentifier);
        when(existingEdition.getPublicationTypeId()).thenReturn(otherTypeId);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEdition);

        // Act
        Edition result = memoRepo.addEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
        verify(existingEdition, atLeastOnce()).getIdentifier();
        verify(existingEdition, atLeastOnce()).getPublicationTypeId();
    }

    @Test
    void findAllKeysShouldReturnAllEditionIds() {

        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);

        Edition edition1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(edition1.identity()).thenReturn(id1);

        Edition edition2 = mock(Edition.class);
        EditionId id2 = mock(EditionId.class);
        when(edition2.identity()).thenReturn(id2);

        memoRepo.save(edition1);
        memoRepo.save(edition2);

        // Act
        List<EditionId> result = memoRepo.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(id1));
        assertTrue(result.contains(id2));
    }

    @Test
    void addEditionShouldSkipIdentifierComparisonWhenNewIdentifierIsNull() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(1960);

        Edition existingEdition = mock(Edition.class);
        EditionId existingEditionId = mock(EditionId.class);

        when(existingEdition.identity()).thenReturn(existingEditionId);
        when(existingEdition.getIdentifier()).thenReturn(mock(Identifier.class));
        when(existingEdition.sameAs(_editionDouble)).thenReturn(false);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEdition);

        // Act
        Edition result = memoRepo.addEdition(
                _typeIdDouble,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
    }

    @Test
    void addEditionShouldSkipIdentifierComparisonWhenExistingIdentifierIsNull() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        Edition existingEdition = mock(Edition.class);
        EditionId existingEditionId = mock(EditionId.class);

        when(existingEdition.identity()).thenReturn(existingEditionId);
        when(existingEdition.getIdentifier()).thenReturn(null);
        when(existingEdition.sameAs(_editionDouble)).thenReturn(false);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEdition);

        // Act
        Edition result = memoRepo.addEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
    }

    @Test
    void addEditionShouldNotThrowWhenExistingEditionHasSameTypeButDifferentIdentifier() {
        // Arrange
        // SUT
        MemoEditionRepo memoRepo = new MemoEditionRepo(_editionFactoryDouble);
        Year publishingYear = Year.of(2020);

        Identifier otherIdentifierDouble = mock(Identifier.class);

        Edition existingEditionDouble = mock(Edition.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);

        when(existingEditionDouble.identity()).thenReturn(existingEditionIdDouble);
        when(existingEditionDouble.getIdentifier()).thenReturn(otherIdentifierDouble);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        memoRepo.save(existingEditionDouble);

        // Act
        Edition result = memoRepo.addEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
    }


}
