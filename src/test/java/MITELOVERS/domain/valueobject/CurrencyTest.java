package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void tests_whether_all_currency_constants_exist() {
        assertNotNull(Currency.AUD);
        assertNotNull(Currency.CAD);
        assertNotNull(Currency.CHF);
        assertNotNull(Currency.CNY);
        assertNotNull(Currency.EUR);
        assertNotNull(Currency.GBP);
        assertNotNull(Currency.INR);
        assertNotNull(Currency.JPY);
        assertNotNull(Currency.NZD);
        assertNotNull(Currency.USD);
    }

    @Test
    void tests_whether_get_symbol_returns_correct_symbols() {
        assertEquals("$", Currency.AUD.getSymbol());
        assertEquals("$", Currency.CAD.getSymbol());
        assertEquals("Fr", Currency.CHF.getSymbol());
        assertEquals("¥", Currency.CNY.getSymbol());
        assertEquals("€", Currency.EUR.getSymbol());
        assertEquals("£", Currency.GBP.getSymbol());
        assertEquals("₹", Currency.INR.getSymbol());
        assertEquals("¥", Currency.JPY.getSymbol());
        assertEquals("$", Currency.NZD.getSymbol());
        assertEquals("$", Currency.USD.getSymbol());
    }

    @Test
    void tests_whether_all_currencies_are_accessible_via_values() {
        Currency[] currencies = Currency.values();

        assertEquals(10, currencies.length);
        assertTrue(java.util.Arrays.asList(currencies).contains(Currency.EUR));
        assertTrue(java.util.Arrays.asList(currencies).contains(Currency.USD));
    }

    @Test
    void tests_value_of_works_by_enum_name() {
        assertEquals(Currency.EUR, Currency.valueOf("EUR"));
        assertEquals(Currency.GBP, Currency.valueOf("GBP"));
        assertEquals(Currency.JPY, Currency.valueOf("JPY"));
    }

    @Test
    void tests_whether_different_currencies_are_not_equal() {
        assertNotEquals(Currency.EUR, Currency.USD);
        assertNotEquals(Currency.GBP, Currency.JPY);
    }
}
