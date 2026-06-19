package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Generated
@AllArgsConstructor
public class ShoppingCartResponseDTO extends RepresentationModel<ShoppingCartResponseDTO> {

    private String shoppingCartId;
    private String buyerId;
    private Double totalAmount;
    private String currency;

}
