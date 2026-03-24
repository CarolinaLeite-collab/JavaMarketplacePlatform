package TOPSECRET.domain;

import TOPSECRET.ddd.ValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ValueObject.Name}.
 *
 * <p>Validates construction rules: normalization of whitespace, allowed characters,
 * length boundaries, and rejection of invalid inputs.</p>
 *
 * <p>No Mockito doubles are used — {@link ValueObject.Name} is a pure Value Object.</p>
 */

class NameTest {

    private static void assertValidName (String rawInput, String expectedNormalized) {

        //Act & SUT
        ValueObject.Name name = new ValueObject.Name(rawInput);

        //Assert
        assertAll(
                () -> assertEquals(expectedNormalized, name.get_Name()),
                () -> assertEquals(expectedNormalized, name.toString())
        );
    }

    private static void assertInvalidName (String rawImput) {
        assertThrows(IllegalArgumentException.class, () -> new ValueObject.Name(rawImput));
    }

    @ParameterizedTest
    @MethodSource("validNames")
    void constuctorValidInputsCreatesNormalizedName (String input, String expected) {
        assertValidName(input, expected);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> validNames() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("Jose Mourinho", "Jose Mourinho"),
                org.junit.jupiter.params.provider.Arguments.of("José Mourinho", "José Mourinho"),
                org.junit.jupiter.params.provider.Arguments.of("  José   Mourinho  ", "José Mourinho"),
                org.junit.jupiter.params.provider.Arguments.of("Ana-Maria", "Ana-Maria"),
                org.junit.jupiter.params.provider.Arguments.of("D'Avila", "D'Avila"),
                org.junit.jupiter.params.provider.Arguments.of("Al", "Al"),
                org.junit.jupiter.params.provider.Arguments.of("A".repeat(80), "A".repeat(80))
        );
    }

    @Test
    void constructorNullThrowsIllegalArgumentException() {
        assertInvalidName(null);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "\t \n"})
    void constructorBlankLikeInputsThrowsIllegalArgumentException(String bad) {
        assertInvalidName(bad);
    }

    @ParameterizedTest
    @MethodSource("invalidLengths")
    void constructorInvalidLengthsThrowsIllegalArgumentException(String bad) {
        assertInvalidName(bad);
    }

    private static Stream<String> invalidLengths() {
        return Stream.of(
                "A",
                "A".repeat(81)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jose2 Mourinho", "1234"})
    void constructorNumbersNotAllowedThrowsIllegalArgumentException(String bad) {
        assertInvalidName(bad);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jose_Mourinho", "Jose@Mourinho", "Jose.Mourinho"})
    void constructorInvalidSymbolsThrowsIllegalArgumentException(String bad) {
        assertInvalidName(bad);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-Jose", "Jose-", "'Jose", "Jose'"})
    void constructorStartOrEndWithSeparatorThrowsIllegalArgumentException(String bad) {
        assertInvalidName(bad);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        ValueObject.Name name = new ValueObject.Name("Jose Mourinho");

        // Assert & Act & SUT
        assertTrue(name.equals(name));
    }

    @Test
    void equalsObjectsWithSameNormalizedValueReturnTrue() {
        // Arrange
        ValueObject.Name name1 = new ValueObject.Name("Jose Mourinho");
        ValueObject.Name name2 = new ValueObject.Name("  Jose   Mourinho  ");

        // Assert & Act & SUT
        assertEquals(name1, name2);
    }

    @Test
    void equalsObjectsWithDifferentValuesReturnFalse() {
        // Arrange
        ValueObject.Name name1 = new ValueObject.Name("Jose Mourinho");
        ValueObject.Name name2 = new ValueObject.Name("Pep Guardiola");

        // Assert & Act & SUT
        assertNotEquals(name1, name2);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        ValueObject.Name name = new ValueObject.Name("Jose Mourinho");

        // Assert & Act & SUT
        assertNotEquals(null, name);
    }

    @Test
    void equalsDifferentClassReturnsFalse() {
        // Arrange
        ValueObject.Name name = new ValueObject.Name("Jose Mourinho");

        // Assert & Act & SUT
        assertNotEquals(name, "Jose Mourinho");
    }

    @Test
    void hashCodeEqualObjectsHaveSameHashCode() {
        // Arrange
        ValueObject.Name n1 = new ValueObject.Name("Jose Mourinho");
        ValueObject.Name n2 = new ValueObject.Name("  Jose   Mourinho ");

        // Assert & Act & SUT
        assertEquals(n1.hashCode(), n2.hashCode());
    }

    @Test
    void hashCodeDifferentNamesProduceDifferentHashCodes() {

        // Arrange
        ValueObject.Name n1 = new ValueObject.Name("Jose Mourinho");
        ValueObject.Name n2 = new ValueObject.Name("Pep Guardiola");

        // Assert & Act & SUT
        assertNotEquals(n1.hashCode(), n2.hashCode());
    }

}