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
 * REST controller responsible for managing auction-related HTTP requests.
 * <p>
 * This controller exposes endpoints for creating auctions and acts as the
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
    private final UserService _userService;
    private final AuctionLinkProvider _auctionLinkProvider;


    public AuctionRestController(AuctionService auctionService, AuctionResponseDTOMapper auctionMapper, UserService userService,
                                 AuctionLinkProvider auctionLinkProvider) {
        _auctionService = auctionService;
        _auctionMapper = auctionMapper;
        _userService = userService;
        _auctionLinkProvider = auctionLinkProvider;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(
            @RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();
        _auctionLinkProvider.getLinks(user).forEach(link -> model.add((Iterable<Link>) link));

        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAuction(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CreateAuctionRequestDTO request) {
        try {
            List<ItemId> itemIds = request.getItemIds().stream()
                    .map(ItemId::new)
                    .toList();

            Price startingPrice = new Price(request.getStartingPrice(), Currency.EUR);
            Price reservePrice = new Price(request.getReservePrice(), Currency.EUR);
            Price outrightPrice = request.getOutrightPrice() != null
                    ? new Price(request.getOutrightPrice(), Currency.EUR)
                    : null;

            ZonedDateTime startDate = request.getStartDate().atZone(ZoneId.of("UTC"));
            ZonedDateTime endDate = request.getEndDate().atZone(ZoneId.of("UTC"));

            Auction auction = _auctionService.putItemOnAuction(
                    itemIds, startingPrice, reservePrice, outrightPrice, startDate, endDate
            );

            AuctionResponseDTO dto = _auctionMapper.toDTO(auction);

            dto.add(linkTo(methodOn(AuctionRestController.class)
                    .createAuction(userId, request)).withSelfRel());

            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
