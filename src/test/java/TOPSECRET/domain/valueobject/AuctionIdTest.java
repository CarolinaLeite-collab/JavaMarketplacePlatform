package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuctionIdTest {

    @Test
    void shouldCreateIdAuctionId() {
        // SUT
        new AuctionId(UUID.randomUUID());
    }

    @Test
    void shouldThrowExceptionWhenCreateIdAuctionIdIsNull() {
        //Assert, Act & SUT
        assertThrows(IllegalArgumentException.class, () -> new AuctionId(null));
    }

    @Test
    void shouldReturnSameUIID() {
        // Act & SUT
        UUID uuid = UUID.randomUUID();
        AuctionId auctionId = new AuctionId(uuid);

        // Assert
        assertEquals(uuid, auctionId.getId());
    }

    @Test
    void shouldReturnTrueWhenComparingSameInstance() {
        // SUT
        new AuctionId(UUID.randomUUID());

        // Act
        AuctionId id = AuctionId.createId();

        // Assert
        assertEquals(id, id);
    }

    @Test
    void ShouldReturnTrueWhenUUIDsAreEqual() {
        // SUT
        UUID uuid = UUID.randomUUID();

        // ACT
        AuctionId id1 = new AuctionId(uuid);
        AuctionId id2 = new AuctionId(uuid);

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldReturnFalseWhenUUIDAreDifferent() {
        // SUT
        new AuctionId(UUID.randomUUID());

        // Act
        AuctionId id1 = AuctionId.createId();
        AuctionId id2 = AuctionId.createId();

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        // SUT
        new AuctionId(UUID.randomUUID());

        // Act
        AuctionId id = AuctionId.createId();

        // Assert
        assertNotEquals(id, null);

    }

    @Test
    void shouldReturnFalseWhenCompareWithDifferentType() {
        // SUT
        new AuctionId(UUID.randomUUID());

        // Act
        AuctionId id = AuctionId.createId();

        // Assert
        assertNotEquals(id, "some string");
    }

    @Test
    void hashCodeShouldBeEqualForSameUUID() {
        // SUT
        UUID uuid = UUID.randomUUID();

        // Act
        AuctionId id1 = new AuctionId(uuid);
        AuctionId id2 = new AuctionId(uuid);

        // Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentUUID() {
        // SUT
        new AuctionId(UUID.randomUUID());

        // Act
        AuctionId id1 = AuctionId.createId();
        AuctionId id2 = AuctionId.createId();

        // Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());

    }

    @Test
    void shouldReturnUUIDString() {
        // SUT
        UUID uuid = UUID.randomUUID();

        // Act
        AuctionId id = new AuctionId(uuid);

        // Assert
        assertEquals(uuid.toString(), id.toString());
    }
}