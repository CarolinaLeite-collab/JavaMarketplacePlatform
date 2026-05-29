package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.dto.GenreRequestDTO;
import MITELOVERS.dto.GenreResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller responsible for exposing genre-related endpoints via HTTP endpoints.
 */

@RestController
@RequestMapping("/genres")
public class GenreRestController {

    private final GenreService _genreService;

    public GenreRestController(GenreService genreService) {
        _genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<GenreResponseDTO> registerGenreAndReturnDTO(
            @RequestBody GenreRequestDTO info) {

        GenreResponseDTO genreResponseDTO = _genreService.registerGenre(
                info.getGenreName()
        );

        return new ResponseEntity<>(genreResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {

        List<GenreResponseDTO> genres = _genreService.getAllGenres();

        if (genres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenreById(
            @PathVariable String id) {

        return ResponseEntity.ok().build();
    }
}
