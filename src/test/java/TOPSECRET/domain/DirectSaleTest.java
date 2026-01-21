package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import java.time.Period;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleTest {

    @Test
    void createsDirectSaleWithPriceAndTimeLimit() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(10.0, Currency.EUR);
        Period limit = Period.ofMonths(3);

        DirectSale sale = new DirectSale(item, price, limit);

        assertEquals(item, sale.getItem());
        assertEquals(price, sale.getPrice());
        assertEquals(limit, sale.getTimeLimit());
    }

    @Test
    void createsDirectSaleWithoutTimeLimit() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(15.0, Currency.USD);

        DirectSale sale = new DirectSale(item, price, null);

        assertEquals(item, sale.getItem());
        assertEquals(price, sale.getPrice());
        assertNull(sale.getTimeLimit());
    }

    @Test
    void throwsExceptionWhenPriceIsNull() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(item, null, Period.ofDays(10))
        );

        assertEquals("Price is required for a direct sale", ex.getMessage());
    }

    @Test
    void throwsExceptionWhenItemIsNull() {
        Price price = new Price(10.0, Currency.EUR);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(null, price, Period.ofDays(10))
        );

        assertEquals("Item is required for a direct sale", ex.getMessage());
    }

    @Test
    void throwsExceptionWhenTimeLimitIsNegative() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        Price price = new Price(10.0, Currency.EUR);
        Period negative = Period.ofMonths(-3);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(item, price, negative)
        );

        assertEquals("Time limit cannot be negative", ex.getMessage());
    }
}