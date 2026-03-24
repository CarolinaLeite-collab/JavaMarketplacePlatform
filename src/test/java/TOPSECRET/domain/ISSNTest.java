package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.ISSN;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ISSN}.
 *
 * <p>Covers construction rules, format validation, equality contract,
 * and hash code consistency.</p>
 *
 * <p>No Mockito doubles are used — {@link ISSN} is a pure Value Object.</p>
 */

class ISSNTest {

    @Test
    void shouldStoreAndReturnIssnValue() {
        // Arrange
        ISSN issn = new ISSN("1234-5678");

        // Assert
        assertAll(
                () -> assertEquals("1234-5678", issn.get_issn()),
                () -> assertEquals("1234-5678", issn.toString()),
                () -> assertEquals("1234-5678", issn.getIdentifier())
        );
    }

    @Test
    void shouldRejectNullIssn(){
        // act + assert
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN(null));
    }

    @Test
    void shouldRejectIssnWithoutHyphen(){
        // act + assert
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("12345678"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234-56B8", "12A4-5678"})
    void shouldRejectIssnWithLetters(String bad) {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new ISSN(bad));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123-5678", "12342-2678", "1234-678", "1234-56782"})
    void shouldRejectIssnWithWrongLength(String bad) {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new ISSN(bad));
    }

    @Test
    void equals_returnsTrueForSameIssn(){
        // arrange
        ISSN issn = new ISSN("1234-5678");
        // assert
        assertTrue(issn.equals(issn));
    }

    @Test
    void equals_returnsFalseForDiferentIssn(){
        // arrange
        ISSN issn = new ISSN("1234-5678");
        ISSN issn2 = new ISSN("1234-5679");
        // assert
        assertFalse(issn.equals(issn2));
    }

    @Test
    void equals_returnsFalseForNull() {
        // arrange
        ISSN a = new ISSN("1234-5679");
        // act + assert
        assertFalse(a.equals(null));
    }

    @Test
    void equals_returnsFalseForDifferentType() {
        // arrange
        ISSN a = new ISSN("1234-5679");
        // act + assert
        assertFalse(a.equals("1234-5679"));
    }

    @Test
    void hashCode_sameValue_sameHash() {
        // arrange
        ISSN a = new ISSN("1234-5678");
        ISSN b = new ISSN("1234-5678");
        // assert
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_returnsTrueForSameValueDifferentObjects() {
        // arrange
        ISSN a = new ISSN("1234-5678");
        ISSN b = new ISSN("1234-5678");

        // assert
        assertTrue(a.equals(b));   // <- mata "return false" na linha 32
        assertTrue(b.equals(a));   // extra: simetria
    }

    @Test
    void hashCode_shouldMatchStringHashCode() {
        // arrange
        ISSN a = new ISSN("1234-5678");
        // assert
        assertEquals("1234-5678".hashCode(), a.hashCode()); // <- mata "return 0"
    }

}