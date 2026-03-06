package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationDetailsTest {

   @Test
    void publicationDetailsShouldExtractAllFieldsFromPublication() {
        // Arrange
        Publication _publicationDouble = mock(Publication.class);
        Title _titleDouble = mock(Title.class);
        Author _authorDouble = mock(Author.class);
        PublicationType _publicationTypeDouble = mock(PublicationType.class);
        Identifier _identifierDouble = mock(Identifier.class);

       when(_publicationDouble.getTitle()).thenReturn(_titleDouble);
       when(_publicationDouble.getAuthor()).thenReturn(_authorDouble);
       when(_publicationDouble.getPublicationType()).thenReturn(_publicationTypeDouble);
       when(_publicationDouble.getIdentifier()).thenReturn(_identifierDouble);


        // Act
        PublicationDetails details = new PublicationDetails(_publicationDouble);

        // Assert
        assertNotNull(details);
        assertEquals(_titleDouble, details.getTitle());
        assertEquals(_authorDouble, details.getAuthor());
        assertEquals(_publicationTypeDouble, details.getPublicationType());
        assertEquals(_identifierDouble, details.getIdentifier());

    }


    }

