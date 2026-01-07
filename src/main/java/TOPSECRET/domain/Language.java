package TOPSECRET.domain;

import java.util.Objects;
import java.util.Locale;

/**
 * Represents a language with support for internationalization.
 * Immutable class following best practices for value objects.
 */
public class Language {
    private final String _code;        // ISO 639-1 code (e.g., "en", "pt")
    private final String _name;        // Language name in English
    private final String _nativeName;  // Language name in its own language
    private final Locale _locale;

    // Private constructor to enforce use of factory methods
    private Language(String code, String name, String nativeName) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Language code cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Language name cannot be null or empty");
        }

        _code = code.toLowerCase().trim();
        _name = name.trim();
        _nativeName = (nativeName != null) ? nativeName.trim() : name.trim();
        _locale = new Locale(this._code);
    }

    // Factory method - recommended approach
    public static Language of(String code, String name, String nativeName) {
        return new Language(code, name, nativeName);
    }

    public static Language of(String code, String name) {
        return new Language(code, name, name);
    }

    // Common language constants
    public static final Language ENGLISH = Language.of("en", "English");
    public static final Language PORTUGUESE = Language.of("pt", "Portuguese", "Português");
    public static final Language SPANISH = Language.of("es", "Spanish", "Español");
    public static final Language FRENCH = Language.of("fr", "French", "Français");
    public static final Language GERMAN = Language.of("de", "German", "Deutsch");
    public static final Language ITALIAN = Language.of("it", "Italian", "Italiano");
    public static final Language CHINESE = Language.of("zh", "Chinese", "中文");
    public static final Language JAPANESE = Language.of("ja", "Japanese", "日本語");

    // Getters
    public String getCode() {
        return _code;
    }

    public String getName() {
        return _name;
    }

    public String getNativeName() {
        return _nativeName;
    }

    public Locale getLocale() {
        return _locale;
    }

    // Check if this language matches a given code
    public boolean hasCode(String code) {
        return this._code.equalsIgnoreCase(code);
    }

    // Get display name based on target language
    public String getDisplayName(Language targetLanguage) {
        return _locale.getDisplayLanguage(targetLanguage.getLocale());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return _code.equals(language._code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_code);
    }

    @Override
    public String toString() {
        return String.format("Language{code='%s', name='%s', nativeName='%s'}",
                _code, _name, _nativeName);
    }

    // Optional: Parse from string (useful for configuration)
    public static Language fromCode(String code) {
        String normalized = code.toLowerCase().trim();

        // Check common languages first
        switch (normalized) {
            case "en": return ENGLISH;
            case "pt": return PORTUGUESE;
            case "es": return SPANISH;
            case "fr": return FRENCH;
            case "de": return GERMAN;
            case "it": return ITALIAN;
            case "zh": return CHINESE;
            case "ja": return JAPANESE;
            default:
                Locale locale = new Locale(normalized);
                String displayName = locale.getDisplayLanguage(Locale.ENGLISH);
                return Language.of(normalized, displayName);
        }
    }
}