package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MagazineTest {

    // Constructor test
    @Test
    void test_a_constructor(){

        // Arrange
        Condition condition = Condition.LIKE_NEW;
        PublicationInfo publicationInfo = new PublicationInfo();

        // Act
        Magazine magazine = new Magazine(condition,publicationInfo);
    }

    // Test invalid constructors
    @Test
    void test_invalid_constructor(){

        //Arrange
        Condition condition = null;
        Condition condition2 = Condition.FAIR;
        PublicationInfo publicationInfo = new PublicationInfo();
        PublicationInfo publicationInfo2 = null;

        // Act and assert
        assertThrows(IllegalArgumentException.class, () -> new Magazine(condition,publicationInfo));
        assertThrows(IllegalArgumentException.class, () -> new Magazine(condition2,publicationInfo2));
    }

    // Test the insertion of an invalid appraisal
    @Test
    void test_invalid_appraisal(){
        // Arrange
        Condition condition = Condition.LIKE_NEW;
        PublicationInfo publicationInfo = new PublicationInfo();
        Magazine magazine = new Magazine(condition,publicationInfo);
        Appraisal appraisal = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> magazine.addAppraisal(appraisal));

    }


    // Test inserting a valid appraisal
    @Test
    void test_add_Appraisal(){

        // Arrange
        Condition condition = Condition.LIKE_NEW;
        PublicationInfo publicationInfo = new PublicationInfo();
        Magazine magazine = new Magazine(condition,publicationInfo);

        Price price1 = new Price (200, Currency.GBP);
        Price price2 = new Price (250, Currency.EUR);
        LocalDateTime date1 = LocalDateTime.now();
        LocalDateTime date2 = LocalDateTime.now();
        String description1 = "test";
        String description2 = "test again";

        Appraisal appraisal1 = new Appraisal(price1,date1,description1);
        Appraisal appraisal2 = new Appraisal(price2,date2,description2);


        // Act and assert
        magazine.addAppraisal(appraisal1);

        assertEquals(1, magazine.getAppraisals().size());

        magazine.addAppraisal(appraisal2);

        assertEquals(2, magazine.getAppraisals().size());

    }

    @Test
    void test_list_of_appraisals(){

        // Arrange
        Condition condition = Condition.LIKE_NEW;
        PublicationInfo publicationInfo = new PublicationInfo();
        Magazine magazine = new Magazine(condition,publicationInfo);

        Price price1 = new Price (200, Currency.GBP);
        Price price2 = new Price (250, Currency.EUR);
        LocalDateTime date1 = LocalDateTime.now();
        LocalDateTime date2 = LocalDateTime.now();
        String description1 = "test";
        String description2 = "test again";

        Appraisal appraisal1 = new Appraisal(price1,date1,description1);
        Appraisal appraisal2 = new Appraisal(price2,date2,description2);
        Appraisal appraisal3 = new Appraisal(price2,date2,description2);

        magazine.addAppraisal(appraisal1);
        magazine.addAppraisal(appraisal2);

        // Act
        List<Appraisal> appraisalsCopy = magazine.getAppraisals();

        // Assert that List is immutable (cannot add new elements to list)
        assertThrows(UnsupportedOperationException.class, () -> appraisalsCopy.add(appraisal3));

        // Assert that appraisalsCopy has the correct content
        assertEquals(2, appraisalsCopy.size());
        assertEquals(appraisal1,appraisalsCopy.get(0));
        assertEquals(appraisal2,appraisalsCopy.get(1));

    }

    // Test returning condition
    @Test
    void test_return_Condition(){
        // Arrange
        Condition condition = Condition.LIKE_NEW;
        PublicationInfo publicationInfo = new PublicationInfo();
        Magazine magazine = new Magazine(condition,publicationInfo);

        // Act and assert
        assertEquals(Condition.LIKE_NEW, magazine.getCondition());

    }

}