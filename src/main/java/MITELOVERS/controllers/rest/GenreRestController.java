package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.GenreLinkProvider;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.request.GenreRequestDTO;
import MITELOVERS.dto.response.GenreResponseDTO;
import MITELOVERS.mapper.GenreResponseDTOMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing genre-related endpoints via HTTP.
 */
@RestController
@RequestMapping("/genres")
public class GenreRestController {

    private final GenreService _genreService;
    private final GenreLinkProvider _genreLinkProvider;
    private final UserService _userService;
    private final GenreResponseDTOMapper _genreResponseDTOMapper;

    public GenreRestController(GenreService genreService,
                               GenreLinkProvider genreLinkProvider,
                               UserService userService,
                               GenreResponseDTOMapper genreResponseDTOMapper) {
        _genreService = genreService;
        _genreLinkProvider = genreLinkProvider;
        _userService = userService;
        _genreResponseDTOMapper = genreResponseDTOMapper;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {
        User user = _userService.getUserByEmail(email);
        RepresentationModel<?> model = new RepresentationModel<>();
        _genreLinkProvider.getLinks(user).forEach(model::add);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<GenreResponseDTO> registerGenreAndReturnDTO(
            @RequestBody GenreRequestDTO info) {

        Genre savedGenre = _genreService.registerGenre(info.getGenreName());
        GenreResponseDTO genreResponseDTO = _genreResponseDTOMapper.toModel(savedGenre);

        genreResponseDTO.add(linkTo(methodOn(GenreRestController.class)
                .getGenreById(genreResponseDTO.getGenreId())).withSelfRel());

        return new ResponseEntity<>(genreResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {
        Iterable<Genre> genres = _genreService.getAllGenres();
        List<GenreResponseDTO> dtoList = new ArrayList<>();

        for (Genre genre : genres) {
            GenreResponseDTO dto = _genreResponseDTOMapper.toModel(genre);
            dto.add(linkTo(methodOn(GenreRestController.class)
                    .getGenreById(dto.getGenreId())).withSelfRel());
            dtoList.add(dto);
        }

        if (dtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenreById(@PathVariable String id) {
        Genre genre = _genreService.getGenreById(id)
                .orElseThrow(() -> new NoSuchElementException("Genre not found"));

        GenreResponseDTO dto = _genreResponseDTOMapper.toModel(genre);
        dto.add(linkTo(methodOn(GenreRestController.class)
                .getGenreById(dto.getGenreId())).withSelfRel());

        return ResponseEntity.ok(dto);
    }
}