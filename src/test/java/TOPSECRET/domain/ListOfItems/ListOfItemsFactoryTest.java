package TOPSECRET.domain.ListOfItems;

import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ListOfItemsFactoryTest {

    @Test
    void shouldSuccessfullyCreatePrivateList() {
        // arrange
        UserId _userIdDouble = mock(UserId.class);
        GenreId _genreIdDouble = mock(GenreId.class);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createListOfItems(_userIdDouble, "My List", _genreIdDouble);

        // Assert
        assertNotNull(list);
        assertEquals(_userIdDouble, list.getUserId());
        assertEquals("My List", list.getName());
        assertEquals(_genreIdDouble, list.getGenreId());
        assertTrue(list.isPrivate());
    }

    @Test
    void shouldSuccessfullyCreatePublicList() {
        // arrange
        UserId _userIdDouble = mock(UserId.class);
        GenreId _genreIdDouble = mock(GenreId.class);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createPublicListOfItems(_userIdDouble, "My List", _genreIdDouble);

        // Assert
        assertNotNull(list);
        assertEquals(_userIdDouble, list.getUserId());
        assertEquals("My List", list.getName());
        assertEquals(_genreIdDouble, list.getGenreId());
        assertFalse(list.isPrivate());
    }

    @Test
    void shouldGenerateNewListId() {
        // Arrange
        UserId _userIdDouble = mock(UserId.class);
        GenreId _genreIdDouble = mock(GenreId.class);
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list1 = factory.createListOfItems(_userIdDouble, "List A", _genreIdDouble);
        ListOfItems list2 = factory.createListOfItems(_userIdDouble, "List B", _genreIdDouble);

        // Assert
        assertNotEquals(list1.identity(), list2.identity());
    }
}