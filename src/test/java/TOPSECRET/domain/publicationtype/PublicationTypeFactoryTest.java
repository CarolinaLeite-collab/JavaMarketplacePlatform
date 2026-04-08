package TOPSECRET.domain.publicationtype;

import TOPSECRET.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class PublicationTypeFactoryTest {

    @Test
    void shouldCreatePublicationType() {

        //Arrange + SUT
        PublicationTypeFactory publicationTypeFactory = new PublicationTypeFactory();

        try (MockedConstruction<PublicationType> mockedConstruction = mockConstruction(PublicationType.class)){

            //Act
            PublicationType publicationTypeResult = publicationTypeFactory.createPublicationType("BOOK");

            //Assert
            assertNotNull(publicationTypeResult);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

    @Test
    void shouldCreatePublicationTypeFromExistingId() {

        //Arrange
        PublicationTypeId _pubTypeIdDouble = mock(PublicationTypeId.class);

        //SUT
        PublicationTypeFactory publicationTypeFactory = new PublicationTypeFactory();

        try (MockedConstruction<PublicationType> mockedConstruction = mockConstruction(PublicationType.class)){

            //Act
            PublicationType publicationTypeResult = publicationTypeFactory.createPublicationType(_pubTypeIdDouble);

            //Assert
            assertNotNull(publicationTypeResult);
            assertEquals(1, mockedConstruction.constructed().size());

        }
    }

}