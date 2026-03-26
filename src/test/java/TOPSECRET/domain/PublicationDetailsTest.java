package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationDetailsTest {

   @Test
    void publicationDetailsShouldExtractAllFieldsFromPublication() {
        // Arrange
        Item _itemDouble = mock(Item.class);
        Title _titleDouble = mock(Title.class);
        Author _authorDouble = mock(Author.class);
        Publication _publicationDouble = mock(Publication.class);
        PublicationType _publicationTypeDouble = mock(PublicationType.class);
        Identifier _identifierDouble = mock(Identifier.class);

        when(_itemDouble.getPublication()).thenReturn(_publicationDouble);

       when(_itemDouble.getPublication().getTitle()).thenReturn(_titleDouble);
       when(_itemDouble.getPublication().getAuthor()).thenReturn(_authorDouble);
       when(_itemDouble.getPublication().getPublicationType()).thenReturn(_publicationTypeDouble);
       when(_itemDouble.getPublication().getIdentifier()).thenReturn(_identifierDouble);


        // SUT
        PublicationDetails details = new PublicationDetails(_itemDouble);

        //Act + Assert
        assertNotNull(details);
        assertEquals(_titleDouble, details.getTitle());
        assertEquals(_authorDouble, details.getAuthor());
        assertEquals(_publicationTypeDouble, details.getPublicationType());
        assertEquals(_identifierDouble, details.getIdentifier());

    }


    }

