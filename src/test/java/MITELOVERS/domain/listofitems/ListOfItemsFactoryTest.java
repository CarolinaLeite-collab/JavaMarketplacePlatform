package MITELOVERS.domain.listofitems;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ListOfItemsFactoryTest {

    @Test
    void shouldSuccessfullyCreatePrivateList() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Name name = new Name("My List");

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createListOfItems(userIdDouble, name, genreIdDouble);

        // Assert
        assertNotNull(list);
        assertEquals(userIdDouble, list.getUserId());
        assertEquals(name, list.getName());
        assertEquals(genreIdDouble, list.getGenreId());
        assertTrue(list.isPrivate());
    }

    @Test
    void shouldSuccessfullyCreatePublicList() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Name name = new Name("My List");
        SharedDuration duration = new SharedDuration(7);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createPublicListOfItems(userIdDouble, name, genreIdDouble, duration);

        // Assert
        assertNotNull(list);
        assertEquals(userIdDouble, list.getUserId());
        assertEquals(name, list.getName());
        assertEquals(genreIdDouble, list.getGenreId());
        assertFalse(list.isPrivate());
        assertNotNull(list.getSharedUntil());
    }

    @Test
    void shouldGenerateNewListId() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list1 = factory.createListOfItems(userIdDouble, new Name("List A"), genreIdDouble);
        ListOfItems list2 = factory.createListOfItems(userIdDouble, new Name("List B"), genreIdDouble);

        // Assert
        assertNotEquals(list1.identity(), list2.identity());
    }

    @Test
    void shouldSuccessfullyCreateListWithExistingId() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        ListOfItemsId listOfItemsId = mock(ListOfItemsId.class);
        Name name = new Name("My List");
        LocalDateTime sharedUntil = LocalDateTime.now().plusDays(7);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createListOfItems(listOfItemsId, userIdDouble, name,
                genreIdDouble, false, sharedUntil);

        // Assert
        assertNotNull(list);
        assertEquals(listOfItemsId, list.identity());
        assertEquals(userIdDouble, list.getUserId());
        assertEquals(name, list.getName());
        assertEquals(genreIdDouble, list.getGenreId());
        assertFalse(list.isPrivate());
        assertEquals(sharedUntil, list.getSharedUntil());
    }

    @Test
    void shouldPreserveProvidedListOfItemsId() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        ListOfItemsId listOfItemsId = ListOfItemsId.newId();
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // Act
        ListOfItems list = factory.createListOfItems(listOfItemsId, userIdDouble,
                new Name("My List"), genreIdDouble, true, null);

        // Assert
        assertEquals(listOfItemsId, list.identity());
    }
}