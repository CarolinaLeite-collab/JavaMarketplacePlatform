package TOPSECRET.domain;

/**
 * Enum representing supported currencies and their corresponding symbols.
 * <p>
 * Each currency has an associated symbol that can be retrieved using {@link #getSymbol()}.
 * </p>
 */

public enum Currency {
    AUD("$"),
    CAD("$"),
    CHF("Fr"),
    CNY("¥"),
    EUR("€"),
    GBP("£"),
    INR("₹"),
    JPY("¥"),
    NZD("$"),
    USD("$");

    private final String _currencySymbol;

    Currency(String currencySymbol) {
        _currencySymbol = currencySymbol;
    }

    public String getSymbol() {
        return _currencySymbol;
    }
}
