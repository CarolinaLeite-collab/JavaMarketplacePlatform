package MITELOVERS.domain.publishingcompany;

import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class PublishingCompanyFactoryTest {

    @Test
    void shouldCreatePublishingCompany() {

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

    @Test
    void createShouldPublishingCompanyWithIdShouldReturnCompanyWithSameId() {

        //Arrange
        PublishingCompanyId _publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        //SUT
        PublishingCompanyFactory publishingCompanyFactory = new PublishingCompanyFactory();

        try (MockedConstruction<PublishingCompany> mockedConstruction = mockConstruction(PublishingCompany.class)) {

            //Act
            PublishingCompany publishingCompany = publishingCompanyFactory.createPublishingCompany(_publishingCompanyIdDouble);

            //Assert
            assertNotNull(publishingCompany);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

}
