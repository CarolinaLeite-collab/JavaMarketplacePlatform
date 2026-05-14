package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Represents a language with support for internationalization.
 * Immutable class following best practices for value objects.
 */

public enum Language implements ValueObject {
    // European
    ENGLISH("en", "English", "English"),
    PORTUGUESE("pt", "Portuguese", "Português"),
    SPANISH("es", "Spanish", "Español"),
    FRENCH("fr", "French", "Français"),
    GERMAN("de", "German", "Deutsch"),
    ITALIAN("it", "Italian", "Italiano"),
    DUTCH("nl", "Dutch", "Nederlands"),
    SWEDISH("sv", "Swedish", "Svenska"),
    NORWEGIAN("no", "Norwegian", "Norsk"),
    DANISH("da", "Danish", "Dansk"),
    FINNISH("fi", "Finnish", "Suomi"),
    POLISH("pl", "Polish", "Polski"),
    CZECH("cs", "Czech", "Čeština"),
    SLOVAK("sk", "Slovak", "Slovenčina"),
    HUNGARIAN("hu", "Hungarian", "Magyar"),
    ROMANIAN("ro", "Romanian", "Română"),
    BULGARIAN("bg", "Bulgarian", "Български"),
    GREEK("el", "Greek", "Ελληνικά"),
    CROATIAN("hr", "Croatian", "Hrvatski"),
    SERBIAN("sr", "Serbian", "Српски"),
    UKRAINIAN("uk", "Ukrainian", "Українська"),
    RUSSIAN("ru", "Russian", "Русский"),
    CATALAN("ca", "Catalan", "Català"),

    // Middle East & Central Asia
    ARABIC("ar", "Arabic", "العربية"),
    HEBREW("he", "Hebrew", "עברית"),
    TURKISH("tr", "Turkish", "Türkçe"),
    PERSIAN("fa", "Persian", "فارسی"),

    // Asian
    CHINESE("zh", "Chinese", "中文"),
    JAPANESE("ja", "Japanese", "日本語"),
    KOREAN("ko", "Korean", "한국어"),
    HINDI("hi", "Hindi", "हिन्दी"),
    BENGALI("bn", "Bengali", "বাংলা"),
    THAI("th", "Thai", "ไทย"),
    VIETNAMESE("vi", "Vietnamese", "Tiếng Việt"),
    INDONESIAN("id", "Indonesian", "Bahasa Indonesia"),
    MALAY("ms", "Malay", "Bahasa Melayu"),

    // African
    SWAHILI("sw", "Swahili", "Kiswahili"),
    AFRIKAANS("af", "Afrikaans", "Afrikaans"),
    AMHARIC("am", "Amharic", "አማርኛ"),
    HAUSA("ha", "Hausa", "Hausa"),
    YORUBA("yo", "Yoruba", "Yorùbá"),

    // North America
    ENGLISH_US("en-us", "English (US)", "English (US)"),

    // Central America & Caribbean
    HAITIAN_CREOLE("ht", "Haitian Creole", "Kreyòl Ayisyen"),
    NAHUATL("nah", "Nahuatl", "Nāhuatlahtōlli"),
    MAYA("myn", "Maya", "Maya"),
    GUARANI("gn", "Guaraní", "Avañe'ẽ"),

    // South America
    PORTUGUESE_BR("pt-br", "Portuguese (Brazil)", "Português (Brasil)"),

    // Ancient & Classical Languages
    LATIN("la", "Latin", "Latina"),
    ANCIENT_GREEK("grc", "Ancient Greek", "Ἀρχαία Ἑλληνική"),
    SANSKRIT("sa", "Sanskrit", "संस्कृतम्"),
    CLASSICAL_ARABIC("arb", "Classical Arabic", "العربية الفصحى"),
    OLD_ENGLISH("ang", "Old English", "Ēald Englisċ"),
    OLD_NORSE("non", "Old Norse", "Dǫnsk tunga"),
    ARAMAIC("arc", "Aramaic", "ܐܪܡܝܐ"),
    COPTIC("cop", "Coptic", "ϯⲙⲉⲧⲣⲉⲙⲛ̀ⲭⲏⲙⲓ"),
    SUMERIAN("sux", "Sumerian", "𒅴𒂠"),
    MIDDLE_ENGLISH("enm", "Middle English", "Middle English"),
    OLD_PORTUGUESE("roa-opt", "Old Portuguese", "Português Antigo");

    private final String _code;
    private final String _name;
    private final String _nativeName;

    Language(String code, String name, String nativeName) {
        _code       = code;
        _name       = name;
        _nativeName = nativeName;
    }

    public String getCode() {
        return _code;
    }

    public String getName() {
        return _name;
    }

    public String getNativeName() {
        return _nativeName;
    }

    public static Language fromCode(String code) {
        for (Language lang : values()) {
            if (lang._code.equalsIgnoreCase(code.trim()))
                return lang;
        }
        throw new IllegalArgumentException("Unknown language code: " + code);
    }

    public boolean hasCode(String code) {
        return _code.equalsIgnoreCase(code.trim());
    }

    @Override
    public String toString() {
        return _code;
    }
}
