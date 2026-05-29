package MITELOVERS.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Data Transfer Object used to receive DirectSale data from client requests.
 */

@Getter
@AllArgsConstructor
public class DirectSaleRequestDTO {

    private final List<String> itemsId;
    private final Double priceValue;
    private final String priceCurrency;
    private final Long timeLimitSeconds; // null = unlimited

}
