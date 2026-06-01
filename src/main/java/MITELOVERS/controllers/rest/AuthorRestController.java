package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.dto.response.AuthorResponseDTO;
import MITELOVERS.dto.request.AuthorRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing author-related endpoints via HTTP endpoints.
 */

@RestController
@RequestMapping("/authors")
public class AuthorRestController {

    private final AuthorService _authorService;

    public AuthorRestController(AuthorService authorService) {
        _authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> registerAuthorAndReturnDTO(
            @RequestBody AuthorRequestDTO info) {

        AuthorResponseDTO authorResponseDTO = _authorService.registerAuthor(
                info.getAuthorName()
        );

        authorResponseDTO.add(
                linkTo(
                        methodOn(AuthorRestController.class)
                                .getAuthorById(authorResponseDTO.getAuthorId())
                ).withSelfRel()
        );

        return new ResponseEntity<>(authorResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {

        List<AuthorResponseDTO> authors = _authorService.getAllAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (AuthorResponseDTO author : authors) {
            author.add(
                    linkTo(
                            methodOn(AuthorRestController.class)
                                    .getAuthorById(author.getAuthorId())
                    ).withSelfRel()
            );
        }

        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(
            @PathVariable String id) {

        return ResponseEntity.ok().build();
    }
}
