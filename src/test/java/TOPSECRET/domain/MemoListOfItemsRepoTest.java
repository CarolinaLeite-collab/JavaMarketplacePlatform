package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MemoListOfItemsRepo}.
 *
 * <p>The following Mockito doubles are used:
 * <ul>
 *   <li>{@link ListOfItemsFactory} — mocked collaborator (creation dependency)</li>
 *   <li>{@link User} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Genre} — mocked dummy (structural input, no behaviour required)</li>
 * </ul>
 */

class MemoListOfItemsRepoTest {

    private ListOfItemsFactory _factoryDouble;

    private UserId _userId1Double;
    private UserId _userId2Double;
    private Genre _actionDouble;
    private Genre _poetryDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(ListOfItemsFactory.class);

        _actionDouble = mock(Genre.class);
        _poetryDouble = mock(Genre.class);

        _userId1Double = mock(UserId.class);
        _userId2Double = mock(UserId.class);
    }

    @Test
    void shouldCreateEmptyListOfItems() {
        new MemoListOfItemsRepo();
    }

    @Test
    void addListOfItemsSuccessfully() {
        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");
        when(_createdDouble.isPrivate()).thenReturn(true);

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        ListOfItems list = repo.addListOfItems(_userId1Double, "My List", _actionDouble);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(_userId1Double, list.getUserId()),
                () -> assertEquals("My List", list.getName()),
                () -> assertEquals(_actionDouble, list.getGenre()),
                () -> assertTrue(list.isPrivate()),
                () -> assertEquals(1, repo.getListOfListOfItems().size())
        );
    }

    @Test
    void cannotAddDuplicateList() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _actionDouble);
        ListOfItems duplicate = repo.addListOfItems(_userId1Double, "My List", _actionDouble);

        // Assert
        assertNull(duplicate);
        assertEquals(1, repo.getListOfListOfItems().size());
    }

    @Test
    void getListReturnsCopy() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        repo.addListOfItems(_userId1Double, "My List", _actionDouble);

        // Act
        List<ListOfItems> lists = repo.getListOfListOfItems();

        // Assert
        assertAll(
                () -> assertEquals(1, lists.size()),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> lists.add(new ListOfItems(_userId1Double, "Other List", _poetryDouble)))
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnOnlyPublicListsOfThatGenre() {

        // Arrange
        ListOfItems _listPubDouble1 = mock(ListOfItems.class);
        when(_listPubDouble1.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble1.getName()).thenReturn("List A");
        when(_listPubDouble1.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble1.isPrivate()).thenReturn(false);


        ListOfItems _listPubDouble2 = mock(ListOfItems.class);
        when(_listPubDouble2.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble2.getName()).thenReturn("List B");
        when(_listPubDouble2.getUserId()).thenReturn(_userId2Double);
        when(_listPubDouble2.isPrivate()).thenReturn(true);


        ListOfItems _listPubDouble3 = mock(ListOfItems.class);
        when(_listPubDouble3.getGenre()).thenReturn(_poetryDouble);
        when(_listPubDouble3.getName()).thenReturn("List C");
        when(_listPubDouble3.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble3.isPrivate()).thenReturn(false);

        when(_factoryDouble.createListOfItems(_userId1Double, "List A", _actionDouble)).thenReturn(_listPubDouble1);
        when(_factoryDouble.createListOfItems(_userId2Double, "List B", _actionDouble)).thenReturn(_listPubDouble2);
        when(_factoryDouble.createListOfItems(_userId1Double, "List C", _poetryDouble)).thenReturn(_listPubDouble3);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "List A", _actionDouble);
        repo.addListOfItems(_userId2Double, "List B", _actionDouble);
        repo.addListOfItems(_userId1Double, "List C", _poetryDouble);

        List<ListOfItems> result = repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("List A", result.get(0).getName()),
                () -> assertEquals(_userId1Double, result.get(0).getUserId()),
                () -> assertEquals(_actionDouble, result.get(0).getGenre()),
                () -> assertFalse(result.get(0).isPrivate())
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnEmptyWhenNoPublicListsForThatGenre() {

        // Arrange
        ListOfItems _listPubDouble = mock(ListOfItems.class);
        when(_listPubDouble.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble.getName()).thenReturn("List A");
        when(_listPubDouble.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble.isPrivate()).thenReturn(true);

        when(_factoryDouble.createListOfItems(_userId1Double, "List A", _actionDouble)).thenReturn(_listPubDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "List A", _actionDouble);
        List<ListOfItems> result = repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicListsByGenreShouldReturnImmutableCopy() {

        // Arrange
        ListOfItems _listPubDouble = mock(ListOfItems.class);
        when(_listPubDouble.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble.getName()).thenReturn("List A");
        when(_listPubDouble.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble.isPrivate()).thenReturn(false);

        when(_factoryDouble.createListOfItems(_userId1Double, "List A", _actionDouble)).thenReturn(_listPubDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "List A", _actionDouble);
        List<ListOfItems> result = repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_listPubDouble));
    }

    @Test
    void findListsByUserShouldReturnOnlyListsOfThatUser() {

        // Arrange
        ListOfItems _listPubDouble1 = mock(ListOfItems.class);
        when(_listPubDouble1.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble1.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble1.getName()).thenReturn("U1 List");

        ListOfItems _listPubDouble2 = mock(ListOfItems.class);
        when(_listPubDouble2.getUserId()).thenReturn(_userId2Double);
        when(_listPubDouble2.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble2.getName()).thenReturn("U2 List");

        when(_factoryDouble.createListOfItems(_userId1Double, "U1 List", _actionDouble)).thenReturn(_listPubDouble1);
        when(_factoryDouble.createListOfItems(_userId2Double, "U2 List", _actionDouble)).thenReturn(_listPubDouble2);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "U1 List", _actionDouble);
        repo.addListOfItems(_userId2Double, "U2 List", _actionDouble);

        List<ListOfItems> result = repo.findListsByUserId(_userId1Double);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("U1 List", result.get(0).getName()),
                () -> assertEquals(_userId1Double, result.get(0).getUserId())
        );
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnListWhenExists() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _actionDouble);
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "My List", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreIgnoresCase() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _actionDouble);
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "my list", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreTrimsName() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _actionDouble);
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "  My List  ", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreReturnsNullWhenListDoesNotExist() {

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "Unknown", _actionDouble);

        // Assert
        assertNull(found);
    }

    @Test
    void findByOwnerNameAndGenreReturnsNullWhenListExistsButDoesNotMatch() {

        // Arrange
        ListOfItems list = mock(ListOfItems.class);
        when(list.getUserId()).thenReturn(_userId2Double);
        when(list.getName()).thenReturn("Other List");
        when(list.getGenre()).thenReturn(_actionDouble);

        when(_factoryDouble.createListOfItems(_userId2Double, "Other List", _actionDouble))
                .thenReturn(list);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId2Double, "Other List", _actionDouble);
        ListOfItems result = repo.findByOwnerNameAndGenre(_userId1Double, "My List", _actionDouble);

        // Assert
        assertNull(result);
    }
}