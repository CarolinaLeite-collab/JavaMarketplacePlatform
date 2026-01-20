package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTest {
    //Happy path

    @Test
    void buildBook_withAllMandatoryFields_succeeds() {
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        assertNotNull(p);
        assertEquals("BOOK", p.getPublicationType().getPublicationType());
        assertEquals(Year.of(2019), p.getPublicationYear());
    }

    @Test
    void buildMagazine_withoutAuthor_succeeds() {
        Publication p = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new Publisher("Nature"))
                .build();

        assertNotNull(p);
        assertNull(p.getAuthor());
    }
}