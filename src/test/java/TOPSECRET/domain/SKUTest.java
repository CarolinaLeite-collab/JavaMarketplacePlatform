package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SKUTest {

    @Test
    void shouldGenerateSkuAutomatically() {
        SKU sku = SKU.generate();

        assertNotNull(sku);
        assertNotNull(sku.getValue());
    }

    @Test
    void generatedSkuShouldHaveCorrectLength() {
        SKU sku = SKU.generate();

        assertEquals(10, sku.getValue().length());
    }

    @Test
    void generatedSkuShouldMatchExpectedFormat() {
        SKU sku = SKU.generate();

        assertTrue(sku.getValue().matches("^[A-F0-9]{10}$"));
    }

    @Test
    void generatedSkuShouldBeUppercase() {
        SKU sku = SKU.generate();

        assertEquals(sku.getValue(), sku.getValue().toUpperCase());
    }

    @Test
    void twoGeneratedSkusShouldBeDifferent() {
        SKU sku1 = SKU.generate();
        SKU sku2 = SKU.generate();

        assertNotEquals(sku1.getValue(), sku2.getValue());
    }
}
