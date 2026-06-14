package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object representing an auction resource returned by the REST API.
 * <p>
 * This DTO contains the information describing an auction, including its
 * identifier, the identifiers of the auctioned items, pricing information,
 * and the period during which the auction is active.
 * </p>
 */

@Generated
@Getter
@AllArgsConstructor
public class AuctionResponseDTO extends RepresentationModel<AuctionResponseDTO> {

    private final String auctionId;
    private final List<String> itemIds;
    private final double startingPrice;
    private final double reservePrice;
    private final Double outrightPrice;
    private final String priceCurrency;
    private final Instant startDate;
    private final Instant endDate;
}
