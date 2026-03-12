package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

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
    void shouldCreateIssnWithValidFormat(){
        // arrange
        ISSN issn = new ISSN ("1234-5678");
        // assert
        assertEquals("1234-5678", issn.get_issn());
    }

    @Test
    void shouldRejectNullIssn(){
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN(null));
    }

    @Test
    void shouldRejectIssnWithoutHyphen(){
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("12345678"));
    }

    @Test
    void shouldRejectIssnWithLetters(){
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("1234-56B8"));
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("12A4-5678"));
    }

    @Test
    void shouldRejectIssnWithWrongLength(){
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("123-5678"));
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("12342-2678"));
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("1234-678"));
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN("1234-56782"));
    }

    @Test
    void toSringShouldReturnIssnValue(){
        // arrange
        ISSN issn = new ISSN ("0000-0000");
        // assert
        assertEquals("0000-0000", issn.toString());
    }

    @Test
    void getIdentifier_returnsStoredIssn() {
        // arrange
        ISSN issn = new ISSN("1234-5679");
        // assert
        assertEquals("1234-5679", issn.getIdentifier());
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
        // assert
        assertFalse(a.equals(null));
    }

    @Test
    void equals_returnsFalseForDifferentType() {
        // arrange
        ISSN a = new ISSN("1234-5679");
        // assert
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