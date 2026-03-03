package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DirectSaleFactoryTest {

    @Test
    void create_validInputs_returnsDirectSale() throws Exception {
        // Arrange
        DirectSaleFactory factory = new DirectSaleFactory();
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        // Act
        DirectSale ds = factory.create(item, price, null);

        // Assert
        assertNotNull(ds);
        assertSame(item, ds.getItem());
        assertSame(price, ds.getPrice());
        assertNull(ds.getTimeLimit());
    }

    @Test
    void create_negativeTimeLimit_throwsInstantiationException() {
        // Arrange
        DirectSaleFactory factory = new DirectSaleFactory();
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        // Act & Assert
        assertThrows(InstantiationException.class,
                () -> factory.create(item, price, Period.ofDays(-1)));
    }

    @Test
    void create_nullItem_throwsInstantiationException() {
        // Arrange
        DirectSaleFactory factory = new DirectSaleFactory();
        Price price = mock(Price.class);

        // Act & Assert
        assertThrows(InstantiationException.class,
                () -> factory.create(null, price, null));
    }

    @Test
    void create_nullPrice_throwsInstantiationException() {
        // Arrange
        DirectSaleFactory factory = new DirectSaleFactory();
        Item item = mock(Item.class);

        // Act & Assert
        assertThrows(InstantiationException.class,
                () -> factory.create(item, null, null));
    }
}