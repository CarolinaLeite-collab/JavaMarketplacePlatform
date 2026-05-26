package MITELOVERS.controllers.rest;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.PublicationRequestDTO;
import MITELOVERS.dto.PublicationResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import MITELOVERS.services.PublicationService;

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
                new Title(info.get_title()),
                new AuthorId(info.get_authorId()),
                Year.of(info.get_releaseYear()),
                new GenreId(info.get_genreId())
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
