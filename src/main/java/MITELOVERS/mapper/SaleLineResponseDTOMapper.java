package MITELOVERS.mapper;

import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembler that maps a {@link SaleLine} domain object to a {@link SaleLineResponseDTO},
 * extracting sale line identity, seller, associated direct sale, price, and currency.
 */

@Component
public class SaleLineResponseDTOMapper implements RepresentationModelAssembler<SaleLine, SaleLineResponseDTO> {
    @Override
    public SaleLineResponseDTO toModel(SaleLine saleLine) {

        String saleLineId = saleLine.get_saleLineId().toString();
        String sellerId = saleLine.get_sellerId().toString();
        String directSaleId = saleLine.get_directSaleId().toString();
        double priceAtSale = saleLine.get_priceAtSale().getValue();
        String currency = saleLine.get_priceAtSale().getCurrency().toString();

        return new SaleLineResponseDTO(saleLineId,
                sellerId,
                directSaleId,
                priceAtSale,
                currency);

    }

}
