package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.ItemResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Year;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemResponseDTOMapperTest {

    private ItemResponseDTOMapper buildMapper(Edition editionDouble,
                                              Publication publicationDouble,
                                              Author authorDouble,
                                              Genre genreDouble) {
        IEditionRepo     editionRepo     = mock(IEditionRepo.class);
        IPublicationRepo publicationRepo = mock(IPublicationRepo.class);
        IAuthorRepo      authorRepo      = mock(IAuthorRepo.class);
        IGenreRepo       genreRepo       = mock(IGenreRepo.class);

        when(editionRepo.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepo.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepo.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepo.ofIdentity(any())).thenReturn(Optional.of(genreDouble));

        return new ItemResponseDTOMapper(editionRepo, publicationRepo, authorRepo, genreRepo);
    }

    @Test
    void toModelReturnsCorrectDTOWithAllFields() {
        // Arrange
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("3C5D126F8B");

        EditionId editionIdDouble = mock(EditionId.class);
        when(editionIdDouble.toString()).thenReturn("E-ABCDEF12");

        ISBN isbnDouble = mock(ISBN.class);
        when(isbnDouble.toString()).thenReturn("978-0-451-52493-5");

        PublicationTypeId typeIdDouble = mock(PublicationTypeId.class);
        when(typeIdDouble.toString()).thenReturn("BOOK");

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("1984");

        Name authorNameDouble = mock(Name.class);
        when(authorNameDouble.toString()).thenReturn("George Orwell");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getCondition()).thenReturn(Condition.GOOD);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice copy"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getEditionId()).thenReturn(editionIdDouble);
        when(editionDouble.getIdentifier()).thenReturn(isbnDouble);
        when(editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(editionDouble.getPublishingYear()).thenReturn(Year.of(2003));
        when(editionDouble.getPublicationTypeId()).thenReturn(typeIdDouble);

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        when(publicationDouble.getReleaseYear()).thenReturn(Year.of(1949));

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(authorNameDouble);

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenre()).thenReturn("Fiction");

        ItemResponseDTOMapper mapper = buildMapper(editionDouble, publicationDouble, authorDouble, genreDouble);

        // Act
        ItemResponseDTO dto = mapper.toModel(itemDouble);

        // Assert
        assertEquals("3C5D126F8B", dto.getItemId());
        assertEquals("GOOD", dto.getCondition());
        assertEquals("1984", dto.getTitle());
        assertEquals("George Orwell", dto.getAuthorName());
        assertEquals("978-0-451-52493-5", dto.getIdentifier());
        assertEquals("BOOK", dto.getPublicationTypeName());
        assertEquals("Fiction", dto.getGenreName());
        assertTrue(dto.hasLinks());
    }

    @Test
    void toModelWithNoIdentifierReturnsCorrectDTO() {
        // Arrange
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("3C5D126F8B");

        EditionId editionIdDouble = mock(EditionId.class);
        when(editionIdDouble.toString()).thenReturn("E-ABCDEF12");

        NoIdentifier noIdentifierDouble = mock(NoIdentifier.class);
        when(noIdentifierDouble.toString()).thenReturn("no identifier");

        PublicationTypeId typeIdDouble = mock(PublicationTypeId.class);
        when(typeIdDouble.toString()).thenReturn("BOOK");

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("1984");

        Name authorNameDouble = mock(Name.class);
        when(authorNameDouble.toString()).thenReturn("George Orwell");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getCondition()).thenReturn(Condition.GOOD);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice copy"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getEditionId()).thenReturn(editionIdDouble);
        when(editionDouble.getIdentifier()).thenReturn(noIdentifierDouble);
        when(editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(editionDouble.getPublishingYear()).thenReturn(Year.of(1949));
        when(editionDouble.getPublicationTypeId()).thenReturn(typeIdDouble);

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        when(publicationDouble.getReleaseYear()).thenReturn(Year.of(1949));

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(authorNameDouble);

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenre()).thenReturn("Fiction");

        ItemResponseDTOMapper mapper = buildMapper(editionDouble, publicationDouble, authorDouble, genreDouble);

        // Act
        ItemResponseDTO dto = mapper.toModel(itemDouble);

        // Assert
        assertEquals("no identifier", dto.getIdentifier());
        assertTrue(dto.hasLinks());
    }

    @Test
    void toModelEditionNotFoundThrowsNoSuchElementException() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

        IEditionRepo editionRepo = mock(IEditionRepo.class);
        when(editionRepo.ofIdentity(any())).thenReturn(Optional.empty());

        ItemResponseDTOMapper mapper = new ItemResponseDTOMapper(
                editionRepo,
                mock(IPublicationRepo.class),
                mock(IAuthorRepo.class),
                mock(IGenreRepo.class)
        );

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> mapper.toModel(mock(Item.class)));

        assertEquals("Edition does not exist in the repository", ex.getMessage());
    }

    @Test
    void toModelPublicationNotFoundThrowsNoSuchElementException() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

        IEditionRepo editionRepo = mock(IEditionRepo.class);
        IPublicationRepo publicationRepo = mock(IPublicationRepo.class);
        when(editionRepo.ofIdentity(any())).thenReturn(Optional.of(mock(Edition.class)));
        when(publicationRepo.ofIdentity(any())).thenReturn(Optional.empty());

        ItemResponseDTOMapper mapper = new ItemResponseDTOMapper(
                editionRepo, publicationRepo,
                mock(IAuthorRepo.class), mock(IGenreRepo.class)
        );

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> mapper.toModel(mock(Item.class)));

        assertEquals("Publication does not exist in the repository", ex.getMessage());
    }

    @Test
    void toModelAuthorNotFoundThrowsNoSuchElementException() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

        IEditionRepo editionRepo = mock(IEditionRepo.class);
        IPublicationRepo publicationRepo = mock(IPublicationRepo.class);
        IAuthorRepo authorRepo = mock(IAuthorRepo.class);
        when(editionRepo.ofIdentity(any())).thenReturn(Optional.of(mock(Edition.class)));
        when(publicationRepo.ofIdentity(any())).thenReturn(Optional.of(mock(Publication.class)));
        when(authorRepo.ofIdentity(any())).thenReturn(Optional.empty());

        ItemResponseDTOMapper mapper = new ItemResponseDTOMapper(
                editionRepo, publicationRepo,
                authorRepo, mock(IGenreRepo.class)
        );

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> mapper.toModel(mock(Item.class)));

        assertEquals("Author does not exist in the repository", ex.getMessage());
    }

    @Test
    void toModelGenreNotFoundThrowsNoSuchElementException() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

        IEditionRepo editionRepo = mock(IEditionRepo.class);
        IPublicationRepo publicationRepo = mock(IPublicationRepo.class);
        IAuthorRepo authorRepo = mock(IAuthorRepo.class);
        IGenreRepo genreRepo = mock(IGenreRepo.class);
        when(editionRepo.ofIdentity(any())).thenReturn(Optional.of(mock(Edition.class)));
        when(publicationRepo.ofIdentity(any())).thenReturn(Optional.of(mock(Publication.class)));
        when(authorRepo.ofIdentity(any())).thenReturn(Optional.of(mock(Author.class)));
        when(genreRepo.ofIdentity(any())).thenReturn(Optional.empty());

        ItemResponseDTOMapper mapper = new ItemResponseDTOMapper(
                editionRepo, publicationRepo, authorRepo, genreRepo
        );

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> mapper.toModel(mock(Item.class)));

        assertEquals("Genre does not exist in the repository", ex.getMessage());
    }

    @Test
    void toModelWithPictureReturnsPictureUrl() {
        // Arrange
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("3C5D126F8B");

        Picture pictureDouble = mock(Picture.class);
        when(pictureDouble.toString()).thenReturn("http://example.com/image.jpg");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getCondition()).thenReturn(Condition.GOOD);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice copy"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(itemDouble.getPicture()).thenReturn(pictureDouble);

        Edition editionDouble     = mock(Edition.class);
        Publication pubDouble     = mock(Publication.class);
        Author authorDouble       = mock(Author.class);
        Genre genreDouble         = mock(Genre.class);

        when(editionDouble.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionDouble.getPublishingYear()).thenReturn(Year.of(2003));
        when(editionDouble.getPublicationTypeId()).thenReturn(mock(PublicationTypeId.class));
        when(editionDouble.getIdentifier()).thenReturn(mock(ISBN.class));
        when(editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(pubDouble.getTitle()).thenReturn(mock(Title.class));
        when(pubDouble.getReleaseYear()).thenReturn(Year.of(1949));
        when(genreDouble.getGenre()).thenReturn("Fiction");
        when(authorDouble.getName()).thenReturn(mock(Name.class));

        ItemResponseDTOMapper mapper = buildMapper(editionDouble, pubDouble, authorDouble, genreDouble);

        // Act
        ItemResponseDTO dto = mapper.toModel(itemDouble);

        // Assert
        assertEquals("http://example.com/image.jpg", dto.getPicture());
        assertTrue(dto.hasLinks());
    }
}