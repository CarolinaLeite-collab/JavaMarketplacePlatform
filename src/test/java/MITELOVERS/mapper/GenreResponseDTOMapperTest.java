package MITELOVERS.mapper;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.dto.GenreResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
class GenreResponseDTOMapperTest {

    @Test
    void toResponseDTOBuildsDto() {
        // Arrange
        GenreFactory factory = new GenreFactory();
        Genre genre = factory.createGenre("Sample");
        GenreResponseDTOMapper mapper = new GenreResponseDTOMapper();

        // Act
        GenreResponseDTO dto = mapper.toModel(genre);

        // Assert
        assertNotNull(dto);
        assertEquals("SAMPLE", dto.getGenreId());
        assertEquals("Sample", dto.getGenreName());
    }
}
