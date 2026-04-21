
package MITELOVERS.controller;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.ItemDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfItemsInMyLibraryControllerTest {
    private UserId _userIdDouble;
    private ILibraryRepo _iLibraryRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private IEditionRepo _iEditionRepoDouble;
    private IPublicationRepo _iPublicationRepoDouble;
    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
    private IAuthorRepo _iAuthorRepoDouble;

    @BeforeEach
    void setUp() {
        _userIdDouble = mock(UserId.class);
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _iEditionRepoDouble = mock(IEditionRepo.class);
        _iPublicationRepoDouble = mock(IPublicationRepo.class);
        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        _iAuthorRepoDouble = mock(IAuthorRepo.class);

    }

    @Test
    void testListOfItemsInMyLibraryController(){
        //Act + SUT
        new ListOfItemsInMyLibraryController(_iLibraryRepoDouble,
                _iItemRepoDouble,
                _iEditionRepoDouble,
                _iPublicationRepoDouble,
                _iAuthorRepoDouble,
                _iPublicationTypeRepoDouble,
                _userIdDouble);
    }

    @Test
    void testListOfItemsInMyLibraryControllerShouldReturnListOfItemDetailsDTO() {
        //Arrange

        Item item1Double = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);

        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);

        PublicationId publicationIdDouble = mock(PublicationId.class);
        Publication publicationDouble = mock(Publication.class);

        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        AuthorId authorIdDouble = mock(AuthorId.class);
        Author authorDouble = mock(Author.class);

        Title titleDouble = mock(Title.class);

        ISBN isbnMock = mock(ISBN.class);

        when(editionDouble.getIdentifier())
                .thenReturn(isbnMock);

        when(isbnMock.toString())
                .thenReturn("978-1-4028-9462-6");

        when(_iLibraryRepoDouble.getItemsInLibraryByUserId(_userIdDouble))
                .thenReturn(List.of(itemIdDouble));

        when(_iItemRepoDouble.ofIdentity(itemIdDouble))
                .thenReturn(Optional.of(item1Double));

        when(item1Double.getEditionId())
                .thenReturn(editionIdDouble);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId())
                .thenReturn(publicationIdDouble);

        when(editionDouble.getPublicationTypeId())
                .thenReturn(publicationTypeIdDouble);

        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
                .thenReturn(Optional.of(publicationDouble));

        when(publicationDouble.getAuthorId())
                .thenReturn(authorIdDouble);

        when(publicationDouble.getTitle())
                .thenReturn(titleDouble);

        when(titleDouble.toString())
                .thenReturn("TitleDouble");

        when(_iPublicationTypeRepoDouble.ofIdentity(publicationTypeIdDouble))
                .thenReturn(Optional.of(publicationTypeDouble));

        when(publicationTypeDouble.toString())
                .thenReturn("PublicationTypeDouble");

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.of(authorDouble));

        when(authorDouble.toString())
                .thenReturn("AuthorDouble");

        //SUT
        ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
                _iLibraryRepoDouble,
                _iItemRepoDouble,
                _iEditionRepoDouble,
                _iPublicationRepoDouble,
                _iAuthorRepoDouble,
                _iPublicationTypeRepoDouble,
                _userIdDouble
        );

        //Act
        List<ItemDetailsDTO> dtos =
                ctl.getListOfItemInfoInMyLibrary(_userIdDouble);

        //Assert
        assertEquals(1, dtos.size());
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenItemNotFound() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        when(_iLibraryRepoDouble.getItemsInLibraryByUserId(_userIdDouble)).thenReturn(List.of(itemIdDouble));

        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        //SUT
        ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
                _iLibraryRepoDouble,
                _iItemRepoDouble,
                _iEditionRepoDouble,
                _iPublicationRepoDouble,
                _iAuthorRepoDouble,
                _iPublicationTypeRepoDouble,
                _userIdDouble
        );

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenEditionNotFound() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        EditionId editionIdDouble = mock(EditionId.class);

        when(_iLibraryRepoDouble.getItemsInLibraryByUserId(_userIdDouble))
                .thenReturn(List.of(itemIdDouble));

        when(_iItemRepoDouble.ofIdentity(itemIdDouble))
                .thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId())
                .thenReturn(editionIdDouble);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.empty());

        //SUT
        ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
                _iLibraryRepoDouble,
                _iItemRepoDouble,
                _iEditionRepoDouble,
                _iPublicationRepoDouble,
                _iAuthorRepoDouble,
                _iPublicationTypeRepoDouble,
                _userIdDouble
        );

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenPublicationNotFound() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Item itemDouble = mock(Item.class);

        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);

        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(_iLibraryRepoDouble.getItemsInLibraryByUserId(_userIdDouble))
                .thenReturn(List.of(itemIdDouble));

        when(_iItemRepoDouble.ofIdentity(itemIdDouble))
                .thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId())
                .thenReturn(editionIdDouble);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId())
                .thenReturn(publicationIdDouble);

        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
                .thenReturn(Optional.empty());

        //SUT
        ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
                _iLibraryRepoDouble,
                _iItemRepoDouble,
                _iEditionRepoDouble,
                _iPublicationRepoDouble,
                _iAuthorRepoDouble,
                _iPublicationTypeRepoDouble,
                _userIdDouble
        );

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenPublicationTypeNotFound() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Item itemDouble = mock(Item.class);

        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);

        PublicationId publicationIdDouble = mock(PublicationId.class);
        Publication publicationDouble = mock(Publication.class);

        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        when(_iLibraryRepoDouble.getItemsInLibraryByUserId(_userIdDouble))
                .thenReturn(List.of(itemIdDouble));

        when(_iItemRepoDouble.ofIdentity(itemIdDouble))
                .thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId())
                .thenReturn(editionIdDouble);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId())
                .thenReturn(publicationIdDouble);

        when(editionDouble.getPublicationTypeId())
                .thenReturn(publicationTypeIdDouble);

        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
                .thenReturn(Optional.of(publicationDouble));

        when(_iPublicationTypeRepoDouble.ofIdentity(publicationTypeIdDouble))
                .thenReturn(Optional.empty());

        //SUT
        ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
                _iLibraryRepoDouble,
                _iItemRepoDouble,
                _iEditionRepoDouble,
                _iPublicationRepoDouble,
                _iAuthorRepoDouble,
                _iPublicationTypeRepoDouble,
                _userIdDouble
        );

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenAuthorNotFound() {

        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Item itemDouble = mock(Item.class);

        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);

        PublicationId publicationIdDouble = mock(PublicationId.class);
        Publication publicationDouble = mock(Publication.class);

        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iLibraryRepoDouble.getItemsInLibraryByUserId(_userIdDouble))
                .thenReturn(List.of(itemIdDouble));

        when(_iItemRepoDouble.ofIdentity(itemIdDouble))
                .thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId())
                .thenReturn(editionIdDouble);

        when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId())
                .thenReturn(publicationIdDouble);

        when(editionDouble.getPublicationTypeId())
                .thenReturn(publicationTypeIdDouble);

        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
                .thenReturn(Optional.of(publicationDouble));

        when(_iPublicationTypeRepoDouble.ofIdentity(publicationTypeIdDouble))
                .thenReturn(Optional.of(publicationTypeDouble));

        when(publicationDouble.getAuthorId())
                .thenReturn(authorIdDouble);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.empty());

        //SUT
        ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
                _iLibraryRepoDouble,
                _iItemRepoDouble,
                _iEditionRepoDouble,
                _iPublicationRepoDouble,
                _iAuthorRepoDouble,
                _iPublicationTypeRepoDouble,
                _userIdDouble
        );

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
    }


}
