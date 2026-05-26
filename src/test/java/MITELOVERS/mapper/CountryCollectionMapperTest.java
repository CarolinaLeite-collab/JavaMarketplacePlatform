package MITELOVERS.mapper;

import MITELOVERS.Link;
import MITELOVERS.dto.CountryCollectionDTO;
import MITELOVERS.dto.CountryDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CountryCollectionMapperTest {

    @Test
    void toDTO_createsCollectionWithCountriesAndSelfLink() {
        // arrange
        CountryDTO dto1 = mock(CountryDTO.class);
        CountryDTO dto2 = mock(CountryDTO.class);
        List<CountryDTO> input = List.of(dto1, dto2);

        CountryCollectionMapper mapper = new CountryCollectionMapper();

        // act (SUT)
        CountryCollectionDTO result = mapper.toDTO(input);

        // assert
        // countries copied
        assertEquals(2, result.countries().size());
        assertEquals(dto1, result.countries().get(0));
        assertEquals(dto2, result.countries().get(1));

        // self link added
        assertEquals(1, result.links().size());
        Link self = result.links().get(0);
        assertEquals("self", self.rel());
        assertEquals("/countries", self.href());

        // no actions added (commented out)
        assertTrue(result.actions().isEmpty());
    }

    @Test
    void toDTO_returnsIndependentCopyOfCountries() {
        // arrange
        CountryDTO dto = mock(CountryDTO.class);
        List<CountryDTO> input = List.of(dto);

        CountryCollectionMapper mapper = new CountryCollectionMapper();

        // act (SUT)
        CountryCollectionDTO result = mapper.toDTO(input);

        // assert
        assertNotSame(input, result.countries());
        assertEquals(1, result.countries().size());
    }

}