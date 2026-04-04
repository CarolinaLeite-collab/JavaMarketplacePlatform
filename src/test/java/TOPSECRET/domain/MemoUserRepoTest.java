package TOPSECRET.domain;


import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// MemoUserRepoTest.java
class MemoUserRepoTest {

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
    void saveShouldReturnUserForNewUser() {
        MemoUserRepo repo = new MemoUserRepo();

        User result = repo.save(_user1Double);

        assertSame(_user1Double, result);
    }

    @Test
    void saveShouldAddUserToRepo() {
        MemoUserRepo repo = new MemoUserRepo();

        repo.save(_user1Double);

        assertEquals(1, ((List<User>) repo.findAll()).size());
    }

    @Test
    void saveShouldThrowForDuplicateUser() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        assertThrows(IllegalStateException.class, () -> repo.save(_user1Double));
    }

    @Test
    void saveShouldNotAddDuplicateUser() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        assertThrows(IllegalStateException.class, () -> repo.save(_user1Double));

        assertEquals(1, ((List<User>) repo.findAll()).size());
    }

    @Test
    void saveShouldAllowMultipleDistinctUsers() {
        MemoUserRepo repo = new MemoUserRepo();

        repo.save(_user1Double);
        repo.save(_user2Double);

        assertEquals(2, ((List<User>) repo.findAll()).size());
    }

    @Test
    void containsOfIdentityShouldReturnTrueIfUserExists() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        assertTrue(repo.containsOfIdentity(_userId1Double));
    }

    @Test
    void containsOfIdentityShouldReturnFalseIfUserDoesNotExist() {
        MemoUserRepo repo = new MemoUserRepo();

        assertFalse(repo.containsOfIdentity(_userId1Double));
    }

    @Test
    void findAllShouldReturnImmutableList() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        List<User> result = (List<User>) repo.findAll();

        assertThrows(UnsupportedOperationException.class, () -> result.add(_user2Double));
    }

    @Test
    void ofIdentityShouldReturnUserIfExists() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        Optional<User> result = repo.ofIdentity(_userId1Double);

        assertTrue(result.isPresent());
        assertSame(_user1Double, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyIfUserDoesNotExist() {
        MemoUserRepo repo = new MemoUserRepo();

        Optional<User> result = repo.ofIdentity(_userId1Double);

        assertTrue(result.isEmpty());
    }
}