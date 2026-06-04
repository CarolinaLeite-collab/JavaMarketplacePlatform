package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SharedDurationTest {

    @Test
    void shouldCreateSharedDuration() {
        new SharedDuration(7);
    }

    @Test
    void shouldThrowWhenDaysIsZero() {
        assertThrows(IllegalArgumentException.class, () -> new SharedDuration(0));
    }

    @Test
    void shouldThrowWhenDaysIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new SharedDuration(-1));
    }

    @Test
    void getDaysShouldReturnCorrectValue() {
        SharedDuration duration = new SharedDuration(30);
        assertEquals(30, duration.getDays());
    }

    @Test
    void equalsDurationsShouldBeEqual() {
        assertEquals(new SharedDuration(7), new SharedDuration(7));
    }

    @Test
    void differentDurationsShouldNotBeEqual() {
        assertNotEquals(new SharedDuration(7), new SharedDuration(14));
    }

    @Test
    void toStringShouldReturnDaysAsString() {
        assertEquals("7", new SharedDuration(7).toString());
    }

    @Test
    void hashCodeShouldBeEqualForEqualDurations() {
        assertEquals(new SharedDuration(7).hashCode(), new SharedDuration(7).hashCode());
    }

    @Test
    void shouldNotBeEqualToDifferentObjectType() {
        //Act & Assert
        assertNotEquals(new SharedDuration(7), "7");
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentDurations() {
        //Act & Assert
        assertNotEquals(new SharedDuration(7).hashCode(), new SharedDuration(14).hashCode());
    }
}