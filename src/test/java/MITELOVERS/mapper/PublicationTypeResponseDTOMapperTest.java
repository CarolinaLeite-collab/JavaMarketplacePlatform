package MITELOVERS.mapper;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationTypeResponseDTOMapperTest {

    @Test
    void shouldMapPublicationTypeToResponseDTO() {
        //Arrange
        PublicationType publicationType = mock(PublicationType.class);

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.identity()).thenReturn(publicationTypeId);
        when(publicationTypeId.toString()).thenReturn("BOOK");

        PublicationTypeResponseDTOMapper mapper = new PublicationTypeResponseDTOMapper();

        //Act
        PublicationTypeResponseDTO dto = mapper.toModel(publicationType);
        //Assert
        assertEquals("BOOK", dto.getPublicationTypeId());
    }
}