package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;

class PublicationTypeFactoryTest {

    @Test
    void shouldCreatePublicationType(){

        //SUT
        PublicationTypeFactory publicationTypeFactory = new PublicationTypeFactory();

        try (MockedConstruction<PublicationType> mockedConstruction = mockConstruction(PublicationType.class)){

            //Act
            PublicationType publicationTypeResult = publicationTypeFactory.newPublicationType("BOOK");

            //Assert
            assertNotNull(publicationTypeResult);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

}