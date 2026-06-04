package MITELOVERS.mapper;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
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
    void shouldMapPublicationToResponseDTO() {
        // Arrange
        Publication publication = mock(Publication.class);

        PublicationId publicationId = mock(PublicationId.class);
        Title title = mock(Title.class);
        AuthorId authorId = mock(AuthorId.class);
        GenreId genreId = mock(GenreId.class);

        when(publication.identity()).thenReturn(publicationId);
        when(publicationId.toString()).thenReturn("PUB-001");

        when(publication.getTitle()).thenReturn(title);
        when(title.toString()).thenReturn("Delirious New York");

        when(publication.getAuthorId()).thenReturn(authorId);
        when(authorId.toString()).thenReturn("HERBERTO_HELDER");

        when(publication.getReleaseYear()).thenReturn(Year.of(1978));

        when(publication.getGenreId()).thenReturn(genreId);
        when(genreId.toString()).thenReturn("ARCHITECTURE");

        PublicationResponseDTOMapper mapper = new PublicationResponseDTOMapper();

        // Act
        PublicationResponseDTO dto = mapper.toModel(publication);

        // Assert
        assertEquals("PUB-001", dto.getPublicationId());
        assertEquals("Delirious New York", dto.getTitle());
        assertEquals("HERBERTO_HELDER", dto.getAuthorId());
        assertEquals(1978, dto.getReleaseYear());
        assertEquals("ARCHITECTURE", dto.getGenreId());
    }
}