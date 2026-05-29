package MITELOVERS.controllers.rest;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.request.PublicationRequestDTO;
import MITELOVERS.dto.response.PublicationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import MITELOVERS.applicationservices.PublicationService;

import java.time.Year;
import java.util.List;

/**
 * REST controller responsible for exposing publication-related endpoints
 * via HTTP endpoints.
 */

@RestController
@RequestMapping("/publications")
@Validated
public class PublicationRestController {

    private final PublicationService _publicationService;

    public PublicationRestController(PublicationService publicationService) {
        _publicationService = publicationService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicationResponseDTO> registerPublicationAndReturnDTO(
            @Valid @RequestBody PublicationRequestDTO info) {


        PublicationResponseDTO publicationResponseDTO =
                _publicationService.registerPublication(
                        new Title(info.getTitle()),
                        new AuthorId(info.getAuthorId()),
                        Year.of(info.getReleaseYear()),
                        new GenreId(info.getGenreId())
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publicationResponseDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicationResponseDTO>> getAllPublications() {

        List<PublicationResponseDTO> publications =
                _publicationService.getAllPublications();

        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(publications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationResponseDTO> getPublicationById(
            @PathVariable String id) {

        PublicationResponseDTO publication =
                _publicationService.getPublicationById(id);

        return ResponseEntity.ok(publication);
    }

}
