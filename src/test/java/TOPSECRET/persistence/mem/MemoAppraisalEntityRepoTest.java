package TOPSECRET.persistence.mem;

import TOPSECRET.domain.appraisalEntity.AppraisalEntity;
import TOPSECRET.domain.appraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.valueobject.AppraisalEntityId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        repo.save(_AppraisalEntityDouble);

        //Act
        Iterable<AppraisalEntity> result = repo.findAll();

        List<AppraisalEntity> list = new ArrayList<>();
        result.forEach(list::add);

        //Assert
        assertEquals(1, list.size());

    }

    @Test
    void ofIdentityShouldReturnAppraisalEntity() {

        //Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        //Act
        repo.save(_AppraisalEntityDouble);

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

        //Act
        repo.save(appraisalEntityDouble2);

        Optional<AppraisalEntity> result = repo.ofIdentity(_AppraisalEntityIdDouble);

        //Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void containsOfIdentityShouldReturnTrueIfAppraisalEntityIsPresent() {

        // Arrange
        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        //Act
        repo.save(_AppraisalEntityDouble);

        boolean result = repo.containsOfIdentity(_AppraisalEntityIdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void containsOfIdentityShouldReturnTrueIfAppraisalEntityIsNotPresent() {

        // Arrange
        AppraisalEntity appraisalEntityDouble2 = mock(AppraisalEntity.class);
        AppraisalEntityId appraisalEntityIdDouble2 = mock(AppraisalEntityId.class);

        when(_AppraisalEntityDouble.identity()).thenReturn(_AppraisalEntityIdDouble);
        when(appraisalEntityDouble2.identity()).thenReturn(appraisalEntityIdDouble2);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        //Act
        repo.save(appraisalEntityDouble2);

        boolean result = repo.containsOfIdentity(_AppraisalEntityIdDouble);

        //Assert
        assertFalse(result);

    }

    @Test
    void shouldAddAppraisalEntity() {

        //Arrange
        when(_AppraisalEntityDouble.getName()).thenReturn(_nameDouble);
        when(_AppraisalEntityDouble.getPublicationTypes()).thenReturn(_publicationTypeIds);
        when(_AppraisalEntityDouble.getGenres()).thenReturn(_genreIds);
        when(_AppraisalEntityFactoryDouble
                .createAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds)).thenReturn(_AppraisalEntityDouble);

        //SUT
        MemoAppraisalEntityRepo repo = new MemoAppraisalEntityRepo(_AppraisalEntityFactoryDouble);

        //Act
        AppraisalEntity appraisalEntity = repo.addAppraisalEntity(_nameDouble, _publicationTypeIds, _genreIds);

        //Assert
        assertEquals(_nameDouble, appraisalEntity.getName());
        assertEquals(_publicationTypeIds, appraisalEntity.getPublicationTypes());
        assertEquals(_genreIds, appraisalEntity.getGenres());

    }

}
