package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemDetailsDTOTest {

    @Test
    void testItemDetailsDTOConstructor() {

        //SUT
        new ItemDetailsDTO("title","author","type","identifier");

    }

    @Test
    void testGetTitle() {

        //SUT
        ItemDetailsDTO dto = new ItemDetailsDTO("title","author","type","identifier");

        //Act
        String title = dto.getTitle();

        //Assert
        assertEquals("title", title);

    }

    @Test
    void testGetAuthorName() {

        //SUT
        ItemDetailsDTO dto = new ItemDetailsDTO("title","author","type","identifier");

        //Act
        String authorName = dto.getAuthorName();

        //Assert
        assertEquals("author", authorName);

    }

    @Test
    void testGetPublicationType() {

        //SUT
        ItemDetailsDTO dto = new ItemDetailsDTO("title","author","type","identifier");

        //Act
        String publicationType = dto.getPublicationType();

        //Assert
        assertEquals("type", publicationType);

    }

    @Test
    void testGetIdentifier() {

        //SUT
        ItemDetailsDTO dto = new ItemDetailsDTO("title","author","type","identifier");

        //Act
        String identifier = dto.getIdentifier();

        //Assert
        assertEquals("identifier", identifier);

    }

    @Test
    void testToString() {

        //SUT
        ItemDetailsDTO dto = new ItemDetailsDTO("Lord Of The Rings","J.R.R. Tolkien","Book","978-0544003415");

        //Act
        String result = dto.toString();

        //Assert
        assertEquals("Book: Lord Of The Rings, by J.R.R. Tolkien, 978-0544003415", result);

    }

}