package MITELOVERS.domain.publicationtype;

import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class PublicationTypeFactoryTest {

    @Test
    void shouldCreatePublicationTypeWithName() {

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
    void shouldCreatePublicationTypeWithId() {

        // Arrange
        PublicationTypeId id = mock(PublicationTypeId.class);

        // SUT
        PublicationTypeFactory publicationTypeFactory = new PublicationTypeFactory();

        try (MockedConstruction<PublicationType> mockedConstruction = mockConstruction(PublicationType.class)){

            //Act
            PublicationType publicationTypeResult = publicationTypeFactory.createPublicationType(id);

            //Assert
            assertNotNull(publicationTypeResult);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

}
