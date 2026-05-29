package MITELOVERS.applicationservices;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
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

    String PUBLICATION_TYPE_NOT_FOUND_MESSAGE =
            "PublicationType with id '%s' does not exist";

    @Mock
    IPublicationTypeRepo publicationTypeRepo;

    @Mock
    PublicationTypeResponseDTOMapper mapper;

    @InjectMocks
    PublicationTypeService service;

    @Test
    void getAllPublicationTypesReturnsMappedDTOs() {
        //Arrange
        PublicationType publicationType1 = mock(PublicationType.class);
        PublicationType publicationType2 = mock(PublicationType.class);

        PublicationTypeResponseDTO dto1 =
                mock(PublicationTypeResponseDTO.class);
        PublicationTypeResponseDTO dto2 =
                mock(PublicationTypeResponseDTO.class);

        when(publicationTypeRepo.findAll())
                .thenReturn(List.of(publicationType1, publicationType2));

        when(mapper.toModel(publicationType1)).thenReturn(dto1);
        when(mapper.toModel(publicationType2)).thenReturn(dto2);

        //Act
        List<PublicationTypeResponseDTO> result =
                service.getAllPublicationTypes();

        //Assert
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));
    }

    @Test
    void getAllPublicationTypesReturnsEmptyListWhenRepoIsEmpty() {
        //Arrange
        when(publicationTypeRepo.findAll()).thenReturn(List.of());

        //Act
        List<PublicationTypeResponseDTO> result =
                service.getAllPublicationTypes();

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

        when(publicationTypeRepo.ofIdentity(any()))
                .thenReturn(Optional.of(publicationType));

        when(mapper.toModel(publicationType))
                .thenReturn(dto);

        //Act
        PublicationTypeResponseDTO result =
                service.getPublicationTypeById("BOOK");

        //Assert
        assertSame(dto, result);
    }

    @Test
    void getPublicationTypeByIdThrowsExceptionWhenPublicationTypeDoesNotExist() {
        //Arrange
        when(publicationTypeRepo.ofIdentity(any()))
                .thenReturn(Optional.empty());

        //Act
        NoSuchElementException exception =
                assertThrows(
                        NoSuchElementException.class,
                        () -> service.getPublicationTypeById("BOOK")
                );

        //Assert
        assertEquals(
                String.format(PUBLICATION_TYPE_NOT_FOUND_MESSAGE, "BOOK"),
                exception.getMessage()
        );
    }
}