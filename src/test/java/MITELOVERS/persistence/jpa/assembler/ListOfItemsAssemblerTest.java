package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ListOfItemsAssemblerTest {

    private ListOfItemsFactory _factoryDouble;
    private ListOfItems _listDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(ListOfItemsFactory.class);
        _listDouble = mock(ListOfItems.class);

        when(_listDouble.identity()).thenReturn(new ListOfItemsId("LOI-ABC123"));
        when(_listDouble.getUserId()).thenReturn(new UserId(new Email("user@mitelovers.com")));
        when(_listDouble.getName()).thenReturn(new Name("My List"));
        when(_listDouble.getGenreId()).thenReturn(new GenreId("FICTION"));
        when(_listDouble.isPrivate()).thenReturn(true);
        when(_listDouble.getSharedUntil()).thenReturn(null);
        when(_listDouble.getItemIds()).thenReturn(List.of());
    }

    @Test
    void toDataModelShouldMapIdCorrectly() {
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        ListOfItemsDataModel result = assembler.toDataModel(_listDouble);

        assertEquals("LOI-ABC123", result.getListOfItemsId());
    }

    @Test
    void toDataModelShouldMapUserIdCorrectly() {
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        ListOfItemsDataModel result = assembler.toDataModel(_listDouble);

        assertEquals("user@mitelovers.com", result.getUserId());
    }

    @Test
    void toDataModelShouldMapNameCorrectly() {
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        ListOfItemsDataModel result = assembler.toDataModel(_listDouble);

        assertEquals("My List", result.getName());
    }

    @Test
    void toDataModelShouldMapGenreIdCorrectly() {
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        ListOfItemsDataModel result = assembler.toDataModel(_listDouble);

        assertEquals("FICTION", result.getGenreId());
    }

    @Test
    void toDataModelShouldMapSharedUntilCorrectly() {
        // Arrange
        LocalDateTime sharedUntil = LocalDateTime.now().plusDays(7);
        when(_listDouble.getSharedUntil()).thenReturn(sharedUntil);
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        // Act
        ListOfItemsDataModel result = assembler.toDataModel(_listDouble);

        // Assert
        assertEquals(sharedUntil, result.getSharedUntil());
    }

    @Test
    void toDomainShouldDelegateToFactory() {
        // Arrange
        ListOfItemsDataModel dmDouble = mock(ListOfItemsDataModel.class);
        ListOfItems listDouble = mock(ListOfItems.class);

        when(dmDouble.getListOfItemsId()).thenReturn("LOI-ABC123");
        when(dmDouble.getUserId()).thenReturn("user@mitelovers.com");
        when(dmDouble.getName()).thenReturn("My List");
        when(dmDouble.getGenreId()).thenReturn("FICTION");
        when(dmDouble.isPrivate()).thenReturn(true);
        when(dmDouble.getSharedUntil()).thenReturn(null);
        when(dmDouble.getItemIds()).thenReturn(List.of());
        when(_factoryDouble.createListOfItems(
                any(ListOfItemsId.class), any(UserId.class),
                any(Name.class), any(GenreId.class),
                anyBoolean(), isNull()))
                .thenReturn(listDouble);

        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        // Act
        ListOfItems result = assembler.toDomain(dmDouble);

        // Assert
        assertEquals(listDouble, result);
    }

    @Test
    void toDomainShouldPassSharedUntilToFactory() {
        // Arrange
        LocalDateTime sharedUntil = LocalDateTime.now().plusDays(7);
        ListOfItemsDataModel dmDouble = mock(ListOfItemsDataModel.class);
        ListOfItems listDouble = mock(ListOfItems.class);

        when(dmDouble.getListOfItemsId()).thenReturn("LOI-ABC123");
        when(dmDouble.getUserId()).thenReturn("user@mitelovers.com");
        when(dmDouble.getName()).thenReturn("My List");
        when(dmDouble.getGenreId()).thenReturn("FICTION");
        when(dmDouble.isPrivate()).thenReturn(false);
        when(dmDouble.getSharedUntil()).thenReturn(sharedUntil);
        when(dmDouble.getItemIds()).thenReturn(List.of());
        when(_factoryDouble.createListOfItems(
                any(ListOfItemsId.class), any(UserId.class),
                any(Name.class), any(GenreId.class),
                anyBoolean(), eq(sharedUntil)))
                .thenReturn(listDouble);

        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        // Act
        assembler.toDomain(dmDouble);

        // Assert
        verify(_factoryDouble).createListOfItems(
                any(ListOfItemsId.class), any(UserId.class),
                any(Name.class), any(GenreId.class),
                eq(false), eq(sharedUntil));
    }

    @Test
    void toDataModelListShouldReturnCorrectSize() {
        // Arrange
        ListOfItems listDouble2 = mock(ListOfItems.class);
        when(listDouble2.identity()).thenReturn(new ListOfItemsId("LOI-DEF456"));
        when(listDouble2.getUserId()).thenReturn(new UserId(new Email("user2@mitelovers.com")));
        when(listDouble2.getName()).thenReturn(new Name("My Second List"));
        when(listDouble2.getGenreId()).thenReturn(new GenreId("FICTION"));
        when(listDouble2.isPrivate()).thenReturn(true);
        when(listDouble2.getSharedUntil()).thenReturn(null);
        when(listDouble2.getItemIds()).thenReturn(List.of());

        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        // Act
        List<ListOfItemsDataModel> result = assembler.toDataModelList(List.of(_listDouble, listDouble2));

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void toDataModelListShouldReturnEmptyWhenInputIsEmpty() {
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        List<ListOfItemsDataModel> result = assembler.toDataModelList(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void toDomainListShouldReturnCorrectSizeAndItemIds() {
        // Arrange
        ListOfItemsDataModel dmDouble1 = mock(ListOfItemsDataModel.class);
        ListOfItemsDataModel dmDouble2 = mock(ListOfItemsDataModel.class);
        ListOfItems listDouble2 = mock(ListOfItems.class);

        when(dmDouble1.getListOfItemsId()).thenReturn("LOI-ABC123");
        when(dmDouble1.getUserId()).thenReturn("user@mitelovers.com");
        when(dmDouble1.getName()).thenReturn("My List");
        when(dmDouble1.getGenreId()).thenReturn("FICTION");
        when(dmDouble1.isPrivate()).thenReturn(true);
        when(dmDouble1.getSharedUntil()).thenReturn(null);
        when(dmDouble1.getItemIds()).thenReturn(List.of());

        when(dmDouble2.getListOfItemsId()).thenReturn("LOI-DEF456");
        when(dmDouble2.getUserId()).thenReturn("user@mitelovers.com");
        when(dmDouble2.getName()).thenReturn("My Second List");
        when(dmDouble2.getGenreId()).thenReturn("FICTION");
        when(dmDouble2.isPrivate()).thenReturn(true);
        when(dmDouble2.getSharedUntil()).thenReturn(null);
        when(dmDouble2.getItemIds()).thenReturn(List.of());

        when(_factoryDouble.createListOfItems(
                any(ListOfItemsId.class), any(UserId.class),
                any(Name.class), any(GenreId.class),
                anyBoolean(), isNull()))
                .thenReturn(_listDouble)
                .thenReturn(listDouble2);

        //SUT
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        // Act
        List<ListOfItems> result = assembler.toDomainList(List.of(dmDouble1, dmDouble2));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_listDouble));
        assertTrue(result.contains(listDouble2));
    }

    @Test
    void toDomainListShouldReturnEmptyWhenInputIsEmpty() {
        ListOfItemsAssembler assembler = new ListOfItemsAssembler(_factoryDouble);

        List<ListOfItems> result = assembler.toDomainList(List.of());

        assertTrue(result.isEmpty());
    }
}