package MITELOVERS.mapper;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.dto.response.SaleResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SaleResponseDTOMapper implements RepresentationModelAssembler<Sale, SaleResponseDTO> {


    @Override
    public SaleResponseDTO toModel(Sale sale) {

        String shoppingCartId = sale.get_saleId().toString();
        String buyerId = sale.get_buyerId().toString();
        double totalAmount = sale.get_totalAmount().getValue();
        String currency = sale.get_totalAmount().getCurrency().toString();
        String createdAt = sale.get_createdAt().toString();
        String completedAt = sale.get_completedAt() != null ? sale.get_completedAt().toString() : null;

        return new SaleResponseDTO(shoppingCartId,
                buyerId,
                totalAmount,
                currency,
                createdAt,
                completedAt);

    }

}
