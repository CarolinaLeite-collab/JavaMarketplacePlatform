package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class DirectSaleResponseDTO extends RepresentationModel<DirectSaleResponseDTO> {

    private final String directSaleId;
    private final List<String> itemsId;
    private final Double priceValue;
    private final String priceCurrency;
    private final Long timeLimitSeconds;
    private final Instant creationDate;

}
