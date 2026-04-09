package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountryIdTest {

	@Test
	void generatesIsoCodeFromCountryName() {
		CountryId pt = new CountryId(new CountryName("Portugal"));
		assertEquals("PT", pt.toString());
		assertEquals("PT".hashCode(), pt.hashCode());
	}

	@Test
	void equalsAndHashCodeUseGeneratedIsoCode() {
		CountryId portugal = new CountryId(new CountryName("Portugal"));
		CountryId samePortugal = new CountryId(new CountryName("  portugal  "));
		CountryId spain = new CountryId(new CountryName("Spain"));

		assertEquals(portugal, samePortugal);
		assertEquals(portugal.hashCode(), samePortugal.hashCode());
		assertNotEquals(portugal, spain);
		assertNotEquals(new Object(), portugal);
	}

	@Test
	void isNotEqualToDifferentType() {
		Object otherType = "PT";
		assertNotEquals(new CountryId(new CountryName("Portugal")), otherType);
	}

	@Test
	void isEqualToSameInstanceAndEquivalentNormalizedValue() {
		CountryId pt = new CountryId(new CountryName("Portugal"));
		assertEquals(pt, identity(pt));
		assertEquals(new CountryId(new CountryName("portugal")), pt);
		assertNotEquals(new CountryId(new CountryName("Portugal")), new CountryId(new CountryName("United States")));
	}

	@Test
	void rejectsNonExistingCountryName() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> new CountryId(new CountryName("Atlantis")));
		assertTrue(ex.getMessage().contains("No ISO 3166 code found"));
    }

	@Test
	void acceptsValidIsoCodeWithTrimAndUppercaseNormalization() {
		CountryId pt = new CountryId("  pt  ");
		assertEquals("PT", pt.toString());
	}

	@Test
	void rejectsInvalidIsoCode() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> new CountryId("A1"));
		assertTrue(ex.getMessage().contains("Invalid ISO 3166 code"));
	}

	@Test
	void rejectsNullOrBlankIsoCode() {
		IllegalArgumentException nullEx = assertThrows(IllegalArgumentException.class,
				() -> new CountryId((String) null));
		assertTrue(nullEx.getMessage().contains("null or blank"));

		IllegalArgumentException blankEx = assertThrows(IllegalArgumentException.class,
				() -> new CountryId("   "));
		assertTrue(blankEx.getMessage().contains("null or blank"));
	}

	@Test
	void rejectsNullCountryName() {
		assertThrows(NullPointerException.class, () -> new CountryId((CountryName) null));
	}

	private CountryId identity(CountryId countryId) {
		return countryId;
	}
}
