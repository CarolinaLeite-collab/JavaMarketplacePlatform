package TOPSECRET.domain.publishingcompany;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockConstruction;

class PublishingCompanyFactoryTest {

    @Test
    void should_create_PublishingCompany() {

        //SUT
        PublishingCompanyFactory publishingCompanyFactory = new PublishingCompanyFactory();

        try (MockedConstruction<PublishingCompany> mockedConstruction = mockConstruction(PublishingCompany.class)) {

            //Act
            PublishingCompany publishingCompany = publishingCompanyFactory.createPublishingCompany("TASCHEN");

            //Assert
            assertNotNull(publishingCompany);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

}