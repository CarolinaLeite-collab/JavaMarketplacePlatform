package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.Objects;

/**
 * Unit tests for {@link Language}.
 *
 * <p>Covers factory method construction, normalization, validation,
 * predefined constants, equality contract, and utility methods.</p>
 *
 * <p>No Mockito doubles are used — {@link Language} is a pure Value Object.</p>
 */
class LanguageTest {

    @Test
    void testFactoryMethodWithAllParameters() {
        // Act
        Language lang = Language.of("pt", "Portuguese", "Português");

        // Assert
        assertAll(
                () -> assertEquals("pt", lang.getCode()),
                () -> assertEquals("Portuguese", lang.getName()),
                () -> assertEquals("Português", lang.getNativeName()),
                () -> assertNotNull(lang.getLocale())
        );
    }

    @Test
    void testFactoryMethodWithTwoParameters() {
        // Act
        Language lang = Language.of("en", "English");

        // Assert
        assertAll(
                () -> assertEquals("en", lang.getCode()),
                () -> assertEquals("English", lang.getName()),
                () -> assertEquals("English", lang.getNativeName())
        );
    }

    @Test
    void testCodeNormalization() {
        // Act
        Language lang = Language.of("EN", "English");

        // Assert
        assertEquals("en", lang.getCode());
    }

    @Test
    void testWhitespaceTrimming() {
        // Act
        Language lang = Language.of("  pt  ", "  Portuguese  ", "  Português  ");

        // Assert
        assertAll(
                () -> assertEquals("pt", lang.getCode()),
                () -> assertEquals("Portuguese", lang.getName()),
                () -> assertEquals("Português", lang.getNativeName())
        );
    }

    @Test
    void testNullCodeThrowsException() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> Language.of(null, "English"));
    }

    @Test
    void testEmptyCodeThrowsException() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> Language.of("", "English"));
    }

    @Test
    void testNullNameThrowsException() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> Language.of("en", null));
    }

    @Test
    void testEmptyNameThrowsException() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> Language.of("en", ""));
    }

    @Test
    void testNullNativeNameDefaultsToName() {
        // Act
        Language lang = Language.of("en", "English", null);

        // Assert
        assertEquals("English", lang.getNativeName());
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
                () -> assertSame(Language.JAPANESE,   Language.fromCode("ja"))
        );
    }

    @Test
    void testHasCodeMatching() {
        // Arrange
        Language lang = Language.PORTUGUESE;

        // Assert
        assertAll(
                () -> assertTrue(lang.hasCode("pt")),
                () -> assertTrue(lang.hasCode("PT")),
                () -> assertTrue(lang.hasCode("Pt"))
        );
    }

    @Test
    void testHasCodeNonMatching() {
        // Arrange
        Language lang = Language.PORTUGUESE;

        // Assert
        assertAll(
                () -> assertFalse(lang.hasCode("en")),
                () -> assertFalse(lang.hasCode("es"))
        );
    }

    @Test
    void testEqualsTrue() {
        // Arrange
        Language lang1 = Language.of("pt", "Portuguese", "Português");
        Language lang2 = Language.of("pt", "Portuguese", "Português");
        Language lang3 = Language.PORTUGUESE;

        // Assert
        assertAll(
                () -> assertEquals(lang1, lang2),
                () -> assertEquals(lang1, lang3)
        );
    }

    @Test
    void testEqualsFalse() {
        // Arrange
        Language lang1 = Language.PORTUGUESE;
        Language lang2 = Language.ENGLISH;

        // Assert
        assertNotEquals(lang1, lang2);
    }

    @Test
    void testEqualsSameObject() {
        // Arrange
        Language lang = Language.PORTUGUESE;

        // Assert
        assertEquals(lang, lang);
    }

    @Test
    void testEqualsNull() {
        // Arrange
        Language lang = Language.PORTUGUESE;

        // Assert
        assertFalse(lang.equals(null));
    }

    @Test
    void testEqualsDifferentClass() {
        // Arrange
        Language lang = Language.PORTUGUESE;
        String str = "Portuguese";

        // Assert
        assertNotEquals(str, lang);
    }

    @Test
    void testHashCodeConsistency() {
        // Arrange
        Language lang1 = Language.of("pt", "Portuguese");
        Language lang2 = Language.of("pt", "Portuguese");

        // Assert
        assertEquals(lang1.hashCode(), lang2.hashCode());
    }

    @Test
    void testHashCodeBasedOnCode() {
        // Arrange
        Language lang = Language.of("pt", "Portuguese");

        // Assert
        assertEquals(Objects.hash("pt"), lang.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Language lang = Language.of("pt", "Portuguese", "Português");

        // Act
        String result = lang.toString();

        // Assert
        assertAll(
                () -> assertTrue(result.contains("pt")),
                () -> assertTrue(result.contains("Portuguese")),
                () -> assertTrue(result.contains("Português"))
        );
    }

    @Test
    void testGetLocale() {
        // Arrange
        Language lang = Language.PORTUGUESE;

        // Act
        Locale locale = lang.getLocale();

        // Assert
        assertEquals("pt", locale.getLanguage());
    }

    @Test
    void testFromCodePredefined() {
        // Act
        Language lang = Language.fromCode("pt");

        // Assert
        assertSame(Language.PORTUGUESE, lang);
    }

    @Test
    void testFromCodeUnknown() {
        // Act
        Language lang = Language.fromCode("nl");

        // Assert
        assertAll(
                () -> assertEquals("nl", lang.getCode()),
                () -> assertNotNull(lang.getName())
        );
    }

    @Test
    void testFromCodeCaseInsensitive() {
        // Act
        Language lang1 = Language.fromCode("PT");
        Language lang2 = Language.fromCode("pt");

        // Assert
        assertEquals(lang1, lang2);
    }

    @Test
    void testGetDisplayName() {
        // Arrange
        Language portuguese = Language.PORTUGUESE;

        // Act
        String displayName = portuguese.getDisplayName(Language.ENGLISH);

        // Assert
        assertAll(
                () -> assertNotNull(displayName),
                () -> assertFalse(displayName.isEmpty())
        );
    }

    @Test
    void testLanguageInCollections() {
        // Arrange
        java.util.Set<Language> languages = new java.util.HashSet<>();

        // Act
        languages.add(Language.PORTUGUESE);
        languages.add(Language.ENGLISH);
        languages.add(Language.of("pt", "Portuguese")); // duplicate

        // Assert
        assertAll(
                () -> assertEquals(2, languages.size()),
                () -> assertTrue(languages.contains(Language.PORTUGUESE)),
                () -> assertTrue(languages.contains(Language.ENGLISH))
        );
    }
}