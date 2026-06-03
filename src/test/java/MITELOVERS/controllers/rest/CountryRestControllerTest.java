package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.CountryService;
import MITELOVERS.dto.CountryCollectionDTO;
import MITELOVERS.dto.CountryDTO;
import MITELOVERS.mapper.CountryCollectionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryRestControllerTest {

    @Test
    void create_returnsDTO_whenServiceSucceeds() {
        // arrange
        CountryService service = mock(CountryService.class);
        CountryCollectionMapper mapper = mock(CountryCollectionMapper.class);
        CountryRestController controller = new CountryRestController(service, mapper);

        CountryDTO dto = mock(CountryDTO.class);
        when(service.createCountry("Portugal")).thenReturn(dto);

        // act (SUT)
        CountryDTO result = controller.create("Portugal");

        // assert
        assertSame(dto, result);
    }

    @Test
    void create_throwsConflict_whenServiceThrowsIllegalArgument() {
        // arrange
        CountryService service = mock(CountryService.class);
        CountryCollectionMapper mapper = mock(CountryCollectionMapper.class);
        CountryRestController controller = new CountryRestController(service, mapper);

        when(service.createCountry("Portugal"))
                .thenThrow(new IllegalArgumentException("duplicate"));

        // act + assert
        assertThrows(ResponseStatusException.class,
                () -> controller.create("Portugal"));
    }

    @Test
    void listAll_returnsCollectionDTO() {
        // arrange
        CountryService service = mock(CountryService.class);
        CountryCollectionMapper mapper = mock(CountryCollectionMapper.class);
        CountryRestController controller = new CountryRestController(service, mapper);

        CountryDTO dto1 = mock(CountryDTO.class);
        CountryDTO dto2 = mock(CountryDTO.class);

        List<CountryDTO> list = List.of(dto1, dto2);

        CountryCollectionDTO collection = mock(CountryCollectionDTO.class);

        when(service.listAllCountries()).thenReturn(list);
        when(mapper.toDTO(list)).thenReturn(collection);

        // act (SUT)
        var response = controller.listAll();

        // assert
        assertSame(collection, response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void findById_returnsDTO_whenServiceSucceeds() {
        // arrange
        CountryService service = mock(CountryService.class);
        CountryCollectionMapper mapper = mock(CountryCollectionMapper.class);
        CountryRestController controller = new CountryRestController(service, mapper);

        CountryDTO dto = mock(CountryDTO.class);
        when(service.findById("123")).thenReturn(dto);

        // act (SUT)
        var response = controller.findById("123");

        // assert
        assertSame(dto, response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void findById_throwsNotFound_whenServiceThrowsNoSuchElement() {
        // arrange
        CountryService service = mock(CountryService.class);
        CountryCollectionMapper mapper = mock(CountryCollectionMapper.class);
        CountryRestController controller = new CountryRestController(service, mapper);

        when(service.findById("123"))
                .thenThrow(new NoSuchElementException("not found"));

        // act + assert
        assertThrows(ResponseStatusException.class,
                () -> controller.findById("123"));
    }

}