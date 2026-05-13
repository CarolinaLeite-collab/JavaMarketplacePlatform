package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.persistence.jpa.datamodel.LibraryDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class LibraryAssemblerTest {

    private LibraryAssembler assembler;
    private LibraryFactory libraryFactory;

    @BeforeEach
    void setup() {

        libraryFactory = mock(LibraryFactory.class);
        assembler = new LibraryAssembler(libraryFactory);
    }

    // ------------------
    // toDataModel TESTS
    // ------------------

    @Test
    void toDataModelShouldThrowWhenLibraryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDataModel(null));
    }

    @Test
    void toDataModelShouldMapLibraryIdCorrectly() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("test@example.com");

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        when(item1.toString()).thenReturn("ABCDEF1234");
        when(item2.toString()).thenReturn("A1B2C3D4E5");

        when(library.getItemsIdInLibrary()).thenReturn(List.of(item1, item2));

        LibraryDataModel dm = assembler.toDataModel(library);

        assertEquals("test@example.com", dm.getLibraryId());
        assertEquals(List.of("ABCDEF1234", "A1B2C3D4E5"), dm.getItemIds());
    }

    @Test
    void toDataModelShouldMapEmptyItemList() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("test@example.com");

        when(library.getItemsIdInLibrary()).thenReturn(List.of());

        LibraryDataModel dm = assembler.toDataModel(library);

        assertTrue(dm.getItemIds().isEmpty());
    }

    @Test
    void toDataModelShouldMapAllItemIds() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("test@example.com");

        when(item1.toString()).thenReturn("ABCDEF1234");
        when(item2.toString()).thenReturn("A1B2C3D4E5");

        when(library.getItemsIdInLibrary()).thenReturn(List.of(item1, item2));

        LibraryDataModel dm = assembler.toDataModel(library);

        assertEquals(List.of("ABCDEF1234", "A1B2C3D4E5"), dm.getItemIds());
    }

    // -------------------------
    // toDomain TESTS
    // -------------------------

    @Test
    void toDomainShouldThrowWhenDataModelIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null));
    }

    @Test
    void toDomainCorrectly() {
        // Arrange
        LibraryDataModel dm = new LibraryDataModel(
                "test@example.com",
                List.of("ABCDEF1234", "A1B2C3D4E5")
        );

        Library expected = mock(Library.class);

        // The assembler will call the factory internally.
        // We only care that it returns what the factory returns.
        when(libraryFactory.createLibrary(any(LibraryId.class), anyList()))
                .thenReturn(expected);

        // Act
        Library result = assembler.toDomain(dm);

        // Assert
        assertSame(expected, result);
    }

    // -------------------------
    // LIST MAPPING TESTS
    // -------------------------

    @Test
    void listToDataModelShouldMapAll() {
        Library lib1 = mock(Library.class);
        Library lib2 = mock(Library.class);

        LibraryId id1 = mock(LibraryId.class);
        LibraryId id2 = mock(LibraryId.class);

        when(lib1.identity()).thenReturn(id1);
        when(lib2.identity()).thenReturn(id2);

        when(id1.toString()).thenReturn("test@example1.com");
        when(id2.toString()).thenReturn("test@example2.com");

        when(lib1.getItemsIdInLibrary()).thenReturn(List.of());
        when(lib2.getItemsIdInLibrary()).thenReturn(List.of());

        List<LibraryDataModel> result =
                assembler.listToDataModel(List.of(lib1, lib2));

        assertEquals(2, result.size());
    }

    @Test
    void listToDomainShouldMapAll() {
        // Arrange
        LibraryDataModel dm1 = new LibraryDataModel("test@example1.com", List.of());
        LibraryDataModel dm2 = new LibraryDataModel("test@example2.com", List.of());

        Library lib1 = mock(Library.class);
        Library lib2 = mock(Library.class);

        // The assembler will call the factory twice internally.
        when(libraryFactory.createLibrary(any(LibraryId.class), anyList()))
                .thenReturn(lib1, lib2);

        // Act
        List<Library> result = assembler.listToDomain(List.of(dm1, dm2));

        // Assert
        assertEquals(2, result.size());
        assertSame(lib1, result.get(0));
        assertSame(lib2, result.get(1));
    }

}