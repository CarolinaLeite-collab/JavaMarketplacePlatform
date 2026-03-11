package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublishingCompanyRepoTest {

    private PublishingCompanyFactory _pcfDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

        _pcfDouble = mock(PublishingCompanyFactory.class);

    }

    @Test
    void should_create_PublishingCompanyRepo() {

        //Act
        PublishingCompanyRepo repo = new PublishingCompanyRepo(_pcfDouble);

        //Assert
        assertNotNull(repo);
    }

    @Test
    void should_add_PublishingCompany_to_PublishingCompanyRepo() throws InstantiationException {

        //Arrange
        PublishingCompany _pubCompanyDouble1 = mock(PublishingCompany.class);
        when(_pcfDouble.createPublishingCompany("TASCHEN")).thenReturn(_pubCompanyDouble1);

        String publishingCompanyName = "TASCHEN";

        //SUT
        PublishingCompanyRepo _repo = new PublishingCompanyRepo(_pcfDouble);

        //Act
        PublishingCompany _pubCompanyResult = _repo.registerPublishingCompany(publishingCompanyName);

        //Assert
        assertEquals(_pubCompanyDouble1, _pubCompanyResult);

    }

    @Test
    void should_fail_to_add_duplicated_PublishingCompany() {

        // Arrange
        String publishingCompanyName = "TASCHEN";

        PublishingCompany pc1 = new PublishingCompany(publishingCompanyName);
        PublishingCompany pc2 = new PublishingCompany(publishingCompanyName);

        when(_pcfDouble.createPublishingCompany("TASCHEN")).thenReturn(pc1, pc2);

        //Sut
        PublishingCompanyRepo repo = new PublishingCompanyRepo(_pcfDouble);

        // Act
        repo.registerPublishingCompany(publishingCompanyName);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.registerPublishingCompany(publishingCompanyName));
        assertEquals("Publishing Company with name " + publishingCompanyName + " already exists.",exception.getMessage());

    }

}
