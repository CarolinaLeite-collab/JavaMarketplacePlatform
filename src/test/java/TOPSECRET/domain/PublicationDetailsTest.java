package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.PublicationTypeId;
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
        AuthorId authorIdDouble = mock(AuthorId.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
        //Identifier identifierDouble = mock(Identifier.class);

        // stub the item to return the publication
        when(itemDouble.get_publication()).thenReturn(publicationDouble);

        // stub the publication fields
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        when(publicationDouble.getAuthorId()).thenReturn(authorIdDouble);
        when(publicationDouble.getPublicationTypeId()).thenReturn(publicationTypeIdDouble);
        //when(publicationDouble.getIdentifier()).thenReturn(identifierDouble);

        // Act
        PublicationDetails details = new PublicationDetails(itemDouble);

        // Assert
        assertNotNull(details);
        assertEquals(titleDouble, details.getTitle());
        assertEquals(authorIdDouble, details.getAuthorId());
        assertEquals(publicationTypeIdDouble, details.getPublicationTypeId());
        //assertEquals(identifierDouble, details.getIdentifier());
    }
}

