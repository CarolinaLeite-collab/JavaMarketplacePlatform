package MITELOVERS.dto.request;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

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
