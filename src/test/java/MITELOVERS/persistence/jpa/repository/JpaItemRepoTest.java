package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.assembler.ItemAssembler;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import MITELOVERS.persistence.springdata.IItemSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class JpaItemRepoTest {

    // SUT
    @InjectMocks
    private JpaItemRepo _jpaItemRepo;

    @Mock
    private IItemSpringDataRepo _itemSpringDataRepoDouble;

    @Mock
    private ItemAssembler _itemAssemblerDouble;

    @Mock
    private ItemDataModel _itemDataModelDouble;

    @Test
    void saveShouldReturnDomainItem() {

        //Arrange
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemAssemblerDouble.toDataModel(itemDouble)).thenReturn(itemDataModelDouble);
        when(_itemSpringDataRepoDouble.save(itemDataModelDouble)).thenReturn(itemDataModelDouble);
        when(_itemAssemblerDouble.toDomain(itemDataModelDouble)).thenReturn(itemDouble);

        //Act
        Item result = _jpaItemRepo.save(itemDouble);

        //Assert
        assertEquals(itemDouble, result);

    }

    @Test
    void findAllKeysShouldReturnListOfItemIds() {

        //Arrange
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of(itemDataModelDouble));
        when(itemDataModelDouble.getId()).thenReturn("ABC123DEF0");

        //Act
        List<ItemId> result = _jpaItemRepo.findAllKeys();

        //Assert
        assertEquals(1, result.size());

    }

    @Test
    void findAllKeysShouldReturnFirstItemId() {

        //Arrange
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of(itemDataModelDouble));
        when(itemDataModelDouble.getId()).thenReturn("ABC123DEF0");

        //Act
        List<ItemId> result = _jpaItemRepo.findAllKeys();
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

        //Act
        Iterable<Item> result = _jpaItemRepo.findAll();
        List<Item> resultList = new ArrayList<>();

        for (Item item : result) {
            resultList.add(item);
        }

        //Assert
        assertEquals(1, resultList.size());
        assertEquals(itemDouble, resultList.get(0));

    }

    @Test
    void ofIdentityShouldReturnItemOfACertainItemId() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDataModelDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findById(itemIdDouble.getValue())).thenReturn(Optional.of(itemDataModelDouble));
        when(_itemAssemblerDouble.toDomain(itemDataModelDouble)).thenReturn(itemDouble);

        //Act
        Optional<Item> result = _jpaItemRepo.ofIdentity(itemIdDouble);

        //Assert
        assertEquals(itemDouble, result.get());

    }

    @Test
    void ofIdentityShouldReturnEmptyWhenItemDoesNotExist() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(_itemSpringDataRepoDouble.findById(itemIdDouble.getValue())).thenReturn(Optional.empty());

        //Act
        Optional<Item> result = _jpaItemRepo.ofIdentity(itemIdDouble);

        //Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenItemExists() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(_itemSpringDataRepoDouble.existsById(itemIdDouble.toString())).thenReturn(true);

        //Act
        boolean result = _jpaItemRepo.containsOfIdentity(itemIdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenItemDoesNotExist() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(_itemSpringDataRepoDouble.existsById(itemIdDouble.toString())).thenReturn(false);

        //Act
        boolean result = _jpaItemRepo.containsOfIdentity(itemIdDouble);

        //Assert
        assertFalse(result);

    }

}