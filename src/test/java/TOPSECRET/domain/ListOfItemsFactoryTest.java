package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOfItemsFactoryTest {

    @Test
    void shouldSuccessfullyCreatePrivateList() {
    // arrange
    User userDouble = mock(User.class);
    Genre genreDouble = mock(Genre.class);

    try (MockedConstruction<ListOfItems> mocked =
                 mockConstruction(ListOfItems.class,
                         (mock, context) -> {
                             when(mock.isPrivate()).thenReturn(true);
                             when(mock.getUser()).thenReturn(userDouble);
                             when(mock.getName()).thenReturn("My List");
                             when(mock.getGenre()).thenReturn(genreDouble);
                         })) {

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        // act
        ListOfItems newList =
                factory.createListOfItems(userDouble, "My List", genreDouble);

        // assert
        assertNotNull(newList);
        assertEquals(userDouble, newList.getUser());
        assertEquals("My List", newList.getName());
        assertEquals(genreDouble, newList.getGenre());
        assertTrue(newList.isPrivate());

        assertEquals(1, mocked.constructed().size());
    }
}

    @Test
    void shouldSuccessfullyCreatePublicList() {
        // arrange
        User userDouble = mock(User.class);
        Genre genreDouble = mock(Genre.class);

        // SUT
        ListOfItemsFactory factory = new ListOfItemsFactory();

        try (MockedConstruction<ListOfItems> mocked =
                     mockConstruction(ListOfItems.class,
                             (mock, context) -> {
                                 doNothing().when(mock).makePublic();
                             })) {

            // act
            ListOfItems newList =
                    factory.createPublicListOfItems(userDouble, "My List", genreDouble);

            // assert
            verify(newList).makePublic();
            assertNotNull(newList);
            assertEquals(1, mocked.constructed().size());
        }
    }
}