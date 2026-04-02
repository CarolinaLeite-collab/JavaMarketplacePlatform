package TOPSECRET.domain;

import TOPSECRET.domain.genre.Genre;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOfPublicationsFactoryTest {

    @Test
    void shouldSuccessfullyCreatePrivateList() {
    // arrange
    User userDouble = mock(User.class);
    Genre genreDouble = mock(Genre.class);

    try (MockedConstruction<ListOfPublications> mocked =
                 mockConstruction(ListOfPublications.class,
                         (mock, context) -> {
                             when(mock.isPrivate()).thenReturn(true);
                             when(mock.getUser()).thenReturn(userDouble);
                             when(mock.getName()).thenReturn("My List");
                             when(mock.getGenre()).thenReturn(genreDouble);
                         })) {

        // SUT
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();

        // act
        ListOfPublications newList =
                factory.createListOfPublications(userDouble, "My List", genreDouble);

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
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();

        try (MockedConstruction<ListOfPublications> mocked =
                     mockConstruction(ListOfPublications.class,
                             (mock, context) -> {
                                 doNothing().when(mock).makePublic();
                             })) {

            // act
            ListOfPublications newList =
                    factory.createPublicListOfPublications(userDouble, "My List", genreDouble);

            // assert
            verify(newList).makePublic();
            assertNotNull(newList);
            assertEquals(1, mocked.constructed().size());
        }
    }
}