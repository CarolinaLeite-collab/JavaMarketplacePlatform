package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryNameTest {

    @Test
    void constructsAndNormalizesName() {
        // Tests both normalization and the value() getter
        CountryName name = new CountryName("   Portugal   ");
        assertEquals("PORTUGAL", name.value());
    }

    @Test
    void hasConsistentEqualityAndHashCode() {
        CountryName portugal = new CountryName("Portugal");
        CountryName samePortugal = new CountryName("  portugal  ");
        CountryName spain = new CountryName("Spain");

        // 1. Reflexivity: Kills "this == o" mutant (Mutation 45.2)
        assertEquals(portugal, portugal);

        // 2. Value Equality & Normalization: Kills "_value.equals" mutants
        assertEquals(portugal, samePortugal);
        assertEquals(portugal.hashCode(), samePortugal.hashCode());

        // 3. Inequality: Kills "replaced boolean return with true" (Mutation 47.2)
        assertNotEquals(portugal, spain);

        // 4. Type Safety: Kills "instanceof" NO_COVERAGE (Mutation 46.1)
        // Passing a different object type forces the logic to return false
        assertNotEquals(portugal, new Object());
        assertNotEquals(null, portugal);

        // 5. HashCode Content: Kills "replaced int return with 0" (Mutation 52.1)
        assertNotEquals(0, portugal.hashCode());
        assertEquals("PORTUGAL".hashCode(), portugal.hashCode());
    }

    @Test
    void normalizationHandlesMultipleSpaces() {
        // Ensures the replaceAll("\\s+", " ") logic is verified
        CountryName name = new CountryName("United    Kingdom");
        assertEquals("UNITED KINGDOM", name.value());
        assertEquals("UNITED KINGDOM", name.toString());
    }

    @Test
    void toStringReturnsValue() {
        // Kills the toString empty return mutant (Mutation 57.1)
        CountryName name = new CountryName("Portugal");
        assertEquals("PORTUGAL", name.toString());
    }

    @Test
    void throwsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> new CountryName(null));
    }

    @Test
    void throwsOnEmpty() {
        // Tests blank space normalization resulting in empty
        assertThrows(IllegalArgumentException.class, () -> new CountryName("   "));
    }

    @Test
    void throwsOnInvalidCharacters() {
        // Tests the Regex pattern
        assertThrows(IllegalArgumentException.class, () -> new CountryName("#Portugal"));
        assertThrows(IllegalArgumentException.class, () -> new CountryName("Portugal123"));
    }
}