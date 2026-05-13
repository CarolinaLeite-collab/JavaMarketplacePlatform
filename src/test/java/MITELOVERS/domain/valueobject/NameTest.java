package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    private static void assertValidName (String rawInput, String expectedNormalized) {

        //Act & SUT
        Name name = new Name(rawInput);

        //Assert
        assertAll(
                () -> assertEquals(expectedNormalized, name.getName()),
                () -> assertEquals(expectedNormalized, name.toString())
        );
    }

    private static void assertInvalidName (String rawImput) {
        assertThrows(IllegalArgumentException.class, () -> new Name(rawImput));
    }

    @ParameterizedTest
    @MethodSource("validNames")
    void constructorValidInputsCreatesNormalizedName(String input, String expected) {
        assertValidName(input, expected);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> validNames() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("jose mourinho", "Jose Mourinho"),
                org.junit.jupiter.params.provider.Arguments.of("JOSE MOURINHO", "Jose Mourinho"),
                org.junit.jupiter.params.provider.Arguments.of("  jOSe   mOuRiNhO  ", "Jose Mourinho"),
                org.junit.jupiter.params.provider.Arguments.of("Ana-Maria", "Ana-Maria"),
                org.junit.jupiter.params.provider.Arguments.of("D'Avila", "D'Avila"),
                org.junit.jupiter.params.provider.Arguments.of("Al", "Al"),
                org.junit.jupiter.params.provider.Arguments.of("A".repeat(80), "A" + "a".repeat(79))
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
    void constructorNormalizesCapitalization() {
        Name name = new Name("jOSe mOuRiNhO");

        assertEquals("Jose Mourinho", name.getName());
    }

    @Test
    void constructorNormalizesSpacesAndCapitalization() {
        Name name = new Name("   jOSe   mOuRiNhO   ");

        assertEquals("Jose Mourinho", name.getName());
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        Name name = new Name("Jose Mourinho");

        // Assert & Act & SUT
        assertTrue(name.equals(name));
    }

    @Test
    void equalsObjectsWithSameNormalizedValueReturnTrue() {
        // Arrange
        Name name1 = new Name("Jose Mourinho");
        Name name2 = new Name("  Jose   Mourinho  ");

        // Assert & Act & SUT
        assertEquals(name1, name2);
    }

    @Test
    void equalsObjectsWithDifferentValuesReturnFalse() {
        // Arrange
        Name name1 = new Name("Jose Mourinho");
        Name name2 = new Name("Pep Guardiola");

        // Assert & Act & SUT
        assertNotEquals(name1, name2);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        Name name = new Name("Jose Mourinho");

        // Assert & Act & SUT
        assertNotEquals(null, name);
    }

    @Test
    void equalsDifferentClassReturnsFalse() {
        // Arrange
        Name name = new Name("Jose Mourinho");

        // Assert & Act & SUT
        assertNotEquals(name, "Jose Mourinho");
    }

    @Test
    void hashCodeEqualObjectsHaveSameHashCode() {
        // Arrange
        Name n1 = new Name("Jose Mourinho");
        Name n2 = new Name("  Jose   Mourinho ");

        // Assert & Act & SUT
        assertEquals(n1.hashCode(), n2.hashCode());
    }

    @Test
    void hashCodeDifferentNamesProduceDifferentHashCodes() {

        // Arrange
        Name n1 = new Name("Jose Mourinho");
        Name n2 = new Name("Pep Guardiola");

        // Assert & Act & SUT
        assertNotEquals(n1.hashCode(), n2.hashCode());
    }

}
