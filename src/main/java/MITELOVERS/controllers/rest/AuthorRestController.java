package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.AuthorLinkProvider;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.AuthorResponseDTO;
import MITELOVERS.dto.request.AuthorRequestDTO;
import MITELOVERS.mapper.AuthorResponseDTOMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/authors")
public class AuthorRestController {

    private final AuthorService _authorService;
    private final AuthorLinkProvider _authorLinkProvider;
    private final UserService _userService;
    private final AuthorResponseDTOMapper _authorResponseDTOMapper;

    public AuthorRestController(AuthorService authorService,
                                AuthorLinkProvider authorLinkProvider,
                                UserService userService,
                                AuthorResponseDTOMapper authorResponseDTOMapper) {
        _authorService = authorService;
        _authorLinkProvider = authorLinkProvider;
        _userService = userService;
        _authorResponseDTOMapper = authorResponseDTOMapper;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {
        User user = _userService.getUserByEmail(email);
        RepresentationModel<?> model = new RepresentationModel<>();
        _authorLinkProvider.getLinks(user).forEach(model::add);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> registerAuthorAndReturnDTO(
            @RequestBody AuthorRequestDTO info) {

        // 1. Service handles domain logic and returns domain object
        Author savedAuthor = _authorService.registerAuthor(info.getAuthorName());

        // 2. Controller converts domain object to DTO
        AuthorResponseDTO responseDTO = _authorResponseDTOMapper.toModel(savedAuthor);

        // 3. Controller adds HATEOAS link
        responseDTO.add(linkTo(methodOn(AuthorRestController.class)
                .getAuthorById(responseDTO.getAuthorId())).withSelfRel());

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        Iterable<Author> authors = _authorService.getAllAuthors();

        List<AuthorResponseDTO> dtoList = new ArrayList<>();
        for (Author author : authors) {
            AuthorResponseDTO dto = _authorResponseDTOMapper.toModel(author);
            dto.add(linkTo(methodOn(AuthorRestController.class)
                    .getAuthorById(dto.getAuthorId())).withSelfRel());
            dtoList.add(dto);
        }

        if (dtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable String id) {
        Author author = _authorService.getAuthorById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Author not found"));

        AuthorResponseDTO dto = _authorResponseDTOMapper.toModel(author);
        dto.add(linkTo(methodOn(AuthorRestController.class)
                .getAuthorById(dto.getAuthorId())).withSelfRel());

        return ResponseEntity.ok(dto);
    }
}