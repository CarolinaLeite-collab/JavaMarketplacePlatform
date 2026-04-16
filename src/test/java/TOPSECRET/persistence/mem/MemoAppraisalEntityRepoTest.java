package TOPSECRET.persistence.mem;

import TOPSECRET.domain.appraisalentity.AppraisalEntity;
import TOPSECRET.domain.appraisalentity.AppraisalEntityFactory;
import TOPSECRET.domain.valueobject.AppraisalEntityId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoAppraisalEntityRepoTest {

    private AppraisalEntityFactory _AppraisalEntityFactoryDouble;
    private AppraisalEntity _AppraisalEntityDouble;
    private AppraisalEntityId _AppraisalEntityIdDouble;
    private Name _nameDouble;
    private List<PublicationTypeId> _publicationTypeIds;
    private List<GenreId> _genreIds;


    @BeforeEach
    void setUp() {

        _AppraisalEntityFactoryDouble = mock(AppraisalEntityFactory.class);

        _AppraisalEntityDouble = mock(AppraisalEntity.class);
        _AppraisalEntityIdDouble = mock(AppraisalEntityId.class);
        _nameDouble = mock(Name.class);

        _publicationTypeIds = List.of(mock(PublicationTypeId.class));
        _genreIds = List.of(mock(GenreId.class));

    }


    @Test
    void shouldConstructRepo() {

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

    }

    @Test
    void shouldSaveAndReturnAppraisalEntity() {

        //Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);
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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);
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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);
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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);
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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);
        repo.save(appraisalEntityDouble2);

        //Act
        boolean result = repo.containsOfIdentity(_AppraisalEntityIdDouble);

        //Assert
        assertFalse(result);

    }

    @Test
    void shouldAddAppraisalEntity() {

        //Arrange
        when(_AppraisalEntityDouble.getName()).thenReturn(_nameDouble);
        when(_AppraisalEntityDouble.getPublicationTypeIds()).thenReturn(_publicationTypeIds);
        when(_AppraisalEntityDouble.getGenreIds()).thenReturn(_genreIds);

        when(_AppraisalEntityFactoryDouble
                .createAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds)).thenReturn(_AppraisalEntityDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        //Act
        AppraisalEntity appraisalEntity = repo.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds);

        //Assert
        assertEquals(_AppraisalEntityDouble, appraisalEntity);

    }

    @Test
    void shouldThrowWhenAddingDuplicateAppraisalEntity() {

        // Arrange
        when(_AppraisalEntityFactoryDouble.createAppraisalEntity(
                _nameDouble, _publicationTypeIds, _genreIds)).thenReturn(_AppraisalEntityDouble);

        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        repo.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds);

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                repo.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds));

    }

    @Test
    void saveShouldOverwriteExistingEntityWithSameIdentity() {

        // Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        AppraisalEntity other = mock(AppraisalEntity.class);
        when(other.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        // Act
        repo.save(_AppraisalEntityDouble);
        repo.save(other);

        // Assert
        assertEquals(other, repo.ofIdentity(_AppraisalEntityIdDouble).orElseThrow());

    }

    @Test
    void findAllShouldReturnEmptyWhenRepoIsEmpty() {

        // Arrange & SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        // Act
        List<AppraisalEntity> list = new ArrayList<>();
        repo.findAll().forEach(list::add);

        // Assert
        assertTrue(list.isEmpty());

    }

    @Test
    void ofIdentityShouldReturnEmptyForNull() {

        // Arrange & SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        // Act
        Optional<AppraisalEntity> result = repo.ofIdentity(null);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void containsOfIdentityShouldReturnFalseForNull() {

        // Arrange & SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        // Act
        boolean result = repo.containsOfIdentity(null);

        // Assert
        assertFalse(result);

    }

    @Test
    void findAllKeysShouldReturnEmptyWhenRepoIsEmpty() {

        // Arrange & SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);
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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

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
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

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
