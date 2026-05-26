package MITELOVERS.mapper;

import MITELOVERS.Link;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.domain.valueobject.CountryName;
import MITELOVERS.dto.CountryDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryMapperTest {

    @Test
    void toDTO_mapsCountryToDTO_andAddsSelfLink() {
        // arrange
        CountryId id = mock(CountryId.class);
        CountryName name = mock(CountryName.class);
        Country country = mock(Country.class);

        when(id.toString()).thenReturn("123");
        when(name.toString()).thenReturn("Portugal");

        when(country.identity()).thenReturn(id);
        when(country.name()).thenReturn(name);

        CountryMapper mapper = new CountryMapper();

        // act (SUT)
        CountryDTO dto = mapper.toDTO(country);

        // assert
        assertEquals("123", dto.id());
        assertEquals("Portugal", dto.name());

        // link added
        assertEquals(1, dto.links().size());
        Link link = dto.links().get(0);

        assertEquals("self", link.rel());
        assertEquals("/countries/123", link.href());

        // no actions added
        assertTrue(dto.actions().isEmpty());
    }

    @Test
    void toDTO_createsNewDTOInstanceEveryTime() {
        // arrange
        CountryId id = mock(CountryId.class);
        CountryName name = mock(CountryName.class);
        Country country = mock(Country.class);

        when(id.toString()).thenReturn("1");
        when(name.toString()).thenReturn("A");
        when(country.identity()).thenReturn(id);
        when(country.name()).thenReturn(name);

        CountryMapper mapper = new CountryMapper();

        // act (SUT)
        CountryDTO dto1 = mapper.toDTO(country);
        CountryDTO dto2 = mapper.toDTO(country);

        // assert
        assertNotSame(dto1, dto2);
    }

}