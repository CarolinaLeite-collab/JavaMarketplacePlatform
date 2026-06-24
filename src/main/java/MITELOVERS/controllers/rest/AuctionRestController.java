package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.AuctionLinkProvider;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.CreateAuctionRequestDTO;
import MITELOVERS.dto.response.AuctionResponseDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.AuctionResponseDTOMapper;
import MITELOVERS.dto.request.PlaceBidRequestDTO;
import MITELOVERS.dto.response.BidResponseDTO;
import MITELOVERS.mapper.BidResponseDTOMapper;
import MITELOVERS.domain.auction.Bid;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * REST controller responsible for managing auction- and bidding-related HTTP requests.
 * <p>
 * This controller exposes endpoints for creating auctions and bids, acting as the
 * entry point between API clients and the application layer.
 * It converts incoming request data into domain objects, delegates business
 * operations to the {@link AuctionService}, and maps domain entities to
 * response DTOs.
 * </p>
 */

@RestController
@RequestMapping("/auctions")
public class AuctionRestController {
    private final AuctionService _auctionService;
    private final AuctionResponseDTOMapper _auctionMapper;
    private final BidResponseDTOMapper _bidResponseDTOMapper;
    private final UserService _userService;
    private final AuctionLinkProvider _auctionLinkProvider;


    public AuctionRestController(AuctionService auctionService, AuctionResponseDTOMapper auctionMapper, BidResponseDTOMapper bidResponseDTOMapper, UserService userService,
                                 AuctionLinkProvider auctionLinkProvider) {
        _auctionService = auctionService;
        _auctionMapper = auctionMapper;
        _bidResponseDTOMapper = bidResponseDTOMapper;
        _userService = userService;
        _auctionLinkProvider = auctionLinkProvider;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options(
            @RequestHeader("X-User-Id") String email) {

        User user = _userService.getUserByEmail(email);
        List<HttpMethod> methods = _auctionLinkProvider.getAllowedMethods(user);

        return ResponseEntity.ok()
                .allow(methods.toArray(HttpMethod[]::new))
                .build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuctionResponseDTO>> getAllActiveAuctions() {

        List<Auction> auctions = _auctionService.getAllActiveAuctions();

        if (auctions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<AuctionResponseDTO> response = auctions.stream()
                .map(_auctionMapper::toDTO)
                .toList();

        response.forEach(dto -> _auctionLinkProvider.addLinksForAuction(dto));

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuctionResponseDTO> createAuction (@RequestBody @Valid CreateAuctionRequestDTO request, @RequestHeader("X-User-Id") String email) {
        List<ItemId> itemIds = request.getItemIds().stream()
                .map(ItemId::new)
                .toList();

        Currency currency = Currency.valueOf(request.getPriceCurrency());
        Price startingPrice = new Price(request.getStartingPrice(), currency);
        Price reservePrice = new Price(request.getReservePrice(), currency);
        Price outrightPrice = request.getOutrightPrice() != null
                ? new Price(request.getOutrightPrice(), currency)
                : null;

        ZonedDateTime startDate = request.getStartDate().atZone(ZoneId.of("UTC"));
        ZonedDateTime endDate = request.getEndDate().atZone(ZoneId.of("UTC"));

        UserId seller = new UserId(new Email(email));

        Auction auction = _auctionService.putItemOnAuction(
                itemIds, startingPrice, reservePrice, outrightPrice, startDate, endDate, seller
        );

        AuctionResponseDTO dto = _auctionMapper.toDTO(auction);

        _auctionLinkProvider.addLinksForAuction(dto);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{auctionId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForSpecificAuction(
            @RequestHeader("X-User-Id") String email) {

        User user = _userService.getUserByEmail(email);

        List<HttpMethod> methods = _auctionLinkProvider.getAllowedMethodsForSpecificAuction(user);

        return ResponseEntity.ok()
                .allow(methods.toArray(HttpMethod[]::new))
                .build();
    }

    @GetMapping(
            path = "/{auctionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuctionResponseDTO> getAuctionById(@PathVariable String auctionId) {

        Auction auction = _auctionService.getAuctionById(new AuctionId(auctionId));

        AuctionResponseDTO dto = _auctionMapper.toDTO(auction);

        _auctionLinkProvider.addLinksForAuction(dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @RequestMapping(path = "/{auctionId}/bids", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForBids(
            @PathVariable String auctionId,
            @RequestHeader("X-User-Id") String email) {

        User user = _userService.getUserByEmail(email);
        AuctionId reconstructedAuctionId = new AuctionId(auctionId);
        Auction auction  = _auctionService.getAuctionById(reconstructedAuctionId);

        List<HttpMethod> methods =
                _auctionLinkProvider.getAllowedMethodsForBids(user, auction);

        ResponseEntity<Void> response = ResponseEntity.ok()
                .allow(methods.toArray(HttpMethod[]::new))
                .build();

        return response;
    }

    @GetMapping(
            path = "/{auctionId}/bids",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CollectionModel<BidResponseDTO>> getBidsForAuction(@PathVariable String auctionId) {

        Auction auction = _auctionService.getAuctionById(new AuctionId(auctionId));

        // Map each Bid to BidResponseDTO
        List<BidResponseDTO> dtos = auction.getBids().stream()
                .map(bid -> _bidResponseDTOMapper.toDTO(auction, bid))
                .toList();

        dtos.forEach(_auctionLinkProvider::addLinksForBidInCollection);

        CollectionModel<BidResponseDTO> collectionModel =
                _auctionLinkProvider.addLinksForBidCollection(dtos, auctionId);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);

    }

    @PostMapping(
            path = "/{auctionId}/bids",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BidResponseDTO> placeBid(
            @PathVariable String auctionId,
            @RequestHeader("X-User-Id") String userIdFromHeader,
            @RequestBody @Valid PlaceBidRequestDTO request) {

        Email email = new Email(userIdFromHeader);
        UserId userId = new UserId(email);

        Currency currency = Currency.valueOf(request.getCurrency());
        Price offerPrice = new Price(request.getOfferPrice(), currency);

        var result = _auctionService.placeBid(new AuctionId(auctionId), userId, offerPrice);
        Auction auction = result.auction();
        Bid bid = result.bid();

        BidResponseDTO dto = _bidResponseDTOMapper.toDTO(auction, bid);

        _auctionLinkProvider.addLinksForCreatedBid(dto);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }

}
