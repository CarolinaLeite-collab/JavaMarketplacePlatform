package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddEditionControllerTest {

    @InjectMocks
    AddEditionController _controller;

    @Mock
    EditionService _editionServiceDouble;

    @Test
    void addEditionDelegatesToServiceAndReturnsEdition() {
        // Arrange
        PublicationTypeId typeId = mock(PublicationTypeId.class);
        Identifier identifier = mock(Identifier.class);
        PublicationId publicationId = mock(PublicationId.class);
        PublishingCompanyId publishingCompanyId = mock(PublishingCompanyId.class);
        Year year = mock(Year.class);
        Language language = mock(Language.class);
        Dimension dimension = mock(Dimension.class);
        Weight weight = mock(Weight.class);
        NumberOfPages numberOfPages = mock(NumberOfPages.class);
        EditionNumber editionNumber = mock(EditionNumber.class);
        Binding binding = mock(Binding.class);
        Edition editionDouble = mock(Edition.class);

        when(_editionServiceDouble.registerEdition(
                typeId, identifier, publicationId, publishingCompanyId,
                year, language, dimension, weight,
                numberOfPages, editionNumber, binding))
                .thenReturn(editionDouble);

        // Act
        Edition result = _controller.addEdition(
                typeId, identifier, publicationId, publishingCompanyId,
                year, language, dimension, weight,
                numberOfPages, editionNumber, binding);

        // Assert
        assertSame(editionDouble, result);
        verify(_editionServiceDouble).registerEdition(
                typeId, identifier, publicationId, publishingCompanyId,
                year, language, dimension, weight,
                numberOfPages, editionNumber, binding);
    }

    @Test
    void addEditionWithNullOptionalFieldsDelegatesToService() {
        // Arrange
        PublicationTypeId typeId = mock(PublicationTypeId.class);
        Identifier identifier = mock(Identifier.class);
        PublicationId publicationId = mock(PublicationId.class);
        PublishingCompanyId publishingCompanyId = mock(PublishingCompanyId.class);
        Year year = mock(Year.class);
        Language language = mock(Language.class);
        Edition editionDouble = mock(Edition.class);

        when(_editionServiceDouble.registerEdition(
                typeId, identifier, publicationId, publishingCompanyId,
                year, language, null, null, null, null, null))
                .thenReturn(editionDouble);

        // Act
        Edition result = _controller.addEdition(
                typeId, identifier, publicationId, publishingCompanyId,
                year, language, null, null, null, null, null);

        // Assert
        assertSame(editionDouble, result);
    }

    @Test
    void addEditionPropagatesServiceException() {
        // Arrange
        PublicationTypeId typeId = mock(PublicationTypeId.class);
        Identifier identifier = mock(Identifier.class);
        PublicationId publicationId = mock(PublicationId.class);
        PublishingCompanyId publishingCompanyId = mock(PublishingCompanyId.class);
        Year year = mock(Year.class);
        Language language = mock(Language.class);

        when(_editionServiceDouble.registerEdition(
                typeId, identifier, publicationId, publishingCompanyId,
                year, language, null, null, null, null, null))
                .thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                _controller.addEdition(
                        typeId, identifier, publicationId, publishingCompanyId,
                        year, language, null, null, null, null, null));
    }
}