package MITELOVERS.dto;

import MITELOVERS.Action;
import MITELOVERS.Link;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CountryDTOTest {

    @Test
    void constructor_storesIdAndName() {
        // arrange
        String id = "123";
        String name = "Portugal";

        // act (SUT)
        CountryDTO dto = new CountryDTO(id, name);

        // assert
        assertEquals(id, dto.id());
        assertEquals(name, dto.name());
    }

    @Test
    void links_initiallyEmptyAndUnmodifiable() {
        // arrange
        CountryDTO dto = new CountryDTO("1", "Portugal");

        // act (SUT)
        List<Link> links = dto.links();

        // assert
        assertTrue(links.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> links.add(mock(Link.class)));
    }

    @Test
    void addLink_addsLinkToInternalList() {
        // arrange
        CountryDTO dto = new CountryDTO("1", "Portugal");
        Link link = mock(Link.class);

        // act (SUT)
        dto.addLink(link);

        // assert
        assertEquals(1, dto.links().size());
        assertSame(link, dto.links().get(0));
    }

    @Test
    void actions_initiallyEmptyAndUnmodifiable() {
        // arrange
        CountryDTO dto = new CountryDTO("1", "Portugal");

        // act (SUT)
        List<Action> actions = dto.actions();

        // assert
        assertTrue(actions.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> actions.add(mock(Action.class)));
    }

    @Test
    void addAction_addsActionToInternalList() {
        // arrange
        CountryDTO dto = new CountryDTO("1", "Portugal");
        Action action = mock(Action.class);

        // act (SUT)
        dto.addAction(action);

        // assert
        assertEquals(1, dto.actions().size());
        assertSame(action, dto.actions().get(0));
    }

}