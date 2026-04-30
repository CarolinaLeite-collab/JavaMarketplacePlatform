package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.assembler.ItemAssembler;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import MITELOVERS.persistence.springdata.IItemSpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaItemRepoTest {

    private IItemSpringDataRepo _itemSpringDataRepoDouble;
    private ItemAssembler _itemAssemblerDouble;

    @BeforeEach
    void setUp() {

        _itemSpringDataRepoDouble = mock(IItemSpringDataRepo.class);
        _itemAssemblerDouble = mock(ItemAssembler.class);

    }

    @Test
    void testConstructor() {

        //Act + SUT
        new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

    }

    @Test
    void saveShouldReturnDomainItem() {

        //Arrange
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemAssemblerDouble.toDataModel(itemDouble)).thenReturn(itemDataModelDouble);
        when(_itemSpringDataRepoDouble.save(itemDataModelDouble)).thenReturn(itemDataModelDouble);
        when(_itemAssemblerDouble.toDomain(itemDataModelDouble)).thenReturn(itemDouble);

        //SUT
        JpaItemRepo repo =  new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act
        Item result = repo.save(itemDouble);

        //Assert
        assertEquals(itemDouble, result);
        verify(_itemAssemblerDouble).toDataModel(itemDouble);
        verify(_itemSpringDataRepoDouble).save(itemDataModelDouble);
        verify(_itemAssemblerDouble).toDomain(itemDataModelDouble);
        verifyNoMoreInteractions(_itemAssemblerDouble, _itemSpringDataRepoDouble);
    }

    @Test
    void findAllKeysShouldReturnListOfItemIds() {

        //Arrange
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of(itemDataModelDouble));
        when(itemDataModelDouble.getId()).thenReturn("ABC123DEF0");

        //SUT
        JpaItemRepo repo = new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act
        List<ItemId> result = repo.findAllKeys();

        //Assert
        assertEquals(1, result.size());

        verify(_itemSpringDataRepoDouble, times(1)).findAll();
        verifyNoMoreInteractions(_itemSpringDataRepoDouble);
        verifyNoInteractions(_itemAssemblerDouble);
    }

    @Test
    void findAllKeysShouldReturnFirstItemId() {

        //Arrange
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of(itemDataModelDouble));
        when(itemDataModelDouble.getId()).thenReturn("ABC123DEF0");

        //SUT
        JpaItemRepo repo = new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act
        List<ItemId> result = repo.findAllKeys();
        ItemId firstItemId = result.get(0);

        //Assert
        assertEquals("ABC123DEF0", firstItemId.getValue());

    }

    @Test
    void findAllShouldReturnListOfSavedItems() {

        //Arrange
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of(itemDataModelDouble));
        when(_itemAssemblerDouble.toDomain(itemDataModelDouble)).thenReturn(itemDouble);

        //SUT
        JpaItemRepo repo = new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act
        Iterable<Item> result = repo.findAll();
        List<Item> resultList = new ArrayList<>();

        for (Item item : result) {
            resultList.add(item);
        }

        //Assert
        assertEquals(1, resultList.size());
        assertEquals(itemDouble, resultList.get(0));
        verify(_itemSpringDataRepoDouble, times(1)).findAll();

    }

    @Test
    void ofIdentityShouldReturnItemOfACertainItemId() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findById(itemIdDouble.getValue())).thenReturn(Optional.of(itemDataModelDouble));
        when(_itemAssemblerDouble.toDomain(itemDataModelDouble)).thenReturn(itemDouble);

        //SUT
        JpaItemRepo repo = new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act
        Optional<Item> result = repo.ofIdentity(itemIdDouble);

        //Assert
        assertEquals(itemDouble, result.get());

    }

    @Test
    void ofIdentityShouldThrowWhenItemDoesNotExist() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(_itemSpringDataRepoDouble.existsById(itemIdDouble.getValue())).thenReturn(true);

        //SUT
        JpaItemRepo repo = new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act + Assert
        assertThrows(IllegalArgumentException.class , () -> repo.ofIdentity(itemIdDouble));

    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenItemExists() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(_itemSpringDataRepoDouble.existsById(itemIdDouble.toString())).thenReturn(true);

        //SUT
        JpaItemRepo repo = new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act
        boolean result = repo.containsOfIdentity(itemIdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenItemDoesNotExist() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(_itemSpringDataRepoDouble.existsById(itemIdDouble.toString())).thenReturn(false);

        //SUT
        JpaItemRepo repo = new JpaItemRepo(_itemSpringDataRepoDouble, _itemAssemblerDouble);

        //Act
        boolean result = repo.containsOfIdentity(itemIdDouble);

        //Assert
        assertFalse(result);

    }

}