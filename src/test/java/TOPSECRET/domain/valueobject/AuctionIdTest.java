package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionIdTest {

    @Test
    void shouldCreateIdAuctionId() {
        // SUT
        new AuctionId();
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        //assert & act
        assertThrows(IllegalArgumentException.class, () -> new AuctionId(null));    // SUT
    }

    @Test
    void shouldThrowExceptionWhenIdHasInvalidFormat() {
        //assert & act
        assertThrows(IllegalArgumentException.class, () -> new AuctionId("INVALID"));   // SUT
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotStartWithAU() {
        //assert & act
        assertThrows(IllegalArgumentException.class, () -> new AuctionId("XX-12345678"));   // SUT
    }

    @Test
    void shouldThrowExceptionWhenIdHasWrongLength() {
        //assert & act
        assertThrows(IllegalArgumentException.class, () -> new AuctionId("AU-1234"));   // SUT
    }

    @Test
    void IdsShouldBeUnique() {
        // Arrange & SUT
        AuctionId id1 = new AuctionId();
        AuctionId id2 = new AuctionId();

        //Assert & Act
        assertNotEquals(id1, id2);
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldReturnTrueWhenComparingSameInstance() {
        // Arrange & SUT
        AuctionId id = new AuctionId();

        // Assert & Act
        assertEquals(id, id);
    }

    @Test
    void ShouldReturnTrueWhenIdsAreEqual() {
        // Arrange & SUT
        AuctionId id1 = new AuctionId("AU-12345678");
        AuctionId id2 = new AuctionId("AU-12345678");

        // Assert & Act
        assertEquals(id1, id2);
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        // Arrange & SUT
        AuctionId id = new AuctionId();

        // Assert & Act
        assertNotEquals(id, null);
    }

    @Test
    void shouldReturnFalseWhenCompareWithDifferentType() {
        // Arrange & SUT
        AuctionId id = new AuctionId();
        String string = "some string";

        // Assert & Act
        assertNotEquals(id, string);
    }

    @Test
    void hashCodeShouldBeEqualForSameUUID() {
        // Arrange & SUT
        AuctionId id1 = new AuctionId("AU-12345678");
        AuctionId id2 = new AuctionId("AU-12345678");

        // Assert & Act
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentUUID() {
        // Arrange & SUT
        AuctionId id1 = new AuctionId();
        AuctionId id2 = new AuctionId();

        // Assert & Act
        assertNotEquals(id1.hashCode(), id2.hashCode());

    }

    @Test
    void shouldReturnUUIDString() {
        // Arrange & SUT
        AuctionId id = new AuctionId();

        // Assert & Act
        assertTrue(id.toString().matches("AU-[A-Z0-9]{8}"));
        assertEquals(11, id.toString().length());
    }
}