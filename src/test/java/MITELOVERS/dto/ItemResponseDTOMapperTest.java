package MITELOVERS.dto;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.ItemResponseDTO;
import MITELOVERS.mapper.ItemResponseDTOMapper;
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

    @Test
    void toDTOReturnsCorrectDTOWithAllFields() {
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

        // Act
        ItemResponseDTOMapper mapper = new ItemResponseDTOMapper();
        ItemResponseDTO dto = mapper.toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble);

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
    void toDTOWithNoIdentifierReturnsCorrectDTO() {
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

        // Act
        ItemResponseDTOMapper mapper = new ItemResponseDTOMapper();
        ItemResponseDTO dto = mapper.toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble);

        // Assert
        assertEquals("no identifier", dto.getIdentifier());
        assertTrue(dto.hasLinks());
    }
}
