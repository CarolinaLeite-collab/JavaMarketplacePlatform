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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryAssemblerTest {

    private LibraryAssembler assembler;

    @BeforeEach
    void setup() {
        assembler = new LibraryAssembler(new LibraryFactory());
    }

    // ----------------
    // domain2dm TESTS
    // ----------------

    @Test
    void domain2dm_shouldThrowWhenLibraryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> assembler.domain2dm(null));
    }

    @Test
    void domain2dm_shouldMapLibraryIdCorrectly() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("test@example.com");

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        when(item1.toString()).thenReturn("ABCDEF1234");
        when(item2.toString()).thenReturn("A1B2C3D4E5");

        when(library.getItemsIdInLibrary()).thenReturn(List.of(item1, item2));

        LibraryDataModel dm = assembler.domain2dm(library);

        assertEquals("test@example.com", dm.getLibraryId());
        assertEquals(List.of("ABCDEF1234", "A1B2C3D4E5"), dm.getItemIds());
    }

    @Test
    void domain2dm_shouldMapEmptyItemList() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("test@example.com");

        when(library.getItemsIdInLibrary()).thenReturn(List.of());

        LibraryDataModel dm = assembler.domain2dm(library);

        assertTrue(dm.getItemIds().isEmpty());
    }

    @Test
    void domain2dm_shouldMapAllItemIds() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("test@example.com");

        when(item1.toString()).thenReturn("ABCDEF1234");
        when(item2.toString()).thenReturn("A1B2C3D4E5");

        when(library.getItemsIdInLibrary()).thenReturn(List.of(item1, item2));

        LibraryDataModel dm = assembler.domain2dm(library);

        assertEquals(List.of("ABCDEF1234", "A1B2C3D4E5"), dm.getItemIds());
    }

    // -------------------------
    // dm2domain TESTS
    // -------------------------

    @Test
    void dm2domain_shouldThrowWhenDataModelIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> assembler.dm2domain(null));
    }

    @Test
    void dm2domain_shouldReconstructDomainCorrectly() {
        LibraryDataModel dm = new LibraryDataModel(
                "test@example.com",
                List.of("ABCDEF1234", "A1B2C3D4E5")
        );

        Library library = assembler.dm2domain(dm);

        assertEquals("test@example.com", library.identity().toString());
        assertEquals(2, library.getItemsIdInLibrary().size());
        assertEquals("ABCDEF1234", library.getItemsIdInLibrary().get(0).toString());
        assertEquals("A1B2C3D4E5", library.getItemsIdInLibrary().get(1).toString());
    }

    // -------------------------
    // LIST MAPPING TESTS
    // -------------------------

    @Test
    void domainList2dmList_shouldMapAll() {
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
                assembler.domainList2dmList(List.of(lib1, lib2));

        assertEquals(2, result.size());
    }

    @Test
    void dmList2DomainList_shouldMapAll() {
        LibraryDataModel dm1 = new LibraryDataModel("test@example1.com", List.of());
        LibraryDataModel dm2 = new LibraryDataModel("test@example2.com", List.of());

        List<Library> result =
                assembler.dmList2DomainList(List.of(dm1, dm2));

        assertEquals(2, result.size());
        assertEquals("test@example1.com", result.get(0).identity().toString());
        assertEquals("test@example2.com", result.get(1).identity().toString());
    }

}