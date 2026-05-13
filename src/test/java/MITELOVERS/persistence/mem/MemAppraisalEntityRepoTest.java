package MITELOVERS.persistence.mem;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemAppraisalEntityRepoTest {

    private AppraisalEntity _AppraisalEntityDouble;
    private AppraisalEntityId _AppraisalEntityIdDouble;

    @BeforeEach
    void setUp() {

        _AppraisalEntityDouble = mock(AppraisalEntity.class);
        _AppraisalEntityIdDouble = mock(AppraisalEntityId.class);

    }


    @Test
    void shouldConstructRepo() {

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

    }

    @Test
    void shouldSaveAndReturnAppraisalEntity() {

        //Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        //Act
        AppraisalEntity result = repo.save(_AppraisalEntityDouble);

        //Assert
        assertEquals(_AppraisalEntityDouble, result);
        assertTrue(repo.containsOfIdentity(_AppraisalEntityIdDouble));

    }

    @Test
    void findAllShouldReturnAllStoredAppraisalEntities() {

        //Arrange
        when(_AppraisalEntityDouble.identity())
                .thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();
        repo.save(_AppraisalEntityDouble);

        //Act
        List<AppraisalEntity> list = new ArrayList<>();
        repo.findAll().forEach(list::add);

        //Assert
        assertEquals(1, list.size());
        assertTrue(list.contains(_AppraisalEntityDouble));

    }

    @Test
    void ofIdentityShouldReturnAppraisalEntity() {

        //Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();
        repo.save(_AppraisalEntityDouble);

        //Act
        AppraisalEntity result = repo.ofIdentity(_AppraisalEntityIdDouble)
                .orElseThrow(() -> new AssertionError("AppraisalEntity not found"));

        //Assert
        assertEquals(_AppraisalEntityDouble, result);

    }

    @Test
    void ofIdentityShouldReturnEmptyIfNotPresent() {

        //Arrange
        AppraisalEntity appraisalEntityDouble2 = mock(AppraisalEntity.class);
        AppraisalEntityId appraisalEntityIdDouble2 = mock(AppraisalEntityId.class);

        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);
        when(appraisalEntityDouble2.identity()).thenReturn(appraisalEntityIdDouble2);

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();
        repo.save(appraisalEntityDouble2);

        //Act
        Optional<AppraisalEntity> result = repo.ofIdentity(_AppraisalEntityIdDouble);

        //Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void containsOfIdentityShouldReturnTrueIfAppraisalEntityIsPresent() {

        //Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();
        repo.save(_AppraisalEntityDouble);

        //Act
        boolean result = repo.containsOfIdentity(_AppraisalEntityIdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void containsOfIdentityShouldReturnTrueIfAppraisalEntityIsNotPresent() {

        //Arrange
        AppraisalEntity appraisalEntityDouble2 = mock(AppraisalEntity.class);
        AppraisalEntityId appraisalEntityIdDouble2 = mock(AppraisalEntityId.class);

        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);
        when(appraisalEntityDouble2.identity()).thenReturn(appraisalEntityIdDouble2);

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();
        repo.save(appraisalEntityDouble2);

        //Act
        boolean result = repo.containsOfIdentity(_AppraisalEntityIdDouble);

        //Assert
        assertFalse(result);

    }

    @Test
    void saveShouldOverwriteExistingEntityWithSameIdentity() {

        // Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        AppraisalEntity other = mock(AppraisalEntity.class);
        when(other.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        // Act
        repo.save(_AppraisalEntityDouble);
        repo.save(other);

        // Assert
        assertEquals(other, repo.ofIdentity(_AppraisalEntityIdDouble).orElseThrow());

    }

    @Test
    void findAllShouldReturnEmptyWhenRepoIsEmpty() {

        // Arrange & SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        // Act
        List<AppraisalEntity> list = new ArrayList<>();
        repo.findAll().forEach(list::add);

        // Assert
        assertTrue(list.isEmpty());

    }

    @Test
    void ofIdentityShouldReturnEmptyForNull() {

        // Arrange & SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        // Act
        Optional<AppraisalEntity> result = repo.ofIdentity(null);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void containsOfIdentityShouldReturnFalseForNull() {

        // Arrange & SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        // Act
        boolean result = repo.containsOfIdentity(null);

        // Assert
        assertFalse(result);

    }

    @Test
    void findAllKeysShouldReturnEmptyWhenRepoIsEmpty() {

        // Arrange & SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        // Act
        List<AppraisalEntityId> keys = repo.findAllKeys();

        // Assert
        assertTrue(keys.isEmpty());

    }

    @Test
    void findAllKeysShouldReturnAllKeys() {

        // Arrange
        AppraisalEntity entity2 = mock(AppraisalEntity.class);
        AppraisalEntityId id2 = mock(AppraisalEntityId.class);

        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);
        when(entity2.identity()).thenReturn(id2);

        // SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        repo.save(_AppraisalEntityDouble);
        repo.save(entity2);

        // Act
        List<AppraisalEntityId> keys = repo.findAllKeys();

        // Assert
        assertEquals(2, keys.size());
        assertTrue(keys.contains(_AppraisalEntityIdDouble));
        assertTrue(keys.contains(id2));

    }

    @Test
    void findAllKeysShouldReturnCopyNotAffectingRepo() {

        // Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        // SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();
        repo.save(_AppraisalEntityDouble);

        // Act
        List<AppraisalEntityId> keys = repo.findAllKeys();
        keys.clear();

        // Assert
        assertTrue(repo.containsOfIdentity(_AppraisalEntityIdDouble));

    }

    @Test
    void saveShouldThrowWhenEntityIsNull() {

        // Arrange & SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        // Act
        Executable action = () -> repo.save(null);

        // Assert
        assertThrows(NullPointerException.class, action);
    }

    @Test
    void findAllKeysOrderShouldNotBeGuaranteed() {

        // Arrange
        AppraisalEntity entity2 = mock(AppraisalEntity.class);
        AppraisalEntityId id2 = mock(AppraisalEntityId.class);

        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);
        when(entity2.identity()).thenReturn(id2);

        // SUT
        MemAppraisalEntityRepo repo = new MemAppraisalEntityRepo();

        repo.save(_AppraisalEntityDouble);
        repo.save(entity2);

        // Act
        List<AppraisalEntityId> keys = repo.findAllKeys();

        // Assert
        assertEquals(2, keys.size());
        assertTrue(keys.contains(_AppraisalEntityIdDouble));
        assertTrue(keys.contains(id2));

    }

}
