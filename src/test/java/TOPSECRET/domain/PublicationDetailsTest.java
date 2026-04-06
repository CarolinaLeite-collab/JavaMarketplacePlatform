package TOPSECRET.domain;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationDetailsTest {

    @Test
    void PublicationDetailsShouldExtractAllFieldsFromPublicationInItem() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Publication publicationDouble = mock(Publication.class);

        Title titleDouble = mock(Title.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        //Identifier identifierDouble = mock(Identifier.class);

        // stub the item to return the publication
        when(itemDouble.get_publication()).thenReturn(publicationDouble);

        // stub the publication fields
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        when(publicationDouble.getAuthor()).thenReturn(authorDouble);
        when(publicationDouble.getPublicationType()).thenReturn(publicationTypeDouble);
        //when(publicationDouble.getIdentifier()).thenReturn(identifierDouble);

        // Act
        PublicationDetails details = new PublicationDetails(itemDouble);

        // Assert
        assertNotNull(details);
        assertEquals(titleDouble, details.getTitle());
        assertEquals(authorDouble, details.getAuthor());
        assertEquals(publicationTypeDouble, details.getPublicationType());
        //assertEquals(identifierDouble, details.getIdentifier());
    }
}

