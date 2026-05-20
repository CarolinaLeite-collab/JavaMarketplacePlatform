package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.assembler.UserAssembler;
import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import MITELOVERS.persistence.springdata.IUserSpringDataRepo;
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
class JpaUserRepoTest {

    // SUT
    @InjectMocks
    private JpaUserRepo jpaUserRepo;

    @Mock
    private IUserSpringDataRepo _springRepoDouble;

    @Mock
    private UserAssembler _assemblerDouble;

    @Mock
    private User _userDouble;

    @Mock
    private UserDataModel _dataModelDouble;


    @Test
    void testSaveShouldReturnDomainUser() {
        // Arrange
        when(_assemblerDouble.toDataModel(_userDouble)).thenReturn(_dataModelDouble);
        when(_springRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_userDouble);

        // Act
        User result = jpaUserRepo.save(_userDouble);

        // Assert
        assertEquals(_userDouble, result);
    }

    @Test
    void testFindAllShouldReturnAllSavedUsers() {
        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_userDouble);


        // Act
        Iterable<User> result = jpaUserRepo.findAll();
        List<User> resultList = new ArrayList<>();
        for (User user : result) {
            resultList.add(user);
        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals(_userDouble, resultList.get(0));
    }

    @Test
    void testFindAllKeysShouldReturnListOfIds() {
        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_dataModelDouble.getId()).thenReturn("test@email.com");

        // Act
        List<UserId> result = jpaUserRepo.findAllKeys();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void testOfIdentityShouldReturnUserOfACertainId() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        when(_springRepoDouble.findById(userIdDouble.toString())).thenReturn(Optional.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_userDouble);

        // Act
        Optional<User> result = jpaUserRepo.ofIdentity(userIdDouble);

        // Assert
        assertEquals(_userDouble, result.get());
    }

    @Test
    void testOfIdentityShouldThrowWhenNotFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        when(_springRepoDouble.findById(userIdDouble.toString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            jpaUserRepo.ofIdentity(userIdDouble);
        });
    }

    @Test
    void testContainsOfIdentityShouldReturnTrueWhenUserExists() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("test@email.com");
        when(_springRepoDouble.existsById(userIdDouble.toString())).thenReturn(true);

        // Act
        boolean result = jpaUserRepo.containsOfIdentity(userIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityShouldReturnFalseWhenUserDoesNotExist() {
        // Arrange
        UserId otherUserIdDouble = mock(UserId.class);
        when(otherUserIdDouble.toString()).thenReturn("test@email.com");

        // Act
        boolean result = jpaUserRepo.containsOfIdentity(otherUserIdDouble);

        // Assert
        assertFalse(result);
    }

}