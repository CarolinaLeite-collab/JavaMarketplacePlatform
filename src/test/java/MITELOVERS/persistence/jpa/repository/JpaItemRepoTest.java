package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.GenreId;
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

    // ------------------------------------------------------------
    // save
    // ------------------------------------------------------------

    @Test
    void saveShouldReturnDomainItem() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDmDouble = mock(ItemDataModel.class);

        when(_itemAssemblerDouble.toDataModel(itemDouble)).thenReturn(itemDmDouble);
        when(_itemSpringDataRepoDouble.save(itemDmDouble)).thenReturn(itemDmDouble);
        when(_itemAssemblerDouble.toDomain(itemDmDouble)).thenReturn(itemDouble);

        // Act
        Item result = _jpaItemRepo.save(itemDouble);

        // Assert
        assertSame(itemDouble, result);
    }

    // ------------------------------------------------------------
    // findAllKeys
    // ------------------------------------------------------------

    @Test
    void findAllKeysShouldReturnListOfItemIds() {

        // Arrange
        ItemDataModel itemDmDouble = mock(ItemDataModel.class);
        when(itemDmDouble.getId()).thenReturn("ABC123DEF0");
        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of(itemDmDouble));

        // Act
        List<ItemId> result = _jpaItemRepo.findAllKeys();

        // Assert
        assertEquals(1, result.size());
        assertEquals("ABC123DEF0", result.get(0).getValue());
    }

    @Test
    void findAllKeysShouldReturnEmptyListWhenNoItemsExist() {

        // Arrange
        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<ItemId> result = _jpaItemRepo.findAllKeys();

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // findAll
    // ------------------------------------------------------------

    @Test
    void findAllShouldReturnListOfDomainItems() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDmDouble = mock(ItemDataModel.class);

        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of(itemDmDouble));
        when(_itemAssemblerDouble.toDomain(itemDmDouble)).thenReturn(itemDouble);

        // Act
        Iterable<Item> result = _jpaItemRepo.findAll();
        List<Item> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        // Assert
        assertEquals(1, resultList.size());
        assertSame(itemDouble, resultList.get(0));
    }

    @Test
    void findAllShouldReturnEmptyIterableWhenNoItemsExist() {

        // Arrange
        when(_itemSpringDataRepoDouble.findAll()).thenReturn(List.of());

        // Act
        Iterable<Item> result = _jpaItemRepo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    // ------------------------------------------------------------
    // ofIdentity
    // ------------------------------------------------------------

    @Test
    void ofIdentityShouldReturnDomainItemWhenExists() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        ItemDataModel itemDmDouble = mock(ItemDataModel.class);

        when(itemIdDouble.getValue()).thenReturn("XYZ");
        when(_itemSpringDataRepoDouble.findById("XYZ")).thenReturn(Optional.of(itemDmDouble));
        when(_itemAssemblerDouble.toDomain(itemDmDouble)).thenReturn(itemDouble);

        // Act
        Optional<Item> result = _jpaItemRepo.ofIdentity(itemIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(itemDouble, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyOptionalWhenItemDoesNotExist() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.getValue()).thenReturn("XYZ");
        when(_itemSpringDataRepoDouble.findById("XYZ")).thenReturn(Optional.empty());

        // Act
        Optional<Item> result = _jpaItemRepo.ofIdentity(itemIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // containsOfIdentity
    // ------------------------------------------------------------

    @Test
    void containsOfIdentityShouldReturnTrueWhenItemExists() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("ID123");
        when(_itemSpringDataRepoDouble.existsById("ID123")).thenReturn(true);

        // Act
        boolean result = _jpaItemRepo.containsOfIdentity(itemIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenItemDoesNotExist() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("ID123");
        when(_itemSpringDataRepoDouble.existsById("ID123")).thenReturn(false);

        // Act
        boolean result = _jpaItemRepo.containsOfIdentity(itemIdDouble);

        // Assert
        assertFalse(result);
    }

    // ------------------------------------------------------------
    // findByIdInOrderByDescriptionAsc
    // ------------------------------------------------------------

    @Test
    void findByIdInOrderByDescriptionAscShouldReturnMappedDomainItems() {

        // Arrange
        ItemDataModel dm1 = mock(ItemDataModel.class);
        ItemDataModel dm2 = mock(ItemDataModel.class);

        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(_itemSpringDataRepoDouble.findByIdInOrderByDescriptionAsc(List.of("A", "B")))
                .thenReturn(List.of(dm1, dm2));

        when(_itemAssemblerDouble.toDomain(dm1)).thenReturn(item1);
        when(_itemAssemblerDouble.toDomain(dm2)).thenReturn(item2);

        // Act
        List<Item> result = _jpaItemRepo.findByIdInOrderByDescriptionAsc(List.of("A", "B"));

        // Assert
        assertEquals(2, result.size());
        assertSame(item1, result.get(0));
        assertSame(item2, result.get(1));
    }

    @Test
    void findByIdInOrderByDescriptionAscShouldReturnEmptyListWhenNoMatches() {

        // Arrange
        when(_itemSpringDataRepoDouble.findByIdInOrderByDescriptionAsc(List.of("A", "B")))
                .thenReturn(List.of());

        // Act
        List<Item> result = _jpaItemRepo.findByIdInOrderByDescriptionAsc(List.of("A", "B"));

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // findByGenreId
    // ------------------------------------------------------------

    @Test
    void findByGenreIdShouldReturnMappedItemIds() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.toString()).thenReturn("GENRE123");

        when(_itemSpringDataRepoDouble.findItemIdsByGenre("GENRE123"))
                .thenReturn(List.of("ID1", "ID2"));

        // Act
        List<ItemId> result = _jpaItemRepo.findByGenreId(genreIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertEquals("ID1", result.get(0).getValue());
        assertEquals("ID2", result.get(1).getValue());
    }

    @Test
    void findByGenreIdShouldReturnEmptyListWhenNoItemsMatchGenre() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.toString()).thenReturn("GENRE123");

        when(_itemSpringDataRepoDouble.findItemIdsByGenre("GENRE123"))
                .thenReturn(List.of());

        // Act
        List<ItemId> result = _jpaItemRepo.findByGenreId(genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

}