package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.persistence.jpa.datamodel.LibraryDataModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryAssemblerTest {

    @Test
    void toDTO_shouldThrowWhenLibraryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> LibraryAssembler.toDTO(null));
    }

    @Test
    void toDTO_shouldMapLibraryIdCorrectly() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("LIB123");

        LibraryDataModel dto = LibraryAssembler.toDTO(library);

        assertEquals("LIB123", dto.getLibraryId());
    }

    @Test
    void toDTO_shouldMapEmptyItemList() {
        Library library = mock(Library.class);

        when(library.identity()).thenReturn(mock(LibraryId.class));
        when(library.getItemsIdInLibrary()).thenReturn(List.of());

        LibraryDataModel dto = LibraryAssembler.toDTO(library);

        assertTrue(dto.getItemIds().isEmpty());
    }

    @Test
    void toDTO_shouldMapAllItemIds() {
        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        when(library.identity()).thenReturn(libraryId);
        when(libraryId.toString()).thenReturn("LIB123");

        when(item1.toString()).thenReturn("ITEM1");
        when(item2.toString()).thenReturn("ITEM2");

        when(library.getItemsIdInLibrary()).thenReturn(List.of(item1, item2));

        LibraryDataModel dto = LibraryAssembler.toDTO(library);

        assertEquals(List.of("ITEM1", "ITEM2"), dto.getItemIds());
    }

    @Test
    void toDTO_shouldReturnImmutableItemList() {
        Library library = mock(Library.class);

        when(library.identity()).thenReturn(mock(LibraryId.class));
        when(library.getItemsIdInLibrary()).thenReturn(List.of(mock(ItemId.class)));

        LibraryDataModel dto = LibraryAssembler.toDTO(library);

        assertThrows(UnsupportedOperationException.class,
                () -> dto.getItemIds().add("X"));
    }

}