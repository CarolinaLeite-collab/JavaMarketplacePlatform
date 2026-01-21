package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterNewAppraisalEntityControllerTest {
    private RegisterNewAppraisalEntityController controller;
    private PublicationTypeRepo publicationTypeRepo;
    private GenreRepo genreRepo;
    private AppraisalEntityRepo appraisalEntityRepo;

    @BeforeEach
    void setUp() {
        publicationTypeRepo = new PublicationTypeRepo();
        genreRepo = new GenreRepo();
        appraisalEntityRepo = new AppraisalEntityRepo();

        publicationTypeRepo.create("book");
        genreRepo.create("romance");
        genreRepo.create("fantasy");

        controller = new RegisterNewAppraisalEntityController(
                appraisalEntityRepo, publicationTypeRepo, genreRepo);
    }

    @Test
    void should_return_false_if_fields_are_empty() {
        List types = controller.getPublicationTypes();
        List genres = controller.getGenres();

        assertFalse(types.isEmpty());
        assertFalse(genres.isEmpty());
    }

    @Test
    void should_get_publicationTypes_from_repo() {
        List types = controller.getPublicationTypes();

        assertEquals(types, publicationTypeRepo.getAll());
    }

    @Test
    void should_get_genres_from_repo() {
        List genres = controller.getGenres();

        assertEquals(genres, genreRepo.getListOfOfficialGenres());
    }

    @Test
    void should_successfully_call_appraisal_entity_creation_method() {
        AppraisalEntity newAppEnt = controller.registerNewAppraisalEntity(new Name ("publicationName"), new ArrayList<PublicationType>(), new ArrayList<Genre>());

        assertNotNull(newAppEnt);
    }
}
