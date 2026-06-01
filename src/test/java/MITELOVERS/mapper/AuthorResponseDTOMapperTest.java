package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.dto.response.AuthorResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
class AuthorResponseDTOMapperTest {

    @Test
    void toResponseDTOBuildsDto() {
        // Arrange
        AuthorFactory factory = new AuthorFactory();
        Author author = factory.createAuthor(new AuthorId("SAMPLE"), new Name("Sample Name"));
        AuthorResponseDTOMapper mapper = new AuthorResponseDTOMapper();

        // Act
        AuthorResponseDTO dto = mapper.toModel(author);

        // Assert
        assertNotNull(dto);
        assertEquals("SAMPLE", dto.getAuthorId());
        assertEquals("Sample Name", dto.getAuthorName());
    }
}
