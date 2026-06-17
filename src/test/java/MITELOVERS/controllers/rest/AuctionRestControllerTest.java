package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.AuctionLinkProvider;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.AuctionResponseDTO;
import MITELOVERS.dto.response.BidResponseDTO;
import MITELOVERS.mapper.AuctionResponseDTOMapper;
import MITELOVERS.mapper.BidResponseDTOMapper;
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
import java.util.NoSuchElementException;

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
    private BidResponseDTOMapper _bidResponseDTOMapper;

    @MockitoBean
    private AuctionLinkProvider _auctionLinkProvider;

    @MockitoBean
    private UserService _userService;

    // ------------------------------------------------------------
    // GET /auctions
    // ------------------------------------------------------------

    @Test
    void getAllActiveAuctions_shouldReturnOk() throws Exception {
        // arrange
        Auction auction = mock(Auction.class);
        AuctionResponseDTO dto = new AuctionResponseDTO(
                "AU-12345678",
                List.of("ABCDEF1234"),
                10.0, 25.0, 50.0, "EUR",
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2099-01-01T10:00:00Z"),
                "pedro@aeiou.com"
        );

        when(_auctionService.getAllActiveAuctions()).thenReturn(List.of(auction));
        when(_auctionMapper.toDTO(auction)).thenReturn(dto);

        // act + assert
        mockMvc.perform(get("/auctions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].auctionId").value("AU-12345678"))
                .andExpect(jsonPath("$[0].startingPrice").value(10.0))
                .andExpect(jsonPath("$[0].links[0].rel").value("self"))
                .andExpect(jsonPath("$[0].links[0].href").value("http://localhost/auctions/AU-12345678"));
    }

    @Test
    void getAllActiveAuctions_shouldReturnNoContentWhenEmpty() throws Exception {
        // arrange
        when(_auctionService.getAllActiveAuctions()).thenReturn(List.of());

        // act + assert
        mockMvc.perform(get("/auctions"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createAuctionValidRequestReturns201() throws Exception {
        // arrange
        Auction auction = mock(Auction.class);
        AuctionResponseDTO dto = new AuctionResponseDTO(
                "auction-123",
                List.of("ABCDEF1234"),
                10.0, 25.0, 50.0, "EUR",
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2026-06-20T10:00:00Z"),
                "pedro@aeiou.com"
        );

        when(_auctionService.putItemOnAuction(any(), any(), any(), any(), any(), any(), any()))
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
                    "endDate": "2026-06-20T10:00:00Z",
                    "seller": "pedro@aeiou.com"
                }
                """;

        // act + assert
        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "pedro@aeiou.com")
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
                Instant.parse("2026-06-20T10:00:00Z"),
                "pedro@aeiou.com"
        );

        when(_auctionService.putItemOnAuction(any(), any(), any(), isNull(), any(), any(), any()))
                .thenReturn(auction);
        when(_auctionMapper.toDTO(auction)).thenReturn(dto);

        String requestBody = """
                {
                    "itemIds": ["ABCDEF1234"],
                    "startingPrice": 10.0,
                    "reservePrice": 25.0,
                    "priceCurrency": "EUR",
                    "startDate": "2026-06-10T10:00:00Z",
                    "endDate": "2026-06-20T10:00:00Z",
                    "seller": "pedro@aeiou.com"
                }
                """;

        // act + assert
        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "pedro@aeiou.com")
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
                        .header("X-User-Id", "pedro@aeiou.com")
                        .content(requestBody))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void optionsUserCanSellReturnsCreateAuctionLink() throws Exception {
        // arrange
        User user = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(user);

        when(_auctionLinkProvider.getLinks(user))
                .thenReturn(List.of(Link.of("/auctions", "create-auction")));

        // act + assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links['create-auction']").exists());
    }

    @Test
    void optionsUserCannotSellReturnsNoLinks() throws Exception {
        // arrange
        User user = mock(User.class);
        when(_userService.getUserByEmail("user@example.com")).thenReturn(user);
        when(_auctionLinkProvider.getLinks(user)).thenReturn(List.of());

        // act + assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void optionsForSpecificAuctionReturnsSelfAndPlaceBidLinks() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(userDouble);

        when(_auctionLinkProvider.getLinks(userDouble, auctionId))
                .thenReturn(List.of(
                        Link.of("/auctions/" + auctionId, "self"),
                        Link.of("/auctions/" + auctionId + "/bids", "place-bid")
                ));

        // Act + Assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions/{auctionId}", auctionId)
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links['place-bid']").exists());
    }

    @Test
    void optionsForSpecificAuctionNoActionsReturnsNoLinks() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(userDouble);

        when(_auctionLinkProvider.getLinks(userDouble, auctionId))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions/{auctionId}", auctionId)
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void getAuctionByIdReturns200AndBody() throws Exception {

        // Arrange
        String auctionId = "AU-12345678";

        Auction auctionDouble = mock(Auction.class);

        AuctionResponseDTO dto = new AuctionResponseDTO(
                auctionId,
                List.of("ABCDEF1234"),
                10.0, 25.0, 50.0, "EUR",
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2026-06-20T10:00:00Z"),
                "pedro@aeiou.com"
        );

        when(_auctionService.getAuctionById(auctionId)).thenReturn(auctionDouble);
        when(_auctionMapper.toDTO(auctionDouble)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/auctions/{auctionId}", auctionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auctionId").value(auctionId))
                .andExpect(jsonPath("$.startingPrice").value(10.0))
                .andExpect(jsonPath("$.reservePrice").value(25.0))
                .andExpect(jsonPath("$.priceCurrency").value("EUR"));
    }

    @Test
    void getAuctionByIdReturns404WhenNotFound() throws Exception {
        // Arrange
        String auctionIdString = "AU-11111111";

        when(_auctionService.getAuctionById(auctionIdString))
                .thenThrow(new NoSuchElementException("Auction not found: " + auctionIdString));

        // Act + Assert
        mockMvc.perform(get("/auctions/{auctionId}", auctionIdString))
                .andExpect(status().isNotFound());
    }

    @Test
    void placeBidValidRequestReturns201() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";

        Auction auctionDouble = mock(Auction.class);
        Bid bidDouble = mock(Bid.class);

        AuctionService.BidPlacementResult result = mock(AuctionService.BidPlacementResult.class);
        when(result.auction()).thenReturn(auctionDouble);
        when(result.bid()).thenReturn(bidDouble);

        when(_auctionService.placeBid(any(), any(), any()))
                .thenReturn(result);

        BidResponseDTO dto = new BidResponseDTO(
                "0bc6c8bf-6f51-4f1a-b6af-cde1dbfbb1ad",
                auctionId,
                "buyer@aeiou.com",
                20.0,
                "EUR",
                Instant.parse("2026-06-10T10:00:00Z")
        );

        when(_bidResponseDTOMapper.toDTO(auctionDouble, bidDouble)).thenReturn(dto);

        String requestBody = """
            {
              "offerPrice": 20.0,
              "currency": "EUR"
            }
            """;

        // Act + Assert
        mockMvc.perform(post("/auctions/{auctionId}/bids", auctionId)
                        .header("X-User-Id", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.auctionId").value(auctionId))
                .andExpect(jsonPath("$.offerPrice").value(20.0))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void placeBidServiceThrowsExceptionReturns500() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";

        when(_auctionService.placeBid(any(), any(), any()))
                .thenThrow(new IllegalStateException("Auction not active"));

        String requestBody = """
        {
          "offerPrice": 5.0,
          "currency": "EUR"
        }
        """;

        // Act + Assert
        mockMvc.perform(post("/auctions/{auctionId}/bids", auctionId)
                        .header("X-User-Id", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError());
    }

}