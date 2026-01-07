package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalTest {

    private Price price(int value, Currency currency) {
        return new Price(value, currency);
    }

    @Test
    void tests_creation_of_valid_appraisal() {
        Price value = price(10, Currency.EUR);
        LocalDateTime date = LocalDateTime.now();
        String description = "Harry Potter and The Philosopher's Stone";

        Appraisal appraisal = new Appraisal(value, date, description);

        assertEquals(value, appraisal.getValueEstimate());
        assertEquals(date, appraisal.getAppraisalDate());
        assertEquals(description, appraisal.getObjectDescription());
    }

    @Test
    void should_return_exception_for_null_value_estimate() {
        LocalDateTime now = LocalDateTime.now();

        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(null, now, "The book is in great condition, with only slight use marks."));
    }

    @Test
    void should_return_exception_for_null_appraisal_date() {
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(price(100, Currency.GBP), null, "The book is in great condition, with only slight use marks."));
    }

    @Test
    void should_return_exception_for_empty_description() {
        LocalDateTime date = LocalDateTime.now();

        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(price(56, Currency.EUR), date, ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(price(56, Currency.EUR), date, "   "));
    }

    @Test
    void tests_whether_equality_depends_on_all_fields() {
        Price value = price(5, Currency.AUD);
        LocalDateTime date = LocalDateTime.of(2026, 1, 3, 13, 40);
        String description = "The book is in a poor condition, with extensive use marks and some ripped pages.";

        Appraisal a1 = new Appraisal(value, date, description);
        Appraisal a2 = new Appraisal(value, date, description);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void tests_whether_different_appraisals_are_not_equal() {
        Price value1 = price(100, Currency.JPY);
        Price value2 = price(200, Currency.CHF);
        LocalDateTime date = LocalDateTime.of(2021, 3, 1, 14, 20);
        String description = "Book";

        Appraisal a1 = new Appraisal(value1, date, description);
        Appraisal a2 = new Appraisal(value2, date, description);

        assertNotEquals(a1, a2);
    }

    @Test
    void tests_whether_to_string_contains_key_information() {
        Price value = price(104, Currency.EUR);
        LocalDateTime date = LocalDateTime.of(2023, 9, 27, 12, 30);
        String description = "The book is in great condition, with no use marks.";

        Appraisal appraisal = new Appraisal(value, date, description);
        String text = appraisal.toString();

        assertTrue(text.contains("Appraisal"));
        assertTrue(text.contains("The book is in great condition, with no use marks."));
        assertTrue(text.contains("104.0 €"));
    }

}
