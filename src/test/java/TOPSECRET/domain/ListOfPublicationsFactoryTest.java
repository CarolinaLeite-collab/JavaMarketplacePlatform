package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOfPublicationsFactoryTest {

    @Test
    void shouldSuccessfullyCreatePrivateList() {
    // arrange
    User _userDouble = mock(User.class);
    Genre _genreDouble = mock(Genre.class);

    try (MockedConstruction<ListOfPublications> mocked =
                 mockConstruction(ListOfPublications.class,
                         (mock, context) -> {
                             when(mock.isPrivate()).thenReturn(true);
                             when(mock.getUser()).thenReturn(_userDouble);
                             when(mock.getName()).thenReturn("My List");
                             when(mock.getGenre()).thenReturn(_genreDouble);
                         })) {

        // SUT
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();

        // act
        ListOfPublications newList =
                factory.createListOfPublications(_userDouble, "My List", _genreDouble);

        // assert
        assertNotNull(newList);
        assertEquals(_userDouble, newList.getUser());
        assertEquals("My List", newList.getName());
        assertEquals(_genreDouble, newList.getGenre());
        assertTrue(newList.isPrivate());

        assertEquals(1, mocked.constructed().size());
    }
}

    @Test
    void shouldSuccessfullyCreatePublicList() {
        // arrange
        User _userDouble = mock(User.class);
        Genre _genreDouble = mock(Genre.class);

        try (MockedConstruction<ListOfPublications> mocked =
                     mockConstruction(ListOfPublications.class,
                             (mock, context) -> {
                                 doNothing().when(mock).makePublic();
                             })) {

            // SUT
            ListOfPublicationsFactory factory = new ListOfPublicationsFactory();

            // act
            ListOfPublications newList =
                    factory.createPublicListOfPublications(_userDouble, "My List", _genreDouble);

            // assert
            assertNotNull(newList);
            assertEquals(1, mocked.constructed().size());
        }
    }
}