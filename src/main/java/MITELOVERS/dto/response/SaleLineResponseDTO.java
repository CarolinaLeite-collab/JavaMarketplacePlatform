package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

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
