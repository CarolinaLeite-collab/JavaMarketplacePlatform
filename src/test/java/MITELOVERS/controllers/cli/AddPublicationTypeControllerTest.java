package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.domain.publicationtype.PublicationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class AddPublicationTypeControllerTest {

    //SUT
    @InjectMocks
    AddPublicationTypeController _controller;

    @Mock
    PublicationTypeService _publicationTypeService;

    @Mock
    PublicationType _publicationTypeDouble;

    @Test
    void shouldAddPublicationTypeAndReturnCreatedType() {
        //Arrange
        String publicationTypeName = "book";

        when(_publicationTypeService.addPublicationType(publicationTypeName))
                .thenReturn(_publicationTypeDouble);

        //Act
        PublicationType result =
                _controller.addPublicationType(publicationTypeName);

        //Assert
        assertSame(_publicationTypeDouble, result);
    }

    @Test
    void shouldPropagateExceptionWhenPublicationTypeAlreadyExists() {
        //Arrange
        String publicationTypeName = "book";

        when(_publicationTypeService.addPublicationType(publicationTypeName))
                .thenThrow(new IllegalArgumentException(
                        "The publication type " + publicationTypeName + " already exists."));

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.addPublicationType(publicationTypeName)
        );

        //Assert
        assertEquals(
                "The publication type " + publicationTypeName + " already exists.",
                exception.getMessage()
        );
    }

}
