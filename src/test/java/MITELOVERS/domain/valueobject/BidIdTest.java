package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BidIdTest {

    private UUID _uuid;

    @BeforeEach
    void setUp() {
        _uuid = UUID.randomUUID();
    }

    @Test
    void testConstructor() {

        //SUT
        new BidId(_uuid);
    }

    @Test
    void throwsExceptionWhenUUIDBidIdIsNull() {

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> new BidId((UUID) null));    }

    @Test
    void throwsExceptionWhenStringBidIsNull() {

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> new BidId((String) null));
    }

    @Test
    void bidIdIsEqualToItself() {

        //SUT
        BidId id = new BidId(_uuid);

        //Act + Assert
        assertEquals(id, id);
    }

    @Test
    void returnNotEqualWhenBidIdIsNull() {

        //SUT
        BidId id = new BidId(_uuid);

        //Act + Assert
        assertNotEquals(null, id);
    }

    @Test
    void returnNotEqualWhenBidIdIsDifferent() {

        //SUT
        BidId id = new BidId(_uuid);

        //Act
        String fakeId = "fakeId";

        //Assert
        assertNotEquals(fakeId, id);
    }

    @Test
    void returnTrueWhenTwoBidIdsAreEqual() {

        //SUT
        BidId id = new BidId(_uuid);
        BidId id2 = new BidId(_uuid);

        //Act + Assert
        assertTrue(id.equals(id2));
    }

    @Test
    void returnFalseWhenTwoBidIdsAreDifferent() {

        //SUT
        BidId id = BidId.newId();
        BidId id2 = BidId.newId();

        //Act + Assert
        assertFalse(id.equals(id2));
    }

    @Test
    void sameBidIdHasSameHashcode() {
        //Arrange
        UUID uuid =  UUID.randomUUID();

        //SUT
        BidId id = new BidId(uuid);
        BidId id2 = new BidId(uuid);

        //Act + Assert
        assertEquals(id.hashCode(), id2.hashCode());
    }

    @Test
    void returnEqualsWhenToStringIsEqual() {

        UUID uuid = UUID.randomUUID();
        BidId id = new BidId(uuid);

        assertEquals(uuid.toString(), id.toString());
    }

    @Test
    void bidIdShouldNotBeEqualToDifferentObjectType() {
        BidId id = new BidId(_uuid);

        assertNotEquals(id, "fakeId");
    }


    @Test
    void differentBidIdsShouldHaveDifferentHashCodes() {
        UUID uuid1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID uuid2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

        BidId id1 = new BidId(uuid1);
        BidId id2 = new BidId(uuid2);

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void throwsExceptionWhenStringBidIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new BidId(" "));
    }

    @Test
    void throwsExceptionWhenStringBidIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new BidId(""));
    }

    @Test
    void shouldCreateBidIdFromValidString() {
        UUID uuid = UUID.randomUUID();

        BidId id = new BidId(uuid.toString());

        assertEquals(uuid.toString(), id.toString());
    }
}
