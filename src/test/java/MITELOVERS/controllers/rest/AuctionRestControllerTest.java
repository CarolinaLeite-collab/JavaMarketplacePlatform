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
                "pedro@aeiou.com",
                10.0
        );

        when(_auctionService.getAllActiveAuctions()).thenReturn(List.of(auction));
        when(_auctionMapper.toDTO(auction)).thenReturn(dto);

        // act + assert
        mockMvc.perform(get("/auctions"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("AU-12345678")));
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
                "pedro@aeiou.com",
                30.0
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
                "pedro@aeiou.com",
                15.0
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
                .andExpect(jsonPath("$.auctionId").value("auction-123"))
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

        when(_userService.getUserByEmail("user@test.com"))
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

        when(_userService.getUserByEmail("user@test.com"))
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
    void optionsForSpecificAuctionReturnsAllowHeaderWithOptionsAndGet() throws Exception {
        // Arrange
        String auctionIdString = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(userDouble);

        when(_auctionService.getAuctionById(auctionIdString))
                .thenReturn(mock(Auction.class));

        when(_auctionLinkProvider.getAllowedMethodsForSpecificAuction(userDouble))
                .thenReturn(List.of(HttpMethod.OPTIONS, HttpMethod.GET));

        // Act + Assert
        mockMvc.perform(options("/auctions/{auctionId}", auctionIdString)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("OPTIONS")))
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(content().string(""));
    }

    @Test
    void optionsForSpecificAuctionReturnsOnlyOptionsWhenNoOtherActions() throws Exception {
        // Arrange
        String auctionIdString = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(userDouble);

        when(_auctionService.getAuctionById(auctionIdString))
                .thenReturn(mock(Auction.class));

        when(_auctionLinkProvider.getAllowedMethodsForSpecificAuction(userDouble))
                .thenReturn(List.of(HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/auctions/{auctionId}", auctionIdString)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("OPTIONS")))
                .andExpect(header().string("Allow", not(containsString("GET"))))
                .andExpect(header().string("Allow", not(containsString("POST"))))
                .andExpect(content().string(""));
    }

    // ------------------------------------------------------------
    // GET /auctions/{auctionId}
    // ------------------------------------------------------------

    @Test
    void getAuctionByIdReturns200AndBody() throws Exception {

        String auctionIdString = "AU-12345678";

        Auction auctionDouble = mock(Auction.class);

        AuctionResponseDTO dto = new AuctionResponseDTO(
                auctionIdString,
                List.of("ABCDEF1234"),
                10.0, 25.0, 50.0, "EUR",
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2026-06-20T10:00:00Z"),
                "pedro@aeiou.com",
                10.0
        );

        dto.add(org.springframework.hateoas.Link.of("http://localhost/auctions/" + auctionIdString, "self"));
        dto.add(org.springframework.hateoas.Link.of("http://localhost/auctions/" + auctionIdString + "/bids", "bids"));

        when(_auctionService.getAuctionById(auctionIdString)).thenReturn(auctionDouble);
        when(_auctionMapper.toDTO(auctionDouble)).thenReturn(dto);

        mockMvc.perform(get("/auctions/{auctionId}", auctionIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auctionId").value(auctionIdString))
                .andExpect(jsonPath("$.startingPrice").value(10.0))
                .andExpect(jsonPath("$.reservePrice").value(25.0))
                .andExpect(jsonPath("$.priceCurrency").value("EUR"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.self.href")
                        .value("http://localhost/auctions/" + auctionIdString))
                .andExpect(jsonPath("$._links.bids").exists())
                .andExpect(jsonPath("$._links.bids.href")
                        .value("http://localhost/auctions/" + auctionIdString + "/bids"));
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
    void optionsForBidsReturnsAllowHeaderWithOptionsGetAndPost() throws Exception {
        // Arrange
        String auctionIdString = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(userDouble);

        when(_auctionService.getAuctionById(auctionIdString))
                .thenReturn(mock(Auction.class));

        when(_auctionLinkProvider.getAllowedMethodsForBids(userDouble))
                .thenReturn(List.of(HttpMethod.OPTIONS, HttpMethod.GET, HttpMethod.POST));

        // Act + Assert
        mockMvc.perform(options("/auctions/{auctionId}/bids", auctionIdString)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("OPTIONS")))
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("POST")))
                .andExpect(content().string(""));
    }

    @Test
    void optionsForBidsReturnsOnlyOptionsWhenNoOtherActions() throws Exception {
        // Arrange
        String auctionIdString = "AU-12345678";
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail("user@example.com"))
                .thenReturn(userDouble);

        when(_auctionService.getAuctionById(auctionIdString))
                .thenReturn(mock(Auction.class));

        when(_auctionLinkProvider.getAllowedMethodsForBids(userDouble))
                .thenReturn(List.of(HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/auctions/{auctionId}/bids", auctionIdString)
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("OPTIONS")))
                .andExpect(header().string("Allow", not(containsString("GET"))))
                .andExpect(header().string("Allow", not(containsString("POST"))))
                .andExpect(content().string(""));
    }

    // ------------------------------------------------------------
    // GET /auctions/{auctionId}/bids
    // ------------------------------------------------------------

    @Test
    void getBidsForAuctionReturns200AndCollectionRepresentation() throws Exception {
        String auctionIdString = "AU-12345678";

        Auction auctionDouble = mock(Auction.class);
        Bid bid1Double = mock(Bid.class);
        Bid bid2Double = mock(Bid.class);

        when(_auctionService.getAuctionById(auctionIdString)).thenReturn(auctionDouble);
        when(auctionDouble.getBids()).thenReturn(List.of(bid1Double, bid2Double));

        BidResponseDTO dto1 = new BidResponseDTO(
                "bid-1",
                auctionIdString,
                "buyer1@aeiou.com",
                20.0,
                "EUR",
                Instant.parse("2026-06-10T10:00:00Z")
        );
        BidResponseDTO dto2 = new BidResponseDTO(
                "bid-2",
                auctionIdString,
                "buyer2@aeiou.com",
                30.0,
                "EUR",
                Instant.parse("2026-06-11T10:00:00Z")
        );

        when(_bidResponseDTOMapper.toDTO(auctionDouble, bid1Double)).thenReturn(dto1);
        when(_bidResponseDTOMapper.toDTO(auctionDouble, bid2Double)).thenReturn(dto2);

        org.springframework.hateoas.CollectionModel<BidResponseDTO> collectionModel =
                org.springframework.hateoas.CollectionModel.of(
                        List.of(dto1, dto2),
                        org.springframework.hateoas.Link.of("http://localhost/auctions/" + auctionIdString + "/bids", "self"),
                        org.springframework.hateoas.Link.of("http://localhost/auctions/" + auctionIdString, "auction")
                );

        when(_auctionLinkProvider.addLinksForBidCollection(any(), any()))
                .thenReturn(collectionModel);

        mockMvc.perform(get("/auctions/{auctionId}/bids", auctionIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href")
                        .value("http://localhost/auctions/" + auctionIdString + "/bids"))
                .andExpect(jsonPath("$._links.auction.href")
                        .value("http://localhost/auctions/" + auctionIdString))
                .andExpect(jsonPath("$._embedded.*[0].auctionId").value(auctionIdString))
                .andExpect(jsonPath("$._embedded.*[0].offerPrice").value(20.0))
                .andExpect(jsonPath("$._embedded.*[0].currency").value("EUR"))
                .andExpect(jsonPath("$._embedded.*[1].auctionId").value(auctionIdString))
                .andExpect(jsonPath("$._embedded.*[1].offerPrice").value(30.0))
                .andExpect(jsonPath("$._embedded.*[1].currency").value("EUR"));
    }

    @Test
    void getBidsForAuctionReturns404WhenAuctionNotFound() throws Exception {
        // Arrange
        String auctionIdString = "AU-99999999";

        when(_auctionService.getAuctionById(auctionIdString))
                .thenThrow(new NoSuchElementException("Auction not found: " + auctionIdString));

        // Act + Assert
        mockMvc.perform(get("/auctions/{auctionId}/bids", auctionIdString))
                .andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------
    // POST /auctions/{auctionId}/bids
    // ------------------------------------------------------------

    @Test
    void placeBidValidRequestReturns201() throws Exception {
        // Arrange
        String auctionIdString = "AU-12345678";

        Auction auctionDouble = mock(Auction.class);
        Bid bidDouble = mock(Bid.class);

        AuctionService.BidPlacementResult result = mock(AuctionService.BidPlacementResult.class);
        when(result.auction()).thenReturn(auctionDouble);
        when(result.bid()).thenReturn(bidDouble);

        when(_auctionService.placeBid(any(), any(), any()))
                .thenReturn(result);

        BidResponseDTO dto = new BidResponseDTO(
                "0bc6c8bf-6f51-4f1a-b6af-cde1dbfbb1ad",
                auctionIdString,
                "buyer@aeiou.com",
                20.0,
                "EUR",
                Instant.parse("2026-06-10T10:00:00Z")
        );

        dto.add(org.springframework.hateoas.Link.of(
                "http://localhost/auctions/" + auctionIdString, "auction"));
        dto.add(org.springframework.hateoas.Link.of(
                "http://localhost/auctions/" + auctionIdString + "/bids", "bids"));

        when(_bidResponseDTOMapper.toDTO(auctionDouble, bidDouble)).thenReturn(dto);

        String requestBody = """
            {
              "bidValue": 20.0,
              "currency": "EUR"
            }
            """;

        // Act + Assert
        mockMvc.perform(post("/auctions/{auctionId}/bids", auctionIdString)
                        .header("X-User-Id", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.auctionId").value(auctionIdString))
                .andExpect(jsonPath("$.offerPrice").value(20.0))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$._links.auction").exists())
                .andExpect(jsonPath("$._links.auction.href")
                        .value("http://localhost/auctions/" + auctionIdString))
                .andExpect(jsonPath("$._links.bids").exists())
                .andExpect(jsonPath("$._links.bids.href")
                .value("http://localhost/auctions/" + auctionIdString + "/bids"));
    }

    @Test
    void placeBidServiceThrowsExceptionReturns404() throws Exception {
        // Arrange
        String auctionIdString = "AU-12345678";

        when(_auctionService.placeBid(any(), any(), any()))
                .thenThrow(new IllegalStateException("Auction not active"));

        String requestBody = """
        {
          "bidValue": 5.0,
          "currency": "EUR"
        }
        """;

        // Act + Assert
        mockMvc.perform(post("/auctions/{auctionId}/bids", auctionIdString)
                        .header("X-User-Id", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

}