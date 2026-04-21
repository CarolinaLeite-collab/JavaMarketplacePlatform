package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListOfItemsIdTest {

    @Test
    void constructorShouldStoreUUID() {
        UUID uuid = UUID.randomUUID();
        ListOfItemsId id = new ListOfItemsId(uuid);

        assertEquals(uuid, id.getValue());
    }

    @Test
    void constructorShouldThrowWhenUUIDIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListOfItemsId(null));
    }

    @Test
    void newIdShouldGenerateUniqueIds() {
        ListOfItemsId id1 = ListOfItemsId.newId();
        ListOfItemsId id2 = ListOfItemsId.newId();

        assertNotEquals(id1, id2);
        assertNotEquals(id1.getValue(), id2.getValue());
    }

    @Test
    void equalsShouldReturnTrueForSameUUID() {
        UUID uuid = UUID.randomUUID();

        ListOfItemsId id1 = new ListOfItemsId(uuid);
        ListOfItemsId id2 = new ListOfItemsId(uuid);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentUUIDs() {
        ListOfItemsId id1 = new ListOfItemsId(UUID.randomUUID());
        ListOfItemsId id2 = new ListOfItemsId(UUID.randomUUID());

        assertNotEquals(id1, id2);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        ListOfItemsId id = new ListOfItemsId(UUID.randomUUID());

        assertNotEquals(id, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        ListOfItemsId id = new ListOfItemsId(UUID.randomUUID());

        assertNotEquals(id, "not an ID");
    }

    @Test
    void toStringShouldReturnUUIDString() {
        UUID uuid = UUID.randomUUID();
        ListOfItemsId id = new ListOfItemsId(uuid);

        assertEquals(uuid.toString(), id.toString());
    }

}
