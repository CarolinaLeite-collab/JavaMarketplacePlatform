package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
import MITELOVERS.persistence.jpa.assembler.AppraisalEntityAssembler;
import MITELOVERS.persistence.jpa.datamodel.AppraisalEntityDataModel;
import MITELOVERS.persistence.jpa.repository.JpaAppraisalEntityRepo;
import MITELOVERS.persistence.springdata.IAppraisalEntitySpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JpaAppraisalEntityRepoTest {

    private IAppraisalEntitySpringDataRepo _springRepoDouble;
    private AppraisalEntityAssembler _assemblerDouble;
    private AppraisalEntity _appraisalEntityDouble;
    private AppraisalEntityDataModel _dataModelDouble;

    @BeforeEach
    void setUp() {

        _springRepoDouble = mock(IAppraisalEntitySpringDataRepo.class);
        _assemblerDouble = mock(AppraisalEntityAssembler.class);
        _appraisalEntityDouble = mock(AppraisalEntity.class);
        _dataModelDouble = mock(AppraisalEntityDataModel.class);

    }

    @Test
    void testAConstructor() {

        new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

    }

    @Test
    void testSaveShouldReturnDomainEntity() {

        // Arrange
        when(_assemblerDouble.toDataModel(_appraisalEntityDouble)).thenReturn(_dataModelDouble);
        when(_springRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_appraisalEntityDouble);

        // SUT
        JpaAppraisalEntityRepo jpaAppraisalEntityRepo =  new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

        // Act
        AppraisalEntity result = jpaAppraisalEntityRepo.save(_appraisalEntityDouble);

        // Assert
        assertEquals(_appraisalEntityDouble, result);
    }

    @Test
    void testFindAllKeysShouldReturnListOfIds() {

        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_dataModelDouble.getId()).thenReturn("id");

        // SUT
        JpaAppraisalEntityRepo jpaAppraisalEntityRepo = new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

        // Act
        Iterable<AppraisalEntityId> result = jpaAppraisalEntityRepo.findAllKeys();
        List<AppraisalEntityId> resultList = new ArrayList<>();

        for (AppraisalEntityId id : result) {

            resultList.add(id);

        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals("entity:I-Id", resultList.get(0).toString());
    }

    @Test
    void testFindAllShouldReturnAllSavedAppraisalEntities() {

        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_appraisalEntityDouble);

        // SUT
        JpaAppraisalEntityRepo jpaAppraisalEntityRepo = new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

        // Act
        Iterable<AppraisalEntity> result = jpaAppraisalEntityRepo.findAll();
        List<AppraisalEntity> resultList = new ArrayList<>();

        for (AppraisalEntity appraisalEntity : result) {

            resultList.add(appraisalEntity);

        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals(_appraisalEntityDouble, resultList.get(0));

    }

    @Test
    void testOfIdentityShouldReturnAppraisalEntityOfACertainId() {

        // Arrange
        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        when(_springRepoDouble.findById(appraisalEntityIdDouble.toString())).thenReturn(Optional.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_appraisalEntityDouble);

        // SUT
        JpaAppraisalEntityRepo jpaAppraisalEntityRepo = new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

        // Act
        Optional<AppraisalEntity> result = jpaAppraisalEntityRepo.ofIdentity(appraisalEntityIdDouble);

        // Assert
        assertEquals(_appraisalEntityDouble, result.get());
    }

    @Test
    void testOfIdentityShouldThrowWhenNotFound() {

        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        when(_springRepoDouble.findById(appraisalEntityIdDouble.toString())).thenReturn(Optional.empty());

        JpaAppraisalEntityRepo jpaAppraisalEntityRepo = new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

        assertThrows(IllegalArgumentException.class, () -> {jpaAppraisalEntityRepo.ofIdentity(appraisalEntityIdDouble);});
    }

    @Test
    void testContainsOfIdentityShouldReturnTrueWhenSavedAppraisalEntityExists() {

        // Arrange
        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        when(appraisalEntityIdDouble.toString()).thenReturn("id");
        when(_springRepoDouble.existsById(appraisalEntityIdDouble.toString())).thenReturn(true);

        // SUT
        JpaAppraisalEntityRepo jpaAppraisalEntityRepo = new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

        // Act
        boolean result = jpaAppraisalEntityRepo.containsOfIdentity(appraisalEntityIdDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void testContainsOfIdentityShouldReturnFalseWhenSavedAppraisalEntityDoesNotExists() {

        // Arrange
        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        AppraisalEntityId otherAppraisalEntityIdDouble = mock(AppraisalEntityId.class);
        when(appraisalEntityIdDouble.toString()).thenReturn("id");
        when(_springRepoDouble.existsById(appraisalEntityIdDouble.toString())).thenReturn(true);

        // SUT
        JpaAppraisalEntityRepo jpaAppraisalEntityRepo = new JpaAppraisalEntityRepo(_springRepoDouble, _assemblerDouble);

        // Act
        boolean result = jpaAppraisalEntityRepo.containsOfIdentity(otherAppraisalEntityIdDouble);

        // Assert
        assertFalse(result);

    }
}