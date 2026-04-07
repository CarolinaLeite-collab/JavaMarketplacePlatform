package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

import java.util.Locale;

/**
 * Value object representing a validated and normalized country name.
 */
public final class CountryName implements ValueObject {

	private final String _value;

	public CountryName(String name) {
		if (name == null) throw new IllegalArgumentException("Country name cannot be null");
		String result = name.trim();
		if (result.isEmpty()) throw new IllegalArgumentException("Country name cannot be empty");

		String pattern = "^[\\p{L}]+(?: [\\p{L}]+)*$";
		if (!result.matches(pattern)) throw new IllegalArgumentException("Invalid country name: " + name);

		result = result.replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
		this._value = result;
	}

	public String value() {
		return _value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CountryName other)) return false;
		return _value.equals(other._value);
	}

	@Override
	public int hashCode() {
		return _value.hashCode();
	}

	@Override
	public String toString() {
		return _value;
	}
}


