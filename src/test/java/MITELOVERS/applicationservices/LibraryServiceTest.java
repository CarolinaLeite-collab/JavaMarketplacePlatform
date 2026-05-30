package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.LibraryItemDetailsDTO;
import MITELOVERS.dto.LibraryItemSummaryDTO;
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
        LibraryItemSummaryDTO summaryDTODouble = mock(LibraryItemSummaryDTO.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(summaryMapperDouble.toDTO(itemDouble, publicationDouble)).thenReturn(summaryDTODouble);

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
        when(detailsMapperDouble.toDTO(editionDouble, authorDouble, publicationTypeDouble))
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
}

