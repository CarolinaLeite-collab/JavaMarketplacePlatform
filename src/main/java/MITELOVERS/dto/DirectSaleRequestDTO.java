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

    @JsonProperty("_itemsId")
    private final List<String> _itemsId;

    @JsonProperty("_priceAmount")
    private final Double _priceValue;

    @JsonProperty("_priceCurrency")
    private final String _priceCurrency;

    @JsonProperty("_timeLimitSeconds")
    private final Long _timeLimitSeconds; // null = unlimited

}
