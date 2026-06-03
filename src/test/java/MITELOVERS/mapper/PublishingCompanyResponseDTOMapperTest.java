package MITELOVERS.mapper;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublishingCompanyResponseDTOMapperTest {

    private PublishingCompany _publishingCompanyDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp() {
        _publishingCompanyDouble = mock(PublishingCompany.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_publishingCompanyDouble.identity()).thenReturn(_publishingCompanyIdDouble);
        when(_publishingCompanyIdDouble.toString()).thenReturn("PORTO EDITORA");
        when(_publishingCompanyDouble.getPublishingCompanyName()).thenReturn("Porto Editora");
    }

    @Test
    void toModelMapsIdCorrectly() {
        // SUT
        PublishingCompanyResponseDTOMapper mapper = new PublishingCompanyResponseDTOMapper();

        // Act
        PublishingCompanyResponseDTO result = mapper.toModel(_publishingCompanyDouble);

        // Assert
        assertEquals("PORTO EDITORA", result.getPublishingCompanyId());
    }

    @Test
    void toModelMapsNameCorrectly() {
        // SUT
        PublishingCompanyResponseDTOMapper mapper = new PublishingCompanyResponseDTOMapper();

        // Act
        PublishingCompanyResponseDTO result = mapper.toModel(_publishingCompanyDouble);

        // Assert
        assertEquals("Porto Editora", result.getPublishingCompanyName());
    }

    @Test
    void toModelMapsAllFieldsCorrectly() {
        // SUT
        PublishingCompanyResponseDTOMapper mapper = new PublishingCompanyResponseDTOMapper();

        // Act
        PublishingCompanyResponseDTO result = mapper.toModel(_publishingCompanyDouble);

        // Assert
        assertAll(
                () -> assertEquals("PORTO EDITORA", result.getPublishingCompanyId()),
                () -> assertEquals("Porto Editora", result.getPublishingCompanyName())
        );
    }
}