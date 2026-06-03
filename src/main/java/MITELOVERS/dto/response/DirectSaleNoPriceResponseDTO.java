package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

@Generated
@AllArgsConstructor
@Getter
public class DirectSaleNoPriceResponseDTO extends RepresentationModel<DirectSaleNoPriceResponseDTO> {
    private final String directSaleId;
    private final List<String> itemsId;
    private final Long timeLimitSeconds;
    private final Instant creationDate;

}