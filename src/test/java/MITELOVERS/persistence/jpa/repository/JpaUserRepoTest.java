package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.assembler.UserAssembler;
import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import MITELOVERS.persistence.springdata.IUserSpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JpaUserRepoTest {
    private IUserSpringDataRepo _springRepoDouble;
    private UserAssembler _assemblerDouble;
    private User _userDouble;
    private UserDataModel _dataModelDouble;

    @BeforeEach
    void setUp() {
        _springRepoDouble = mock(IUserSpringDataRepo.class);
        _assemblerDouble = mock(UserAssembler.class);
        _userDouble = mock(User.class);
        _dataModelDouble = mock(UserDataModel.class);
    }

    @Test
    void testConstructor() {
        new JpaUserRepo(_springRepoDouble, _assemblerDouble);
    }

    @Test
    void testSaveShouldReturnDomainUser() {
        // Arrange
        when(_assemblerDouble.toDataModel(_userDouble)).thenReturn(_dataModelDouble);
        when(_springRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_userDouble);

        // SUT
        JpaUserRepo jpaUserRepo = new JpaUserRepo(_springRepoDouble, _assemblerDouble);

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

        // SUT
        JpaUserRepo jpaUserRepo = new JpaUserRepo(_springRepoDouble, _assemblerDouble);

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

        // SUT
        JpaUserRepo jpaUserRepo = new JpaUserRepo(_springRepoDouble, _assemblerDouble);

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

        // SUT
        JpaUserRepo jpaUserRepo = new JpaUserRepo(_springRepoDouble, _assemblerDouble);

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

        // SUT
        JpaUserRepo jpaUserRepo = new JpaUserRepo(_springRepoDouble, _assemblerDouble);

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

        // SUT
        JpaUserRepo jpaUserRepo = new JpaUserRepo(_springRepoDouble, _assemblerDouble);

        // Act
        boolean result = jpaUserRepo.containsOfIdentity(userIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityShouldReturnFalseWhenUserDoesNotExist() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        UserId otherUserIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("test@email.com");
        when(_springRepoDouble.existsById(userIdDouble.toString())).thenReturn(true);

        // SUT
        JpaUserRepo jpaUserRepo = new JpaUserRepo(_springRepoDouble, _assemblerDouble);

        // Act
        boolean result = jpaUserRepo.containsOfIdentity(otherUserIdDouble);

        // Assert
        assertFalse(result);
    }

}