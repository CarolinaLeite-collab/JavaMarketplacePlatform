package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * Response DTO representing a sale, including the buyer, total amount, currency,
 * and timestamps for creation and completion. {@code completedAt} is null for
 * pending or cancelled sales. Extends {@link RepresentationModel} to support HATEOAS links.
 */

@Generated
@Getter
@AllArgsConstructor
public class SaleResponseDTO extends RepresentationModel<SaleResponseDTO> {

    private String saleId;
    private String buyerId;
    private double totalAmount;
    private String currency;
    private String createdAt;
    private String completedAt;
    private List<SaleLineResponseDTO> saleLines;

}
