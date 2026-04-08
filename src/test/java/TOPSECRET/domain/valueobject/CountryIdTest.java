package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryIdTest {

	@Test
	void acceptsValidCodesAndNormalizes() {
		CountryId pt = new CountryId("PT");
		assertEquals("PT", pt.toString());
		assertEquals("PT".hashCode(), pt.hashCode());

		CountryId us = new CountryId("us");
		assertEquals("US", us.toString());
		assertEquals("US".hashCode(), us.hashCode());
	}

	@Test
	void isNotEqualToDifferentType() {
		Object otherType = "PT";
		assertNotEquals(new CountryId("PT"), otherType);
	}

	@Test
	void isEqualToSameInstanceAndEquivalentNormalizedValue() {
		CountryId pt = new CountryId("PT");
		assertEquals(pt, identity(pt));
		assertEquals(new CountryId("pt"), pt);
		assertNotEquals(new CountryId("PT"), new CountryId("US"));
	}

	@Test
	void rejectsInvalidCodes() {
		assertThrows(IllegalArgumentException.class, () -> new CountryId("USA"));
		assertThrows(IllegalArgumentException.class, () -> new CountryId("12"));
		assertThrows(IllegalArgumentException.class, () -> new CountryId(null));
	}

	private CountryId identity(CountryId countryId) {
		return countryId;
	}
}


