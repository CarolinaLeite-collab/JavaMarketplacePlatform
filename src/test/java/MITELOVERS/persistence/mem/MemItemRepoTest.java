package MITELOVERS.persistence.mem;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublicationId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemItemRepoTest {

    private IEditionRepo _iEditionRepoDouble;
    private IPublicationRepo _iPublicationRepoDouble;

    @BeforeEach
    void setup() {
        _iEditionRepoDouble = mock(IEditionRepo.class);
        _iPublicationRepoDouble = mock(IPublicationRepo.class);
    }

    // ------------------------------------------------------------
    // save
    // ------------------------------------------------------------

    @Test
    void saveValidItemReturnsSameItem() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

        // Act
        Item result = sut.save(itemDouble);

        // Assert
        assertSame(itemDouble, result);
    }

    @Test
    void saveValidItemStoresItemInRepository() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

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
    void findAllEmptyRepositoryReturnsEmptyIterable() {

        // SUT + Arrange
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

        // Act
        Iterable<Item> result = sut.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void findAllRepositoryWithItemsReturnsStoredItems() {

        // Arrange
        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
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
    void ofIdentityExistingIdReturnsItemWrappedInOptional() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(itemDouble);

        // Act
        Optional<Item> result = sut.ofIdentity(itemIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(itemDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingIdReturnsEmptyOptional() {

        // Arrange
        ItemId unknownIdDouble = mock(ItemId.class);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

        // Act
        Optional<Item> result = sut.ofIdentity(unknownIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ofIdentityNullIdReturnsEmptyOptional() {

        // SUT + Arrange
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

        // Act
        Optional<Item> result = sut.ofIdentity(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // containsOfIdentity
    // ------------------------------------------------------------

    @Test
    void containsOfIdentityExistingIdReturnsTrue() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(itemDouble);

        // Act
        boolean result = sut.containsOfIdentity(itemIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingIdReturnsFalse() {

        // Arrange
        ItemId unknownIdDouble = mock(ItemId.class);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

        // Act
        boolean result = sut.containsOfIdentity(unknownIdDouble);

        // Assert
        assertFalse(result);
    }

    // ------------------------------------------------------------
    // findAllKeys
    // ------------------------------------------------------------

    @Test
    void findAllKeysShouldReturnEmptyListWhenRepoIsEmpty() {

        // SUT + Arrange
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

        // Act
        List<ItemId> result = sut.findAllKeys();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeysWhenRepoHasMultipleItems() {

        // Arrange
        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(item1Double);
        sut.save(item2Double);

        // Act
        List<ItemId> result = sut.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(itemId1Double));
        assertTrue(result.contains(itemId2Double));
    }

    @Test
    void findAllKeysShouldReturnIndependentList() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.findAllKeys();
        result.clear();
        List<ItemId> newResult = sut.findAllKeys();

        // Assert
        assertEquals(1, newResult.size());
        assertTrue(newResult.contains(itemIdDouble));
    }

    // ------------------------------------------------------------
    // findByIdInOrderByDescriptionAsc
    // ------------------------------------------------------------

    @Test
    void findByIdInOrderByDescriptionAscAlwaysThrowsUnsupportedOperationException() {

        // SUT + Arrange
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);

        // Act + Assert
        assertThrows(UnsupportedOperationException.class,
                () -> sut.findByIdInOrderByDescriptionAsc(List.of("a", "b")));
    }

    // ------------------------------------------------------------
    // findByGenreId
    // ------------------------------------------------------------

    @Test
    void findByGenreIdReturnsMatchingItems() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getPublicationId()).thenReturn(publicationIdDouble);

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.isByGenreId(genreIdDouble)).thenReturn(true);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble)).thenReturn(Optional.of(publicationDouble));

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.findByGenreId(genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(itemIdDouble));
    }

    @Test
    void findByGenreIdSkipsItemsNotMatchingGenre() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getPublicationId()).thenReturn(publicationIdDouble);

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.isByGenreId(genreIdDouble)).thenReturn(false);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble)).thenReturn(Optional.of(publicationDouble));

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.findByGenreId(genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByGenreIdThrowsWhenEditionMissing() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);

        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.empty());

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(itemDouble);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> sut.findByGenreId(genreIdDouble));
    }

    @Test
    void findByGenreIdThrowsWhenPublicationMissing() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getPublicationId()).thenReturn(publicationIdDouble);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble)).thenReturn(Optional.empty());

        // SUT
        MemItemRepo sut = new MemItemRepo(_iEditionRepoDouble, _iPublicationRepoDouble);
        sut.save(itemDouble);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> sut.findByGenreId(genreIdDouble));
    }

}
