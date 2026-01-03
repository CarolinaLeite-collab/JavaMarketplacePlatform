package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

/**
 * JUnit 5 test class for Language
 */
class LanguageTest {

    @Test
    @DisplayName("Factory method creates language with all parameters")
    void testFactoryMethodWithAllParameters() {
        Language lang = Language.of("pt", "Portuguese", "Português");

        assertEquals("pt", lang.getCode());
        assertEquals("Portuguese", lang.getName());
        assertEquals("Português", lang.getNativeName());
        assertNotNull(lang.getLocale());
    }

    @Test
    @DisplayName("Factory method creates language with two parameters")
    void testFactoryMethodWithTwoParameters() {
        Language lang = Language.of("en", "English");

        assertEquals("en", lang.getCode());
        assertEquals("English", lang.getName());
        assertEquals("English", lang.getNativeName()); // Should default to name
    }

    @Test
    @DisplayName("Language code is normalized to lowercase")
    void testCodeNormalization() {
        Language lang = Language.of("EN", "English");

        assertEquals("en", lang.getCode());
    }

    @Test
    @DisplayName("Whitespace is trimmed from all fields")
    void testWhitespaceTrimming() {
        Language lang = Language.of("  pt  ", "  Portuguese  ", "  Português  ");

        assertEquals("pt", lang.getCode());
        assertEquals("Portuguese", lang.getName());
        assertEquals("Português", lang.getNativeName());
    }

    @Test
    @DisplayName("Null code throws IllegalArgumentException")
    void testNullCodeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Language.of(null, "English"));
    }

    @Test
    @DisplayName("Empty code throws IllegalArgumentException")
    void testEmptyCodeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Language.of("", "English"));
    }

    @Test
    @DisplayName("Null name throws IllegalArgumentException")
    void testNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Language.of("en", null));
    }

    @Test
    @DisplayName("Empty name throws IllegalArgumentException")
    void testEmptyNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Language.of("en", ""));
    }

    @Test
    @DisplayName("Null native name defaults to name")
    void testNullNativeNameDefaultsToName() {
        Language lang = Language.of("en", "English", null);

        assertEquals("English", lang.getNativeName());
    }

    @Test
    @DisplayName("Predefined constants have correct values")
    void testPredefinedConstants() {
        assertEquals("en", Language.ENGLISH.getCode());
        assertEquals("pt", Language.PORTUGUESE.getCode());
        assertEquals("es", Language.SPANISH.getCode());
        assertEquals("fr", Language.FRENCH.getCode());
        assertEquals("de", Language.GERMAN.getCode());
        assertEquals("it", Language.ITALIAN.getCode());
        assertEquals("zh", Language.CHINESE.getCode());
        assertEquals("ja", Language.JAPANESE.getCode());
    }

    @Test
    @DisplayName("hasCode returns true for matching code")
    void testHasCodeMatching() {
        Language lang = Language.PORTUGUESE;

        assertTrue(lang.hasCode("pt"));
        assertTrue(lang.hasCode("PT")); // Case-insensitive
        assertTrue(lang.hasCode("Pt"));
    }

    @Test
    @DisplayName("hasCode returns false for non-matching code")
    void testHasCodeNonMatching() {
        Language lang = Language.PORTUGUESE;

        assertFalse(lang.hasCode("en"));
        assertFalse(lang.hasCode("es"));
    }

    @Test
    @DisplayName("equals returns true for same language code")
    void testEqualsTrue() {
        Language lang1 = Language.of("pt", "Portuguese", "Português");
        Language lang2 = Language.of("pt", "Portuguese", "Português");
        Language lang3 = Language.PORTUGUESE;

        assertEquals(lang1, lang2);
        assertEquals(lang1, lang3);
    }

    @Test
    @DisplayName("equals returns false for different language codes")
    void testEqualsFalse() {
        Language lang1 = Language.PORTUGUESE;
        Language lang2 = Language.ENGLISH;

        assertNotEquals(lang1, lang2);
    }

    @Test
    @DisplayName("equals returns true for same object")
    void testEqualsSameObject() {
        Language lang = Language.PORTUGUESE;

        assertEquals(lang, lang);
    }

    @Test
    @DisplayName("equals returns false for null")
    void testEqualsNull() {
        Language lang = Language.PORTUGUESE;

        assertNotEquals(null, lang);
    }

    @Test
    @DisplayName("equals returns false for different class")
    void testEqualsDifferentClass() {
        Language lang = Language.PORTUGUESE;
        String str = "Portuguese";

        assertNotEquals(str, lang);
    }

    @Test
    @DisplayName("hashCode is consistent for equal objects")
    void testHashCodeConsistency() {
        Language lang1 = Language.of("pt", "Portuguese");
        Language lang2 = Language.of("pt", "Portuguese");

        assertEquals(lang1.hashCode(), lang2.hashCode());
    }

    @Test
    @DisplayName("hashCode is different for different codes")
    void testHashCodeDifferent() {
        Language lang1 = Language.PORTUGUESE;
        Language lang2 = Language.ENGLISH;

        assertNotEquals(lang1.hashCode(), lang2.hashCode());
    }

    @Test
    @DisplayName("toString contains all relevant information")
    void testToString() {
        Language lang = Language.of("pt", "Portuguese", "Português");
        String result = lang.toString();

        assertTrue(result.contains("pt"));
        assertTrue(result.contains("Portuguese"));
        assertTrue(result.contains("Português"));
    }

    @Test
    @DisplayName("getLocale returns correct Locale")
    void testGetLocale() {
        Language lang = Language.PORTUGUESE;
        Locale locale = lang.getLocale();

        assertEquals("pt", locale.getLanguage());
    }

    @Test
    @DisplayName("fromCode returns predefined constant for known codes")
    void testFromCodePredefined() {
        Language lang = Language.fromCode("pt");

        assertSame(Language.PORTUGUESE, lang);
    }

    @Test
    @DisplayName("fromCode creates new language for unknown codes")
    void testFromCodeUnknown() {
        Language lang = Language.fromCode("nl");

        assertEquals("nl", lang.getCode());
        assertNotNull(lang.getName());
    }

    @Test
    @DisplayName("fromCode is case insensitive")
    void testFromCodeCaseInsensitive() {
        Language lang1 = Language.fromCode("PT");
        Language lang2 = Language.fromCode("pt");

        assertEquals(lang1, lang2);
    }

    @Test
    @DisplayName("getDisplayName returns name in target language")
    void testGetDisplayName() {
        Language portuguese = Language.PORTUGUESE;
        String displayName = portuguese.getDisplayName(Language.ENGLISH);

        assertNotNull(displayName);
        assertFalse(displayName.isEmpty());
    }

    @Test
    @DisplayName("Languages can be used in collections")
    void testLanguageInCollections() {
        java.util.Set<Language> languages = new java.util.HashSet<>();

        languages.add(Language.PORTUGUESE);
        languages.add(Language.ENGLISH);
        languages.add(Language.of("pt", "Portuguese")); // Duplicate

        assertEquals(2, languages.size()); // Should have only 2 unique languages
        assertTrue(languages.contains(Language.PORTUGUESE));
        assertTrue(languages.contains(Language.ENGLISH));
    }
}