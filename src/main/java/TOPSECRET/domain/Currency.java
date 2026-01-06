package TOPSECRET.domain;

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
