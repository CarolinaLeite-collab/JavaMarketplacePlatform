package MITELOVERS.mapper;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfItemsResponseDTOMapperTest {

    @Test
    void toDTOReturnsListOfItemsResponseDTO() {
        //arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsResponseDTO responseDTO = mock(ListOfItemsResponseDTO.class);
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        Name nameDouble = mock(Name.class);
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(listOfItemsDouble.identity()).thenReturn(listOfItemsIdDouble);
        when(listOfItemsIdDouble.toString()).thenReturn("LOI-1234");

        when(listOfItemsDouble.getUserId()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user@cenas.com");

        when(listOfItemsDouble.getName()).thenReturn(nameDouble);
        when(nameDouble.toString()).thenReturn("Listinha");

        when(listOfItemsDouble.getGenreId()).thenReturn(genreIdDouble);
        when(genreIdDouble.toString()).thenReturn("ROMANCE");

        when(listOfItemsDouble.isPrivate()).thenReturn(true);
        when(listOfItemsDouble.getSharedUntil()).thenReturn(null);

        when(listOfItemsDouble.getItemIds()).thenReturn(List.of(itemIdDouble));
        when(itemIdDouble.toString()).thenReturn("ABCDEF1234");

        //SUT
        ListOfItemsResponseDTOMapper mapper = new ListOfItemsResponseDTOMapper();

        //act
        ListOfItemsResponseDTO result = mapper.toModel(listOfItemsDouble);

        //assert
        assertEquals("LOI-1234", result.getListId());
        assertEquals("user@cenas.com", result.getUserId());
        assertEquals("Listinha", result.getName());
        assertEquals("ROMANCE", result.getGenreId());
        assertTrue(result.isPrivate());
        assertNull(result.getSharedUntil());
        assertEquals(List.of("ABCDEF1234"), result.getItemsId());
    }

}