package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.AuctionLinkProvider;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
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

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.not;
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

    @Test
    void getAllActiveAuctionsShouldReturnOk() throws Exception {
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
    void getAllActiveAuctionsShouldReturnNoContentWhenEmpty() throws Exception {
        // arrange
        when(_auctionService.getAllActiveAuctions()).thenReturn(List.of());

        // act + assert
        mockMvc.perform(get("/auctions"))
                .andExpect(status().isNoContent());
    }

    // ------------------------------------------------------------
    // POST /auctions
    // ------------------------------------------------------------

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
    void createAuctionReturns404WhenItemNotFound() throws Exception {

        when(_auctionService.putItemOnAuction(
                any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new NoSuchElementException("Item not found"));

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
                        .header("X-User-Id", "pedro@aeiou.com")
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------
    // OPTIONS /auctions
    // ------------------------------------------------------------

    @Test
    void shouldReturnAllowHeaderWithOptionsAndPost() throws Exception {
        // arrange
        User user = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("user@test.com"))))
                .thenReturn(user);

        when(_auctionLinkProvider.getAllowedMethods(user))
                .thenReturn(List.of(
                        HttpMethod.OPTIONS,
                        HttpMethod.POST));

        // act + assert
        mockMvc.perform(
                        options("/auctions")
                                .header("X-User-Id", "user@test.com"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Allow",
                        containsString("OPTIONS")))
                .andExpect(header().string(
                        "Allow",
                        containsString("POST")))
                .andExpect(content().string(""));
    }

    @Test
    void shouldReturnAllowHeaderWithOnlyOptions() throws Exception {
        // arrange
        User user = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("user@test.com"))))
                .thenReturn(user);

        when(_auctionLinkProvider.getAllowedMethods(user))
        .thenReturn(List.of(HttpMethod.OPTIONS));

        // act + assert
        mockMvc.perform(
                options("/auctions")
                        .header("X-User-Id", "user@test.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow",
                        containsString("OPTIONS")))
                .andExpect(header().string(
                        "Allow",
                        not(containsString("POST"))))
                .andExpect(content().string(""));
    }

    // ------------------------------------------------------------
    // OPTIONS /auctions/{auctionId}
    // ------------------------------------------------------------

    @Test
    void optionsForSpecificAuctionReturnsSelfAndPlaceBidLinks() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("user@example.com"))))
                .thenReturn(userDouble);

        when(_auctionLinkProvider.getLinks(userDouble, auctionId))
                .thenReturn(List.of(
                        Link.of("/auctions/" + auctionId, "self"),
                        Link.of("/auctions/" + auctionId + "/bids", "place-bid")
                ));

        // Act + Assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions/{auctionId}", auctionId)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links['place-bid']").exists());
    }

    @Test
    void optionsForSpecificAuctionNoActionsReturnsNoLinks() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("user@example.com"))))
                .thenReturn(userDouble);

        when(_auctionLinkProvider.getLinks(userDouble, auctionId))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions/{auctionId}", auctionId)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    // ------------------------------------------------------------
    // GET /auctions/{auctionId}
    // ------------------------------------------------------------

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
                .andExpect(jsonPath("$.priceCurrency").value("EUR"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.self.href")
                        .value("http://localhost/auctions/" + auctionId))
                .andExpect(jsonPath("$._links['bid-options']").exists())
                .andExpect(jsonPath("$._links['bid-options'].href")
                        .value("http://localhost/auctions/" + auctionId + "/bids"));
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

    // ------------------------------------------------------------
    // OPTIONS /auctions/{auctionId}/bids
    // ------------------------------------------------------------

    @Test
    void optionsForBidsUserCanViewAuctionReturnsSelfAndViewBidsLinks() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("user@example.com"))))
                .thenReturn(userDouble);

        when(_auctionLinkProvider.getBidLinks(userDouble, auctionId))
                .thenReturn(List.of(
                        Link.of("/auctions/" + auctionId + "/bids", "self"),
                        Link.of("/auctions/" + auctionId + "/bids", "view-bids")
                ));

        // Act + Assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions/{auctionId}/bids", auctionId)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links['view-bids']").exists());
    }

    @Test
    void optionsForBidsNoActionsReturnsNoLinks() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("user@example.com"))))
                .thenReturn(userDouble);

        when(_auctionLinkProvider.getBidLinks(userDouble, auctionId))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(request(HttpMethod.OPTIONS, "/auctions/{auctionId}/bids", auctionId)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    // ------------------------------------------------------------
    // GET /auctions/{auctionId}/bids
    // ------------------------------------------------------------

    @Test
    void getBidsForAuctionReturns200AndListOfBids() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";

        Auction auctionDouble = mock(Auction.class);
        Bid bid1Double = mock(Bid.class);
        Bid bid2Double = mock(Bid.class);

        when(_auctionService.getAuctionById(auctionId)).thenReturn(auctionDouble);
        when(auctionDouble.getBids()).thenReturn(List.of(bid1Double, bid2Double));

        BidResponseDTO dto1 = new BidResponseDTO(
                "bid-1",
                auctionId,
                "buyer1@aeiou.com",
                20.0,
                "EUR",
                Instant.parse("2026-06-10T10:00:00Z")
        );
        BidResponseDTO dto2 = new BidResponseDTO(
                "bid-2",
                auctionId,
                "buyer2@aeiou.com",
                30.0,
                "EUR",
                Instant.parse("2026-06-11T10:00:00Z")
        );

        when(_bidResponseDTOMapper.toDTO(auctionDouble, bid1Double)).thenReturn(dto1);
        when(_bidResponseDTOMapper.toDTO(auctionDouble, bid2Double)).thenReturn(dto2);

        // Act + Assert
        mockMvc.perform(get("/auctions/{auctionId}/bids", auctionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // list size 2
                .andExpect(jsonPath("$.length()").value(2))
                // first bid basic fields
                .andExpect(jsonPath("$[0].auctionId").value(auctionId))
                .andExpect(jsonPath("$[0].offerPrice").value(20.0))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                // second bid basic fields
                .andExpect(jsonPath("$[1].auctionId").value(auctionId))
                .andExpect(jsonPath("$[1].offerPrice").value(30.0))
                .andExpect(jsonPath("$[1].currency").value("EUR"));
    }

    @Test
    void getBidsForAuctionReturns404WhenAuctionNotFound() throws Exception {
        // Arrange
        String auctionId = "AU-99999999";

        when(_auctionService.getAuctionById(auctionId))
                .thenThrow(new NoSuchElementException("Auction not found: " + auctionId));

        // Act + Assert
        mockMvc.perform(get("/auctions/{auctionId}/bids", auctionId))
                .andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------
    // POST /auctions/{auctionId}/bids
    // ------------------------------------------------------------

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
              "bidValue": 20.0,
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
    void placeBidServiceThrowsExceptionReturns404() throws Exception {
        // Arrange
        String auctionId = "AU-12345678";

        when(_auctionService.placeBid(any(), any(), any()))
                .thenThrow(new IllegalStateException("Auction not active"));

        String requestBody = """
        {
          "bidValue": 5.0,
          "currency": "EUR"
        }
        """;

        // Act + Assert
        mockMvc.perform(post("/auctions/{auctionId}/bids", auctionId)
                        .header("X-User-Id", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

}