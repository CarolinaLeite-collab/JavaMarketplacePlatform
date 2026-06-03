package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.domain.author.Author;
import MITELOVERS.dto.response.AuthorResponseDTO;
import MITELOVERS.mapper.AuthorResponseDTOMapper;
import org.springframework.stereotype.Component;

/**
 * Controller responsible for creating new authors in the system.
 * <p>
 * This controller delegates the creation of authors to {@link AuthorService}.
 * </p>
 */
@Component
public class CreateAuthorController {

    private final AuthorService _authorService;
    private final AuthorResponseDTOMapper _authorResponseDTOMapper;

    public CreateAuthorController(AuthorService authorService, AuthorResponseDTOMapper authorResponseDTOMapper) {
        _authorService = authorService;
        _authorResponseDTOMapper = authorResponseDTOMapper;
    }

    public AuthorResponseDTO createAuthor(String authorName) {

        Author savedAuthor = _authorService.registerAuthor(authorName);

        return _authorResponseDTOMapper.toModel(savedAuthor);
    }
}