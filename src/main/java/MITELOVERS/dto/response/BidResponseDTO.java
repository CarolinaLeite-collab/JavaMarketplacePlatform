package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

/**
 * Data Transfer Object used to expose bid information in API responses
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BidResponseDTO extends RepresentationModel<BidResponseDTO> {

    private String bidId;
    private String auctionId;
    private String buyerId;
    private double offerPrice;
    private String currency;
    private Instant bidDate;
}