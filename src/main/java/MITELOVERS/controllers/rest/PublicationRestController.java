package MITELOVERS.controllers.rest;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.PublicationRequestDTO;
import MITELOVERS.dto.PublicationResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class PublicationRestController {

    private final PublicationService _publicationService;

    public PublicationRestController(PublicationService publicationService) {
        _publicationService = publicationService;
    }

    @PostMapping
    public ResponseEntity<PublicationResponseDTO> registerPublicationAndReturnDTO(
            @RequestBody PublicationRequestDTO info) {

        PublicationResponseDTO publicationResponseDTO = _publicationService.registerPublication(
                new Title(info.getTitle()),
                new AuthorId(info.getAuthorId()),
                Year.of(info.getReleaseYear()),
                new GenreId(info.getGenreId())
        );

        return new ResponseEntity<>(publicationResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PublicationResponseDTO>> getAllPublications() {

        List<PublicationResponseDTO> publications =
                _publicationService.getAllPublications();

        return ResponseEntity.ok(publications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationResponseDTO> getPublicationById(
            @PathVariable String id) {

        return ResponseEntity.ok().build();
    }

}
