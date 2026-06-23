package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    static final String EDITION_NOT_FOUND     = "Edition does not exist in the repository";
    static final String ITEM_NOT_FOUND        = "Item does not exist in the repository";
    static final String PUBLICATION_NOT_FOUND = "Publication does not exist in the repository";
    static final String AUTHOR_NOT_FOUND      = "Author does not exist in the repository";
    static final String GENRE_NOT_FOUND       = "Genre does not exist in the repository";
    static final String PUBLISHER_NOT_FOUND   = "PublishingCompany does not exist in the repository";

    IItemRepo              itemRepoDouble;
    ItemFactory            itemFactoryDouble;
    IEditionRepo           editionRepoDouble;
    IPublicationRepo       publicationRepoDouble;
    IAuthorRepo            authorRepoDouble;
    IGenreRepo             genreRepoDouble;
    IPublishingCompanyRepo publishingCompanyRepoDouble;
    ItemService            itemService;

    @BeforeEach
    void setUp() {
        itemRepoDouble              = mock(IItemRepo.class);
        itemFactoryDouble           = mock(ItemFactory.class);
        editionRepoDouble           = mock(IEditionRepo.class);
        publicationRepoDouble       = mock(IPublicationRepo.class);
        authorRepoDouble            = mock(IAuthorRepo.class);
        genreRepoDouble             = mock(IGenreRepo.class);
        publishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);

        itemService = new ItemService(
                itemRepoDouble,
                itemFactoryDouble,
                editionRepoDouble,
                publicationRepoDouble,
                authorRepoDouble,
                genreRepoDouble,
                publishingCompanyRepoDouble
        );
    }

    // ----------------------------------------------------------------
    // Constructor null checks
    // ----------------------------------------------------------------

    @Test
    void constructorWithNullItemRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(null, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, publishingCompanyRepoDouble));
    }

    @Test
    void constructorWithNullItemFactoryThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, null, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, publishingCompanyRepoDouble));
    }

    @Test
    void constructorWithNullEditionRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, null,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, publishingCompanyRepoDouble));
    }

    @Test
    void constructorWithNullPublicationRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        null, authorRepoDouble, genreRepoDouble, publishingCompanyRepoDouble));
    }

    @Test
    void constructorWithNullAuthorRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, null, genreRepoDouble, publishingCompanyRepoDouble));
    }

    @Test
    void constructorWithNullGenreRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, null, publishingCompanyRepoDouble));
    }

    @Test
    void constructorWithNullPublishingCompanyRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, null));
    }

    // ----------------------------------------------------------------
    // registerItem
    // ----------------------------------------------------------------

    @Test
    void registerItemEditionNotFoundThrowsNoSuchElementException() {
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(mock(EditionId.class), Condition.GOOD, new Description("copy")));

        assertEquals(EDITION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void registerItemValidRequestReturnsItem() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Item newItemDouble        = mock(Item.class);
        ItemId itemIdDouble       = mock(ItemId.class);
        Item savedItemDouble      = mock(Item.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(mock(Edition.class)));
        when(itemFactoryDouble.createItem(any(), any(), any())).thenReturn(newItemDouble);
        when(newItemDouble.identity()).thenReturn(itemIdDouble);
        when(itemRepoDouble.containsOfIdentity(itemIdDouble)).thenReturn(false);
        when(itemRepoDouble.save(newItemDouble)).thenReturn(savedItemDouble);

        Item result = itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("Nice copy"));

        // Assert
        assertSame(savedItemDouble, result);
        verify(itemRepoDouble).save(newItemDouble);
    }

    @Test
    void registerItemContainsItemButOfIdentityEmptyThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Item newItemDouble        = mock(Item.class);
        ItemId itemIdDouble       = mock(ItemId.class);

        when(itemIdDouble.toString()).thenReturn("TEST-ITEM-ID");
        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(mock(Edition.class)));
        when(itemFactoryDouble.createItem(any(), any(), any())).thenReturn(newItemDouble);
        when(newItemDouble.identity()).thenReturn(itemIdDouble);
        when(itemRepoDouble.containsOfIdentity(itemIdDouble)).thenReturn(true);
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("copy")));

        // Assert
        assertEquals("Item with id 'TEST-ITEM-ID' does not exist", ex.getMessage());
    }

    @Test
    void registerItemAlreadyExistsReturnsExistingItem() {
        EditionId editionIdDouble = mock(EditionId.class);
        Item newItemDouble        = mock(Item.class);
        ItemId itemIdDouble       = mock(ItemId.class);
        Item existingItemDouble   = mock(Item.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(mock(Edition.class)));
        when(itemFactoryDouble.createItem(any(), any(), any())).thenReturn(newItemDouble);
        when(newItemDouble.identity()).thenReturn(itemIdDouble);
        when(itemRepoDouble.containsOfIdentity(itemIdDouble)).thenReturn(true);
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(existingItemDouble));

        Item result = itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("copy"));

        assertSame(existingItemDouble, result);
    }

    // ----------------------------------------------------------------
    // getAllItems
    // ----------------------------------------------------------------

    @Test
    void getAllItemsEmptyReturnsEmptyList() {
        when(itemRepoDouble.findAll()).thenReturn(List.of());

        List<Item> result = itemService.getAllItems();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllItemsReturnsListOfItems() {
        Item itemDouble = mock(Item.class);
        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));

        List<Item> result = itemService.getAllItems();

        assertEquals(1, result.size());
        assertSame(itemDouble, result.get(0));
    }

    // ----------------------------------------------------------------
    // getItemById
    // ----------------------------------------------------------------

    @Test
    void getItemByIdItemNotFoundThrowsNoSuchElementException() {
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.getItemById("3C5D126F8B"));

        assertEquals(ITEM_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getItemByIdValidIdReturnsItem() {
        Item itemDouble = mock(Item.class);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));

        Item result = itemService.getItemById("3C5D126F8B");

        assertSame(itemDouble, result);
    }

    @Test
    void resolveRelatedReturnsAllRelatedEntities() {
        Item item               = mock(Item.class);
        Edition edition         = mock(Edition.class);
        Publication publication = mock(Publication.class);
        Author author           = mock(Author.class);
        Genre genre             = mock(Genre.class);
        PublishingCompany pub   = mock(PublishingCompany.class);

        when(item.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock());
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.getAuthorId()).thenReturn(mock());
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(author));
        when(publication.getGenreId()).thenReturn(mock());
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genre));
        when(edition.getPublishingCompanyId()).thenReturn(mock());
        when(publishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.of(pub));

        ItemService.ItemRelated result = itemService.resolveRelated(item);

        assertSame(edition,     result.edition());
        assertSame(publication, result.publication());
        assertSame(author,      result.author());
        assertSame(genre,       result.genre());
        assertSame(pub,         result.publisher());
    }

    @Test
    void resolveRelatedEditionNotFoundThrowsNoSuchElementException() {
        Item item = mock(Item.class);
        when(item.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.resolveRelated(item));

        assertEquals(EDITION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void resolveRelatedPublicationNotFoundThrowsNoSuchElementException() {
        Item item       = mock(Item.class);
        Edition edition = mock(Edition.class);

        when(item.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock());
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.resolveRelated(item));

        assertEquals(PUBLICATION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void resolveRelatedAuthorNotFoundThrowsNoSuchElementException() {
        Item item               = mock(Item.class);
        Edition edition         = mock(Edition.class);
        Publication publication = mock(Publication.class);

        when(item.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock());
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.getAuthorId()).thenReturn(mock());
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.resolveRelated(item));

        assertEquals(AUTHOR_NOT_FOUND, ex.getMessage());
    }

    @Test
    void resolveRelatedGenreNotFoundThrowsNoSuchElementException() {
        Item item               = mock(Item.class);
        Edition edition         = mock(Edition.class);
        Publication publication = mock(Publication.class);
        Author author           = mock(Author.class);

        when(item.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock());
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.getAuthorId()).thenReturn(mock());
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(author));
        when(publication.getGenreId()).thenReturn(mock());
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.resolveRelated(item));

        assertEquals(GENRE_NOT_FOUND, ex.getMessage());
    }

    @Test
    void resolveRelatedPublisherNotFoundThrowsNoSuchElementException() {
        Item item               = mock(Item.class);
        Edition edition         = mock(Edition.class);
        Publication publication = mock(Publication.class);
        Author author           = mock(Author.class);
        Genre genre             = mock(Genre.class);

        when(item.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock());
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.getAuthorId()).thenReturn(mock());
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(author));
        when(publication.getGenreId()).thenReturn(mock());
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genre));
        when(edition.getPublishingCompanyId()).thenReturn(mock());
        when(publishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.resolveRelated(item));

        assertEquals(PUBLISHER_NOT_FOUND, ex.getMessage());
    }

    @Test
    void markItemAsSoldReturnsSavedItem() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Item savedItemDouble = mock(Item.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(itemRepoDouble.save(itemDouble)).thenReturn(savedItemDouble);

        // Act
        Item result = itemService.markItemAsSold("3C5D126F8B");

        // Assert
        assertSame(savedItemDouble, result);
    }

    @Test
    void markItemAsSoldThrowsWhenItemNotFound() {
        // Arrange
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.markItemAsSold("3C5D126F8B"));

        assertEquals(ITEM_NOT_FOUND, ex.getMessage());
    }
}