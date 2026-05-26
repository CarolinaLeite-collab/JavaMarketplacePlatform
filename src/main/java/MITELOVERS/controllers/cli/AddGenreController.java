package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.dto.GenreResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Controller responsible for handling the addition of new genres.
 * <p>
 * This controller is stateless and acts as an intermediary between the user interface
 * and the service layer, delegating genre registration to {@link GenreService}.
 * </p>
 */

@Component
public class AddGenreController {
    private final GenreService _genreService;

    public AddGenreController(GenreService genreService) {
        _genreService = genreService;
    }

    public GenreResponseDTO addGenre(String genreName) {
        return _genreService.registerGenre(genreName);
    }
}
