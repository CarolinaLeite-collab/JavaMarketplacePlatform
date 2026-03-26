package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

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


    //Commented out below test because SKU.generate() uses UUID.randomUUID()
    // making collisions extremely unlikely, but not impossible.

    /*@Test
    void twoGeneratedSkusShouldBeDifferent() {
        SKU sku1 = SKU.generate();
        SKU sku2 = SKU.generate();

        assertNotEquals(sku1, sku2);
    }*/

    @Test
    void skuShouldBeEqualToItself() {
        SKU sku = SKU.generate();

        assertEquals(sku, sku);
    }

    @Test
    void skuShouldNotBeEqualToDifferentType() {
        SKU sku = SKU.generate();

        assertNotEquals(sku, "NOT_A_SKU");
    }

    @Test
    void skusWithSameValueShouldBeEqual() throws Exception {

        Constructor<SKU> constructor =
                SKU.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        SKU sku1 = constructor.newInstance("ABCDEF1234");
        SKU sku2 = constructor.newInstance("ABCDEF1234");

        assertEquals(sku1, sku2);
        assertEquals(sku1.hashCode(), sku2.hashCode());
    }

    @Test
    void skusWithDifferentValuesShouldNotBeEqual() throws Exception {

        Constructor<SKU> constructor =
                SKU.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        SKU sku1 = constructor.newInstance("ABCDEF1234");
        SKU sku2 = constructor.newInstance("1234ABCDEF");

        assertNotEquals(sku1, sku2);
    }

    @Test
    void toStringShouldReturnSkuValue() {
        SKU sku = SKU.generate();

        assertEquals(sku.getValue(), sku.toString());
    }

    @Test
    void shouldRejectInvalidSkuFormat() throws Exception {

        Constructor<SKU> constructor =
                SKU.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () ->
                constructor.newInstance("INVALID!!"));

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals(exception.getCause().getMessage(), "Invalid SKU format");
    }

    @Test
    void shouldRejectNullSku() throws Exception {

        Constructor<SKU> constructor =
                SKU.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () ->
                constructor.newInstance((String) null));

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals(exception.getCause().getMessage(), "Invalid SKU format");
    }

    @Test
    void hashCodeShouldBeEqual() throws Exception {
        SKU sku1 = SKU.generate();
        SKU sku2 = sku1;

        assertEquals(sku1.hashCode(), sku2.hashCode());
    }

    //Commented out below test because non-equal objects may still have the same hash code

   /* @Test
    void hashCodeShouldBeNotEqual() throws Exception {
        SKU sku1 = SKU.generate();
        SKU sku2 = SKU.generate();

        assertNotEquals(sku1.hashCode(), sku2.hashCode());
    }*/
}

