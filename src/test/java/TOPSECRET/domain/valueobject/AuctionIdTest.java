package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionIdTest {

    @Test
    void shouldCreateIdAuctionId() {
        // Act
        new AuctionId();
    }

    @Test
    void IdsShouldBeUnique() {
        // Act
        AuctionId id1 = new AuctionId();
        AuctionId id2 = new AuctionId();

        //Assert
        assertNotEquals(id1, id2);
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldReturnTrueWhenComparingSameInstance() {
        // Act
        AuctionId id = new AuctionId();

        // Assert
        assertEquals(id, id);
    }

    @Test
    void ShouldReturnTrueWhenIdsAreEqual() {
        // ACT
        AuctionId id1 = new AuctionId("AU-12345678");
        AuctionId id2 = new AuctionId("AU-12345678");

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldReturnFalseWhenIdAreDifferent() {
        // Act
        AuctionId id1 = new AuctionId();
        AuctionId id2 = new AuctionId();

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        // Act
        AuctionId id = new AuctionId();

        // Assert
        assertNotEquals(id, null);
    }

    @Test
    void shouldReturnFalseWhenCompareWithDifferentType() {
        // Act
        AuctionId id = new AuctionId();

        // Assert
        assertNotEquals(id, "some string");
    }

    @Test
    void hashCodeShouldBeEqualForSameUUID() {
        // Act
        AuctionId id1 = new AuctionId("AU-12345678");
        AuctionId id2 = new AuctionId("AU-12345678");

        // Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentUUID() {
        // Act
        AuctionId id1 = new AuctionId();
        AuctionId id2 = new AuctionId();

        // Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());

    }

    @Test
    void shouldReturnUUIDString() {
        // Act
        AuctionId id = new AuctionId();

        // Assert
        assertEquals(id.toString(), id.toString());
        assertTrue(id.toString().startsWith("AU-"));
        assertEquals(11, id.toString().length());
    }
}