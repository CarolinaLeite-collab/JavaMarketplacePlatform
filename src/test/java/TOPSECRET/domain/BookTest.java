package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    // Constructor test
    @Test
    void test_a_constructor() {

        // Arrange

        PublicationInfo publicationInfo = new PublicationInfo(
                new Title ("title"),
                Genre.ACTION,
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISBN(9789720048758L),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );

        Condition condition = Condition.LIKE_NEW;

        // Act
        Book book = new Book(publicationInfo, condition);
    }

    // Test invalid constructors
    @Test
    void test_invalid_constructor() {

        // Arrange

        PublicationInfo publicationInfo = new PublicationInfo(
                new Title ("title"),
                Genre.ACTION,
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISBN(9789720048758L),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );

        Condition condition = null;
        Condition condition2 = Condition.FAIR;
        PublicationInfo publicationInfo2 = null;

        // Act and assert
        assertThrows(IllegalArgumentException.class, () -> new Book(publicationInfo, condition));
        assertThrows(IllegalArgumentException.class, () -> new Book(publicationInfo2, condition2));
    }

    // Test the insertion of an invalid appraisal
    @Test
    void test_invalid_appraisal() {

        // Arrange

        PublicationInfo publicationInfo = new PublicationInfo(
                new Title ("title"),
                Genre.ACTION,
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISBN(9789720048758L),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );

        Condition condition = Condition.LIKE_NEW;
        Book book = new Book(publicationInfo, condition);
        Appraisal appraisal = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> book.addAppraisal(appraisal));
    }

    // Test inserting a valid appraisal
    @Test
    void test_add_Appraisal() {

        // Arrange

        PublicationInfo publicationInfo = new PublicationInfo(
                new Title ("title"),
                Genre.ACTION,
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISBN(9789720048758L),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );

        Condition condition = Condition.LIKE_NEW;
        Book book = new Book(publicationInfo, condition);

        Price price1 = new Price(200, Currency.GBP);
        Price price2 = new Price(250, Currency.EUR);
        LocalDateTime date1 = LocalDateTime.now();
        LocalDateTime date2 = LocalDateTime.now();
        String description1 = "test";
        String description2 = "test again";

        Appraisal appraisal1 = new Appraisal(price1, date1, description1);
        Appraisal appraisal2 = new Appraisal(price2, date2, description2);

        // Act and assert
        book.addAppraisal(appraisal1);

        assertEquals(1, book.getAppraisals().size());

        book.addAppraisal(appraisal2);

        assertEquals(2, book.getAppraisals().size());
    }

    @Test
    void test_list_of_appraisals() {

        // Arrange

        PublicationInfo publicationInfo = new PublicationInfo(
                new Title ("title"),
                Genre.ACTION,
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISBN(9789720048758L),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );

        Condition condition = Condition.LIKE_NEW;
        Book book = new Book(publicationInfo, condition);

        Price price1 = new Price(200, Currency.GBP);
        Price price2 = new Price(250, Currency.EUR);
        LocalDateTime date1 = LocalDateTime.now();
        LocalDateTime date2 = LocalDateTime.now();
        String description1 = "test";
        String description2 = "test again";

        Appraisal appraisal1 = new Appraisal(price1, date1, description1);
        Appraisal appraisal2 = new Appraisal(price2, date2, description2);
        Appraisal appraisal3 = new Appraisal(price2, date2, description2);

        book.addAppraisal(appraisal1);
        book.addAppraisal(appraisal2);

        // Act
        List<Appraisal> appraisalsCopy = book.getAppraisals();

        // Assert that List is immutable (cannot add new elements to list)
        assertThrows(UnsupportedOperationException.class, () -> appraisalsCopy.add(appraisal3));

        // Assert that appraisalsCopy has the correct content
        assertEquals(2, appraisalsCopy.size());
        assertEquals(appraisal1, appraisalsCopy.get(0));
        assertEquals(appraisal2, appraisalsCopy.get(1));
    }

    // Test returning condition
    @Test
    void test_return_Condition() {

        // Arrange

        PublicationInfo publicationInfo = new PublicationInfo(
                new Title ("title"),
                Genre.ACTION,
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISBN(9789720048758L),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );

        Condition condition = Condition.LIKE_NEW;
        Book book = new Book(publicationInfo, condition);

        // Act and assert
        assertEquals(Condition.LIKE_NEW, book.getCondition());
    }

    // Test returning publicationInfo (extra, mas consistente e útil)
    @Test
    void test_return_PublicationInfo() {

        // Arrange

        PublicationInfo publicationInfo = new PublicationInfo(
                new Title ("title"),
                Genre.ACTION,
                new Author ("Eça de Queirós"),
                new Edition(
                        new ISBN(9789720048758L),
                        30,
                        3,
                        LocalDate.of(2001, 4, 23),
                        Binding.SADDLE_STITCH,
                        new Description("Amazing Magazine"),
                        new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                        new Weight(224.7, Weight.WeightUnit.GRAMS),
                        Language.of("pt", "Portuguese", "Português")),
                new Publisher("My Publisher")
        );

        Condition condition = Condition.LIKE_NEW;
        Book book = new Book(publicationInfo, condition);

        // Act and assert
        assertEquals(publicationInfo, book.getPublicationInfo());
    }
}
