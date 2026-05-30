package MITELOVERS.applicationservices;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import MITELOVERS.mapper.PublicationTypeResponseDTOMapper;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicationTypeServiceTest {

    @InjectMocks
    PublicationTypeService _service;

    @Mock
    IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @Mock
    private PublicationTypeFactory _publicationTypeFactoryDouble;

    @Mock
    PublicationTypeResponseDTOMapper _mapperDouble;

    String PUBLICATION_TYPE_NOT_FOUND_MESSAGE =
            "PublicationType with id '%s' does not exist";

    @Test
    void getAllPublicationTypesReturnsMappedDTOs() {
        //Arrange
        PublicationType publicationType1 = mock(PublicationType.class);
        PublicationType publicationType2 = mock(PublicationType.class);

        PublicationTypeResponseDTO dto1 =
                mock(PublicationTypeResponseDTO.class);
        PublicationTypeResponseDTO dto2 =
                mock(PublicationTypeResponseDTO.class);

        when(_iPublicationTypeRepoDouble.findAll())
                .thenReturn(List.of(publicationType1, publicationType2));

        when(_mapperDouble.toModel(publicationType1)).thenReturn(dto1);
        when(_mapperDouble.toModel(publicationType2)).thenReturn(dto2);

        //Act
        List<PublicationTypeResponseDTO> result =
                _service.getAllPublicationTypes();

        //Assert
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));
    }

    @Test
    void getAllPublicationTypesReturnsEmptyListWhenRepoIsEmpty() {
        //Arrange
        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(List.of());

        //Act
        List<PublicationTypeResponseDTO> result =
                _service.getAllPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPublicationTypeByIdReturnsDTOWhenPublicationTypeExists() {
        //Arrange
        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeResponseDTO dto =
                mock(PublicationTypeResponseDTO.class);

        when(_iPublicationTypeRepoDouble.ofIdentity(any()))
                .thenReturn(Optional.of(publicationType));

        when(_mapperDouble.toModel(publicationType))
                .thenReturn(dto);

        //Act
        PublicationTypeResponseDTO result =
                _service.getPublicationTypeById("BOOK");

        //Assert
        assertSame(dto, result);
    }

    @Test
    void getPublicationTypeByIdThrowsExceptionWhenPublicationTypeDoesNotExist() {
        //Arrange
        when(_iPublicationTypeRepoDouble.ofIdentity(any()))
                .thenReturn(Optional.empty());

        //Act
        NoSuchElementException exception =
                assertThrows(
                        NoSuchElementException.class,
                        () -> _service.getPublicationTypeById("BOOK")
                );

        //Assert
        assertEquals(
                String.format(PUBLICATION_TYPE_NOT_FOUND_MESSAGE, "BOOK"),
                exception.getMessage()
        );
    }

    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);
        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(false);
        when(_iPublicationTypeRepoDouble.save(pubTypeDouble)).thenReturn(pubTypeDouble);

        //Act
        PublicationType pubTypeResult = _service.addPublicationType(publicationTypeName);

        //Assert
        assertEquals(pubTypeDouble, pubTypeResult);

    }

    @Test
    void shouldNotAddExistingPublicationType() {

        //Arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);

        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(true);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> _service.addPublicationType(publicationTypeName));

        //Assert
        assertEquals("The publication type "
                + publicationTypeName + " already exists.", exception.getMessage());

    }

    @Test
    void shouldNotSaveWhenPublicationTypeAlreadyExists() {
        //Arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);

        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(true);

        //Act
        Exception result = assertThrows(IllegalArgumentException.class,
                () -> _service.addPublicationType(publicationTypeName));

        //Assert
        assertEquals("The publication type " +
                publicationTypeName + " already exists.", result.getMessage());

    }
}