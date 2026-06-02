package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RequestMapping("/publishingCompanies")
@RestController
public class PublishingCompanyRestController {

    private final PublishingCompanyService _publishingCompanyService;

    public PublishingCompanyRestController(PublishingCompanyService publishingCompanyService) {

        _publishingCompanyService = publishingCompanyService;

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerEdition(@RequestBody PublishingCompanyResponseDTO dto) {

        try{

            PublishingCompanyResponseDTO result = _publishingCompanyService.registerPublishingCompany(dto);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllPublishingCompanies() {

        try{

            List<PublishingCompanyResponseDTO> result = _publishingCompanyService.getAllPublishingCompanies();

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "/{publishingCompanyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPublishingCompanyById(@RequestParam String publishingCompanyId) {

        try{

            PublishingCompanyResponseDTO dto = _publishingCompanyService.getPublishingCompanyById(publishingCompanyId);

            return new ResponseEntity<>(dto, HttpStatus.OK);

        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
