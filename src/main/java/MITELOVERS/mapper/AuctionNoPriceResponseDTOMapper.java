package MITELOVERS.mapper;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.AuctionNoPriceResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Maps {@link Auction} domain entities to {@link AuctionNoPriceResponseDTO} instances.
 * <p>
 * This component is responsible for transforming auction domain objects into
 * a representation suitable for API responses. It extracts the relevant auction
 * data and converts domain-specific value objects into primitive or serializable
 * values that can be returned to clients.
 * </p>
 */

@Component
public class AuctionNoPriceResponseDTOMapper {
    public AuctionNoPriceResponseDTO toDTO(Auction auction) {

        List<String> itemIds = auction.getItemsId().stream()
                .map(ItemId::toString)
                .toList();

        return new AuctionNoPriceResponseDTO(
                auction.identity().toString(),
                itemIds,
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate(),
                auction.getSeller().toString()
        );
    }
}
