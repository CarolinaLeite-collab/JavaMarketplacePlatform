package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Currency;

import java.util.Objects;

/**
 * Represents a monetary price with a specific value and {@link Currency}.
 * <p>
 * Ensures that the price value is greater than zero and that the currency is not null.
 * Provides getters for value and currency, and overrides {@link #equals(Object)}, {@link #hashCode()}, and {@link #toString()}.
 * </p>
 */

public class Price {
    private final double _value;
    private final Currency _currency;

    //Public constructor to allow instantiation from other classes
    //Value validation: must be greater than 0, otherwise an exception is thrown
    //Currency validation: the field cannot be null
    public Price(double value, Currency currency) {
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid price value, must be greater than zero");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
        _value = value;
        _currency = currency;
    }

    public double getValue() {
        return _value;
    }

    public Currency getCurrency() {
        return _currency;
    }

    // Overrides
    @Override
    public int hashCode() {
        return Objects.hash(_value, _currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price other)) return false;
        return _value == other._value && _currency == other._currency;
    }

    @Override
    public String toString() {
        return _value + " " + _currency.getSymbol();
    }
}


