package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response DTO representing a shopping cart line, including the associated direct sale,
 * seller, price at the time of addition, currency, and timestamp.
 * Extends {@link RepresentationModel} to support HATEOAS links.
 */

@Getter
@Generated
@AllArgsConstructor
public class ShoppingCartLineResponseDTO extends RepresentationModel<ShoppingCartLineResponseDTO> {

    private String shoppingCartLineId;
    private String directSaleId;
    private String sellerId;
    private double priceAtAddition;
    private String currency;
    private String addedAt;

}
