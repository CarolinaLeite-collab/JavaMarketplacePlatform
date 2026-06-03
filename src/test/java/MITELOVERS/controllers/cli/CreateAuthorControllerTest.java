package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.domain.author.Author;
import MITELOVERS.dto.response.AuthorResponseDTO;
import MITELOVERS.mapper.AuthorResponseDTOMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class CreateAuthorControllerTest {

    @InjectMocks
    private CreateAuthorController _controller;

    @Mock
    private AuthorService _authorService;

    @Mock
    private AuthorResponseDTOMapper _authorResponseDTOMapper;

    @Test
    void createAuthorReturnsResponseDTOFromService() {
        // Arrange
        String authorName = "Sample Name";
        Author mockAuthor = mock(Author.class);
        AuthorResponseDTO responseDTODouble = new AuthorResponseDTO("SAMPLE", "Sample Name");

        when(_authorService.registerAuthor(authorName)).thenReturn(mockAuthor);
        when(_authorResponseDTOMapper.toModel(mockAuthor)).thenReturn(responseDTODouble);

        // Act
        AuthorResponseDTO result = _controller.createAuthor(authorName);

        // Assert
        assertSame(responseDTODouble, result);
        verify(_authorService).registerAuthor(authorName);
        verify(_authorResponseDTOMapper).toModel(mockAuthor);
    }

    @Test
    void createAuthorThrowsWhenServiceFails() {
        // Arrange
        String authorName = "Sample Name";

        when(_authorService.registerAuthor(authorName))
                .thenThrow(new IllegalStateException("Author already exists in the repository"));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> _controller.createAuthor(authorName));

        assertEquals("Author already exists in the repository", exception.getMessage());
        verify(_authorService).registerAuthor(authorName);
    }
}