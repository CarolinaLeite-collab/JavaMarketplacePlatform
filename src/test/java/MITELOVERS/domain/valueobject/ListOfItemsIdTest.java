package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListOfItemsIdTest {

    @Test
    void newIdShouldGenerateIdWithCorrectPrefix() {

        ListOfItemsId id = ListOfItemsId.newId();

        assertTrue(id.getValue().startsWith("LOI-"));
    }

    @Test
    void newIdShouldGenerateUniqueIds() {

        ListOfItemsId id1 = ListOfItemsId.newId();
        ListOfItemsId id2 = ListOfItemsId.newId();

        assertNotEquals(id1, id2);
        assertNotEquals(id1.getValue(), id2.getValue());
    }

    @Test
    void equalsShouldReturnTrueForSameStringValue() {

        String value = "LOI-ABCDEF12";
        ListOfItemsId id1 = new ListOfItemsId(value);
        ListOfItemsId id2 = new ListOfItemsId(value);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentValues() {
        ListOfItemsId id1 = new ListOfItemsId("LOI-AAAA1111");
        ListOfItemsId id2 = new ListOfItemsId("LOI-BBBB2222");

        assertNotEquals(id1, id2);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        ListOfItemsId id = new ListOfItemsId("LOI-12345678");

        assertNotEquals(id, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        ListOfItemsId id = new ListOfItemsId("LOI-12345678");

        assertNotEquals(id, "not an ID");
    }

    @Test
    void toStringShouldReturnInternalValue() {
        String value = "LOI-CAFEBABE";
        ListOfItemsId id = new ListOfItemsId(value);

        assertEquals(value, id.toString());
    }

    @Test
    void hashCodeShouldDependOnValue() {
        ListOfItemsId id1 = new ListOfItemsId("LOI-AAAA1111");
        ListOfItemsId id2 = new ListOfItemsId("LOI-BBBB2222");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldBeEqualForEqualIds() {
        ListOfItemsId id1 = new ListOfItemsId("LOI-ABCDEF12");
        ListOfItemsId id2 = new ListOfItemsId("LOI-ABCDEF12");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

}
