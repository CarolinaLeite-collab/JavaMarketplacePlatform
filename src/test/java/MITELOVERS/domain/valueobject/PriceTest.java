package MITELOVERS.domain.valueobject;

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
    }

    @Test
    void testCreationOfValidPrice() {
        //arrange
        _price = new Price(100.0, _currency);

        //act + assert
        assertEquals(100.0, _price.getValue());
        assertEquals(_currency, _price.getCurrency());
    }

    @Test
    void shouldAceptZeroValue() {
        //act
        _price = new Price(0.0, _currency);

        //assert
        assertEquals(0.0, _price.getValue());
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
    void shouldReturnTrueWhenValueIsGreater() {
        //arrange
        Price higher = new Price(100.0, _currency);
        Price lower = new Price(50.0, _currency);

        //assert + act
        assertTrue(higher.isGreaterOrEqualThan(lower));
    }

    @Test
    void shouldReturnTrueWhenValuesAreEqual() {
        //arrange
        Price p1 = new Price(100.0, _currency);
        Price p2 = new Price(100.0, _currency);

        //assert + act
        assertTrue(p1.isGreaterOrEqualThan(p2));
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        //arrange
        Price price = new Price(100.0, _currency);

        //assert + act
        assertThrows(IllegalArgumentException.class,
                () -> price.isGreaterOrEqualThan(null));
    }

    @Test
    void shouldReturnFalseWhenValueIsLower() {
        //arrange
        Price higher = new Price(100.0, _currency);
        Price lower = new Price(50.0, _currency);

        //assert + act
        assertFalse(lower.isGreaterOrEqualThan(higher));
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
        _price = new Price(100.0, _currency);
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

    @Test
    void samePriceObjectShouldBeEqual() {
        //arrange
        Price price1 = new Price(100.0, _currency);

        //assert
        assertTrue(price1.equals(price1));
    }

    @Test
    void differentObjectTypesShouldNotBeEqual() {
        //arrange
        Price price1 = new Price(100.0, _currency);
        String price = "price";

        //assert
        assertNotEquals(price1, price);
    }

    @Test
    void hashCodeShouldBeEqualForEqualObjects() {
        //arrange
        Price price1 = new Price(100.0, _currency);
        Price price2 = new Price(100.0, _currency);

        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void hashCodeShouldNotBeEqualForDifferentObjects() {
        //arrange
        Price price1 = new Price(100.0, _currency);
        Price price2 = new Price(12.0, _currency);

        //assert
        assertNotEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void shouldReturnTrueWhenPricesAreEqual() {
        //Act
        Price price1 = new Price(10.0, Currency.EUR);
        Price price2 = new Price(10.0, Currency.EUR);

        //Assert
        assertEquals(price1, price2);
    }

    @Test
    void shouldReturnFalseWhenCurrenciesAreDifferent() {
        //Act
        Price price1 = new Price(10.0, Currency.EUR);
        Price price2 = new Price(10.0, Currency.USD);

        //Assert
        assertNotEquals(price1, price2);
    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentType() {
        //Act
        Price price = new Price(10.0, Currency.EUR);

        //Assert
        assertNotEquals(price, "not a price");
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        //Act
        Price price = new Price(10.0, Currency.EUR);

        //Assert
        assertNotEquals(price, null);
    }

    @Test
    void shouldReturnFalseWhenValuesAreDifferent() {
        //Act
        Price price1 = new Price(10.0, Currency.EUR);
        Price price2 = new Price(20.0, Currency.EUR);

        //Assert
        assertNotEquals(price1, price2);
    }
}
