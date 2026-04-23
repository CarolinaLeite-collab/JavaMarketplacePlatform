package MITELOVERS.persistence.mem;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemPublishingCompanyRepoTest {

    @Test
    void constructorShouldCreatePublishingCompanyRepo() {

        //SUT + Act
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        //Assert
        assertNotNull(repo);
    }

    @Test
    void saveNewPublishingCompanyShouldReturnPublishingCompany() {

        //Arrange
        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        //Act
        PublishingCompany pubCoResult = repo.save(pubCoDouble1);

        //Assert
        assertEquals(pubCoDouble1, pubCoResult);

    }

    @Test
    void findAllShouldReturnAllSavedPublishingCompanies() {

        // Arrange
        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        PublishingCompanyId pubCoIdDouble2 = mock(PublishingCompanyId.class);

        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);
        when(pubCoDouble2.identity()).thenReturn(pubCoIdDouble2);

        // SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        repo.save(pubCoDouble1);
        repo.save(pubCoDouble2);

        // Act
        List<PublishingCompany> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pubCoDouble1));
        assertTrue(result.contains(pubCoDouble2));
    }

    @Test
    void findAllOnEmptyRepoShouldReturnEmptyIterable() {

        //SUT + Arrange
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        // Act
        List<PublishingCompany> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        //Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void ofIdentityShouldReturnPublishingCompanyWhenIdPresent(){

        //Arrange
        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();
        repo.save(pubCoDouble1);

        //Act
        Optional<PublishingCompany> result = repo.ofIdentity(pubCoIdDouble1);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(pubCoDouble1, result.get());

    }

    @Test
    void ofIdentityShouldReturnEmptyOptionalWhenIdNotPresent(){

        //SUT + Arrange
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        PublishingCompanyId notSavedIdDouble = mock(PublishingCompanyId.class);

        //Act
        Optional<PublishingCompany> result = repo.ofIdentity(notSavedIdDouble);

        //Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenIdPresent() {

        //Arrange
        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();
        repo.save(pubCoDouble1);

        //Act
        boolean contains = repo.containsOfIdentity(pubCoIdDouble1);

        //Assert
        assertTrue(contains);

    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenIdNotPresent() {

        //SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        PublishingCompanyId notSavedIdDouble = mock(PublishingCompanyId.class);

        //Act
        boolean contains = repo.containsOfIdentity(notSavedIdDouble);

        //Assert
        assertFalse(contains);

    }

    @Test
    void findAllKeysShouldReturnListOfIds() {

        // Arrange
        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);

        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        PublishingCompanyId pubCoIdDouble2 = mock(PublishingCompanyId.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);
        when(pubCoDouble2.identity()).thenReturn(pubCoIdDouble2);

        // SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        repo.save(pubCoDouble1);
        repo.save(pubCoDouble2);

        // Act
        List<PublishingCompanyId> result = repo.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pubCoIdDouble1));
        assertTrue(result.contains(pubCoIdDouble2));
    }

    @Test
    void findAllKeysShouldReturnDefensiveCopy() {

        // Arrange
        PublishingCompany pubCoDouble1 = mock(PublishingCompany.class);
        PublishingCompany pubCoDouble2 = mock(PublishingCompany.class);

        PublishingCompanyId pubCoIdDouble1 = mock(PublishingCompanyId.class);
        PublishingCompanyId pubCoIdDouble2 = mock(PublishingCompanyId.class);

        when(pubCoDouble1.identity()).thenReturn(pubCoIdDouble1);
        when(pubCoDouble2.identity()).thenReturn(pubCoIdDouble2);

        // SUT
        MemPublishingCompanyRepo repo = new MemPublishingCompanyRepo();

        repo.save(pubCoDouble1);
        repo.save(pubCoDouble2);

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
