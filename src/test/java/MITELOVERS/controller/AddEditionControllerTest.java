package MITELOVERS.controller;

import MITELOVERS.controllers.cli.AddEditionController;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class AddEditionControllerTest {

    @Mock
    IEditionRepo _iEditionRepoDouble;

    @Mock
    EditionFactory _editionFactoryDouble;

    @InjectMocks
    AddEditionController _addEditionController;
    
    private Edition _editionDouble;
    private PublicationTypeId _typeIdDouble;
    private Identifier _identifierDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private Language _editionLanguageDouble;
    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _numberOfPagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    private static final String expectedMessageIdentifierAlreadyExists = "An Edition with this identifier already exists!";
    private static final String expectedMessageEditionAlreadyExists = "Edition already exists!";

    @BeforeEach
    void setUp() {
        _editionDouble = mock(Edition.class);
        _typeIdDouble = mock(PublicationTypeId.class);
        _identifierDouble = mock(Identifier.class);
        _publicationIdDouble = mock(PublicationId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _editionLanguageDouble = mock(Language.class);
        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
        _numberOfPagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble =  mock(EditionNumber.class);
        _bindingDouble = mock(Binding.class);

    }

    @Test
    void testAddEditionController() {
        // SUT
        new AddEditionController(_iEditionRepoDouble, _editionFactoryDouble);
    }

    @Test
    void addEditionShouldCreateSaveAndReturnEditionWhenItDoesNotExist() {
        // Arrange
        Year publishingYear = Year.of(2020);

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _editionLanguageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        when(_iEditionRepoDouble.save(_editionDouble)).thenReturn(_editionDouble);

        // Act
        Edition result = _addEditionController.addEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _editionLanguageDouble,
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
    void addEditionShouldSaveAndReturnEditionWhenEquivalentEditionDoesNotExistAndIdentifierIsNull() {

        // Arrange
        Year publishingYear = Year.of(1960);

        Edition existingEditionDouble = mock(Edition.class);

        when(_iEditionRepoDouble.findAll())
                .thenReturn(List.of(existingEditionDouble));

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _editionLanguageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        when(_iEditionRepoDouble.save(any(Edition.class)))
                .thenReturn(_editionDouble);

        // Act
        Edition result = _addEditionController.addEdition(
                _typeIdDouble,
                null,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _editionLanguageDouble,
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
        Year publishingYear = Year.of(2020);

        Edition existingEditionDouble = mock(Edition.class);
        when(existingEditionDouble.getIdentifier()).thenReturn(_identifierDouble);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);

        when(_iEditionRepoDouble.findAll()).thenReturn(List.of(existingEditionDouble)); // ← corrigido

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble, _identifierDouble, _publicationIdDouble,
                _publishingCompanyIdDouble, publishingYear, _editionLanguageDouble,
                _dimensionDouble, _weightDouble, _numberOfPagesDouble,
                _editionNumberDouble, _bindingDouble
        )).thenReturn(_editionDouble);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                _addEditionController.addEdition(
                        _typeIdDouble, _identifierDouble, _publicationIdDouble,
                        _publishingCompanyIdDouble, publishingYear, _editionLanguageDouble,
                        _dimensionDouble, _weightDouble, _numberOfPagesDouble,
                        _editionNumberDouble, _bindingDouble
                )
        );

        assertEquals(expectedMessageIdentifierAlreadyExists, exception.getMessage());
    }

    @Test
    void addEditionShouldNotThrowWhenExistingEditionHasDifferentTypeButSameIdentifier() {
        // Arrange
        Year publishingYear = Year.of(2020);

        PublicationTypeId otherTypeIdDouble = mock(PublicationTypeId.class);

        Edition existingEditionDouble = mock(Edition.class);
        EditionId existingEditionIdDouble = mock(EditionId.class);
        when(existingEditionDouble.getIdentifier()).thenReturn(_identifierDouble);
        when(existingEditionDouble.getPublicationTypeId()).thenReturn(otherTypeIdDouble);

        when(_iEditionRepoDouble.findAll()).thenReturn(List.of(existingEditionDouble));

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _editionLanguageDouble,
                _dimensionDouble,
                _weightDouble,
                _numberOfPagesDouble,
                _editionNumberDouble,
                _bindingDouble
        )).thenReturn(_editionDouble);

        when(_iEditionRepoDouble.save(_editionDouble)).thenReturn(_editionDouble);

        // Act
        Edition result = _addEditionController.addEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _publishingCompanyIdDouble,
                publishingYear,
                _editionLanguageDouble,
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
        Year publishingYear = Year.of(2020);

        Identifier existingIdentifier = mock(Identifier.class);
        Edition existingEdition = mock(Edition.class);
        PublicationTypeId otherTypeId = mock(PublicationTypeId.class);

        when(existingEdition.getIdentifier()).thenReturn(existingIdentifier);
        when(existingEdition.getPublicationTypeId()).thenReturn(otherTypeId);

        when(_iEditionRepoDouble.findAll()).thenReturn(List.of(existingEdition)); // ← edição no repo

        when(_editionFactoryDouble.createEdition(
                _typeIdDouble, _identifierDouble, _publicationIdDouble,
                _publishingCompanyIdDouble, publishingYear, _editionLanguageDouble,
                _dimensionDouble, _weightDouble, _numberOfPagesDouble,
                _editionNumberDouble, _bindingDouble
        )).thenReturn(_editionDouble);

        when(_iEditionRepoDouble.save(_editionDouble)).thenReturn(_editionDouble);

        // Act
        Edition result = _addEditionController.addEdition(
                _typeIdDouble, _identifierDouble, _publicationIdDouble,
                _publishingCompanyIdDouble, publishingYear, _editionLanguageDouble,
                _dimensionDouble, _weightDouble, _numberOfPagesDouble,
                _editionNumberDouble, _bindingDouble
        );

        // Assert
        assertEquals(_editionDouble, result);
        verify(existingEdition, atLeastOnce()).getIdentifier();
        verify(existingEdition, atLeastOnce()).getPublicationTypeId();
    }

    @Test
    void addEditionShouldThrowExceptionWhenIdentifierAlreadyExists() {

        // Arrange
        Year publishingYear = Year.of(2020);

        Edition existingEditionDouble = mock(Edition.class);

        when(existingEditionDouble.getIdentifier()).thenReturn(_identifierDouble);

        when(existingEditionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);

        when(_iEditionRepoDouble.findAll()).thenReturn(List.of(existingEditionDouble));

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _addEditionController.addEdition(
                        _typeIdDouble,
                        _identifierDouble,
                        _publicationIdDouble,
                        _publishingCompanyIdDouble,
                        publishingYear,
                        _editionLanguageDouble,
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
        Year publishingYear = Year.of(1960);

        Edition existingEditionDouble = mock(Edition.class);

        when(existingEditionDouble.sameAs(any())).thenReturn(true);

        when(_iEditionRepoDouble.findAll())
                .thenReturn(List.of(existingEditionDouble));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _addEditionController.addEdition(
                        _typeIdDouble,
                        null,
                        _publicationIdDouble,
                        _publishingCompanyIdDouble,
                        publishingYear,
                        _editionLanguageDouble,
                        _dimensionDouble,
                        _weightDouble,
                        _numberOfPagesDouble,
                        _editionNumberDouble,
                        _bindingDouble
                )
        );

        assertEquals(expectedMessageEditionAlreadyExists, exception.getMessage());
    }

}