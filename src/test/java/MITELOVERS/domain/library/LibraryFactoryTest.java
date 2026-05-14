package MITELOVERS.domain.library;

import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class LibraryFactoryTest {

    @Test
    void createLibraryShouldReturnLibraryWithCorrectUser() {
        // Arrange
        UserId userId = mock(UserId.class);
        LibraryId libraryIdDouble = mock(LibraryId.class);

        //SUT
        LibraryFactory factory = new LibraryFactory();

        try (MockedConstruction<Library> mocked =
                     mockConstruction(Library.class,
                             (mock, context) -> {
                                 when(mock.identity())
                                         .thenReturn(libraryIdDouble);
                             })) {
            //Act
            Library newLibrary = factory.createLibrary(userId);

            //Assert
            assertEquals(libraryIdDouble, newLibrary.identity());
        }

    }

    @Test
    void createLibraryShouldReturnLibraryUsingRehydrationConstructor() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        List<ItemId> itemIds = List.of(itemIdDouble);

        LibraryFactory factory = new LibraryFactory();

        try (MockedConstruction<Library> mocked =
                     mockConstruction(Library.class,
                             (mock, context) -> {
                             })) {

            // Act
            Library newLibrary = factory.createLibrary(libraryIdDouble, itemIds);

            // Assert
            assertNotNull(newLibrary);
            assertEquals(mocked.constructed().get(0), newLibrary);
        }
    }

}
