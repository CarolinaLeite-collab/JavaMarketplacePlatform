package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.response.PublicationResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationResponseDTOMapperTest {

    @Test
    void shouldMapPublicationsToResponseDTO() {

        // Arrange
        Publication publication = mock(Publication.class);
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);

        PublicationId publicationId = mock(PublicationId.class);
        Title title = mock(Title.class);
        Name authorName = mock(Name.class);

        when(publication.identity()).thenReturn(publicationId);
        when(publicationId.toString()).thenReturn("PUB-001");

        when(publication.getTitle()).thenReturn(title);
        when(title.toString()).thenReturn("Delirious New York");

        when(author.getName()).thenReturn(authorName);
        when(authorName.toString()).thenReturn("Rem Koolhaas");

        when(publication.getReleaseYear()).thenReturn(Year.of(1978));

        when(genre.getGenre()).thenReturn("Architecture");

        PublicationResponseDTOMapper mapper =
                new PublicationResponseDTOMapper();

        // Act
        PublicationResponseDTO dto =
                mapper.toResponseDTO(publication, author, genre);

        // Assert
        assertEquals("PUB-001", dto.getPublicationId());
        assertEquals("Delirious New York", dto.getTitle());
        assertEquals("Rem Koolhaas", dto.getAuthorName());
        assertEquals(1978, dto.getReleaseYear());
        assertEquals("Architecture", dto.getGenreName());
    }
}