package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppraisalTest {

    private static final LocalDateTime _fixedDate =  LocalDateTime.of(2025, 1, 1, 10, 0);
    private static final String _validDescription =  "The book is in great condition, with only slight use marks.";

    @Test
    void tests_creation_of_valid_appraisal() {

        Price _priceDouble = mock(Price.class);
        Appraisal appraisal = new Appraisal(_priceDouble, _fixedDate,_validDescription);

        assertEquals(_priceDouble, appraisal.getValueEstimate());
        assertEquals(_fixedDate, appraisal.getAppraisalDate());
        assertEquals(_validDescription, appraisal.getObjectDescription());
    }

    @Test
    void should_return_exception_for_null_value_estimate() {

        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(null, _fixedDate, _validDescription));
    }

    @Test
    void should_return_exception_for_null_appraisal_date() {
        Price priceDouble = mock(Price.class);

        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(priceDouble, null, _validDescription));
    }

    @Test
    void should_return_exception_for_empty_description() {

        Price priceDouble = mock(Price.class);

        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(priceDouble, _fixedDate, ""));
    }
    @Test
    void should_return_exception_for_blank_description() {

        Price priceDouble = mock(Price.class);
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(priceDouble, _fixedDate, "   "));
    }

    @Test
    void tests_whether_equality_depends_on_all_fields() {

        Price _priceDouble = mock(Price.class);

        Appraisal a1 = new Appraisal(_priceDouble, _fixedDate,_validDescription);
        Appraisal a2 = new Appraisal(_priceDouble, _fixedDate,_validDescription);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void tests_whether_different_price_makes_appraisal_not_equal() {
        Price priceDouble = mock(Price.class);
        Price priceDouble1 = mock(Price.class);

        Appraisal a1 = new Appraisal(priceDouble, _fixedDate, _validDescription );
        Appraisal differentPrice = new Appraisal(priceDouble1, _fixedDate, _validDescription );

        assertNotEquals(a1, differentPrice);
    }

    @Test
    void tests_whether_different_date_makes_appraisals_not_equal() {

        Price _priceDouble = mock(Price.class);
        LocalDateTime _differentDate = LocalDateTime.of(2025, 1, 5, 10, 0);

        Appraisal a1 = new Appraisal(_priceDouble, _fixedDate, _validDescription );
        Appraisal differentDate = new Appraisal(_priceDouble, _differentDate , _validDescription );

        assertNotEquals(a1, differentDate);
    }

    @Test
    void tests_whether_different_description_makes_appraisals_not_equal() {

        Price _priceDouble = mock(Price.class);

        Appraisal a1 = new Appraisal(_priceDouble, _fixedDate, _validDescription);
        Appraisal a2 = new Appraisal(_priceDouble, _fixedDate, "Different description");

        assertNotEquals(a1, a2);
    }

    @Test
    void tests_whether_to_string_contains_key_information() {
        Price priceDouble = mock(Price.class);
        when(priceDouble.toString()).thenReturn("10.0 €");

        Appraisal appraisal = new Appraisal(priceDouble, _fixedDate, _validDescription );
        String text = appraisal.toString();

        assertTrue(text.contains("Appraisal"));
        assertTrue(text.contains(_validDescription));
        assertTrue(text.contains("10.0 €"));
    }

}
