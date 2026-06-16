package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.AuctionLinkProvider;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.dto.request.CreateAuctionRequestDTO;
import MITELOVERS.dto.response.AuctionResponseDTO;
import MITELOVERS.mapper.AuctionResponseDTOMapper;
import MITELOVERS.dto.request.PlaceBidRequestDTO;
import MITELOVERS.dto.response.BidResponseDTO;
import MITELOVERS.mapper.BidResponseDTOMapper;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.domain.auction.Bid;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<RepresentationModel<?>> options(
            @RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();
        for (Link link : _auctionLinkProvider.getLinks(user)) {
            model.add(link);
        }

        return ResponseEntity.ok(model);
    }

    @RequestMapping(path = "/{auctionId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> optionsForSpecificAuction(
            @PathVariable String auctionId,
            @RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();

        for (Link link : _auctionLinkProvider.getLinks(user, auctionId)) {
            model.add(link);
        }

        return ResponseEntity.ok(model);
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

        response.forEach(dto ->
                dto.add(linkTo(methodOn(AuctionRestController.class)
                        .getAllActiveAuctions())
                        .slash(dto.getAuctionId())
                        .withSelfRel())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAuction (@RequestBody CreateAuctionRequestDTO request) {
        try {
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

            Auction auction = _auctionService.putItemOnAuction(
                    itemIds, startingPrice, reservePrice, outrightPrice, startDate, endDate
            );

            AuctionResponseDTO dto = _auctionMapper.toDTO(auction);

            dto.add(linkTo(AuctionRestController.class)
                    .withSelfRel());

            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(
            path = "/{auctionId}/bids",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> placeBid(
            @PathVariable String auctionId,
            @RequestHeader("X-User-Id") String userIdFromHeader,
            @RequestBody PlaceBidRequestDTO request) {
        try {
            Email email = new Email(userIdFromHeader);
            UserId userId = new UserId(email);

            Currency currency = Currency.valueOf(request.getCurrency());
            Price offerPrice = new Price(request.getOfferPrice(), currency);

            var result = _auctionService.placeBid(auctionId, userId, offerPrice);
            Auction auction = result.auction();
            Bid bid = result.bid();

            BidResponseDTO dto = _bidResponseDTOMapper.toDTO(auction, bid);

            _auctionLinkProvider.addBidLinks(dto);

            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
