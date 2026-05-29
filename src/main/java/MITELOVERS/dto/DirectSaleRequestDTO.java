package MITELOVERS.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class DirectSaleRequestDTO {

    @JsonProperty("itemIds")
    private final List<String> itemIds;

    @JsonProperty("priceAmount")
    private final Double priceValue;

    @JsonProperty("priceCurrency")
    private final String priceCurrency;

    @JsonProperty("timeLimitSeconds")
    private final Long timeLimitSeconds; // null = unlimited

}
