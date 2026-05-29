package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.dto.AuthorResponseDTO;
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

    public CreateAuthorController(AuthorService authorService) {
        _authorService = authorService;
    }

    public AuthorResponseDTO createAuthor(String authorName) {
        return _authorService.registerAuthor(authorName);
    }
}
