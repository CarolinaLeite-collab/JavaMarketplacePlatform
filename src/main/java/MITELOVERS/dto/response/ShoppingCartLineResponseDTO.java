package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

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
