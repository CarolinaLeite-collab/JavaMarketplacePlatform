package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {
    private Price _price;
    private Currency _currency;
    private Currency _currency2;

    @BeforeEach
    void setUp()
    {
        _currency = Currency.EUR;
        _currency2 = Currency.USD;

        _price = new Price(100.0, _currency);
    }

    @Test
    void tests_creation_of_valid_price() {
        assertEquals(100.0, _price.getValue());
        assertEquals(_currency, _price.getCurrency());
    }

    @Test
    void should_reject_zero_value() {
        assertThrows(IllegalArgumentException.class,
                () -> new Price(0.0, _currency));
    }

    @Test
    void should_reject_negative_value() {
        assertThrows(IllegalArgumentException.class,
                () -> new Price(-50.0, _currency));
    }

    @Test
    void should_reject_null_currency() {
        assertThrows(IllegalArgumentException.class,
                () -> new Price(150.0, null));
    }

    @Test
    void tests_price_to_string_shows_value_and_symbol() {
        Price eurPrice = new Price(25.0, _currency);
        Price usdPrice = new Price(30.0, _currency2);

        assertEquals("25.0 €", eurPrice.toString());
        assertEquals("30.0 $", usdPrice.toString());
    }

    @Test
    void tests_equal_prices_are_equal() {
        Price price1 = new Price(100.0, _currency);

        assertEquals(price1, _price);
        assertEquals(price1.hashCode(), _price.hashCode());
    }

    @Test
    void tests_different_prices_are_not_equal() {
        Price price1 = new Price(12.0, _currency);
        Price price2= new Price(12.0, _currency2);

        assertNotEquals(price1, price2);
        assertNotEquals(price1, _price);
    }
}
