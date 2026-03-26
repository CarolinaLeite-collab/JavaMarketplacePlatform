package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BindingTest {

    // Verifying each enum constant returns its expected display name; implicitly tests enum constructor wiring
    @Test
    void allBindingsShouldReturnCorrectDisplayName(){
        assertEquals("PUR binding", Binding.PUR.getDisplayName());
        assertEquals("Saddle stitch binding", Binding.SADDLE_STITCH.getDisplayName());
        assertEquals("Hardcover binding", Binding.HARDCOVER.getDisplayName());
        assertEquals("Singer sewn binding", Binding.SINGER_SEWN.getDisplayName());
        assertEquals("Section sewn binding", Binding.SECTION_SEWN.getDisplayName());
        assertEquals("Coptic stitch binding", Binding.COPTIC_STITCH.getDisplayName());
        assertEquals("Wiro binding", Binding.WIRO.getDisplayName());
        assertEquals("Interscrew binding", Binding.INTERSCREW.getDisplayName());
    }

    // Ensuring toString() returns the exact text
    @Test
    void toStringShouldReturnDisplayName() {

        assertEquals("PUR binding", Binding.PUR.toString());
        assertEquals("Saddle stitch binding", Binding.SADDLE_STITCH.toString());
        assertEquals("Hardcover binding", Binding.HARDCOVER.toString());
        assertEquals("Singer sewn binding", Binding.SINGER_SEWN.toString());
        assertEquals("Section sewn binding", Binding.SECTION_SEWN.toString());
        assertEquals("Coptic stitch binding", Binding.COPTIC_STITCH.toString());
        assertEquals("Wiro binding", Binding.WIRO.toString());
        assertEquals("Interscrew binding", Binding.INTERSCREW.toString());
    }

    // Not checking for exact text, but the relationships between toString() and getDisplayName()
    @Test
    void toStringShouldMatchDisplayNameForAllBindings() {
        for (Binding binding : Binding.values()) {
            assertEquals(binding.getDisplayName(), binding.toString());
        }
    }

}