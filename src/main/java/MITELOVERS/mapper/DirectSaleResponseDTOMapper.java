package MITELOVERS.mapper;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import org.springframework.stereotype.Component;

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
