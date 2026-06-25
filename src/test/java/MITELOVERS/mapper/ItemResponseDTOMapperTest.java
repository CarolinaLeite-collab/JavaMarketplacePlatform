package MITELOVERS.mapper;

import MITELOVERS.applicationservices.ItemService.ItemRelated;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.ItemResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemResponseDTOMapperTest {

    private final ItemResponseDTOMapper mapper = new ItemResponseDTOMapper();

    private void setRequestContext() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    private ItemRelated buildRelated(Edition edition, Publication publication,
                                     Author author, Genre genre, PublishingCompany publisher) {
        return new ItemRelated(edition, publication, author, genre, publisher);
    }

    @Test
    void toModelReturnsCorrectDTOWithAllFields() {
        setRequestContext();

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

        PublishingCompany publisherDouble = mock(PublishingCompany.class);
        when(publisherDouble.getPublishingCompanyName()).thenReturn("Secker & Warburg");

        ItemRelated related = buildRelated(editionDouble, publicationDouble, authorDouble, genreDouble, publisherDouble);

        ItemResponseDTO dto = mapper.toModel(itemDouble, related);

        assertEquals("3C5D126F8B",          dto.getItemId());
        assertEquals("GOOD",                dto.getCondition());
        assertEquals("1984",                dto.getTitle());
        assertEquals("George Orwell",       dto.getAuthorName());
        assertEquals("978-0-451-52493-5",   dto.getIdentifier());
        assertEquals("BOOK",                dto.getPublicationTypeName());
        assertEquals("Fiction",             dto.getGenreName());
        assertEquals("Secker & Warburg",    dto.getPublisherName());
        assertTrue(dto.hasLinks());
    }

    @Test
    void toModelWithNoIdentifierReturnsCorrectDTO() {
        setRequestContext();

        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("3C5D126F8B");

        NoIdentifier noIdentifierDouble = mock(NoIdentifier.class);
        when(noIdentifierDouble.toString()).thenReturn("no identifier");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getCondition()).thenReturn(Condition.GOOD);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice copy"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionDouble.getIdentifier()).thenReturn(noIdentifierDouble);
        when(editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(editionDouble.getPublishingYear()).thenReturn(Year.of(1949));
        when(editionDouble.getPublicationTypeId()).thenReturn(mock(PublicationTypeId.class));

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(mock(Title.class));
        when(publicationDouble.getReleaseYear()).thenReturn(Year.of(1949));

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(mock(Name.class));

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenre()).thenReturn("Fiction");

        PublishingCompany publisherDouble = mock(PublishingCompany.class);
        when(publisherDouble.getPublishingCompanyName()).thenReturn("Secker & Warburg");

        ItemRelated related = buildRelated(editionDouble, publicationDouble, authorDouble, genreDouble, publisherDouble);

        ItemResponseDTO dto = mapper.toModel(itemDouble, related);

        assertEquals("no identifier", dto.getIdentifier());
        assertTrue(dto.hasLinks());
    }

    @Test
    void toModelWithPictureReturnsPictureUrl() {
        setRequestContext();

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

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionDouble.getIdentifier()).thenReturn(mock(ISBN.class));
        when(editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(editionDouble.getPublishingYear()).thenReturn(Year.of(2003));
        when(editionDouble.getPublicationTypeId()).thenReturn(mock(PublicationTypeId.class));

        Publication pubDouble = mock(Publication.class);
        when(pubDouble.getTitle()).thenReturn(mock(Title.class));
        when(pubDouble.getReleaseYear()).thenReturn(Year.of(1949));

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(mock(Name.class));

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenre()).thenReturn("Fiction");

        PublishingCompany publisherDouble = mock(PublishingCompany.class);
        when(publisherDouble.getPublishingCompanyName()).thenReturn("Some Publisher");

        ItemRelated related = buildRelated(editionDouble, pubDouble, authorDouble, genreDouble, publisherDouble);

        ItemResponseDTO dto = mapper.toModel(itemDouble, related);

        assertEquals("http://example.com/image.jpg", dto.getPicture());
        assertTrue(dto.hasLinks());
    }

    @Test
    void toModelWithNotOnSaleStatusAddsCreateAuctionLink() {
        setRequestContext();

        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("3C5D126F8B");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getCondition()).thenReturn(Condition.GOOD);
        when(itemDouble.getDescription()).thenReturn(new Description("Nice copy"));
        when(itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getEditionId()).thenReturn(mock(EditionId.class));
        when(editionDouble.getIdentifier()).thenReturn(mock(NoIdentifier.class));
        when(editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(editionDouble.getPublishingYear()).thenReturn(Year.of(1949));
        when(editionDouble.getPublicationTypeId()).thenReturn(mock(PublicationTypeId.class));

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(mock(Title.class));
        when(publicationDouble.getReleaseYear()).thenReturn(Year.of(1949));

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(mock(Name.class));

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenre()).thenReturn("Fiction");

        PublishingCompany publisherDouble = mock(PublishingCompany.class);
        when(publisherDouble.getPublishingCompanyName()).thenReturn("Secker & Warburg");

        ItemRelated related = buildRelated(editionDouble, publicationDouble, authorDouble, genreDouble, publisherDouble);

        ItemResponseDTO dto = mapper.toModel(itemDouble, related);

        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("create-auction")));
    }
}
