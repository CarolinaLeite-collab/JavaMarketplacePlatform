package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListOfItemsIdTest {

    @Test
    void newIdShouldGenerateIdWithCorrectPrefix() {
        GenreId genreId = new GenreId("Fantasy");
        ListOfItemsId id = ListOfItemsId.newId(genreId);

        assertTrue(id.getValue().startsWith("LOI-FANTASY-"));
    }

    @Test
    void newIdShouldGenerateUniqueIds() {
        GenreId genreId = new GenreId("Rock");

        ListOfItemsId id1 = ListOfItemsId.newId(genreId);
        ListOfItemsId id2 = ListOfItemsId.newId(genreId);

        assertNotEquals(id1, id2);
        assertNotEquals(id1.getValue(), id2.getValue());
    }

    @Test
    void equalsShouldReturnTrueForSameStringValue() {
        GenreId genreId = new GenreId("Jazz");

        String value = "LOI-JAZZ-ABCDEF12";
        ListOfItemsId id1 = new ListOfItemsId(value);
        ListOfItemsId id2 = new ListOfItemsId(value);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentValues() {
        ListOfItemsId id1 = new ListOfItemsId("LOI-POP-AAAA1111");
        ListOfItemsId id2 = new ListOfItemsId("LOI-POP-BBBB2222");

        assertNotEquals(id1, id2);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        ListOfItemsId id = new ListOfItemsId("LOI-ROCK-12345678");

        assertNotEquals(id, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        ListOfItemsId id = new ListOfItemsId("LOI-ROCK-12345678");

        assertNotEquals(id, "not an ID");
    }

    @Test
    void toStringShouldReturnInternalValue() {
        String value = "LOI-METAL-CAFEBABE";
        ListOfItemsId id = new ListOfItemsId(value);

        assertEquals(value, id.toString());
    }

    @Test
    void hashCodeShouldDependOnValue() {
        ListOfItemsId id1 = new ListOfItemsId("LOI-ROCK-AAAA1111");
        ListOfItemsId id2 = new ListOfItemsId("LOI-ROCK-BBBB2222");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldBeEqualForEqualIds() {
        ListOfItemsId id1 = new ListOfItemsId("LOI-JAZZ-ABCDEF12");
        ListOfItemsId id2 = new ListOfItemsId("LOI-JAZZ-ABCDEF12");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void newIdShouldThrowWhenGenreIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> ListOfItemsId.newId(null));
    }

}
