package MITELOVERS.domain.listofitems;

import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfItemsFactoryTest {

    @Test
    void shouldSuccessfullyCreatePrivateList() {
        // arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createListOfItems(userIdDouble, "My List", genreIdDouble);

        // Assert
        assertNotNull(list);
        assertEquals(userIdDouble, list.getUserId());
        assertEquals("My List", list.getName());
        assertEquals(genreIdDouble, list.getGenreId());
        assertTrue(list.isPrivate());
    }

    @Test
    void shouldSuccessfullyCreatePublicList() {
        // arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createPublicListOfItems(userIdDouble, "My List", genreIdDouble);

        // Assert
        assertNotNull(list);
        assertEquals(userIdDouble, list.getUserId());
        assertEquals("My List", list.getName());
        assertEquals(genreIdDouble, list.getGenreId());
        assertFalse(list.isPrivate());
    }

    @Test
    void shouldGenerateNewListId() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);

        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list1 = factory.createListOfItems(userIdDouble, "List A", genreIdDouble);
        ListOfItems list2 = factory.createListOfItems(userIdDouble, "List B", genreIdDouble);

        // Assert
        assertNotEquals(list1.identity(), list2.identity());
    }

    @Test
    void shouldSuccessfullyCreateListWithExistingId() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        ListOfItemsId listOfItemsId = mock(ListOfItemsId.class);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createListOfItems(listOfItemsId, userIdDouble, "My List", genreIdDouble);

        // Assert
        assertNotNull(list);
        assertEquals(listOfItemsId, list.identity());
        assertEquals(userIdDouble, list.getUserId());
        assertEquals("My List", list.getName());
        assertEquals(genreIdDouble, list.getGenreId());
        assertTrue(list.isPrivate());
    }

    @Test
    void shouldPreserveProvidedListOfItemsId() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        ListOfItemsId listOfItemsId = ListOfItemsId.newId();

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createListOfItems(listOfItemsId, userIdDouble, "My List", genreIdDouble);

        // Assert
        assertEquals(listOfItemsId, list.identity());
    }

}
