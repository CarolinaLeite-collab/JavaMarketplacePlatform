package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
import MITELOVERS.persistence.jpa.assembler.AppraisalEntityAssembler;
import MITELOVERS.persistence.jpa.datamodel.AppraisalEntityDataModel;
import MITELOVERS.persistence.springdata.IAppraisalEntitySpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAppraisalEntityRepoTest {

    @Mock
    private IAppraisalEntitySpringDataRepo _springRepoDouble;

    @Mock
    private AppraisalEntityAssembler _assemblerDouble;

    @Mock
    private AppraisalEntity _appraisalEntityDouble;

    @Mock
    private AppraisalEntityDataModel _dataModelDouble;

    @InjectMocks
    private JpaAppraisalEntityRepo _jpaAppraisalEntityRepo;

    @Test
    void testSaveShouldReturnDomainEntity() {

        // Arrange
        when(_assemblerDouble.toDataModel(_appraisalEntityDouble)).thenReturn(_dataModelDouble);
        when(_springRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_appraisalEntityDouble);

        // Act
        AppraisalEntity result = _jpaAppraisalEntityRepo.save(_appraisalEntityDouble);

        // Assert
        assertEquals(_appraisalEntityDouble, result);
    }

    @Test
    void testFindAllKeysShouldReturnListOfIds() {

        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_dataModelDouble.getId()).thenReturn("id");

        // Act
        Iterable<AppraisalEntityId> result = _jpaAppraisalEntityRepo.findAllKeys();
        List<AppraisalEntityId> resultList = new ArrayList<>();

        for (AppraisalEntityId id : result) {

            resultList.add(id);

        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals("id", resultList.get(0).toString());
    }

    @Test
    void testFindAllShouldReturnAllSavedAppraisalEntities() {

        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_appraisalEntityDouble);

        // Act
        Iterable<AppraisalEntity> result = _jpaAppraisalEntityRepo.findAll();
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

        // Act
        Optional<AppraisalEntity> result = _jpaAppraisalEntityRepo.ofIdentity(appraisalEntityIdDouble);

        // Assert
        assertEquals(_appraisalEntityDouble, result.get());
    }

    @Test
    void testOfIdentityShouldThrowWhenNotFound() {

        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        when(_springRepoDouble.findById(appraisalEntityIdDouble.toString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {_jpaAppraisalEntityRepo.ofIdentity(appraisalEntityIdDouble);});
    }

    @Test
    void testContainsOfIdentityShouldReturnTrueWhenSavedAppraisalEntityExists() {

        // Arrange
        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        when(appraisalEntityIdDouble.toString()).thenReturn("id");
        when(_springRepoDouble.existsById(appraisalEntityIdDouble.toString())).thenReturn(true);

        // Act
        boolean result = _jpaAppraisalEntityRepo.containsOfIdentity(appraisalEntityIdDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void testContainsOfIdentityShouldReturnFalseWhenSavedAppraisalEntityDoesNotExists() {

        // Arrange
        AppraisalEntityId appraisalEntityIdDouble = mock(AppraisalEntityId.class);
        AppraisalEntityId otherAppraisalEntityIdDouble = mock(AppraisalEntityId.class);

        // This stubbing is commented because the SUT shares its state across tests now
//        when(appraisalEntityIdDouble.toString()).thenReturn("id");
//        when(_springRepoDouble.existsById(appraisalEntityIdDouble.toString())).thenReturn(true);

        // Act
        boolean result = _jpaAppraisalEntityRepo.containsOfIdentity(otherAppraisalEntityIdDouble);

        // Assert
        assertFalse(result);

    }
}