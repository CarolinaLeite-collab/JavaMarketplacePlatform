package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.dto.response.GenreResponseDTO;
import MITELOVERS.mapper.GenreResponseDTOMapper;
import org.springframework.stereotype.Component;

/**
 * Controller responsible for handling the addition of new genres via the CLI.
 */
@Component
public class AddGenreController {

    private final GenreService _genreService;
    private final GenreResponseDTOMapper _genreResponseDTOMapper;

    public AddGenreController(GenreService genreService, GenreResponseDTOMapper genreResponseDTOMapper) {
        _genreService = genreService;
        _genreResponseDTOMapper = genreResponseDTOMapper;
    }

    public GenreResponseDTO addGenre(String genreName) {
        Genre savedGenre = _genreService.registerGenre(genreName);
        return _genreResponseDTOMapper.toModel(savedGenre);
    }
}