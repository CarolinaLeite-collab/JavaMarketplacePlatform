package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.AuctionLinkProvider;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.AuctionResponseDTO;
import MITELOVERS.mapper.AuctionResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionRestController.class)
class AuctionRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuctionService _auctionService;

    @MockitoBean
    private AuctionResponseDTOMapper _auctionMapper;

    @MockitoBean
    private AuctionLinkProvider _auctionLinkProvider;

    @MockitoBean
    private UserService _userService;

    @Test
    void createAuctionValidRequestReturns201() throws Exception {
        // arrange
        Auction auction = mock(Auction.class);
        AuctionResponseDTO dto = new AuctionResponseDTO(
                "auction-123",
                List.of("ABCDEF1234"),
                10.0, 25.0, 50.0, "EUR",
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2026-06-20T10:00:00Z")
        );

        when(_auctionService.putItemOnAuction(any(), any(), any(), any(), any(), any()))
                .thenReturn(auction);
        when(_auctionMapper.toDTO(auction)).thenReturn(dto);

        String requestBody = """
                {
                    "itemIds": ["ABCDEF1234"],
                    "startingPrice": 10.0,
                    "reservePrice": 25.0,
                    "outrightPrice": 50.0,
                    "priceCurrency": "EUR",
                    "startDate": "2026-06-10T10:00:00Z",
                    "endDate": "2026-06-20T10:00:00Z"
                }
                """;

        // act + assert
        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.auctionId").value("auction-123"))
                .andExpect(jsonPath("$.startingPrice").value(10.0))
                .andExpect(jsonPath("$.reservePrice").value(25.0))
                .andExpect(jsonPath("$.outrightPrice").value(50.0))
                .andExpect(jsonPath("$.priceCurrency").value("EUR"));
    }

    @Test
    void createAuctionWithoutOutrightPriceReturns201() throws Exception {
        // arrange
        Auction auction = mock(Auction.class);
        AuctionResponseDTO dto = new AuctionResponseDTO(
                "auction-123",
                List.of("ABCDEF1234"),
                10.0, 25.0, null, "EUR",
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2026-06-20T10:00:00Z")
        );

        when(_auctionService.putItemOnAuction(any(), any(), any(), isNull(), any(), any()))
                .thenReturn(auction);
        when(_auctionMapper.toDTO(auction)).thenReturn(dto);

        String requestBody = """
                {
                    "itemIds": ["ABCDEF1234"],
                    "startingPrice": 10.0,
                    "reservePrice": 25.0,
                    "priceCurrency": "EUR",
                    "startDate": "2026-06-10T10:00:00Z",
                    "endDate": "2026-06-20T10:00:00Z"
                }
                """;

        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$.outrightPrice").isEmpty())
                .andExpect(jsonPath("$.priceCurrency").value("EUR"));
    }

    @Test
    void createAuctionServiceThrowsExceptionReturns500() throws Exception {
        // arrange
        when(_auctionService.putItemOnAuction(any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalStateException("Item not found"));

        String requestBody = """
                {
                    "itemIds": ["item-nao-existe"],
                    "startingPrice": 10.0,
                    "reservePrice": 25.0,
                    "priceCurrency": "EUR",
                    "startDate": "2026-06-10T10:00:00Z",
                    "endDate": "2026-06-20T10:00:00Z"
                }
                """;

        // act + assert
        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void optionsUserCanSellReturnsCreateAuctionLink() throws Exception {
        User user = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(user);

        when(_auctionLinkProvider.getLinks(user))
                .thenReturn(List.of(Link.of("/auctions", "create-auction")));

        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links['create-auction']").exists());
    }

    @Test
    void optionsUserCannotSellReturnsNoLinks() throws Exception {
        User user = mock(User.class);
        when(_userService.getUserByEmail("user@example.com")).thenReturn(user);
        when(_auctionLinkProvider.getLinks(user)).thenReturn(List.of());

        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").doesNotExist());
    }
}