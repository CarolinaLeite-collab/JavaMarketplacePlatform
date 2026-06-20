package MITELOVERS.dto.response;

import MITELOVERS.domain.valueobject.DirectSaleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

/**
 * Detailed DTO representation of a Direct Sale, used for controller responses
 * where full sale information must be exposed to API clients.
 *
 * <p>Includes identifiers, pricing information, time limits, and creation
 * metadata. Extends {@link RepresentationModel} to allow HATEOAS link
 * enrichment at the controller layer.</p>
 */

@Getter
@AllArgsConstructor
public class DirectSaleResponseDTO extends RepresentationModel<DirectSaleResponseDTO> {

    private final String directSaleId;
    private final List<String> itemsId;
    private final Double priceValue;
    private final String priceCurrency;
    private final Long timeLimitSeconds;
    private final Instant creationDate;
    private final Instant endDate;
    private final DirectSaleStatus status;
    private final String sellerId;

}
