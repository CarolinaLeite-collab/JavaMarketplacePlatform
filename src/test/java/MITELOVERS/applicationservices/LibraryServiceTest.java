package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.ItemDetailsDTO;
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
    LibraryService libraryService;

    @BeforeEach
    void setUp() {
        libraryRepoDouble = mock(ILibraryRepo.class);
        itemRepoDouble = mock(IItemRepo.class);
        editionRepoDouble = mock(IEditionRepo.class);
        publicationRepoDouble = mock(IPublicationRepo.class);
        authorRepoDouble = mock(IAuthorRepo.class);
        publicationTypeRepoDouble = mock(IPublicationTypeRepo.class);

        libraryService = new LibraryService(
                libraryRepoDouble, itemRepoDouble, editionRepoDouble,
                publicationRepoDouble, authorRepoDouble, publicationTypeRepoDouble);
    }

    @Test
    void testGetListOfItemInfoInMyLibraryEmptyLibraryReturnsEmptyList() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Library libraryDouble = mock(Library.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

        // Act
        List<ItemDetailsDTO> result = libraryService.getListOfItemInfoInMyLibrary(userIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetListOfItemInfoInMyLibraryLibraryNotFoundThrowsIllegalStateException() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryValidUserReturnsItemDetailsDTOList() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        Author authorDouble = mock(Author.class);

        Title titleDouble = mock(Title.class);
        Name authorNameDouble = mock(Name.class);
        Identifier identifierDouble = mock(Identifier.class);

        when(titleDouble.toString()).thenReturn("The Hobbit");
        when(authorNameDouble.toString()).thenReturn("J.R.R. Tolkien");
        when(identifierDouble.toString()).thenReturn("9780007525492");
        when(publicationTypeDouble.toString()).thenReturn("Book");

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));

        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        when(authorDouble.getName()).thenReturn(authorNameDouble);
        when(editionDouble.getIdentifier()).thenReturn(identifierDouble);

        // Act
        List<ItemDetailsDTO> result = libraryService.getListOfItemInfoInMyLibrary(userIdDouble);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetListOfItemInfoInMyLibraryItemNotFoundThrowsIllegalStateException() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryEditionNotFoundThrowsIllegalStateException() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryPublicationNotFoundThrowsIllegalStateException() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryPublicationTypeNotFoundThrowsIllegalStateException() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble));
    }

    @Test
    void testGetListOfItemInfoInMyLibraryAuthorNotFoundThrowsIllegalStateException() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ItemId itemIdDouble = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        when(libraryRepoDouble.ofIdentity(any())).thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(itemIdDouble));
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationTypeRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationTypeDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalStateException.class, () ->
                libraryService.getListOfItemInfoInMyLibrary(userIdDouble));
    }
}