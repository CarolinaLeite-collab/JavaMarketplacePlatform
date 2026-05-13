//
//package MITELOVERS.controller;
//
//import MITELOVERS.domain.author.Author;
//import MITELOVERS.domain.edition.Edition;
//import MITELOVERS.domain.item.Item;
//import MITELOVERS.domain.library.Library;
//import MITELOVERS.domain.publication.Publication;
//import MITELOVERS.domain.publicationtype.PublicationType;
//import MITELOVERS.domain.repository.*;
//import MITELOVERS.domain.valueobject.*;
//import MITELOVERS.dto.ItemDetailsDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//class ListOfItemsInMyLibraryControllerTest {
//    private UserId _userIdDouble;
//    private ILibraryRepo _iLibraryRepoDouble;
//    private IItemRepo _iItemRepoDouble;
//    private IEditionRepo _iEditionRepoDouble;
//    private IPublicationRepo _iPublicationRepoDouble;
//    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
//    private IAuthorRepo _iAuthorRepoDouble;
//
//    @BeforeEach
//    void setUp() {
//        _userIdDouble = mock(UserId.class);
//        _iLibraryRepoDouble = mock(ILibraryRepo.class);
//        _iItemRepoDouble = mock(IItemRepo.class);
//        _iEditionRepoDouble = mock(IEditionRepo.class);
//        _iPublicationRepoDouble = mock(IPublicationRepo.class);
//        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
//        _iAuthorRepoDouble = mock(IAuthorRepo.class);
//
//    }
//
//    @Test
//    void testListOfItemsInMyLibraryController(){
//        //Act + SUT
//        new ListOfItemsInMyLibraryController(_iLibraryRepoDouble,
//                _iItemRepoDouble,
//                _iEditionRepoDouble,
//                _iPublicationRepoDouble,
//                _iAuthorRepoDouble,
//                _iPublicationTypeRepoDouble,
//                _userIdDouble);
//    }
//
//    @Test
//    void testListOfItemsInMyLibraryControllerShouldReturnListOfItemDetailsDTO() {
//
//        // Arrange
//        Item item1Double = mock(Item.class);
//        ItemId itemIdDouble = mock(ItemId.class);
//
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//
//        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
//        PublicationType publicationTypeDouble = mock(PublicationType.class);
//
//        AuthorId authorIdDouble = mock(AuthorId.class);
//        Author authorDouble = mock(Author.class);
//
//        Title titleDouble = mock(Title.class);
//        Name nameDouble = mock(Name.class);
//        ISBN isbnMock = mock(ISBN.class);
//
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//        Library libraryDouble = mock(Library.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
//                    .thenReturn(Optional.of(libraryDouble));
//
//            when(libraryDouble.getItemsIdInLibrary())
//                    .thenReturn(List.of(itemIdDouble));
//
//            when(_iItemRepoDouble.ofIdentity(itemIdDouble))
//                    .thenReturn(Optional.of(item1Double));
//
//            when(item1Double.getEditionId())
//                    .thenReturn(editionIdDouble);
//
//            when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
//                    .thenReturn(Optional.of(editionDouble));
//
//            when(editionDouble.getPublicationId())
//                    .thenReturn(publicationIdDouble);
//
//            when(editionDouble.getPublicationTypeId())
//                    .thenReturn(publicationTypeIdDouble);
//
//            when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
//                    .thenReturn(Optional.of(publicationDouble));
//
//            when(publicationDouble.getAuthorId())
//                    .thenReturn(authorIdDouble);
//
//            when(publicationDouble.getTitle()).thenReturn(titleDouble);
//            when(nameDouble.toString()).thenReturn("J.D. Salinger");
//            when(authorDouble.getName()).thenReturn(nameDouble);
//            when(editionDouble.getIdentifier()).thenReturn(isbnMock);
//
//            when(_iPublicationTypeRepoDouble.ofIdentity(publicationTypeIdDouble))
//                    .thenReturn(Optional.of(publicationTypeDouble));
//
//            when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
//                    .thenReturn(Optional.of(authorDouble));
//
//            // SUT
//            ListOfItemsInMyLibraryController ctl =
//                    new ListOfItemsInMyLibraryController(
//                            _iLibraryRepoDouble,
//                            _iItemRepoDouble,
//                            _iEditionRepoDouble,
//                            _iPublicationRepoDouble,
//                            _iAuthorRepoDouble,
//                            _iPublicationTypeRepoDouble,
//                            _userIdDouble
//                    );
//
//            // Act
//            List<ItemDetailsDTO> dtos =
//                    ctl.getListOfItemInfoInMyLibrary(_userIdDouble);
//
//            // Assert
//            assertEquals(1, dtos.size());
//        }
//    }
//
//    @Test
//    void testListOfItemsInMyLibraryControllerShouldReturnCorrectDTOFields() {
//
//        // Arrange
//        Item item1Double = mock(Item.class);
//        ItemId itemIdDouble = mock(ItemId.class);
//
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//
//        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
//        PublicationType publicationTypeDouble = mock(PublicationType.class);
//
//        AuthorId authorIdDouble = mock(AuthorId.class);
//        Author authorDouble = mock(Author.class);
//
//        Title titleDouble = mock(Title.class);
//        Name nameDouble = mock(Name.class);
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//        Library libraryDouble = mock(Library.class);
//
//        ISBN isbnDouble = mock(ISBN.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
//                    .thenReturn(Optional.of(libraryDouble));
//
//            when(libraryDouble.getItemsIdInLibrary())
//                    .thenReturn(List.of(itemIdDouble));
//
//            when(_iItemRepoDouble.ofIdentity(itemIdDouble))
//                    .thenReturn(Optional.of(item1Double));
//
//            when(item1Double.getEditionId())
//                    .thenReturn(editionIdDouble);
//
//            when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
//                    .thenReturn(Optional.of(editionDouble));
//
//            when(editionDouble.getPublicationId())
//                    .thenReturn(publicationIdDouble);
//
//            when(editionDouble.getPublicationTypeId())
//                    .thenReturn(publicationTypeIdDouble);
//
//            when(editionDouble.getIdentifier())
//                    .thenReturn(isbnDouble);
//
//            when(isbnDouble.toString())
//                    .thenReturn("978-0316769488");
//
//            when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
//                    .thenReturn(Optional.of(publicationDouble));
//
//            when(publicationDouble.getTitle())
//                    .thenReturn(titleDouble);
//
//            when(titleDouble.toString())
//                    .thenReturn("The Catcher in the Rye");
//
//            when(nameDouble.toString()).thenReturn("J.D. Salinger");
//
//            when(publicationDouble.getAuthorId())
//                    .thenReturn(authorIdDouble);
//
//            when(_iPublicationTypeRepoDouble.ofIdentity(publicationTypeIdDouble))
//                    .thenReturn(Optional.of(publicationTypeDouble));
//
//            when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
//                    .thenReturn(Optional.of(authorDouble));
//
//            when(authorDouble.getName())
//                    .thenReturn(nameDouble);
//
//            when(publicationTypeDouble.toString())
//                    .thenReturn("Book");
//
//            // SUT
//            ListOfItemsInMyLibraryController ctl =
//                    new ListOfItemsInMyLibraryController(
//                            _iLibraryRepoDouble,
//                            _iItemRepoDouble,
//                            _iEditionRepoDouble,
//                            _iPublicationRepoDouble,
//                            _iAuthorRepoDouble,
//                            _iPublicationTypeRepoDouble,
//                            _userIdDouble
//                    );
//
//            // Act
//            List<ItemDetailsDTO> dtos =
//                    ctl.getListOfItemInfoInMyLibrary(_userIdDouble);
//
//            ItemDetailsDTO dto = dtos.get(0);
//
//            // Assert
//            assertEquals(1, dtos.size());
//            assertEquals("The Catcher in the Rye", dto.getTitle());
//            assertEquals("J.D. Salinger", dto.getAuthorName());
//            assertEquals("Book", dto.getPublicationType());
//            assertEquals("978-0316769488", dto.getIdentifier());
//        }
//    }
//
//    @Test
//    void shouldThrowIllegalStateExceptionWhenItemNotFound() {
//
//        // Arrange
//        ItemId itemIdDouble = mock(ItemId.class);
//
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//        Library libraryDouble = mock(Library.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
//                    .thenReturn(Optional.of(libraryDouble));
//
//            when(libraryDouble.getItemsIdInLibrary())
//                    .thenReturn(List.of(itemIdDouble));
//
//            when(_iItemRepoDouble.ofIdentity(itemIdDouble))
//                    .thenReturn(Optional.empty());
//
//            // SUT
//            ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
//                    _iLibraryRepoDouble,
//                    _iItemRepoDouble,
//                    _iEditionRepoDouble,
//                    _iPublicationRepoDouble,
//                    _iAuthorRepoDouble,
//                    _iPublicationTypeRepoDouble,
//                    _userIdDouble
//            );
//
//            // Act + Assert
//            assertThrows(IllegalStateException.class,
//                    () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
//        }
//    }
//
//    @Test
//    void shouldThrowIllegalStateExceptionWhenEditionNotFound() {
//
//        // Arrange
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//        EditionId editionIdDouble = mock(EditionId.class);
//
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//        Library libraryDouble = mock(Library.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
//                    .thenReturn(Optional.of(libraryDouble));
//
//            when(libraryDouble.getItemsIdInLibrary())
//                    .thenReturn(List.of(itemIdDouble));
//
//            when(_iItemRepoDouble.ofIdentity(itemIdDouble))
//                    .thenReturn(Optional.of(itemDouble));
//
//            when(itemDouble.getEditionId())
//                    .thenReturn(editionIdDouble);
//
//            when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
//                    .thenReturn(Optional.empty());
//
//            //SUT
//            ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
//                    _iLibraryRepoDouble,
//                    _iItemRepoDouble,
//                    _iEditionRepoDouble,
//                    _iPublicationRepoDouble,
//                    _iAuthorRepoDouble,
//                    _iPublicationTypeRepoDouble,
//                    _userIdDouble
//            );
//
//            // Act + Assert
//            assertThrows(IllegalStateException.class,
//                    () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
//        }
//    }
//
//    @Test
//    void shouldThrowIllegalStateExceptionWhenPublicationNotFound() {
//
//        // Arrange
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//        Library libraryDouble = mock(Library.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
//                    .thenReturn(Optional.of(libraryDouble));
//
//            when(libraryDouble.getItemsIdInLibrary())
//                    .thenReturn(List.of(itemIdDouble));
//
//            when(_iItemRepoDouble.ofIdentity(itemIdDouble))
//                    .thenReturn(Optional.of(itemDouble));
//
//            when(itemDouble.getEditionId())
//                    .thenReturn(editionIdDouble);
//
//            when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
//                    .thenReturn(Optional.of(editionDouble));
//
//            when(editionDouble.getPublicationId())
//                    .thenReturn(publicationIdDouble);
//
//            when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
//                    .thenReturn(Optional.empty());
//
//            //SUT
//            ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
//                    _iLibraryRepoDouble,
//                    _iItemRepoDouble,
//                    _iEditionRepoDouble,
//                    _iPublicationRepoDouble,
//                    _iAuthorRepoDouble,
//                    _iPublicationTypeRepoDouble,
//                    _userIdDouble
//            );
//
//            // Act + Assert
//            assertThrows(IllegalStateException.class,
//                    () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
//        }
//    }
//
//    @Test
//    void shouldThrowIllegalStateExceptionWhenPublicationTypeNotFound() {
//
//        // Arrange
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//
//        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
//
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//        Library libraryDouble = mock(Library.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
//                    .thenReturn(Optional.of(libraryDouble));
//
//            when(libraryDouble.getItemsIdInLibrary())
//                    .thenReturn(List.of(itemIdDouble));
//
//            when(_iItemRepoDouble.ofIdentity(itemIdDouble))
//                    .thenReturn(Optional.of(itemDouble));
//
//            when(itemDouble.getEditionId())
//                    .thenReturn(editionIdDouble);
//
//            when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
//                    .thenReturn(Optional.of(editionDouble));
//
//            when(editionDouble.getPublicationId())
//                    .thenReturn(publicationIdDouble);
//
//            when(editionDouble.getPublicationTypeId())
//                    .thenReturn(publicationTypeIdDouble);
//
//            when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
//                    .thenReturn(Optional.of(publicationDouble));
//
//            when(_iPublicationTypeRepoDouble.ofIdentity(publicationTypeIdDouble))
//                    .thenReturn(Optional.empty());
//
//            //SUT
//            ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
//                    _iLibraryRepoDouble,
//                    _iItemRepoDouble,
//                    _iEditionRepoDouble,
//                    _iPublicationRepoDouble,
//                    _iAuthorRepoDouble,
//                    _iPublicationTypeRepoDouble,
//                    _userIdDouble
//            );
//
//            // Act + Assert
//            assertThrows(IllegalStateException.class,
//                    () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
//        }
//    }
//
//    @Test
//    void shouldThrowIllegalStateExceptionWhenAuthorNotFound() {
//
//        // Arrange
//        ItemId itemIdDouble = mock(ItemId.class);
//        Item itemDouble = mock(Item.class);
//
//        EditionId editionIdDouble = mock(EditionId.class);
//        Edition editionDouble = mock(Edition.class);
//
//        PublicationId publicationIdDouble = mock(PublicationId.class);
//        Publication publicationDouble = mock(Publication.class);
//
//        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
//        PublicationType publicationTypeDouble = mock(PublicationType.class);
//
//        AuthorId authorIdDouble = mock(AuthorId.class);
//
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//        Library libraryDouble = mock(Library.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
//                    .thenReturn(Optional.of(libraryDouble));
//
//            when(libraryDouble.getItemsIdInLibrary())
//                    .thenReturn(List.of(itemIdDouble));
//
//            when(_iItemRepoDouble.ofIdentity(itemIdDouble))
//                    .thenReturn(Optional.of(itemDouble));
//
//            when(itemDouble.getEditionId())
//                    .thenReturn(editionIdDouble);
//
//            when(_iEditionRepoDouble.ofIdentity(editionIdDouble))
//                    .thenReturn(Optional.of(editionDouble));
//
//            when(editionDouble.getPublicationId())
//                    .thenReturn(publicationIdDouble);
//
//            when(editionDouble.getPublicationTypeId())
//                    .thenReturn(publicationTypeIdDouble);
//
//            when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
//                    .thenReturn(Optional.of(publicationDouble));
//
//            when(_iPublicationTypeRepoDouble.ofIdentity(publicationTypeIdDouble))
//                    .thenReturn(Optional.of(publicationTypeDouble));
//
//            when(publicationDouble.getAuthorId())
//                    .thenReturn(authorIdDouble);
//
//            when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
//                    .thenReturn(Optional.empty());
//
//            //SUT
//            ListOfItemsInMyLibraryController ctl = new ListOfItemsInMyLibraryController(
//                    _iLibraryRepoDouble,
//                    _iItemRepoDouble,
//                    _iEditionRepoDouble,
//                    _iPublicationRepoDouble,
//                    _iAuthorRepoDouble,
//                    _iPublicationTypeRepoDouble,
//                    _userIdDouble
//            );
//
//            // Act + Assert
//            assertThrows(IllegalStateException.class,
//                    () -> ctl.getListOfItemInfoInMyLibrary(_userIdDouble));
//        }
//    }
//
//
//}
