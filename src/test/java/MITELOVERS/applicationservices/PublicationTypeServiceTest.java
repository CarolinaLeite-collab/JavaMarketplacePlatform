package MITELOVERS.applicationservices;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicationTypeServiceTest {

    @InjectMocks
    PublicationTypeService _service;

    @Mock
    IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @Mock
    PublicationTypeFactory _publicationTypeFactoryDouble;

    String PUBLICATION_TYPE_NOT_FOUND_MESSAGE =
            "PublicationType with id '%s' does not exist";


    @Test
    void getAllPublicationTypesReturnsListOfPublicationTypes() {
        // Arrange
        PublicationType publicationType1 = mock(PublicationType.class);
        PublicationType publicationType2 = mock(PublicationType.class);

        when(_iPublicationTypeRepoDouble.findAll())
                .thenReturn(List.of(publicationType1, publicationType2));

        // Act
        List<PublicationType> result = _service.getAllPublicationTypes();

        // Assert
        assertEquals(2, result.size());
        assertSame(publicationType1, result.get(0));
        assertSame(publicationType2, result.get(1));
    }

    @Test
    void getAllPublicationTypesReturnsEmptyListWhenRepoIsEmpty() {
        // Arrange
        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<PublicationType> result = _service.getAllPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void getPublicationTypeByIdReturnsPublicationTypeWhenExists() {
        // Arrange
        PublicationType publicationType = mock(PublicationType.class);

        when(_iPublicationTypeRepoDouble.ofIdentity(any()))
                .thenReturn(Optional.of(publicationType));

        // Act
        PublicationType result = _service.getPublicationTypeById("BOOK");

        // Assert
        assertSame(publicationType, result);
    }

    @Test
    void getPublicationTypeByIdThrowsWhenNotFound() {
        // Arrange
        when(_iPublicationTypeRepoDouble.ofIdentity(any()))
                .thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> _service.getPublicationTypeById("BOOK")
        );
        assertEquals(
                String.format(PUBLICATION_TYPE_NOT_FOUND_MESSAGE, "BOOK"),
                exception.getMessage()
        );
    }

    // --- addPublicationType ---

    @Test
    void addPublicationTypeReturnsCreatedType() {
        // Arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);
        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(false);
        when(_iPublicationTypeRepoDouble.save(pubTypeDouble)).thenReturn(pubTypeDouble);

        // Act
        PublicationType result = _service.addPublicationType(publicationTypeName);

        // Assert
        assertSame(pubTypeDouble, result);
    }

    @Test
    void addPublicationTypeThrowsWhenAlreadyExists() {
        // Arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);
        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> _service.addPublicationType(publicationTypeName)
        );
        assertEquals("The publication type " + publicationTypeName + " already exists.",
                exception.getMessage());
    }
}