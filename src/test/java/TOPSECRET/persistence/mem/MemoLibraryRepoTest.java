package TOPSECRET.persistence.mem;

import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.library.LibraryFactory;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.Email;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.LibraryId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MemoLibraryRepoTest {

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
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

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
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

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
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

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
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

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
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

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
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

        // Act
        repo.save(libraryDouble);
        repo.save(library2Double);

        boolean result = repo.containsOfIdentity(libraryId3Double);

        // Assert
        assertFalse(result);
    }



    @Test
    void addLibraryShouldReturnLibrary() {

        // Arrange
        Library libraryDouble = mock(Library.class);
        LibraryId libraryIdDouble = LibraryId.fromUserId(_userIdDouble);
        when(libraryDouble.sameAs(libraryIdDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(libraryIdDouble)).thenReturn(libraryDouble);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);

        // Act
        Library mylibrary = libraryRepo.addLibrary(_userIdDouble);

        // Assert
        assertEquals(libraryDouble, mylibrary);
    }

    @Test
    void addLibraryShouldThrowAnExceptionWhenLibraryAlreadyExists() {

        // Arrange
        Library libraryDouble = mock(Library.class);
        LibraryId libraryIdDouble = LibraryId.fromUserId(_userIdDouble);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(_libraryFactoryDouble.createLibrary(libraryIdDouble)).thenReturn(libraryDouble);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);

        // Act
        libraryRepo.addLibrary(_userIdDouble);

        // Assert
        assertThrows(IllegalStateException.class, () -> libraryRepo.addLibrary(_userIdDouble));
    }

    @Test
    void findLibraryByUserShouldReturnLibraryWhenExists() {

        // Arrange
        LibraryId libraryIdDouble = LibraryId.fromUserId(_userIdDouble);

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(_libraryFactoryDouble.createLibrary(libraryIdDouble)).thenReturn(libraryDouble);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);
        libraryRepo.addLibrary(_userIdDouble);

        // Act
        Library result = libraryRepo.findLibraryByUserId(_userIdDouble);

        // Assert
        assertEquals(libraryDouble, result);
    }

    @Test
    void addLibraryShouldSucceedWhenOtherUserAlreadyHasLibrary() {
        // Arrange
        UserId otherUserIdDouble = mock(UserId.class);
        Email otherEmailDouble = mock(Email.class);
        when(otherUserIdDouble.getEmail()).thenReturn(otherEmailDouble);

        LibraryId libraryIdDouble = LibraryId.fromUserId(_userIdDouble);
        LibraryId otherLibraryIdDouble = LibraryId.fromUserId(otherUserIdDouble);

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(otherLibraryIdDouble);
        when(_libraryFactoryDouble.createLibrary(otherLibraryIdDouble)).thenReturn(libraryDouble);

        Library libraryDouble2 = mock(Library.class);
        when(libraryDouble2.identity()).thenReturn(libraryIdDouble);
        when(_libraryFactoryDouble.createLibrary(libraryIdDouble)).thenReturn(libraryDouble2);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);

        libraryRepo.addLibrary(otherUserIdDouble);

        // Act
        Library result = libraryRepo.addLibrary(_userIdDouble);

        // Assert
        assertEquals(libraryDouble2, result);
    }

    @Test
    void findLibraryByUserShouldThrowExceptionWhenLibraryDoesNotExist() {

        // Arrange
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble); // SUT

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> libraryRepo.findLibraryByUserId(_userIdDouble));
    }

    @Test
    void getItemsInLibraryShouldReturnListOfItemIds() {

        // Arrange
        ItemId itemIdInLibrary1 = mock(ItemId.class);
        ItemId itemIdInLibrary2 = mock(ItemId.class);
        LibraryId libraryIdDouble = LibraryId.fromUserId(_userIdDouble);
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(Arrays.asList(itemIdInLibrary1, itemIdInLibrary2));

        // SUT
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

        // Act
        repo.save(libraryDouble);
        List<ItemId> result = repo.getItemsInLibraryByUserId(_userIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getItemsInLibraryShouldThrowWhenLibraryNotFoundForUser() {

        // Arrange
        ItemId itemIdInLibrary1 = mock(ItemId.class);
        ItemId itemIdInLibrary2 = mock(ItemId.class);
        LibraryId libraryIdDouble = LibraryId.fromUserId(_userIdDouble);
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.identity()).thenReturn(libraryIdDouble);
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(Arrays.asList(itemIdInLibrary1, itemIdInLibrary2));

        UserId otherUserIdDouble = mock(UserId.class);
        Email otherEmailDouble = mock(Email.class);
        when(otherUserIdDouble.getEmail()).thenReturn(otherEmailDouble);

        // SUT
        MemoLibraryRepo repo = new MemoLibraryRepo(_libraryFactoryDouble);

        // Act
        repo.save(libraryDouble);

        // Assert
        assertThrows(IllegalStateException.class,
                () -> repo.getItemsInLibraryByUserId(otherUserIdDouble));
    }
}