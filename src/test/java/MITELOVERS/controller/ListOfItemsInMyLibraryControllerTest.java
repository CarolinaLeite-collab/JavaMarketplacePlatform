
package MITELOVERS.controller;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.ItemDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class ListOfItemsInMyLibraryControllerTest {

    @Mock private IRepository<LibraryId, Library> _libraryRepo;
    @Mock private IRepository<ItemId, Item> _itemRepo;
    @Mock private IRepository<EditionId, Edition> _editionRepo;
    @Mock private IRepository<PublicationId, Publication> _publicationRepo;
    @Mock private IRepository<AuthorId, Author> _authorRepo;
    @Mock private IRepository<PublicationTypeId, PublicationType> _publicationTypeRepo;

    @Mock private UserId _userId;

    @InjectMocks
    private ListOfItemsInMyLibraryController _controller;

    @BeforeEach
    void setUp() {
        _controller = new ListOfItemsInMyLibraryController(
                _libraryRepo,
                _itemRepo,
                _editionRepo,
                _publicationRepo,
                _authorRepo,
                _publicationTypeRepo
        );
    }

    @Test
    void shouldReturnListOfItemDetailsDTO() {

        // Arrange
        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);

        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);

        PublicationId publicationId = mock(PublicationId.class);
        Publication publication = mock(Publication.class);

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        PublicationType publicationType = mock(PublicationType.class);

        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);

        Title title = mock(Title.class);
        Name name = mock(Name.class);
        ISBN isbn = mock(ISBN.class);

        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(_itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(_editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);
            when(edition.getIdentifier()).thenReturn(isbn);

            when(_publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(publication.getAuthorId()).thenReturn(authorId);
            when(publication.getTitle()).thenReturn(title);

            when(_publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.of(publicationType));
            when(_authorRepo.ofIdentity(authorId)).thenReturn(Optional.of(author));

            when(title.toString()).thenReturn("The Catcher in the Rye");
            when(name.toString()).thenReturn("J.D. Salinger");
            when(author.getName()).thenReturn(name);
            when(publicationType.toString()).thenReturn("Book");
            when(isbn.toString()).thenReturn("978-0316769488");

            // Act
            List<ItemDetailsDTO> dtos = _controller.getListOfItemInfoInMyLibrary(_userId);

            // Assert
            assertEquals(1, dtos.size());
        }
    }

    @Test
    void shouldReturnCorrectDTOFields() {

        // Arrange
        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);

        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);

        PublicationId publicationId = mock(PublicationId.class);
        Publication publication = mock(Publication.class);

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        PublicationType publicationType = mock(PublicationType.class);

        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);

        Title title = mock(Title.class);
        Name name = mock(Name.class);
        ISBN isbn = mock(ISBN.class);

        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(_itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(_editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);
            when(edition.getIdentifier()).thenReturn(isbn);

            when(_publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(publication.getAuthorId()).thenReturn(authorId);
            when(publication.getTitle()).thenReturn(title);

            when(_publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.of(publicationType));
            when(_authorRepo.ofIdentity(authorId)).thenReturn(Optional.of(author));

            when(title.toString()).thenReturn("The Catcher in the Rye");
            when(name.toString()).thenReturn("J.D. Salinger");
            when(author.getName()).thenReturn(name);
            when(publicationType.toString()).thenReturn("Book");
            when(isbn.toString()).thenReturn("978-0316769488");

            // Act
            List<ItemDetailsDTO> dtos = _controller.getListOfItemInfoInMyLibrary(_userId);
            ItemDetailsDTO dto = dtos.get(0);

            // Assert
            assertEquals("The Catcher in the Rye", dto.getTitle());
            assertEquals("J.D. Salinger", dto.getAuthorName());
            assertEquals("Book", dto.getPublicationType());
            assertEquals("978-0316769488", dto.getIdentifier());
        }
    }

    // ---------------------------------------------------------
    // ERROR PATH TESTS
    // ---------------------------------------------------------

    @Test
    void shouldThrowWhenItemNotFound() {

        ItemId itemId = mock(ItemId.class);
        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));
            when(_itemRepo.ofIdentity(itemId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> _controller.getListOfItemInfoInMyLibrary(_userId));
        }
    }

    @Test
    void shouldThrowWhenEditionNotFound() {

        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);
        EditionId editionId = mock(EditionId.class);

        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(_itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(_editionRepo.ofIdentity(editionId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> _controller.getListOfItemInfoInMyLibrary(_userId));
        }
    }

    @Test
    void shouldThrowWhenPublicationNotFound() {

        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);

        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);

        PublicationId publicationId = mock(PublicationId.class);

        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(_itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(_editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);

            when(_publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> _controller.getListOfItemInfoInMyLibrary(_userId));
        }
    }

    @Test
    void shouldThrowWhenPublicationTypeNotFound() {

        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);

        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);

        PublicationId publicationId = mock(PublicationId.class);
        Publication publication = mock(Publication.class);

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(_itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(_editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);

            when(_publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(_publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> _controller.getListOfItemInfoInMyLibrary(_userId));
        }
    }

    @Test
    void shouldThrowWhenAuthorNotFound() {

        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);

        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);

        PublicationId publicationId = mock(PublicationId.class);
        Publication publication = mock(Publication.class);

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        PublicationType publicationType = mock(PublicationType.class);

        AuthorId authorId = mock(AuthorId.class);

        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(_itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(_editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);

            when(_publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(_publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.of(publicationType));

            when(publication.getAuthorId()).thenReturn(authorId);
            when(_authorRepo.ofIdentity(authorId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> _controller.getListOfItemInfoInMyLibrary(_userId));
        }
    }

}
