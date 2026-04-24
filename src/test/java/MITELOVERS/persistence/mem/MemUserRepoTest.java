package MITELOVERS.persistence.mem;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemUserRepoTest {

    private User _user1Double;
    private User _user2Double;
    private UserId _userId1Double;
    private UserId _userId2Double;


    @BeforeEach
    void setUp() {
        _user1Double = mock(User.class);
        _user2Double = mock(User.class);
        _userId1Double = mock(UserId.class);
        _userId2Double = mock(UserId.class);

        when(_user1Double.identity()).thenReturn(_userId1Double);
        when(_user2Double.identity()).thenReturn(_userId2Double);
    }


    @Test
    void saveShouldReturnSavedUser() {
        MemUserRepo repo = new MemUserRepo();

        User result = repo.save(_user1Double);

        assertSame(_user1Double, result);
    }


    @Test
    void saveShouldAddUserToRepo() {
        MemUserRepo repo = new MemUserRepo();
        repo.save(_user1Double);

        int count = 0;
        for (User ignored : repo.findAll()) count++;
        assertEquals(1, count);
    }

    @Test
    void saveShouldAllowMultipleDistinctUsers() {
        MemUserRepo repo = new MemUserRepo();

        repo.save(_user1Double);
        repo.save(_user2Double);

        int count = 0;
        for (User ignored : repo.findAll()) count++;
        assertEquals(2, count);
    }

    @Test
    void saveShouldReplaceOnDuplicateIdentity() {
        MemUserRepo repo = new MemUserRepo();

        repo.save(_user1Double);
        repo.save(_user1Double);

        int count = 0;
        for (User ignored : repo.findAll()) count++;
        assertEquals(1, count);
    }


    @Test
    void containsOfIdentityShouldReturnTrueIfUserExists() {
        MemUserRepo repo = new MemUserRepo();
        repo.save(_user1Double);

        assertTrue(repo.containsOfIdentity(_userId1Double));
    }


    @Test
    void containsOfIdentityShouldReturnFalseIfUserDoesNotExist() {
        MemUserRepo repo = new MemUserRepo();

        assertFalse(repo.containsOfIdentity(_userId1Double));
    }


    @Test
    void findAllShouldReturnUnmodifiableCollection() {
        MemUserRepo repo = new MemUserRepo();
        repo.save(_user1Double);

        Iterable<User> result = repo.findAll();

        assertThrows(UnsupportedOperationException.class,
                () -> ((java.util.Collection<User>) result).add(_user2Double));
    }


    @Test
    void ofIdentityShouldReturnUserIfExists() {
        MemUserRepo repo = new MemUserRepo();
        repo.save(_user1Double);

        Optional<User> result = repo.ofIdentity(_userId1Double);

        assertTrue(result.isPresent());
        assertSame(_user1Double, result.get());
    }


    @Test
    void ofIdentityShouldReturnEmptyIfUserDoesNotExist() {
        MemUserRepo repo = new MemUserRepo();

        Optional<User> result = repo.ofIdentity(_userId1Double);

        assertTrue(result.isEmpty());
    }


    @Test
    void findAllKeysShouldReturnEmptyWhenNoUsers() {
        MemUserRepo repo = new MemUserRepo();

        assertTrue(repo.findAllKeys().isEmpty());
    }


    @Test
    void findAllKeysShouldReturnAllKeys() {
        MemUserRepo repo = new MemUserRepo();
        repo.save(_user1Double);
        repo.save(_user2Double);

        List<UserId> keys = repo.findAllKeys();

        assertEquals(2, keys.size());
        assertTrue(keys.contains(_userId1Double));
        assertTrue(keys.contains(_userId2Double));
    }


    @Test
    void findAllKeysShouldReturnMutableList() {
        MemUserRepo repo = new MemUserRepo();
        repo.save(_user1Double);

        List<UserId> keys = repo.findAllKeys();

        assertDoesNotThrow(() -> keys.add(_userId2Double));
    }

}