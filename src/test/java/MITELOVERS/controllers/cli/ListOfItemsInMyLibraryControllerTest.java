package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.LibraryItemDetails;
import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOfItemsInMyLibraryControllerTest {

    @Mock
    private LibraryService _libraryService;

    @InjectMocks
    private ListOfItemsInMyLibraryController _controller;

    @Test
    void shouldReturnItemListFromService() {
        // Arrange
        String userId = "pedro@aeiou.com";

        Item itemDouble = mock(Item.class);
        Publication publicationDouble = mock(Publication.class);
        Edition editionDouble = mock(Edition.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        LibraryItemDetails details = new LibraryItemDetails(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        when(_libraryService.getListOfItemInfoInMyLibraryFull(userId))
                .thenReturn(List.of(details));

        // Act
        List<LibraryItemDetails> result = _controller.getListOfItemInfoInMyLibrary(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(details, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenLibraryIsEmpty() {
        // Arrange
        String userId = "pedro@aeiou.com";
        when(_libraryService.getListOfItemInfoInMyLibraryFull(userId)).thenReturn(List.of());

        // Act
        List<LibraryItemDetails> result = _controller.getListOfItemInfoInMyLibrary(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldPropagateExceptionFromService() {
        // Arrange
        String userId = "pedro@aeiou.com";
        when(_libraryService.getListOfItemInfoInMyLibraryFull(userId))
                .thenThrow(new IllegalStateException("Library not found for user!"));

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getListOfItemInfoInMyLibrary(userId));
    }
}
