package MITELOVERS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotEmpty
    private final List<String> itemIds;

    @Positive
    private final double startingPrice;

    @Positive
    private final double reservePrice;

    @Positive
    private final Double outrightPrice;

    @NotBlank
    private final String priceCurrency;

    @NotNull
    private final Instant startDate;

    @NotNull
    private final Instant endDate;

}
