package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.DirectSaleLinkProvider;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.DirectSaleStatus;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.UserId;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectSaleRestControllerTest {

    @Mock
    private DirectSaleService _service;

    @Mock
    private DirectSaleResponseDTOMapper _responseMapper;

    @Mock
    private DSFilteredItemsResponseMapper _filteredMapper;

    @Mock
    private DirectSaleLinkProvider _linkProvider;

    @Mock
    private UserService _userService;

    @Mock
    private DirectSaleNoPriceResponseDTOMapper _noPriceMapper;

    @InjectMocks
    private DirectSaleRestController _controller;

    //-----------------
    // Options test
    //-----------------

    @Test
    void options_shouldReturnSelfLinkAndProviderLinks() {

        // Arrange
        String email = "john@example.com";

        User user = mock(User.class);
        when(_userService.getUserByEmail(email)).thenReturn(user);

        Link link1 = Link.of("/direct-sales").withRel("self");
        Link link2 = Link.of("/direct-sales/create").withRel("create");

        when(_linkProvider.getLinks(user)).thenReturn(List.of(link1, link2));

        // Act
        ResponseEntity<RepresentationModel<?>> result =
                _controller.options(email);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        RepresentationModel<?> body = result.getBody();
        assertNotNull(body);

        // 1) Self link added by controller
        assertTrue(body.getLinks().hasLink("self"));

        // 2) Provider links
        assertTrue(body.getLinks().hasLink("create"));

        // 3) Optional: assert order (strong mutation killer)
        List<Link> links = body.getLinks().toList();
        assertEquals("self", links.get(0).getRel().value());   // controller self link
        assertEquals("self", links.get(1).getRel().value());   // provider link1
        assertEquals("create", links.get(2).getRel().value()); // provider link2
    }

    // ------------------------------------------------------------
    // POST /direct-sales
    // ------------------------------------------------------------

    @Test
    void createDirectSale_shouldReturnCreated() {

        // Arrange
        DirectSaleRequestDTO request = mock(DirectSaleRequestDTO.class);

        DirectSale domain = mock(DirectSale.class);

        String email = "email@email.com";
        UserId userIdDouble = mock(UserId.class);

        DirectSaleResponseDTO response =
                new DirectSaleResponseDTO(
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

        when(_service.createDirectSale(request, email)).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(response);

        // Act (SUT)
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.createDirectSale(email, request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(response, result.getBody());
        assertTrue(result.getBody().getLinks().hasLink("self"));
    }

    // ------------------------------------------------------------
    // GET /direct-sales
    // ------------------------------------------------------------

    @Test
    void getAllDirectSales_shouldReturnOk() {

        // Arrange
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
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

        // Act
        ResponseEntity<List<DirectSaleResponseDTO>> result =
                _controller.getAllDirectSales();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertTrue(result.getBody().get(0).getLinks().hasLink("self"));
    }

    @Test
    void getAllDirectSales_shouldReturnNoContentWhenEmpty() {

        // Arrange
        when(_service.getAllDirectSales()).thenReturn(List.of());

        // Act
        ResponseEntity<List<DirectSaleResponseDTO>> result =
                _controller.getAllDirectSales();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    // ------------------------------------------------------------
    // GET /direct-sales/active
    // ------------------------------------------------------------

    @Test
    void getAllActiveDirectSales_shouldReturnOk() {

        // Arrange
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

        when(_service.getAllActiveDirectSales()).thenReturn(List.of(domain));
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        // Act
        ResponseEntity<CollectionModel<DirectSaleResponseDTO>> result =
                _controller.getAllActiveDirectSales(userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, new ArrayList<>(result.getBody().getContent()).size());
        assertSame(dto, new ArrayList<>(result.getBody().getContent()).get(0));
        verify(_linkProvider).addResourceLinks(dto, userId);
        verify(_linkProvider).addCollectionLinks(result.getBody(), userId);
    }

    @Test
    void getAllActiveDirectSales_shouldReturnNoContentWhenEmpty() {

        // Arrange
        String userId = "user@email.com";

        when(_service.getAllActiveDirectSales()).thenReturn(List.of());

        // Act
        ResponseEntity<CollectionModel<DirectSaleResponseDTO>> result =
                _controller.getAllActiveDirectSales(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    // ------------------------------------------------------------
    // GET /direct-sales/{id}
    // ------------------------------------------------------------

    @Test
    void getDirectSaleById_shouldReturnOk() {

        // Arrange
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
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

        when(_service.getDirectSaleById("DS-A1B2C3D4")).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        // Act
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("DS-A1B2C3D4");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(dto, result.getBody());
        assertTrue(result.getBody().getLinks().hasLink("self"));
        assertTrue(result.getBody().getLinks().hasLink("item"));
        assertTrue(result.getBody().getRequiredLink("item").getHref()
                .endsWith("/items/ABCDEF1234"));
    }

    @Test
    void getDirectSaleById_shouldAddItemLinkForEachItemId() {

        // Arrange
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
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

        when(_service.getDirectSaleById("DS-A1B2C3D4")).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        // Act
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("DS-A1B2C3D4");

        // Assert
        List<Link> itemLinks = result.getBody().getLinks().stream()
                .filter(l -> l.getRel().value().equals("item"))
                .toList();

        assertEquals(2, itemLinks.size());
        assertTrue(itemLinks.stream().anyMatch(l -> l.getHref().endsWith("/items/ABCDEF1234")));
        assertTrue(itemLinks.stream().anyMatch(l -> l.getHref().endsWith("/items/1234ABCDEF")));
    }

    @Test
    void getDirectSaleById_shouldAddNoItemLinksWhenNoItems() {

        // Arrange
        DirectSale domain = mock(DirectSale.class);

        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
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

        when(_service.getDirectSaleById("DS-A1B2C3D4")).thenReturn(domain);
        when(_responseMapper.toResponseDTO(domain)).thenReturn(dto);

        // Act
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("DS-A1B2C3D4");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().getLinks().hasLink("self"));
        assertFalse(result.getBody().getLinks().hasLink("item"));
    }

    // ------------------------------------------------------------
    // GET /direct-sales/genre/{genreId}
    // ------------------------------------------------------------

    @Test
    void getDirectSaleItemsByGenre_shouldReturnOk() {

        // Arrange
        String genreId = "FICTION";

        List<DirectSaleId> domainIds = List.of(
                new DirectSaleId("DS-A1B2C3D4"),
                new DirectSaleId("DS-1234ABCD")
        );

        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4"),
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-1234ABCD")
                ));

        when(_service.getDirectSaleItemsByGenreAsc(genreId)).thenReturn(domainIds);
        when(_filteredMapper.toDTO(List.of("DS-A1B2C3D4", "DS-1234ABCD")))
                .thenReturn(dto);

        // Act
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        DSFilteredItemsResponseDTO body = result.getBody();
        assertNotNull(body);

        assertEquals(2, body.getDirectSales().size());

        // Each entry has self link
        assertTrue(body.getDirectSales().get(0).getLinks().hasLink("self"));
        assertTrue(body.getDirectSales().get(1).getLinks().hasLink("self"));

        // Collection has self link
        assertTrue(body.getLinks().hasLink("self"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldThrowWhenServiceThrows() {

        // Arrange
        String genreId = "ROMANCE";
        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenThrow(new IllegalStateException("No matches"));

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenre(genreId)
        );
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToEachEntry() {

        // Arrange
        String genreId = "FICTION";

        List<DirectSaleId> domainIds = List.of(new DirectSaleId("DS-A1B2C3D4"));

        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4")
                ));

        when(_service.getDirectSaleItemsByGenreAsc(genreId)).thenReturn(domainIds);
        when(_filteredMapper.toDTO(List.of("DS-A1B2C3D4"))).thenReturn(dto);

        // Act
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        var entry = result.getBody().getDirectSales().get(0);
        assertTrue(entry.getLinks().hasLink("self"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToCollection() {

        // Arrange
        String genreId = "FICTION";

        List<DirectSaleId> domainIds = List.of(new DirectSaleId("DS-A1B2C3D4"));

        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(
                        List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4")
                ));

        when(_service.getDirectSaleItemsByGenreAsc(genreId)).thenReturn(domainIds);
        when(_filteredMapper.toDTO(List.of("DS-A1B2C3D4"))).thenReturn(dto);

        // Act
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        assertTrue(result.getBody().getLinks().hasLink("self"));
    }

    @Test
    void getDirectSalesWithNoPriceShouldReturnNoPriceDto() {

        // Arrange
        DirectSale domain = mock(DirectSale.class);

        DirectSaleNoPriceResponseDTO dto =
                new DirectSaleNoPriceResponseDTO(
                        "DS-A1B2C3D4",
                        List.of("ABCDEF1234"),
                        3600L,
                        Instant.now()
                );

        when(_service.getAllDirectSales()).thenReturn(List.of(domain));
        when(_noPriceMapper.toModel(domain)).thenReturn(dto);

        // Act
        ResponseEntity<List<DirectSaleNoPriceResponseDTO>> result =
                _controller.getDirectSalesWithoutPrice();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertTrue(result.getBody().get(0).getLinks().hasLink("self"));
    }

    @Test
    void deleteDirectSale_shouldReturnNoContent() {

        // Arrange
        String id = "DS-A1B2C3D4";
        // no stubbing needed — service returns void

        // Act
        ResponseEntity<Void> response = _controller.deleteDirectSale(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteDirectSale_shouldPropagateExceptionFromService() {

        // Arrange
        String id = "DS-A1B2C3D4";

        doThrow(new IllegalStateException("boom"))
                .when(_service)
                .deleteDirectSale(new DirectSaleId(id));

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.deleteDirectSale(id));
    }

}