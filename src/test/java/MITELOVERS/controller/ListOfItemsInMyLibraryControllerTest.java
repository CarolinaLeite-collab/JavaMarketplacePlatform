
package MITELOVERS.controller;

import MITELOVERS.ddd.IRepository;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListOfItemsInMyLibraryControllerTest {

    @Mock private IRepository<LibraryId, Library> libraryRepo;
    @Mock private IRepository<ItemId, Item> itemRepo;
    @Mock private IRepository<EditionId, Edition> editionRepo;
    @Mock private IRepository<PublicationId, Publication> publicationRepo;
    @Mock private IRepository<AuthorId, Author> authorRepo;
    @Mock private IRepository<PublicationTypeId, PublicationType> publicationTypeRepo;

    @Mock private UserId userId;

    private ListOfItemsInMyLibraryController controller;

    @BeforeEach
    void setUp() {
        controller = new ListOfItemsInMyLibraryController(
                libraryRepo,
                itemRepo,
                editionRepo,
                publicationRepo,
                authorRepo,
                publicationTypeRepo
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

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);
            when(edition.getIdentifier()).thenReturn(isbn);

            when(publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(publication.getAuthorId()).thenReturn(authorId);
            when(publication.getTitle()).thenReturn(title);

            when(publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.of(publicationType));
            when(authorRepo.ofIdentity(authorId)).thenReturn(Optional.of(author));

            when(title.toString()).thenReturn("The Catcher in the Rye");
            when(name.toString()).thenReturn("J.D. Salinger");
            when(author.getName()).thenReturn(name);
            when(publicationType.toString()).thenReturn("Book");
            when(isbn.toString()).thenReturn("978-0316769488");

            // Act
            List<ItemDetailsDTO> dtos = controller.getListOfItemInfoInMyLibrary(userId);

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

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);
            when(edition.getIdentifier()).thenReturn(isbn);

            when(publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(publication.getAuthorId()).thenReturn(authorId);
            when(publication.getTitle()).thenReturn(title);

            when(publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.of(publicationType));
            when(authorRepo.ofIdentity(authorId)).thenReturn(Optional.of(author));

            when(title.toString()).thenReturn("The Catcher in the Rye");
            when(name.toString()).thenReturn("J.D. Salinger");
            when(author.getName()).thenReturn(name);
            when(publicationType.toString()).thenReturn("Book");
            when(isbn.toString()).thenReturn("978-0316769488");

            // Act
            List<ItemDetailsDTO> dtos = controller.getListOfItemInfoInMyLibrary(userId);
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

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));
            when(itemRepo.ofIdentity(itemId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> controller.getListOfItemInfoInMyLibrary(userId));
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

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(editionRepo.ofIdentity(editionId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> controller.getListOfItemInfoInMyLibrary(userId));
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

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);

            when(publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> controller.getListOfItemInfoInMyLibrary(userId));
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

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);

            when(publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> controller.getListOfItemInfoInMyLibrary(userId));
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

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId));

            when(itemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
            when(item.getEditionId()).thenReturn(editionId);

            when(editionRepo.ofIdentity(editionId)).thenReturn(Optional.of(edition));
            when(edition.getPublicationId()).thenReturn(publicationId);
            when(edition.getPublicationTypeId()).thenReturn(publicationTypeId);

            when(publicationRepo.ofIdentity(publicationId)).thenReturn(Optional.of(publication));
            when(publicationTypeRepo.ofIdentity(publicationTypeId)).thenReturn(Optional.of(publicationType));

            when(publication.getAuthorId()).thenReturn(authorId);
            when(authorRepo.ofIdentity(authorId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> controller.getListOfItemInfoInMyLibrary(userId));
        }
    }

}
