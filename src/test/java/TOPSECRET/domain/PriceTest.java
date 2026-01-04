package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void tests_creation_of_valid_price() {
        Price price = new Price(100.0, Currency.EUR);

        assertEquals(100.0, price.getValue());
        assertEquals(Currency.EUR, price.getCurrency());
    }

    @Test
    void should_reject_zero_value() {
        assertThrows(IllegalArgumentException.class,
                () -> new Price(0.0, Currency.USD));
    }

    @Test
    void should_reject_negative_value() {
        assertThrows(IllegalArgumentException.class,
                () -> new Price(-50.0, Currency.GBP));
    }

    @Test
    void should_reject_null_currency() {
        assertThrows(IllegalArgumentException.class,
                () -> new Price(150.0, null));
    }

    @Test
    void tests_price_to_string_shows_value_and_symbol() {
        Price eurPrice = new Price(25.0, Currency.EUR);
        Price usdPrice = new Price(30.0, Currency.USD);

        assertEquals("25.0 €", eurPrice.toString());
        assertEquals("30.0 $", usdPrice.toString());
    }

    @Test
    void tests_equal_prices_are_equal() {
        Price price1 = new Price(100.0, Currency.EUR);
        Price price2 = new Price(100.0, Currency.EUR);

        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void tests_different_prices_are_not_equal() {
        Price price1 = new Price(12.0, Currency.EUR);
        Price price2= new Price(12.0, Currency.USD);
        Price price3 = new Price(16.0, Currency.EUR);

        assertNotEquals(price1, price2);
        assertNotEquals(price1, price3);
    }
}
