package MITELOVERS.persistence.mem;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MemLibraryRepoTest {

    private Email _emailDouble;
    private UserId _userIdDouble;
    private LibraryFactory _libraryFactoryDouble;

    @BeforeEach
    void setUp() {

        _emailDouble = mock(Email.class);
        _userIdDouble = mock(UserId.class);
        when(_userIdDouble.getEmail()).thenReturn(_emailDouble);

        _libraryFactoryDouble = mock(LibraryFactory.class);

    }

    @Test
    void saveLibraryShouldSaveAndReturnLibrary(){

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);

        // SUT
        MemLibraryRepo repo = new MemLibraryRepo();

        // Act
        Library result = repo.save(libraryDouble);

        // Assert
        assertEquals(libraryDouble, result);
        assertTrue(repo.containsOfIdentity(libraryIdDouble));

    }

    @Test
    void findAllShouldReturnAllStoredLibraries(){

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        LibraryId libraryId2Double = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);
        Library library2Double = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(library2Double.identity()).thenReturn(libraryId2Double);

        // SUT
        MemLibraryRepo repo = new MemLibraryRepo();

        // Act
        repo.save(libraryDouble);
        repo.save(library2Double);

        List<Library> list = new ArrayList<>();
        Iterable<Library> result = repo.findAll();

        result.forEach(list::add);

        // Assert
        assertEquals(2, list.size());
    }

    @Test
    void ofIdentityShouldReturnLibraryIfPresent(){

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        LibraryId libraryId2Double = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);
        Library library2Double = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(library2Double.identity()).thenReturn(libraryId2Double);

        // SUT
        MemLibraryRepo repo = new MemLibraryRepo();

        // Act
        repo.save(libraryDouble);
        repo.save(library2Double);

        Library result = repo.ofIdentity(libraryIdDouble)
                .orElseThrow(() -> new AssertionError("Library not found"));

        // Assert
        assertEquals(libraryDouble, result);
    }

    @Test
    void ofIdentityShouldReturnEmptyIfNotPresent(){

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        LibraryId libraryId2Double = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);
        Library library2Double = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(library2Double.identity()).thenReturn(libraryId2Double);

        // SUT
        MemLibraryRepo repo = new MemLibraryRepo();

        // Act
        repo.save(libraryDouble);
        Optional<Library> result = repo.ofIdentity(libraryId2Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityShouldReturnTrueIfLibraryPresent(){

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        LibraryId libraryId2Double = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);
        Library library2Double = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(library2Double.identity()).thenReturn(libraryId2Double);

        // SUT
        MemLibraryRepo repo = new MemLibraryRepo();

        // Act
        repo.save(libraryDouble);
        repo.save(library2Double);

        boolean result = repo.containsOfIdentity(libraryIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseIfLibraryNotPresent(){

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        LibraryId libraryId2Double = mock(LibraryId.class);
        LibraryId libraryId3Double = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);
        Library library2Double = mock(Library.class);
        Library library3Double = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(library2Double.identity()).thenReturn(libraryId2Double);
        when(library3Double.identity()).thenReturn(libraryId3Double);

        // SUT
        MemLibraryRepo repo = new MemLibraryRepo();

        // Act
        repo.save(libraryDouble);
        repo.save(library2Double);

        boolean result = repo.containsOfIdentity(libraryId3Double);

        // Assert
        assertFalse(result);
    }

    @Test
    void getItemsInLibraryShouldReturnListOfItemIds() {
        // Arrange
        ItemId itemIdInLibrary1Double = mock(ItemId.class);
        ItemId itemIdInLibrary2Double = mock(ItemId.class);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);

        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(libraryDouble.getItemsIdInLibrary())
                .thenReturn(Arrays.asList(itemIdInLibrary1Double, itemIdInLibrary2Double));

        // SUT
        MemLibraryRepo repo = new MemLibraryRepo();

        // Act
        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            repo.save(libraryDouble);
            Optional<Library> savedLibrary = repo.ofIdentity(libraryIdDouble);

            List<ItemId> result = savedLibrary.get().getItemsIdInLibrary();

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.contains(itemIdInLibrary1Double));
            assertTrue(result.contains(itemIdInLibrary2Double));
        }
    }
}
