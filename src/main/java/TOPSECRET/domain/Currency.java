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

    private final String currencySymbol;

    Currency(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getSymbol() {
        return currencySymbol;
    }
}
