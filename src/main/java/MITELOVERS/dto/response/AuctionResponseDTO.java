package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

@Generated
@Getter
@AllArgsConstructor
public class AuctionResponseDTO extends RepresentationModel<AuctionResponseDTO> {

    private final String auctionId;
    private final List<String> itemIds;
    private final double startingPrice;
    private final double reservePrice;
    private final Double outrightPrice;
    private final Instant startDate;
    private final Instant endDate;
}
