package MITELOVERS.mapper;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.dto.response.BidResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper responsible for converting {@link Bid} domain entities into {@link BidResponseDTO} instances.
 <p>
 * This component is responsible for transforming bid domain objects into
 * a representation suitable for API responses. It extracts the relevant bid
 * data and converts domain-specific value objects into primitive values that
 * can be returned to clients.
 </p>
 */

@Component
public class BidResponseDTOMapper {

    public BidResponseDTO toDTO(Auction auction, Bid bid) {

        String bidId = bid.identity().toString();
        String auctionId = auction.identity().toString();
        String buyerId = bid.getUserId().toString();

        Price offerPrice = bid.getOfferPrice();
        double amount = offerPrice.getValue();
        String currency = offerPrice.getCurrency().toString();

        return new BidResponseDTO(
                bidId,
                auctionId,
                buyerId,
                amount,
                currency,
                bid.getBidDate()
        );
    }
}