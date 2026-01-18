package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ISSNTest {

    @Test
    void shouldCreateIssnWithValidFormat(){
        ISSN issn = new ISSN ("1234-5678");
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
        ISSN issn = new ISSN ("0000-0000");
        assertEquals("0000-0000", issn.toString());
    }

    @Test
    void getIdentifier_returnsStoredIssn() {
        ISSN issn = new ISSN("1234-5679");
        assertEquals("1234-5679", issn.getIdentifier());
    }

    @Test
    void equals_returnsTrueForSameIssn(){
        ISSN issn = new ISSN("1234-5678");
        Object o = issn;
        assertEquals(issn,o);


    }
    @Test
    void equals_returnsFalseForDiferentIssn(){
        ISSN issn = new ISSN("1234-5678");
        ISSN issn2 = new ISSN("1234-5679");
        assertFalse(issn.equals(issn2));
    }
    @Test
    void equals_returnsFalseForNull() {
        ISSN a = new ISSN("1234-5679");
        assertFalse(a.equals(null));
    }

    @Test
    void equals_returnsFalseForDifferentType() {
        ISSN a = new ISSN("1234-5679");
        assertFalse(a.equals("1234-5679"));
    }

}