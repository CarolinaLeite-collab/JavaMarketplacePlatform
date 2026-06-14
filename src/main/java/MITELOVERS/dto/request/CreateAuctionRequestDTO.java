package MITELOVERS.dto.request;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object representing the request payload used to create an auction.
 * <p>
 * This DTO contains the information required to place one or more items on
 * auction, including the identifiers of the items, pricing information,
 * and the time interval during which the auction will be active.
 * </p>
 */

@Generated
@Getter
@AllArgsConstructor
public class CreateAuctionRequestDTO {

    private final List<String> itemIds;
    private final double startingPrice;
    private final double reservePrice;
    private final Double outrightPrice;
    private final String priceCurrency;
    private final Instant startDate;
    private final Instant endDate;

}
