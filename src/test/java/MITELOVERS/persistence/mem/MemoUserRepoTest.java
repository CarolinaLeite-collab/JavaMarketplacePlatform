package MITELOVERS.persistence.mem;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoUserRepoTest {

    private User _user1Double;
    private User _user2Double;
    private UserId _userId1Double;
    private UserId _userId2Double;
    private UserFactory _userFactoryDouble;
    private Name _nameDouble;
    private Address _addressDouble;
    private Email _emailDouble;
    private Phone _phoneDouble;

    @BeforeEach
    void setUp() {
        _user1Double = mock(User.class);
        _user2Double = mock(User.class);
        _userId1Double = mock(UserId.class);
        _userId2Double = mock(UserId.class);
        _userFactoryDouble = mock(UserFactory.class);
        _nameDouble = mock(Name.class);
        _addressDouble = mock(Address.class);
        _emailDouble = mock(Email.class);
        _phoneDouble = mock(Phone.class);

        when(_user1Double.identity()).thenReturn(_userId1Double);
        when(_user2Double.identity()).thenReturn(_userId2Double);
    }

    private <T> long count(Iterable<T> iterable) {
        long count = 0;
        for (T ignored : iterable) count++;
        return count;
    }

    @Test
    void saveShouldReturnUserForNewUser() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);

        User result = repo.save(_user1Double);

        assertSame(_user1Double, result);
    }

    @Test
    void saveShouldAddUserToRepo() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);

        repo.save(_user1Double);

        assertEquals(1, count(repo.findAll()));
    }

    @Test
    void saveShouldAllowMultipleDistinctUsers() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);

        repo.save(_user1Double);
        repo.save(_user2Double);

        assertEquals(2, count(repo.findAll()));
    }

    @Test
    void addUserShouldReturnUserForNewUser() {
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_user1Double);
        when(_user1Double.identity()).thenReturn(_userId1Double);

        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);

        User result = repo.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        assertSame(_user1Double, result);
    }

    @Test
    void addUserShouldThrowForDuplicateUser() {
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_user1Double);
        when(_user1Double.identity()).thenReturn(_userId1Double);

        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        assertThrows(IllegalStateException.class,
                () -> repo.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble));
    }

    @Test
    void addUserShouldNotAddDuplicateUser() {
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_user1Double);
        when(_user1Double.identity()).thenReturn(_userId1Double);

        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        assertThrows(IllegalStateException.class,
                () -> repo.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        assertEquals(1, count(repo.findAll()));
    }

    @Test
    void addUserShouldThrowCorrectMessage() {
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_user1Double);
        when(_user1Double.identity()).thenReturn(_userId1Double);

        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> repo.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        assertEquals("User already exists", ex.getMessage());
    }

    @Test
    void containsOfIdentityShouldReturnTrueIfUserExists() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.save(_user1Double);

        assertTrue(repo.containsOfIdentity(_userId1Double));
    }

    @Test
    void containsOfIdentityShouldReturnFalseIfUserDoesNotExist() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);

        assertFalse(repo.containsOfIdentity(_userId1Double));
    }

    @Test
    void findAllShouldReturnUnmodifiableCollection() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.save(_user1Double);

        Iterable<User> result = repo.findAll();

        assertThrows(UnsupportedOperationException.class,
                () -> ((java.util.Collection<User>) result).add(_user2Double));
    }

    @Test
    void ofIdentityShouldReturnUserIfExists() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.save(_user1Double);

        Optional<User> result = repo.ofIdentity(_userId1Double);

        assertTrue(result.isPresent());
        assertSame(_user1Double, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyIfUserDoesNotExist() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);

        Optional<User> result = repo.ofIdentity(_userId1Double);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnEmptyWhenNoUser() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);

        assertTrue(repo.findAllKeys().isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeys() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.save(_user1Double);
        repo.save(_user2Double);

        List<UserId> keys = repo.findAllKeys();

        assertEquals(2, keys.size());
        assertTrue(keys.contains(_userId1Double));
        assertTrue(keys.contains(_userId2Double));
    }

    @Test
    void findAllKeysShouldReturnMutableList() {
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble);
        repo.save(_user1Double);

        List<UserId> keys = repo.findAllKeys();

        assertDoesNotThrow(() -> keys.add(_userId2Double));
    }

}
