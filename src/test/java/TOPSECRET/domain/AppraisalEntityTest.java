package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppraisalEntityTest {

    private Name name;
    private List<PublicationType> pubType;
    private List<Genre> genre;

    @BeforeEach
    void setUp() {
        name = new Name("HelpingzTM");
        pubType = new ArrayList<>();
        genre = new ArrayList<>();
    }

    @Test
    void should_create_appraisal_entity_with_correct_data() {
        PublicationType type1 = new PublicationType("ugly book");
        PublicationType type2 = new PublicationType("pretty book");
        Genre genre1 = new Genre("romance");
        List<PublicationType> types = pubType;
        List<Genre> genres = genre;

        pubType.add(type1);
        pubType.add(type2);
        genre.add(genre1);


        AppraisalEntity entity = new AppraisalEntity(name, types, genres);

        assertEquals(name, entity.getName());
        assertEquals(pubType, entity.getPublicationTypes());
        assertEquals(genre, entity.getGenres());
    }

    @Test
    void should_copy_lists_correctly() {
        PublicationType type1 = new PublicationType("ugly book");
        PublicationType type2 = new PublicationType("pretty book");
        Genre genre1 = new Genre("romance");

        pubType.add(type1);
        pubType.add(type2);
        genre.add(genre1);

        AppraisalEntity entity = new AppraisalEntity(name, pubType, genre);

        assertEquals(2, entity.getPublicationTypes().size());
        assertEquals(1, entity.getGenres().size());
        assertTrue(entity.getPublicationTypes().contains(type2));
        assertTrue(entity.getPublicationTypes().contains(type1));
        assertTrue(entity.getGenres().contains(genre1));
    }

}
