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
    void testCreationOfValidPrice() {
        //act + assert
        assertEquals(100.0, _price.getValue());
        assertEquals(_currency, _price.getCurrency());
    }

    @Test
    void shouldRejectZeroValue() {
        //act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new Price(0.0, _currency));
    }

    @Test
    void shouldRejectNegativeValue() {
        //act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new Price(-50.0, _currency));
    }

    @Test
    void shouldRejectNullCurrency() {
        //act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new Price(150.0, null));
    }

    @Test
    void testPriceToStringShowsValueAndSymbol() {
        //arrange
        Price eurPrice = new Price(25.0, _currency);
        Price usdPrice = new Price(30.0, _currency2);

        //act + assert
        assertEquals("25.0 €", eurPrice.toString());
        assertEquals("30.0 $", usdPrice.toString());
    }

    @Test
    void testEqualPricesAreEqual() {
        //arrange
        Price price1 = new Price(100.0, _currency);

        //act + assert
        assertEquals(price1, _price);
        assertEquals(price1.hashCode(), _price.hashCode());
    }

    @Test
    void testDifferentPricesAreNotEqual() {
        //arrange
        Price price1 = new Price(12.0, _currency);
        Price price2= new Price(12.0, _currency2);

        //assert
        assertNotEquals(price1, price2);
        assertNotEquals(price1, _price);
    }
}
