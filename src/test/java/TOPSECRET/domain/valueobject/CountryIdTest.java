package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryIdTest {

	@Test
	void acceptsValidCodesAndNormalizes() {
		CountryId pt = new CountryId("PT");
		assertEquals("PT", pt.toString());

		CountryId us = new CountryId("us");
		assertEquals("US", us.toString());
	}

	@Test
	void rejectsInvalidCodes() {
		assertThrows(IllegalArgumentException.class, () -> new CountryId("USA"));
		assertThrows(IllegalArgumentException.class, () -> new CountryId("12"));
		assertThrows(IllegalArgumentException.class, () -> new CountryId(null));
	}
}


