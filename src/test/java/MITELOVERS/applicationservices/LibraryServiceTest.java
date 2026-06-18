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
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.LibraryItemDetailsDTO;
import MITELOVERS.dto.response.LibraryItemSummaryDTO;
import MITELOVERS.mapper.ItemDetailsMapper;
import MITELOVERS.mapper.LibraryItemDetailsMapper;
import MITELOVERS.mapper.LibraryItemSummaryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    LibraryItemSummaryMapper summaryMapperDouble;
    LibraryItemDetailsMapper detailsMapperDouble;
    ItemDetailsMapper itemDetailsMapperDouble;
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
        summaryMapperDouble = mock(LibraryItemSummaryMapper.class);
        detailsMapperDouble = mock(LibraryItemDetailsMapper.class);
        itemDetailsMapperDouble = mock(ItemDetailsMapper.class);
        libraryFactory = mock(LibraryFactory.class);



        libraryService = new LibraryService(
                libraryRepoDouble, itemRepoDouble, editionRepoDouble,
                publicationRepoDouble, authorRepoDouble, publicationTypeRepoDouble, detailsMapperDouble, summaryMapperDouble, itemDetailsMapperDouble, libraryFactory);
    }

    @Test
    void testGetListOfItemInfoInMyLibraryNoLibraryReturnsEmptyList() {
        // Arrange
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        List<LibraryItemSummaryDTO> result = libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com");

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
        List<LibraryItemSummaryDTO> result = libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com");

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
        LibraryItemSummaryDTO summaryDTODouble = mock(LibraryItemSummaryDTO.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble)).thenReturn(summaryDTODouble);
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));

        // Act
        List<LibraryItemSummaryDTO> result = libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(summaryDTODouble, result.get(0));
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
    void testGetListShouldSortByTitle() {
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        LibraryItemSummaryDTO z = new LibraryItemSummaryDTO(
                "1", "Zulu", "Author", "Book", "999", null);
        LibraryItemSummaryDTO a = new LibraryItemSummaryDTO(
                "2", "Alpha", "Author", "Book", "111", null);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble,
                authorDouble, typeDouble)).thenReturn(z, a);

        List<LibraryItemSummaryDTO> result =
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble, LibrarySort.TITLE);

        assertSame(a, result.get(0));
        assertSame(z, result.get(1));
    }

    @Test
    void testGetListShouldSortByAuthor() {
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        LibraryItemSummaryDTO z = new LibraryItemSummaryDTO(
                "1", "Title", "Zulu", "Book", "999", null);
        LibraryItemSummaryDTO a = new LibraryItemSummaryDTO(
                "2", "Title", "Alpha", "Book", "111", null);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble,
                authorDouble, typeDouble)).thenReturn(z, a);

        List<LibraryItemSummaryDTO> result =
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble, LibrarySort.AUTHOR);

        assertSame(a, result.get(0));
        assertSame(z, result.get(1));
    }

    @Test
    void testGetListShouldSortByPublicationType() {
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        LibraryItemSummaryDTO z = new LibraryItemSummaryDTO(
                "1", "Title", "Author", "Zine", "999", null);
        LibraryItemSummaryDTO a = new LibraryItemSummaryDTO(
                "2", "Title", "Author", "Book", "111", null);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble,
                authorDouble, typeDouble)).thenReturn(z, a);

        List<LibraryItemSummaryDTO> result =
                libraryService.getListOfItemInfoInMyLibrary(
                        userIdDouble, LibrarySort.PUBLICATION_TYPE);

        assertSame(a, result.get(0));
        assertSame(z, result.get(1));
    }

    @Test
    void testGetListShouldSortByIdentifier() {
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        LibraryItemSummaryDTO z = new LibraryItemSummaryDTO(
                "1", "Title", "Author", "Book", "999", null);
        LibraryItemSummaryDTO a = new LibraryItemSummaryDTO(
                "2", "Title", "Author", "Book", "111", null);

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble,
                authorDouble, typeDouble)).thenReturn(z, a);

        List<LibraryItemSummaryDTO> result =
                libraryService.getListOfItemInfoInMyLibrary(
                        userIdDouble, LibrarySort.IDENTIFIER);

        assertSame(a, result.get(0));
        assertSame(z, result.get(1));
    }

    @Test
    void getListWithNoneShouldKeepOriginalOrder() {
        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        Library libraryDouble = mock(Library.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType typeDouble = mock(PublicationType.class);

        LibraryItemSummaryDTO first = new LibraryItemSummaryDTO(
                "1", "Zulu", "Author", "Book", "999", null
        );
        LibraryItemSummaryDTO second = new LibraryItemSummaryDTO(
                "2", "Alpha", "Author", "Book", "111", null
        );

        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(id1, id2));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(typeDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble, authorDouble, typeDouble)).thenReturn(first, second);

        List<LibraryItemSummaryDTO> result =
                libraryService.getListOfItemInfoInMyLibrary(
                        userIdDouble,
                        LibrarySort.NONE
                );

        assertSame(first, result.get(0));
        assertSame(second, result.get(1));
    }

    // ----------------------------------------------------------------
    // getItemDetail
    // ----------------------------------------------------------------

    @Test
    void testGetItemDetailValidItemIdReturnsLibraryItemDetailsDTO() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        LibraryItemDetailsDTO detailsDTODouble = mock(LibraryItemDetailsDTO.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(detailsMapperDouble.toDTO(authorDouble, editionDouble,  publicationTypeDouble))
                .thenReturn(detailsDTODouble);

        // Act
        LibraryItemDetailsDTO result = libraryService.getItemDetail("3C5D126F8B");

        // Assert
        assertNotNull(result);
        assertEquals(detailsDTODouble, result);
    }

    @Test
    void testGetItemDetailItemNotFoundThrowsIllegalStateException() {
        // Arrange
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail("3C5D126F8B"));
    }

    @Test
    void testGetItemDetailEditionNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail("3C5D126F8B"));
    }

    @Test
    void testGetItemDetailPublicationNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail("3C5D126F8B"));
    }

    @Test
    void testGetItemDetailAuthorNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail("3C5D126F8B"));
    }

    @Test
    void testGetItemDetailPublicationTypeNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getItemDetail("3C5D126F8B"));
    }

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
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com"));
    }

    @Test
    void testAddItemToLibraryItemAlreadyInLibraryThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(false);

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com"));
    }

    @Test
    void testAddItemToLibraryEditionNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com"));
    }

    @Test
    void testAddItemToLibraryPublicationNotFoundThrowsIllegalStateException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        Edition editionDouble = mock(Edition.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com"));
    }

    @Test
    void testAddItemToLibraryAuthorNotFoundThrowsIllegalStateException() {
        Item item = mock(Item.class);
        Library library = mock(Library.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(item));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(library));
        when(library.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com"));
    }

    @Test
    void testAddItemToLibraryPublicationTypeNotFoundThrowsIllegalStateException() {
        Item item = mock(Item.class);
        Library library = mock(Library.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);
        Author author = mock(Author.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(item));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(library));
        when(library.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(author));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () ->
                libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com"));
    }

    @Test
    void testAddItemToLibraryNewLibraryCreatedAndItemAddedReturnsSummaryDTO() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        LibraryItemSummaryDTO summaryDTODouble = mock(LibraryItemSummaryDTO.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());
        when(libraryFactory.createLibrary(any())).thenReturn(libraryDouble);
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble)).thenReturn(summaryDTODouble);
        // Act
        LibraryItemSummaryDTO result = libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertEquals(summaryDTODouble, result);
    }

    @Test
    void testAddItemToLibraryExistingLibraryItemAddedReturnsSummaryDTO() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Library libraryDouble = mock(Library.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        LibraryItemSummaryDTO summaryDTODouble = mock(LibraryItemSummaryDTO.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.addItemIdToLibrary(any())).thenReturn(true);
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble)).thenReturn(summaryDTODouble);
        // Act
        LibraryItemSummaryDTO result = libraryService.addItemToLibrary("3C5D126F8B", "pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertEquals(summaryDTODouble, result);
    }

    // ----------------------------------------------------------------
// getListOfItemInfoInMyLibraryFull
// ----------------------------------------------------------------

    @Test
    void testGetListOfItemInfoInMyLibraryFullNoLibraryReturnsEmptyList() {
        // Arrange
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
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

        // SUT
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
        MITELOVERS.dto.response.ItemDetailsDTO itemDetailsDTODouble =
                mock(MITELOVERS.dto.response.ItemDetailsDTO.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(itemDetailsMapperDouble.toDTO(editionDouble, publicationDouble, publicationTypeDouble, authorDouble))
                .thenReturn(itemDetailsDTODouble);

        // SUT
        var result = libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemDetailsDTODouble, result.get(0));
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
    void testGetListOfItemInfoInMyLibraryFullPublicationTypeNotFoundThrowsIllegalStateException() {
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
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

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
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibraryFull("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryAuthorNotFoundThrowsIllegalStateException() {
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

        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com"));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryPublicationTypeNotFoundThrowsIllegalStateException() {
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

        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary("pedro@aeiou.com"));
    }
}

