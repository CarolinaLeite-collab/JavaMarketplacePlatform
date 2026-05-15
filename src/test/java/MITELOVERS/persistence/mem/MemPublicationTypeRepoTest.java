package MITELOVERS.persistence.mem;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemPublicationTypeRepoTest {

    @Test
    void constructorShouldCreatePublicationTypeRepo() {

        //SUT + Act
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

    }


    @Test
    void saveNewPublicationTypeShouldReturnPublicationType() {

        // Arrange
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
        when(publicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);


        // SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

        // Act
        PublicationType pubTypeResult = repo.save(publicationTypeDouble);

        // Assert
        assertEquals(publicationTypeDouble, pubTypeResult);
    }

    @Test
    void findAllShouldReturnAllSavedPublicationTypes() {

        // Arrange
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        PublicationType publicationType2Double = mock(PublicationType.class);

        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);

        when(publicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);
        when(publicationType2Double.identity()).thenReturn(publicationTypeId2Double);

        // SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

        // Act
        repo.save(publicationTypeDouble);
        repo.save(publicationType2Double);

        List<PublicationType> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(publicationTypeDouble));
        assertTrue(result.contains(publicationType2Double));
    }

    @Test
    void findAllOnEmptyRepoShouldReturnEmptyIterable() {

        //SUT + Arrange
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

        //Act
        List<PublicationType> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        //Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void ofIdentityShouldReturnPublicationTypeWhenIdPresent() {

        // Arrange
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        when(publicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();
        repo.save(publicationTypeDouble);

        // Act
        Optional<PublicationType> result = repo.ofIdentity(publicationTypeIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(publicationTypeDouble, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyOptionalWhenIdNotPresent() {
        // SUT + Arrange
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

        PublicationTypeId notSavedIdDouble = mock(PublicationTypeId.class);

        // Act
        Optional<PublicationType> result = repo.ofIdentity(notSavedIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenIdPresent() {

        // Arrange
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        when(publicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();
        repo.save(publicationTypeDouble);

        // Act
        boolean contains = repo.containsOfIdentity(publicationTypeIdDouble);

        //Assert
        assertTrue(contains);

    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenIdNotPresent() {

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

        PublicationTypeId notSavedIdDouble = mock(PublicationTypeId.class);

        // Act
        boolean contains = repo.containsOfIdentity(notSavedIdDouble);

        //Assert
        assertFalse(contains);
    }


    @Test
    void findAllKeysShouldReturnListOfIds() {

        //Arrange
        PublicationType publicationType1Double = mock(PublicationType.class);
        PublicationType publicationType2Double = mock(PublicationType.class);
        PublicationType publicationType3Double = mock(PublicationType.class);

        PublicationTypeId publicationTypeId1Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId3Double = mock(PublicationTypeId.class);

        when(publicationType1Double.identity()).thenReturn(publicationTypeId1Double);
        when(publicationType2Double.identity()).thenReturn(publicationTypeId2Double);
        when(publicationType3Double.identity()).thenReturn(publicationTypeId3Double);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

        repo.save(publicationType1Double);
        repo.save(publicationType2Double);
        repo.save(publicationType3Double);

        //Act
        List<PublicationTypeId> result = repo.findAllKeys();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(publicationTypeId1Double));
        assertTrue(result.contains(publicationTypeId2Double));
        assertTrue(result.contains(publicationTypeId3Double));

    }

    @Test
    void findAllKeysShouldReturnDefensiveCopy() {

        //Arrange
        PublicationType publicationType1Double = mock(PublicationType.class);
        PublicationType publicationType2Double = mock(PublicationType.class);
        PublicationType publicationType3Double = mock(PublicationType.class);

        PublicationTypeId publicationTypeId1Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId3Double = mock(PublicationTypeId.class);

        when(publicationType1Double.identity()).thenReturn(publicationTypeId1Double);
        when(publicationType2Double.identity()).thenReturn(publicationTypeId2Double);
        when(publicationType3Double.identity()).thenReturn(publicationTypeId3Double);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo();

        repo.save(publicationType1Double);
        repo.save(publicationType2Double);
        repo.save(publicationType3Double);

        List<PublicationTypeId> result = repo.findAllKeys();
        result.clear();
        List<PublicationTypeId> resultAfterClear = repo.findAllKeys();

        // Assert
        assertEquals(3, resultAfterClear.size());
        assertTrue(resultAfterClear.contains(publicationTypeId1Double));
        assertTrue(resultAfterClear.contains(publicationTypeId2Double));
        assertTrue(resultAfterClear.contains(publicationTypeId3Double));
    }

}
