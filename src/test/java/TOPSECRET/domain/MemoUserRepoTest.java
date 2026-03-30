package TOPSECRET.domain;


import TOPSECRET.domain.valueobject.UserID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoUserRepoTest {

    private User _user1Double;
    private User _user2Double;
    private UserID _userId1Double;
    private UserID _userId2Double;

    @BeforeEach
    void setUp() {
        _user1Double = mock(User.class);
        _user2Double = mock(User.class);
        _userId1Double = mock(UserID.class);
        _userId2Double = mock(UserID.class);

        when(_user1Double.identity()).thenReturn(_userId1Double);
        when(_user2Double.identity()).thenReturn(_userId2Double);
    }

    @Test
    void saveShouldReturnTrueForNewUser() {
        MemoUserRepo repo = new MemoUserRepo();

        boolean result = repo.save(_user1Double);

        assertTrue(result);
    }

    @Test
    void saveShouldAddUserToRepo() {
        MemoUserRepo repo = new MemoUserRepo();

        repo.save(_user1Double);

        assertEquals(1, repo.getAll().size());
    }

    @Test
    void saveShouldReturnFalseForDuplicateUser() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        // same identity → duplicate
        when(_user1Double.identity()).thenReturn(_userId1Double);
        boolean result = repo.save(_user1Double);

        assertFalse(result);
    }

    @Test
    void saveShouldAllowMultipleDistinctUsers() {
        MemoUserRepo repo = new MemoUserRepo();

        repo.save(_user1Double);
        repo.save(_user2Double);

        assertEquals(2, repo.getAll().size());
    }

    @Test
    void containsOfIdentityShouldReturnTrueIfUserExists() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        boolean result = repo.containsOfIdentity(_userId1Double);

        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseIfUserDoesNotExist() {
        MemoUserRepo repo = new MemoUserRepo();

        boolean result = repo.containsOfIdentity(_userId1Double);

        assertFalse(result);
    }

    @Test
    void getAllShouldReturnImmutableList() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        List<User> result = repo.getAll();

        assertThrows(UnsupportedOperationException.class, () -> result.add(_user2Double));
    }

    @Test
    void saveShouldNotAddDuplicateUser() {
        MemoUserRepo repo = new MemoUserRepo();
        repo.save(_user1Double);

        repo.save(_user1Double); // duplicado — save retorna false mas não lança exceção

        assertEquals(1, repo.getAll().size());
    }
}