package TOPSECRET.persistence.mem;

import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publishingcompany.PublishingCompanyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoPublishingCompanyRepoTest {

    private PublishingCompanyFactory _pcfDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

        _pcfDouble = mock(PublishingCompanyFactory.class);

    }

    @Test
    void shouldCreatePublishingCompanyRepo() {

        //Act
        MemoPublishingCompanyRepo repo = new MemoPublishingCompanyRepo(_pcfDouble);

        //Assert
        assertNotNull(repo);
    }

    @Test
    void shouldAddPublishingCompanyToPublishingCompanyRepo() {

        //Arrange
        PublishingCompany _pubCompanyDouble1 = mock(PublishingCompany.class);
        when(_pcfDouble.createPublishingCompany("TASCHEN")).thenReturn(_pubCompanyDouble1);

        String publishingCompanyName = "TASCHEN";

        //SUT
        MemoPublishingCompanyRepo _repo = new MemoPublishingCompanyRepo(_pcfDouble);

        //Act
        PublishingCompany _pubCompanyResult = _repo.registerPublishingCompany(publishingCompanyName);

        //Assert
        assertEquals(_pubCompanyDouble1, _pubCompanyResult);

    }

    @Test
    void shouldFailToAddDuplicatedPublishingCompany() {

        // Arrange
        String publishingCompanyName = "TASCHEN";

        PublishingCompany pc1 = mock(PublishingCompany.class);
        PublishingCompany pc2 = mock(PublishingCompany.class);

        when(_pcfDouble.createPublishingCompany("TASCHEN")).thenReturn(pc1, pc2);
        when(pc1.isSamePublishingCompany("TASCHEN")).thenReturn(true);
        when(pc2.isSamePublishingCompany("TASCHEN")).thenReturn(true);

        //SUT
        MemoPublishingCompanyRepo repo = new MemoPublishingCompanyRepo(_pcfDouble);

        // Act
        repo.registerPublishingCompany(publishingCompanyName);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.registerPublishingCompany(publishingCompanyName));
        assertEquals("Publishing Company with name " + publishingCompanyName + " already exists.",exception.getMessage());

    }

}
