package TOPSECRET.domain;

import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.Description;
import TOPSECRET.domain.valueobject.EditionId;
import TOPSECRET.domain.valueobject.ItemId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoItemRepoTest {
        // ------------------------------------------------------------
        // save
        // ------------------------------------------------------------

        @Test
        void save_validItem_returnsSameItem() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);
            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            Item result = sut.save(itemDouble);

            // Assert
            assertSame(itemDouble, result);
        }

        @Test
        void save_validItem_storesItemInRepository() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);
            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            sut.save(itemDouble);
            Optional<Item> result = sut.ofIdentity(itemIdDouble);

            // Assert
            assertTrue(result.isPresent());
            assertSame(itemDouble, result.get());
        }

        // ------------------------------------------------------------
        // findAll
        // ------------------------------------------------------------

        @Test
        void findAll_emptyRepository_returnsEmptyIterable() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            Iterable<Item> result = sut.findAll();

            // Assert
            assertFalse(result.iterator().hasNext());
        }

        @Test
        void findAll_repositoryWithItems_returnsStoredItems() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item item1Double = mock(Item.class);
            ItemId itemId1Double = mock(ItemId.class);
            when(item1Double.identity()).thenReturn(itemId1Double);

            Item item2Double = mock(Item.class);
            ItemId itemId2Double = mock(ItemId.class);
            when(item2Double.identity()).thenReturn(itemId2Double);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(item1Double);
            sut.save(item2Double);

            // Act
            List<Item> result = new ArrayList<>();
            sut.findAll().forEach(result::add);

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.contains(item1Double));
            assertTrue(result.contains(item2Double));
        }

        // ------------------------------------------------------------
        // ofIdentity
        // ------------------------------------------------------------

        @Test
        void ofIdentity_existingId_returnsItemWrappedInOptional() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(itemDouble);

            // Act
            Optional<Item> result = sut.ofIdentity(itemIdDouble);

            // Assert
            assertTrue(result.isPresent());
            assertSame(itemDouble, result.get());
        }

        @Test
        void ofIdentity_nonExistingId_returnsEmptyOptional() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);
            ItemId unknownIdDouble = mock(ItemId.class);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            Optional<Item> result = sut.ofIdentity(unknownIdDouble);

            // Assert
            assertTrue(result.isEmpty());
        }

        // ------------------------------------------------------------
        // containsOfIdentity
        // ------------------------------------------------------------

        @Test
        void containsOfIdentity_existingId_returnsTrue() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(itemDouble);

            // Act
            boolean result = sut.containsOfIdentity(itemIdDouble);

            // Assert
            assertTrue(result);
        }

        @Test
        void containsOfIdentity_nonExistingId_returnsFalse() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);
            ItemId unknownIdDouble = mock(ItemId.class);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            boolean result = sut.containsOfIdentity(unknownIdDouble);

            // Assert
            assertFalse(result);
        }

        // ------------------------------------------------------------
        // addItem
        // ------------------------------------------------------------

        @Test
        void addItem_validArguments_usesFactoryToCreateItem() {
            // Arrange
            EditionId editionIdDouble = mock(EditionId.class);
            Condition conditionDouble = mock(Condition.class);
            Description descriptionDouble = mock(Description.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            ItemFactory factoryDouble = mock(ItemFactory.class);
            when(factoryDouble.createItem(editionIdDouble, conditionDouble, descriptionDouble))
                    .thenReturn(itemDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            sut.addItem(editionIdDouble, conditionDouble, descriptionDouble);

            // Assert
            verify(factoryDouble).createItem(editionIdDouble, conditionDouble, descriptionDouble);
        }

        @Test
        void addItem_validArguments_returnsCreatedItem() {
            // Arrange
            EditionId editionIdDouble = mock(EditionId.class);
            Condition conditionDouble = mock(Condition.class);
            Description descriptionDouble = mock(Description.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            ItemFactory factoryDouble = mock(ItemFactory.class);
            when(factoryDouble.createItem(editionIdDouble, conditionDouble, descriptionDouble))
                    .thenReturn(itemDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            Item result = sut.addItem(editionIdDouble, conditionDouble, descriptionDouble);

            // Assert
            assertSame(itemDouble, result);
        }

        @Test
        void addItem_validArguments_storesCreatedItemInRepository() {
            // Arrange
            EditionId editionIdDouble = mock(EditionId.class);
            Condition conditionDouble = mock(Condition.class);
            Description descriptionDouble = mock(Description.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            ItemFactory factoryDouble = mock(ItemFactory.class);
            when(factoryDouble.createItem(editionIdDouble, conditionDouble, descriptionDouble))
                    .thenReturn(itemDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);

            // Act
            sut.addItem(editionIdDouble, conditionDouble, descriptionDouble);

            // Assert
            assertTrue(sut.containsOfIdentity(itemIdDouble));
            assertEquals(Optional.of(itemDouble), sut.ofIdentity(itemIdDouble));
        }

        // ------------------------------------------------------------
        // getDifferentOf
        // ------------------------------------------------------------

        @Test
        void getDifferentOf_nullList_returnsAllStoredItems() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item item1Double = mock(Item.class);
            ItemId itemId1Double = mock(ItemId.class);
            when(item1Double.identity()).thenReturn(itemId1Double);

            Item item2Double = mock(Item.class);
            ItemId itemId2Double = mock(ItemId.class);
            when(item2Double.identity()).thenReturn(itemId2Double);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(item1Double);
            sut.save(item2Double);

            // Act
            List<Item> result = sut.getDifferentOf(null);

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.contains(item1Double));
            assertTrue(result.contains(item2Double));
        }

        @Test
        void getDifferentOf_emptyList_returnsAllStoredItems() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(itemDouble);

            // Act
            List<Item> result = sut.getDifferentOf(List.of());

            // Assert
            assertEquals(1, result.size());
            assertTrue(result.contains(itemDouble));
        }

        @Test
        void getDifferentOf_allItemsAlreadyExist_returnsEmptyList() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(itemDouble);

            // Act
            List<Item> result = sut.getDifferentOf(List.of(itemDouble));

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void getDifferentOf_someItemsAlreadyExist_returnsOnlyDifferentItems() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item item1Double = mock(Item.class);
            ItemId itemId1Double = mock(ItemId.class);
            when(item1Double.identity()).thenReturn(itemId1Double);

            Item item2Double = mock(Item.class);
            ItemId itemId2Double = mock(ItemId.class);
            when(item2Double.identity()).thenReturn(itemId2Double);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(item1Double);
            sut.save(item2Double);

            // Act
            List<Item> existentItems = List.of(item1Double);
            List<Item> result = sut.getDifferentOf(existentItems);

            // Assert
            assertEquals(List.of(item2Double), result);
        }

        @Test
        void getDifferentOf_resultIsUnmodifiable() {
            // Arrange
            ItemFactory factoryDouble = mock(ItemFactory.class);

            Item itemDouble = mock(Item.class);
            ItemId itemIdDouble = mock(ItemId.class);
            when(itemDouble.identity()).thenReturn(itemIdDouble);

            // SUT
            MemoItemRepo sut = new MemoItemRepo(factoryDouble);
            sut.save(itemDouble);

            // Act
            List<Item> result = sut.getDifferentOf(List.of());

            // Assert
            assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Item.class)));
        }
    }
