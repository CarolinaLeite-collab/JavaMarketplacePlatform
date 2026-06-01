package MITELOVERS.mapper;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Maps a {@link DirectSale} domain object into a {@link DirectSaleResponseDTO}
 * suitable for exposure at the API layer.
 *
 * <p>This mapper performs a pure transformation from domain to DTO without
 * adding HATEOAS links, which are handled exclusively at the controller
 * level to preserve proper layering.</p>
 */

@Component
public class DirectSaleResponseDTOMapper {

    public DirectSaleResponseDTO toResponseDTO(DirectSale directSale) {

        return new DirectSaleResponseDTO(
                directSale.identity().toString(),
                directSale.getItemsId().stream()
                        .map(ItemId::toString)
                        .toList(),
                directSale.getPrice().getValue(),
                directSale.getPrice().getCurrency().name(),
                directSale.getTimeLimit() != null ? directSale.getTimeLimit().getSeconds() : null,
                directSale.getCreationDate()
        );
    }

}
