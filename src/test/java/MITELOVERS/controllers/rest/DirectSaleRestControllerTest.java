package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.DirectSaleLinkProvider;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleNoPriceResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.mapper.DSFilteredItemsResponseMapper;
import MITELOVERS.mapper.DirectSaleNoPriceResponseDTOMapper;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectSaleRestControllerTest {

    @Mock private DirectSaleService              _service;
    @Mock private DirectSaleResponseDTOMapper    _responseMapper;
    @Mock private DSFilteredItemsResponseMapper  _filteredMapper;
    @Mock private DirectSaleLinkProvider         _linkProvider;
    @Mock private UserService                    _userService;
    @Mock private DirectSaleNoPriceResponseDTOMapper _noPriceMapper;

    @InjectMocks
    private DirectSaleRestController _controller;

    @Test
    void options_shouldReturnHateoasLinks() {
        String userId = "john@example.com";
        User user = mock(User.class);
        when(_userService.getUserByEmail(new UserId(new Email(userId))))
                .thenReturn(user);

        Link link1 = Link.of("/direct-sales").withRel("direct-sales");
        Link link2 = Link.of("/direct-sales/create").withRel("create-direct-sale");
        when(_linkProvider.getLinks(user)).thenReturn(List.of(link1, link2));

        ResponseEntity<RepresentationModel<?>> result = _controller.options(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().getLink("direct-sales").isPresent());
        assertEquals(
                "/direct-sales",
                result.getBody().getRequiredLink("direct-sales").getHref()
        );
        assertTrue(result.getBody().getLink("create-direct-sale").isPresent());
        assertEquals(
                "/direct-sales/create",
                result.getBody().getRequiredLink("create-direct-sale").getHref()
        );
    }

    @Test
    void createDirectSale_shouldReturnCreated() {
        DirectSaleRequestDTO request = mock(DirectSaleRequestDTO.class);
        when(request.getItemsId()).thenReturn(List.of("ABCDEF1234"));
        when(request.getPriceValue()).thenReturn(10.0);
        when(request.getPriceCurrency()).thenReturn("EUR");
        when(request.getTimeLimitSeconds()).thenReturn(3600L);

        DirectSale domain = mock(DirectSale.class);
        String email = "user@aeiou.com";
        User userDouble = mock(User.class);

        DirectSaleResponseDTO response = new DirectSaleResponseDTO(
                "DS-A1B2C3D4",
                List.of("ABCDEF1234"),
                10.0,
                "EUR",
                3600L,
                Instant.now(),
                null,
                DirectSaleStatus.ACTIVE,
                email
        );

        when(_service.createDirectSale(anyList(), any(), any(), any())).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(response);
        when(_userService.getUserByEmail(new UserId(new Email(email)))).thenReturn(userDouble);

        ResponseEntity<DirectSaleResponseDTO> result = _controller.createDirectSale(email, request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(response, result.getBody());
        verify(_linkProvider).addResourceLinks(response, userDouble);
    }

    @Test
    void getAllDirectSales_shouldReturnOk() {
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-A1B2C3D4",
                List.of("ABCDEF1234"),
                10.0,
                "EUR",
                3600L,
                Instant.now(),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        when(_service.getAllDirectSales()).thenReturn(List.of(domain));
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        ResponseEntity<List<DirectSaleResponseDTO>> result = _controller.getAllDirectSales();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(_linkProvider).addResourceLinks(dto);
    }

    @Test
    void getAllDirectSales_shouldReturnNoContentWhenEmpty() {
        when(_service.getAllDirectSales()).thenReturn(List.of());

        ResponseEntity<List<DirectSaleResponseDTO>> result = _controller.getAllDirectSales();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getAllActiveDirectSales_shouldReturnOk() {
        DirectSale domain = mock(DirectSale.class);
        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-A1B2C3D4",
                List.of("ABCDEF1234"),
                10.0,
                "EUR",
                3600L,
                Instant.now(),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        String userId = "user@email.com";
        User userDouble = mock(User.class);

        when(_service.getAllActiveDirectSales()).thenReturn(List.of(domain));
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);
        when(_userService.getUserByEmail(new UserId(new Email(userId))))
                .thenReturn(userDouble);

        ResponseEntity<CollectionModel<DirectSaleResponseDTO>> result =
                _controller.getAllActiveDirectSales(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, new ArrayList<>(result.getBody().getContent()).size());
        assertSame(dto, new ArrayList<>(result.getBody().getContent()).get(0));
        verify(_linkProvider).addResourceLinks(dto, userDouble);
        verify(_linkProvider).addCollectionLinks(result.getBody(), userId);
    }

    @Test
    void getAllActiveDirectSales_shouldReturnNoContentWhenEmpty() {
        String userId = "user@email.com";
        when(_service.getAllActiveDirectSales()).thenReturn(List.of());

        ResponseEntity<CollectionModel<DirectSaleResponseDTO>> result =
                _controller.getAllActiveDirectSales(userId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getDirectSaleById_shouldReturnOk() {
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-A1B2C3D4",
                List.of("ABCDEF1234"),
                10.0,
                "EUR",
                3600L,
                Instant.now(),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("ABCDEF1234");
        when(domain.getItemsId()).thenReturn(List.of(itemIdDouble));

        when(_service.getDirectSaleById(any(DirectSaleId.class))).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("pedro@aeiou.com", "DS-A1B2C3D4");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(dto, result.getBody());
        assertTrue(result.getBody().getLinks().hasLink("item"));
        assertTrue(result.getBody().getRequiredLink("item").getHref()
                .endsWith("/items/ABCDEF1234"));
    }

    @Test
    void getDirectSaleById_shouldAddItemLinkForEachItemId() {
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-A1B2C3D4",
                List.of("ABCDEF1234", "1234ABCDEF"),
                10.0,
                "EUR",
                3600L,
                Instant.now(),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        ItemId itemId1Double = mock(ItemId.class);
        when(itemId1Double.toString()).thenReturn("ABCDEF1234");

        ItemId itemId2Double = mock(ItemId.class);
        when(itemId2Double.toString()).thenReturn("1234ABCDEF");

        when(domain.getItemsId()).thenReturn(List.of(itemId1Double, itemId2Double));

        when(_service.getDirectSaleById(any(DirectSaleId.class))).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("pedro@aeiou.com", "DS-A1B2C3D4");

        List<Link> itemLinks = result.getBody().getLinks().stream()
                .filter(l -> l.getRel().value().equals("item"))
                .toList();

        assertEquals(2, itemLinks.size());
        assertTrue(itemLinks.stream().anyMatch(l -> l.getHref().endsWith("/items/ABCDEF1234")));
        assertTrue(itemLinks.stream().anyMatch(l -> l.getHref().endsWith("/items/1234ABCDEF")));
    }

    @Test
    void getDirectSaleById_shouldAddNoItemLinksWhenNoItems() {
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-A1B2C3D4",
                List.of(),
                10.0,
                "EUR",
                3600L,
                Instant.now(),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        when(domain.getItemsId()).thenReturn(List.of());

        when(_service.getDirectSaleById(any(DirectSaleId.class))).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("pedro@aeiou.com", "DS-A1B2C3D4");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().getLinks().hasLink("item"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldReturnOk() {
        String genreId = "FICTION";
        List<DirectSaleId> domainIds = List.of(
                new DirectSaleId("DS-A1B2C3D4"),
                new DirectSaleId("DS-1234ABCD"));

        DSFilteredItemsResponseDTO dto = new DSFilteredItemsResponseDTO(List.of(
                new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4"),
                new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-1234ABCD")));

        when(_service.getDirectSaleItemsByGenreAsc(any(GenreId.class))).thenReturn(domainIds);
        when(_filteredMapper.toDTO(List.of("DS-A1B2C3D4", "DS-1234ABCD"))).thenReturn(dto);

        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().getDirectSales().size());
        verify(_linkProvider).addCollectionLinks(dto, genreId);
    }

    @Test
    void getDirectSaleItemsByGenre_shouldThrowWhenServiceThrows() {
        when(_service.getDirectSaleItemsByGenreAsc(any(GenreId.class)))
                .thenThrow(new IllegalStateException("No matches"));

        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenre("ROMANCE"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToEachEntry() {
        String genreId = "FICTION";
        List<DirectSaleId> domainIds = List.of(new DirectSaleId("DS-A1B2C3D4"));
        DSFilteredItemsResponseDTO dto = new DSFilteredItemsResponseDTO(
                List.of(new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4")));

        when(_service.getDirectSaleItemsByGenreAsc(any(GenreId.class))).thenReturn(domainIds);
        when(_filteredMapper.toDTO(List.of("DS-A1B2C3D4"))).thenReturn(dto);

        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        assertSame(dto.getDirectSales().get(0), result.getBody().getDirectSales().get(0));
        verify(_linkProvider).addCollectionLinks(dto, genreId);
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToCollection() {
        String genreId = "FICTION";
        List<DirectSaleId> domainIds = List.of(new DirectSaleId("DS-A1B2C3D4"));
        DSFilteredItemsResponseDTO dto = new DSFilteredItemsResponseDTO(
                List.of(new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4")));

        when(_service.getDirectSaleItemsByGenreAsc(any(GenreId.class))).thenReturn(domainIds);
        when(_filteredMapper.toDTO(List.of("DS-A1B2C3D4"))).thenReturn(dto);

        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        assertSame(dto, result.getBody());
        verify(_linkProvider).addCollectionLinks(dto, genreId);
    }

    @Test
    void getDirectSalesWithNoPriceShouldReturnNoPriceDto() {
        DirectSale domain = mock(DirectSale.class);
        DirectSaleNoPriceResponseDTO dto = new DirectSaleNoPriceResponseDTO(
                "DS-A1B2C3D4", List.of("ABCDEF1234"), 3600L, Instant.now());

        when(_service.getAllActiveDirectSales()).thenReturn(List.of(domain));
        when(_noPriceMapper.toModel(domain)).thenReturn(dto);

        ResponseEntity<List<DirectSaleNoPriceResponseDTO>> result =
                _controller.getDirectSalesWithoutPrice();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(_linkProvider).addResourceLinks(dto);
    }

    @Test
    void deleteDirectSale_shouldReturnNoContent() {
        ResponseEntity<Void> response = _controller.deleteDirectSale("DS-A1B2C3D4");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteDirectSale_shouldPropagateExceptionFromService() {
        doThrow(new IllegalStateException("boom"))
                .when(_service)
                .deleteDirectSale(new DirectSaleId("DS-A1B2C3D4"));

        assertThrows(IllegalStateException.class,
                () -> _controller.deleteDirectSale("DS-A1B2C3D4"));
    }
}
