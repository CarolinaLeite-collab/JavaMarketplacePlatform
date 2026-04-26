package MITELOVERS.persistence;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemPublishingCompanyRepoTest {

    private PublishingCompanyFactory _pcfDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

        //Arrange
        _pcfDouble = mock(PublishingCompanyFactory.class);

    }

    @Test
    void constructorShouldCreateNonNullPublishingCompanyRepo() {

        //SUT + Act
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        //Assert
        assertNotNull(repo);
    }

    @Test
    void registerPublishingCompanyShouldReturnPublishingCompany() {

        //Arrange
        String pubCoName = "Pendant Publishing";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        when(_pcfDouble.createPublishingCompany(pubCoName)).thenReturn(pubCoDouble1);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        //Act
        PublishingCompany pubCoResult = repo.registerPublishingCompany(pubCoName);

        //Assert
        assertEquals(pubCoDouble1, pubCoResult);

    }

    @Test
    void shouldAddPublishingCompanySuccessfullyAndListNotEmpty() {

        // Arrange
        String pubCoName = "Pendant Publishing";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        when(_pcfDouble.createPublishingCompany(pubCoName)).thenReturn(pubCoDouble1);

        // SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        // Act
        repo.registerPublishingCompany(pubCoName);

        List<PublishingCompany> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldNotAllowDuplicatePublishingCompanies() {

        // Arrange
        String pubCoName = "Penguin Random House";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);

        when(_pcfDouble.createPublishingCompany(pubCoName)).thenReturn(pubCoDouble1, pubCoDouble2);
        when(pubCoDouble1.sameAs(pubCoName)).thenReturn(true);
        when(pubCoDouble2.sameAs(pubCoName)).thenReturn(true);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        // Act
        repo.registerPublishingCompany(pubCoName);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> repo.registerPublishingCompany(pubCoName));

    }

    @Test
    void shouldThrowCorrectMessageOnDuplicatePublishingCompanies() {

        // Arrange
        String pubCoName = "Penguin Random House";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);

        when(_pcfDouble.createPublishingCompany(pubCoName)).thenReturn(pubCoDouble1, pubCoDouble2);
        when(pubCoDouble1.sameAs(pubCoName)).thenReturn(true);
        when(pubCoDouble2.sameAs(pubCoName)).thenReturn(true);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        // Act
        repo.registerPublishingCompany(pubCoName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.registerPublishingCompany(pubCoName));

        // Assert
        assertEquals("This publishing company is already registered.", exception.getMessage());
    }

    @Test
    void shouldAllowRegisteringDifferentPublishingCompanies() {

        // Arrange
        String pubCoName = "Penguin Random House";
        String pubCoName2 = "Pendant Publishing";
        String pubCoName3 = "Simon & Schuster";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble3 = mock(PublishingCompany.class);

        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        PublishingCompanyId pubCoIdDouble2 = mock(PublishingCompanyId.class);
        PublishingCompanyId pubCoIdDouble3 = mock(PublishingCompanyId.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);
        when(pubCoDouble2.identity()).thenReturn(pubCoIdDouble2);
        when(pubCoDouble3.identity()).thenReturn(pubCoIdDouble3);

        when(_pcfDouble.createPublishingCompany(pubCoName)).thenReturn(pubCoDouble1);
        when(_pcfDouble.createPublishingCompany(pubCoName2)).thenReturn(pubCoDouble2);
        when(_pcfDouble.createPublishingCompany(pubCoName3)).thenReturn(pubCoDouble3);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        // Act
        repo.registerPublishingCompany(pubCoName);
        repo.registerPublishingCompany(pubCoName2);
        repo.registerPublishingCompany(pubCoName3);

        List<PublishingCompany> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        // Assert
       assertEquals(3, result.size());

    }

    @Test
    void ofIdentityShouldReturnPublishingCompanyWhenIdPresent(){

        //Arrange
        String pubCoName = "Penguin Random House";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);
        when(_pcfDouble.createPublishingCompany(pubCoName)).thenReturn(pubCoDouble1);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);
        repo.registerPublishingCompany(pubCoName);

        //Act
        var result = repo.ofIdentity(pubCoIdDouble1);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(pubCoDouble1, result.get());

    }

    @Test
    void ofIdentityShouldReturnEmptyOptionalWhenIdNotPresent(){

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        PublishingCompanyId notSavedIdDouble = mock(PublishingCompanyId.class);

        //Act
        var result = repo.ofIdentity(notSavedIdDouble);

        //Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void findAllKeysShouldReturnListOfIds() {

        // Arrange
        String pubCoName1 = "Penguin Random House";
        String pubCoName2 = "Pendant Publishing";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);

        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        PublishingCompanyId pubCoIdDouble2 = mock(PublishingCompanyId.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);
        when(pubCoDouble2.identity()).thenReturn(pubCoIdDouble2);

        when(_pcfDouble.createPublishingCompany(pubCoName1)).thenReturn(pubCoDouble1);
        when(_pcfDouble.createPublishingCompany(pubCoName2)).thenReturn(pubCoDouble2);

        // SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        // Act
        repo.registerPublishingCompany(pubCoName1);
        repo.registerPublishingCompany(pubCoName2);
        List<PublishingCompanyId> result = repo.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pubCoIdDouble1));
        assertTrue(result.contains(pubCoIdDouble2));
    }

    @Test
    void findAllKeysShouldReturnDefensiveCopy() {

        // Arrange
        String pubCoName1 = "Penguin Random House";
        String pubCoName2 = "Pendant Publishing";

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);

        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        PublishingCompanyId pubCoIdDouble2 = mock(PublishingCompanyId.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);
        when(pubCoDouble2.identity()).thenReturn(pubCoIdDouble2);

        when(_pcfDouble.createPublishingCompany(pubCoName1)).thenReturn(pubCoDouble1);
        when(_pcfDouble.createPublishingCompany(pubCoName2)).thenReturn(pubCoDouble2);

        // SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo(_pcfDouble);

        repo.registerPublishingCompany(pubCoName1);
        repo.registerPublishingCompany(pubCoName2);

        // Act
        List<PublishingCompanyId> result = repo.findAllKeys();
        result.clear();
        List<PublishingCompanyId> resultAfterClear = repo.findAllKeys();

        // Assert
        assertEquals(2, resultAfterClear.size());
        assertTrue(resultAfterClear.contains(pubCoIdDouble1));
        assertTrue(resultAfterClear.contains(pubCoIdDouble2));
    }

}
