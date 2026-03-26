package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class SKUTest {

    // -------------------------------------------------
    // Generation tests
    // -------------------------------------------------

    /**
     * Verifica se o SKU é gerado automaticamente pela aplicação
     * e se o objeto e o seu valor interno não são nulos.
     */
    @Test
    void shouldGenerateSkuAutomatically() {
        SKU sku = SKU.generate();

        assertNotNull(sku);
        assertNotNull(sku.getValue());
    }

    /**
     * Verifica se o SKU gerado tem exatamente o comprimento esperado (10 caracteres).
     */
    @Test
    void generatedSkuShouldHaveCorrectLength() {
        SKU sku = SKU.generate();

        assertEquals(10, sku.getValue().length());
    }

    /**
     * Verifica se o SKU gerado respeita o formato definido:
     * apenas letras A–F e números 0–9, com 10 caracteres.
     */
    @Test
    void generatedSkuShouldMatchExpectedFormat() {
        SKU sku = SKU.generate();

        assertTrue(sku.getValue().matches("^[A-F0-9]{10}$"));
    }

    /**
     * Verifica se o SKU é sempre gerado em letras maiúsculas.
     * Isto garante um formato canónico único.
     */
    @Test
    void generatedSkuShouldBeUppercase() {
        SKU sku = SKU.generate();

        assertEquals(sku.getValue(), sku.getValue().toUpperCase());
    }

    /**
     * Verifica se dois SKUs gerados em momentos diferentes
     * não são considerados iguais.
     */

    //Commented out below test because SKU.generate() uses UUID.randomUUID()
    // making collisions extremely unlikely, but not impossible.

    /*@Test
    void twoGeneratedSkusShouldBeDifferent() {
        SKU sku1 = SKU.generate();
        SKU sku2 = SKU.generate();

        assertNotEquals(sku1, sku2);
    }*/

    // -------------------------------------------------
    // equals, hashCode e toString
    // -------------------------------------------------

    /**
     * Verifica a propriedade de reflexividade do equals:
     * um SKU deve ser sempre igual a si próprio.
     */
    @Test
    void skuShouldBeEqualToItself() {
        SKU sku = SKU.generate();

        assertEquals(sku, sku);
    }

    /**
     * Verifica que um SKU nunca é igual a um objeto de outro tipo.
     */
    @Test
    void skuShouldNotBeEqualToDifferentType() {
        SKU sku = SKU.generate();

        assertNotEquals(sku, "NOT_A_SKU");
    }

    /**
     * Verifica que dois SKUs com o mesmo valor interno
     * são considerados iguais e têm o mesmo hashCode.
     *
     * Usa reflection porque o construtor é privado por design.
     */
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

    /**
     * Verifica que dois SKUs com valores diferentes
     * não são considerados iguais.
     */
    @Test
    void skusWithDifferentValuesShouldNotBeEqual() throws Exception {

        Constructor<SKU> constructor =
                SKU.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        SKU sku1 = constructor.newInstance("ABCDEF1234");
        SKU sku2 = constructor.newInstance("1234ABCDEF");

        assertNotEquals(sku1, sku2);
    }

    /**
     * Verifica se o método toString devolve exatamente
     * o valor interno do SKU.
     */
    @Test
    void toStringShouldReturnSkuValue() {
        SKU sku = SKU.generate();

        assertEquals(sku.getValue(), sku.toString());
    }

    // -------------------------------------------------
    // Constructor validation (via reflection)
    // -------------------------------------------------

    /**
     * Verifica que o construtor rejeita SKUs com formato inválido.
     * Reflection é usada apenas em testes para validar invariantes.
     */
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

    /**
     * Verifica que o construtor rejeita valores nulos,
     * garantindo que nunca existe um SKU inválido.
     */
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

