package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibrarySort;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.NoIdentifier;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryServiceTest {

    ILibraryRepo libraryRepoDouble;
    IItemRepo itemRepoDouble;
    IEditionRepo editionRepoDouble;
    IPublicationRepo publicationRepoDouble;
    IAuthorRepo authorRepoDouble;
    IPublicationTypeRepo publicationTypeRepoDouble;
    LibraryService libraryService;
    LibraryFactory libraryFactory;

    @BeforeEach
    void setUp() {
        libraryRepoDouble = mock(ILibraryRepo.class);
        itemRepoDouble = mock(IItemRepo.class);
        editionRepoDouble = mock(IEditionRepo.class);
        publicationRepoDouble = mock(IPublicationRepo.class);
        authorRepoDouble = mock(IAuthorRepo.class);
        publicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        libraryFactory = mock(LibraryFactory.class);

        libraryService = new LibraryService(
                libraryRepoDouble, itemRepoDouble, editionRepoDouble,
                publicationRepoDouble, authorRepoDouble, publicationTypeRepoDouble,
                libraryFactory);
    }

    // ----------------------------------------------------------------
    // getListOfItemInfoInMyLibrary
    // ----------------------------------------------------------------

    @Test
    void testGetListOfItemInfoInMyLibraryNoLibraryReturnsEmptyList() {
        // Arrange
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        List<LibraryItemDetails> result = libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetListOfItemInfoInMyLibraryEmptyLibraryReturnsEmptyList() {
        // Arrange
        Library libraryDouble = mock(Library.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

        // Act
        List<LibraryItemDetails> result = libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetListOfItemInfoInMyLibraryValidUserReturnsListWithOneItem() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));

        // Act
        List<LibraryItemDetails> result = libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                new LibraryItemDetails(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble),
                result.get(0));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryItemNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryEditionNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryPublicationNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryAuthorNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemId = mock(ItemId.class);
        Library library = mock(Library.class);
        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(library));
        when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));
        when(itemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryPublicationTypeNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemId = mock(ItemId.class);
        Library library = mock(Library.class);
        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);
        Author author = mock(Author.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(library));
        when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));
        when(itemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(author));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com"));
    }

    // ----------------------------------------------------------------
    // sorting
    // ----------------------------------------------------------------

    @Test
    void testGetListShouldSortByTitle() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Edition edition1 = mock(Edition.class);
        Edition edition2 = mock(Edition.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        Publication zuluPublication = mock(Publication.class);
        Title zuluTitle = mock(Title.class);
        when(zuluTitle.toString()).thenReturn("Zulu");
        when(zuluPublication.getTitle()).thenReturn(zuluTitle);

        Publication alphaPublication = mock(Publication.class);
        Title alphaTitle = mock(Title.class);
        when(alphaTitle.toString()).thenReturn("Alpha");
        when(alphaPublication.getTitle()).thenReturn(alphaTitle);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(id1)).thenReturn(Optional.of(item1));
        when(itemRepoDouble.ofIdentity(id2)).thenReturn(Optional.of(item2));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition1), Optional.of(edition2));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(zuluPublication), Optional.of(alphaPublication));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));

        // Act
        List<LibraryItemDetails> result =
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble, LibrarySort.TITLE);

        // Assert
        assertEquals("Alpha", result.get(0).publication().getTitle().toString());
        assertEquals("Zulu", result.get(1).publication().getTitle().toString());
    }

    @Test
    void testGetListShouldSortByAuthor() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationType typeDouble = mock(PublicationType.class);

        Author zuluAuthor = mock(Author.class);
        Name zuluName = mock(Name.class);
        when(zuluName.toString()).thenReturn("Zulu");
        when(zuluAuthor.getName()).thenReturn(zuluName);

        Author alphaAuthor = mock(Author.class);
        Name alphaName = mock(Name.class);
        when(alphaName.toString()).thenReturn("Alpha");
        when(alphaAuthor.getName()).thenReturn(alphaName);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(id1)).thenReturn(Optional.of(item1));
        when(itemRepoDouble.ofIdentity(id2)).thenReturn(Optional.of(item2));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(zuluAuthor), Optional.of(alphaAuthor));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));

        // Act
        List<LibraryItemDetails> result =
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble, LibrarySort.AUTHOR);

        // Assert
        assertEquals("Alpha", result.get(0).author().getName().toString());
        assertEquals("Zulu", result.get(1).author().getName().toString());
    }

    @Test
    void testGetListShouldSortByPublicationType() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);

        PublicationType zineType = mock(PublicationType.class);
        when(zineType.toString()).thenReturn("Zine");
        PublicationType bookType = mock(PublicationType.class);
        when(bookType.toString()).thenReturn("Book");

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(id1)).thenReturn(Optional.of(item1));
        when(itemRepoDouble.ofIdentity(id2)).thenReturn(Optional.of(item2));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(zineType), Optional.of(bookType));

        // Act
        List<LibraryItemDetails> result =
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble, LibrarySort.PUBLICATION_TYPE);

        // Assert
        assertEquals("Book", result.get(0).publicationType().toString());
        assertEquals("Zine", result.get(1).publicationType().toString());
    }

    @Test
    void testGetListShouldSortByIdentifier() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        Edition edition999 = mock(Edition.class);
        NoIdentifier id999 = mock(NoIdentifier.class);
        when(id999.toString()).thenReturn("999");
        when(edition999.getIdentifier()).thenReturn(id999);

        Edition edition111 = mock(Edition.class);
        NoIdentifier id111 = mock(NoIdentifier.class);
        when(id111.toString()).thenReturn("111");
        when(edition111.getIdentifier()).thenReturn(id111);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(id1)).thenReturn(Optional.of(item1));
        when(itemRepoDouble.ofIdentity(id2)).thenReturn(Optional.of(item2));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition999), Optional.of(edition111));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));

        // Act
        List<LibraryItemDetails> result =
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble, LibrarySort.IDENTIFIER);

        // Assert
        assertEquals("111", result.get(0).edition().getIdentifier().toString());
        assertEquals("999", result.get(1).edition().getIdentifier().toString());
    }

    @Test
    void getListWithNoneShouldKeepOriginalOrder() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(id1)).thenReturn(Optional.of(item1));
        when(itemRepoDouble.ofIdentity(id2)).thenReturn(Optional.of(item2));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));

        // Act
        List<LibraryItemDetails> result =
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble, LibrarySort.NONE);

        // Assert
        assertSame(item1, result.get(0).item());
        assertSame(item2, result.get(1).item());
    }

    // ----------------------------------------------------------------
    // getItemDetail
    // ----------------------------------------------------------------

    @Test
    void testGetItemDetailValidItemIdReturnsLibraryItemDetails() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        ItemId itemIdDouble = mock(ItemId.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));

        // Act
        LibraryItemDetails result = libraryService.getItemDetail(itemIdDouble);

        // Assert
        assertNotNull(result);
        assertEquals(
                new LibraryItemDetails(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble),
                result);
    }

    @Test
    void testGetItemDetailItemNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail(itemIdDouble));
    }

    @Test
    void testGetItemDetailEditionNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail(itemIdDouble));
    }

    @Test
    void testGetItemDetailPublicationNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        ItemId itemIdDouble = mock(ItemId.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail(itemIdDouble));
    }

    @Test
    void testGetItemDetailAuthorNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        ItemId itemIdDouble = mock(ItemId.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail(itemIdDouble));
    }

    @Test
    void testGetItemDetailPublicationTypeNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        ItemId itemIdDouble = mock(ItemId.class);


        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail(itemIdDouble));
    }

    // ----------------------------------------------------------------
    // getItemIdsInLibrary
    // ----------------------------------------------------------------

    @Test
    void getItemIdsFromLibraryNoLibraryReturnsEmptyList() {
        // Arrange
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        List<ItemId> result = libraryService.getItemIdsInLibrary("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getItemIdsFromLibraryEmptyLibraryReturnsEmptyList() {
        // Arrange
        Library libraryDouble = mock(Library.class);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

        // Act
        List<ItemId> result = libraryService.getItemIdsInLibrary("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getItemIdsFromLibraryValidUserReturnsItemIds() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));

        // Act
        List<ItemId> result = libraryService.getItemIdsInLibrary("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(itemIdDouble, result.get(0));
    }

    // ----------------------------------------------------------------
    // addItemToLibrary
    // ----------------------------------------------------------------

    @Test
    void testAddItemToLibraryItemNotFoundThrowsIllegalArgumentException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.addItemToLibrary(
                        itemIdDouble, userIdDouble));
    }

    @Test
    void testAddItemToLibraryItemAlreadyInLibraryThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);


        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(false);

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary(
                        itemIdDouble, userIdDouble));
    }

    @Test
    void testAddItemToLibraryEditionNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary(
                        itemIdDouble, userIdDouble));
    }

    @Test
    void testAddItemToLibraryPublicationNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        Edition editionDouble = mock(Edition.class);
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary(
                        itemIdDouble, userIdDouble));
    }

    @Test
    void testAddItemToLibraryAuthorNotFoundThrowsIllegalStateException() {
        // Arrange
        Item item = mock(Item.class);
        Library library = mock(Library.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(item));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(library));
        when(library.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary(
                        itemIdDouble, userIdDouble));
    }

    @Test
    void testAddItemToLibraryPublicationTypeNotFoundThrowsIllegalStateException() {
        // Arrange
        Item item = mock(Item.class);
        Library library = mock(Library.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);
        Author author = mock(Author.class);
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(item));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(library));
        when(library.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(author));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary(
                        itemIdDouble, userIdDouble));
    }

    @Test
    void testAddItemToLibraryNewLibraryCreatedAndItemAddedReturnsLibraryItemDetails() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());
        when(libraryFactory.createLibrary(any())).thenReturn(libraryDouble);
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));

        // Act
        LibraryItemDetails result = libraryService.addItemToLibrary(
                itemIdDouble, userIdDouble);

        // Assert
        assertNotNull(result);
        assertEquals(
                new LibraryItemDetails(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble),
                result);
    }

    @Test
    void testAddItemToLibraryExistingLibraryItemAddedReturnsLibraryItemDetails() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        ItemId itemIdDouble = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));

        // Act
        LibraryItemDetails result = libraryService.addItemToLibrary(
                itemIdDouble, userIdDouble);

        // Assert
        assertNotNull(result);
        assertEquals(
                new LibraryItemDetails(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble),
                result);
    }

    // ----------------------------------------------------------------
    // getListOfItemInfoInMyLibraryFull
    // ----------------------------------------------------------------

    @Test
    void testGetListOfItemInfoInMyLibraryFullNoLibraryReturnsEmptyList() {
        // Arrange
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        var result = libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetListOfItemInfoInMyLibraryFullEmptyLibraryReturnsEmptyList() {
        // Arrange
        Library libraryDouble = mock(Library.class);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

        // Act
        var result = libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetListOfItemInfoInMyLibraryFullValidUserReturnsListWithOneItem() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        Author authorDouble = mock(Author.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));

        // Act
        var result = libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                new LibraryItemDetails(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble),
                result.get(0));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryFullItemNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryFullEditionNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryFullPublicationNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryFullAuthorNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryFullPublicationTypeNotFoundThrowsIllegalStateException() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com"));
    }
}