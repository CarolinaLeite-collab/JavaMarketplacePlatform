package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOfItemsAssemblerTest {

    private ListOfItemsFactory _factoryDouble;
    private ListOfItemsAssembler _assembler;
    private ListOfItems _listDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(ListOfItemsFactory.class);
        _assembler = new ListOfItemsAssembler(_factoryDouble);
        _listDouble = mock(ListOfItems.class);

        when(_listDouble.identity()).thenReturn(new ListOfItemsId("LOI-ABC123"));
        when(_listDouble.getUserId()).thenReturn(new UserId(new Email("user@mitelovers.com")));
        when(_listDouble.getName()).thenReturn("My List");
        when(_listDouble.getGenreId()).thenReturn(new GenreId("FICTION"));
        when(_listDouble.isPrivate()).thenReturn(true);
        when(_listDouble.getItemIds()).thenReturn(List.of());
    }

    @Test
    void domain2DMShouldMapIdCorrectly() {
        ListOfItemsDataModel result = _assembler.domain2DM(_listDouble);
        assertEquals("LOI-ABC123", result.getListOfItemsId());
    }

    @Test
    void domain2DMShouldMapUserIdCorrectly() {
        ListOfItemsDataModel result = _assembler.domain2DM(_listDouble);
        assertEquals("user@mitelovers.com", result.getUserId());
    }

    @Test
    void domain2DMShouldMapNameCorrectly() {
        ListOfItemsDataModel result = _assembler.domain2DM(_listDouble);
        assertEquals("My List", result.getName());
    }

    @Test
    void domain2DMShouldMapGenreIdCorrectly() {
        ListOfItemsDataModel result = _assembler.domain2DM(_listDouble);
        assertEquals("FICTION", result.getGenreId());
    }

    @Test
    void domain2DMShouldThrowWhenListOfItemsIsNull() {
        assertThrows(IllegalArgumentException.class, () -> _assembler.domain2DM(null));
    }

    @Test
    void DM2DomainShouldDelegateToFactory() {
        // Arrange
        ListOfItemsDataModel dmDouble = mock(ListOfItemsDataModel.class);
        ListOfItems listDouble = mock(ListOfItems.class);

        when(dmDouble.getListOfItemsId()).thenReturn("LOI-ABC123");
        when(dmDouble.getUserId()).thenReturn("user@mitelovers.com");
        when(dmDouble.getName()).thenReturn("My List");
        when(dmDouble.getGenreId()).thenReturn("FICTION");
        when(dmDouble.isPrivate()).thenReturn(true);
        when(dmDouble.getItemIds()).thenReturn(List.of());
        when(_factoryDouble.createListOfItems(
                any(ListOfItemsId.class), any(UserId.class),
                any(String.class), any(GenreId.class)))
                .thenReturn(listDouble);

        // Act
        ListOfItems result = _assembler.DM2Domain(dmDouble);

        // Assert
        assertEquals(listDouble, result);
    }

    @Test
    void DM2DomainShouldThrowWhenDataModelIsNull() {
        assertThrows(IllegalArgumentException.class, () -> _assembler.DM2Domain(null));
    }

    @Test
    void DM2DomainShouldCallMakePublicWhenNotPrivate() {
        // Arrange
        ListOfItemsDataModel dmDouble = mock(ListOfItemsDataModel.class);
        ListOfItems listDouble = mock(ListOfItems.class);

        when(dmDouble.getListOfItemsId()).thenReturn("LOI-ABC123");
        when(dmDouble.getUserId()).thenReturn("user@mitelovers.com");
        when(dmDouble.getName()).thenReturn("My List");
        when(dmDouble.getGenreId()).thenReturn("FICTION");
        when(dmDouble.isPrivate()).thenReturn(false);
        when(dmDouble.getItemIds()).thenReturn(List.of());
        when(_factoryDouble.createListOfItems(
                any(ListOfItemsId.class), any(UserId.class),
                any(String.class), any(GenreId.class)))
                .thenReturn(listDouble);

        // Act
        _assembler.DM2Domain(dmDouble);

        // Assert
        verify(listDouble).makePublic();
    }

    @Test
    void domainList2DMListShouldReturnCorrectSize() {
        // Arrange
        ListOfItems listDouble2 = mock(ListOfItems.class);
        when(listDouble2.identity()).thenReturn(new ListOfItemsId("LOI-DEF456"));
        when(listDouble2.getUserId()).thenReturn(new UserId(new Email("user2@mitelovers.com")));
        when(listDouble2.getName()).thenReturn("My Second List");
        when(listDouble2.getGenreId()).thenReturn(new GenreId("FICTION"));
        when(listDouble2.isPrivate()).thenReturn(true);
        when(listDouble2.getItemIds()).thenReturn(List.of());

        // Act
        List<ListOfItemsDataModel> result = _assembler.domainList2DMList(List.of(_listDouble, listDouble2));

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void domainList2DMListShouldReturnEmptyWhenInputIsEmpty() {
        // Act
        List<ListOfItemsDataModel> result = _assembler.domainList2DMList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void dmList2DomainListShouldReturnCorrectSize() {
        // Arrange
        ListOfItemsDataModel dmDouble1 = mock(ListOfItemsDataModel.class);
        ListOfItemsDataModel dmDouble2 = mock(ListOfItemsDataModel.class);
        ListOfItems listDouble2 = mock(ListOfItems.class);

        when(dmDouble1.getListOfItemsId()).thenReturn("LOI-ABC123");
        when(dmDouble1.getUserId()).thenReturn("user@mitelovers.com");
        when(dmDouble1.getName()).thenReturn("My List");
        when(dmDouble1.getGenreId()).thenReturn("FICTION");
        when(dmDouble1.isPrivate()).thenReturn(true);
        when(dmDouble1.getItemIds()).thenReturn(List.of());

        when(dmDouble2.getListOfItemsId()).thenReturn("LOI-DEF456");
        when(dmDouble2.getUserId()).thenReturn("user2@mitelovers.com");
        when(dmDouble2.getName()).thenReturn("My Second List");
        when(dmDouble2.getGenreId()).thenReturn("FICTION");
        when(dmDouble2.isPrivate()).thenReturn(true);
        when(dmDouble2.getItemIds()).thenReturn(List.of());

        when(_factoryDouble.createListOfItems(
                any(ListOfItemsId.class), any(UserId.class),
                any(String.class), any(GenreId.class)))
                .thenReturn(_listDouble)
                .thenReturn(listDouble2);

        // Act
        List<ListOfItems> result = _assembler.dmList2DomainList(List.of(dmDouble1, dmDouble2));

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void dmList2DomainListShouldReturnEmptyWhenInputIsEmpty() {
        // Act
        List<ListOfItems> result = _assembler.dmList2DomainList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

}