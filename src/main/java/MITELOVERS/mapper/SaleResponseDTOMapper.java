package MITELOVERS.mapper;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import MITELOVERS.dto.response.SaleResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Assembler that maps a {@link Sale} domain object to a {@link SaleResponseDTO},
 * extracting sale identity, buyer, total amount, currency, and timestamps.
 * Handles null {@code completedAt} for pending or cancelled sales.
 */

@Component
public class SaleResponseDTOMapper implements RepresentationModelAssembler<Sale, SaleResponseDTO> {

    @Override
    public SaleResponseDTO toModel(Sale sale) {

        List<SaleLineResponseDTO> saleLines = sale.get_saleLines().stream()
                .map(this::toSaleLineDTO)
                .toList();

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
                completedAt,
                saleLines);

    }

    private SaleLineResponseDTO toSaleLineDTO(SaleLine line) {
        return new SaleLineResponseDTO(
                line.get_saleLineId().toString(),
                line.get_sellerId().toString(),
                line.get_directSaleId().toString(),
                line.get_priceAtSale().getValue(),
                line.get_priceAtSale().getCurrency().toString()
        );
    }
}