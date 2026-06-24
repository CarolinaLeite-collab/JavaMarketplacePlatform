package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response DTO representing a sale line, including the seller, associated direct sale,
 * price, and currency. Extends {@link RepresentationModel} to support HATEOAS links.
 */

@Generated
@Getter
@AllArgsConstructor
public class SaleLineResponseDTO extends RepresentationModel<SaleLineResponseDTO> {

    private String saleLineId;
    private String sellerId;
    private String directSaleId;
    private double price;
    private String currency;

}
