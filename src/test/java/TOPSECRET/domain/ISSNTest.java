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
}