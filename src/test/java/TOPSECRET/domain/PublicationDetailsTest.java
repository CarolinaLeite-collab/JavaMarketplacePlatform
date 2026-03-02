package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class PublicationDetailsTest {

   @Test
    void publicationDetailsShouldExtractAllFieldsFromPublication() {
        // Arrange
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        // Act
        PublicationDetails details = new PublicationDetails(p);

        // Assert
        assertNotNull(details);
        assertEquals(p.getTitle(), details.getTitle());
        assertEquals(p.getAuthor(), details.getAuthor());
        assertEquals(p.getPublicationType(), details.getPublicationType());
        assertEquals(p.getIdentifier(), details.getIdentifier());

    }


    }

