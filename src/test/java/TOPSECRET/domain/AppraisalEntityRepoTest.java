package TOPSECRET.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityRepoTest {

    private AppraisalEntityRepo repo;

    @BeforeEach
    void setUp() {
        repo = new AppraisalEntityRepo();
    }

    @Test
    void should_register_new_appraisalEntity() {
        List<Genre> selfHelp = new ArrayList<>();
        selfHelp.add(new Genre("Self-Help"));
        List<PublicationType> bookType = new ArrayList<>();
        bookType.add(new PublicationType("Books"));
        Name helpingzTM = new Name("Helpingz TM");

        AppraisalEntity entity = repo.registerNewAppraisalEntity(helpingzTM, bookType, selfHelp);

        assertNotNull(entity);
        assertEquals(helpingzTM, entity.getName());
    }

    @Test
    void should_throw_exception_when_duplicate_dame() {
        List<Genre> genre = new ArrayList<>();
        genre.add(new Genre("Self-Help"));
        List<PublicationType> pubType = new ArrayList<>();
        pubType.add(new PublicationType("Books"));
        Name name = new Name("Helpingz TM");

        repo.registerNewAppraisalEntity(name, pubType, genre);

        assertThrows(IllegalStateException.class, () ->
                repo.registerNewAppraisalEntity(name, pubType, genre));
    }

    @Test
    void should_throw_exception_when_name_is_null() {
        List<PublicationType> pubType = new ArrayList<>();
        List<Genre> genre = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () ->
                repo.registerNewAppraisalEntity(null, pubType, genre));
    }


    @Test
    void should_throw_exception_when_publication_types_nuull() {
        Name name = new Name("HelpingzTM");
        List<Genre> genre = new ArrayList<>();
        genre.add(new Genre("Self-Help"));

        assertThrows(IllegalArgumentException.class, () ->
                repo.registerNewAppraisalEntity(name, null, genre));
    }

    @Test
    void should_throw_exception_when_genres_null() {
        Name name = new Name("HelpingzTM");
        List<PublicationType> pubType = new ArrayList<>();
        pubType.add(new PublicationType("Book"));

        assertThrows(IllegalArgumentException.class, () ->
                repo.registerNewAppraisalEntity(name, pubType, null));
    }
}
