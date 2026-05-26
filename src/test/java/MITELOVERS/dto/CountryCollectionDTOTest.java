package MITELOVERS.dto;

import MITELOVERS.Action;
import MITELOVERS.Link;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CountryCollectionDTOTest {

    @Test
    void constructor_copiesCountriesIntoInternalList() {
        // arrange
        CountryDTO dto1 = mock(CountryDTO.class);
        CountryDTO dto2 = mock(CountryDTO.class);
        List<CountryDTO> input = List.of(dto1, dto2);

        // act (SUT)
        CountryCollectionDTO collection = new CountryCollectionDTO(input);

        // assert
        assertEquals(2, collection.countries().size());
        assertEquals(dto1, collection.countries().get(0));
        assertEquals(dto2, collection.countries().get(1));

        assertNotSame(input, collection.countries());
    }

    @Test
    void countries_returnsUnmodifiableCopy() {
        // arrange
        CountryDTO dto = mock(CountryDTO.class);
        CountryCollectionDTO collection = new CountryCollectionDTO(List.of(dto));

        // act (SUT)
        List<CountryDTO> result = collection.countries();

        // assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(dto));
    }

    @Test
    void links_initiallyEmptyAndUnmodifiable() {
        // arrange
        CountryCollectionDTO collection = new CountryCollectionDTO(List.of());

        // act (SUT)
        List<Link> links = collection.links();

        // assert
        assertTrue(links.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> links.add(mock(Link.class)));
    }

    @Test
    void addLink_addsLinkToInternalList() {
        // arrange
        CountryCollectionDTO collection = new CountryCollectionDTO(List.of());
        Link link = mock(Link.class);

        // act (SUT)
        collection.addLink(link);

        // assert
        assertEquals(1, collection.links().size());
        assertSame(link, collection.links().get(0));
    }

    @Test
    void actions_initiallyEmptyAndUnmodifiable() {
        // arrange
        CountryCollectionDTO collection = new CountryCollectionDTO(List.of());

        // act (SUT)
        List<Action> actions = collection.actions();

        // assert
        assertTrue(actions.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> actions.add(mock(Action.class)));
    }

    @Test
    void addAction_addsActionToInternalList() {
        // arrange
        CountryCollectionDTO collection = new CountryCollectionDTO(List.of());
        Action action = mock(Action.class);

        // act (SUT)
        collection.addAction(action);

        // assert
        assertEquals(1, collection.actions().size());
        assertSame(action, collection.actions().get(0));
    }

}