package MITELOVERS;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class ActionTest {

    @Test
    void createActionWithoutSchema_shouldStoreFieldsCorrectly() {
        Action action = new Action("updateCountry", "PUT", "/countries/123");

        assertEquals("updateCountry", action.name());
        assertEquals("PUT", action.method());
        assertEquals("/countries/123", action.href());
        assertNull(action.schema());
    }

    @Test
    void createActionWithMockedSchema_shouldStoreSchema() {
        Object mockedSchema = mock(Object.class);

        Action action = new Action("createCountry", "POST", "/countries", mockedSchema);

        assertEquals("createCountry", action.name());
        assertEquals("POST", action.method());
        assertEquals("/countries", action.href());
        assertSame(mockedSchema, action.schema());

        // No interactions expected with schema object
        verifyNoInteractions(mockedSchema);
    }

    @Test
    void constructor_shouldThrowException_whenNameIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new Action(null, "POST", "/countries"));

        assertThrows(IllegalArgumentException.class,
                () -> new Action("   ", "POST", "/countries"));
    }

    @Test
    void constructor_shouldThrowException_whenMethodIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new Action("createCountry", null, "/countries"));

        assertThrows(IllegalArgumentException.class,
                () -> new Action("createCountry", "   ", "/countries"));
    }

    @Test
    void constructor_shouldThrowException_whenHrefIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new Action("createCountry", "POST", null));

        assertThrows(IllegalArgumentException.class,
                () -> new Action("createCountry", "POST", "   "));
    }

    @Test
    void getters_shouldReturnValuesExactlyAsProvided() {
        Object schema = mock(Object.class);

        Action action = new Action("deleteCountry", "DELETE", "/countries/1", schema);

        assertEquals("deleteCountry", action.name());
        assertEquals("DELETE", action.method());
        assertEquals("/countries/1", action.href());
        assertSame(schema, action.schema());
    }

}