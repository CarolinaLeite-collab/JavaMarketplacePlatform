package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void testGetters() {
        // Assert
        assertAll(
                () -> assertEquals("pt", Language.PORTUGUESE.getCode()),
                () -> assertEquals("Portuguese", Language.PORTUGUESE.getName()),
                () -> assertEquals("Português", Language.PORTUGUESE.getNativeName())
        );
    }

    @Test
    void testFromCodePredefinedAllConstants() {
        // Assert
        assertAll(
                () -> assertSame(Language.ENGLISH,    Language.fromCode("en")),
                () -> assertSame(Language.PORTUGUESE, Language.fromCode("pt")),
                () -> assertSame(Language.SPANISH,    Language.fromCode("es")),
                () -> assertSame(Language.FRENCH,     Language.fromCode("fr")),
                () -> assertSame(Language.GERMAN,     Language.fromCode("de")),
                () -> assertSame(Language.ITALIAN,    Language.fromCode("it")),
                () -> assertSame(Language.CHINESE,    Language.fromCode("zh")),
                () -> assertSame(Language.JAPANESE,   Language.fromCode("ja")),
                () -> assertSame(Language.LATIN,          Language.fromCode("la")),
                () -> assertSame(Language.ANCIENT_GREEK,  Language.fromCode("grc")),
                () -> assertSame(Language.PORTUGUESE_BR,  Language.fromCode("pt-br"))
        );
    }

    @Test
    void testHasCodeMatching() {
        // Assert
        assertAll(
                () -> assertTrue(Language.PORTUGUESE.hasCode("pt")),
                () -> assertTrue(Language.PORTUGUESE.hasCode("PT")),
                () -> assertTrue(Language.PORTUGUESE.hasCode("Pt"))
        );
    }

    @Test
    void testHasCodeNonMatching() {
        // Assert
        assertAll(
                () -> assertFalse(Language.PORTUGUESE.hasCode("en")),
                () -> assertFalse(Language.PORTUGUESE.hasCode("es"))
        );
    }

    @Test
    void testEqualsTrue() {
        // Assert
        assertEquals(Language.PORTUGUESE, Language.PORTUGUESE);
    }

    @Test
    void testEqualsFalse() {
        // Assert
        assertNotEquals(Language.PORTUGUESE, Language.ENGLISH);
    }

    @Test
    void testToString() {
        // Assert
        assertEquals("pt", Language.PORTUGUESE.toString());
    }

    @Test
    void testFromCodeUnknownThrowsException() {
        // Assert & Act
        assertThrows(IllegalArgumentException.class, () -> Language.fromCode("xx"));
    }

    @Test
    void testFromCodeCaseInsensitive() {
        // Assert
        assertAll(
                () -> assertSame(Language.PORTUGUESE, Language.fromCode("PT")),
                () -> assertSame(Language.ENGLISH,    Language.fromCode("EN")),
                () -> assertSame(Language.LATIN,      Language.fromCode("LA"))
        );
    }
}
