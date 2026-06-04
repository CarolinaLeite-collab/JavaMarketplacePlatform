package MITELOVERS.mapper;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.dto.response.DirectSaleNoPriceResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DirectSaleNoPriceResponseDTOMapperTest {

    @Test
    void toModelShouldCorrectlyConvertDomainIntoDTO() {
        //arrange
        DirectSale directSale = mock(DirectSale.class);

        when(directSale.identity()).thenReturn(new DirectSaleId("DS-ABC12345"));
        when(directSale.getItemsId()).thenReturn(List.of(new ItemId("ABCDEF1234")));
        when(directSale.getPrice()).thenReturn(new Price(10.0, Currency.EUR));
        when(directSale.getTimeLimit()).thenReturn(Duration.ofSeconds(3600));
        Instant now = Instant.now();
        when(directSale.getCreationDate()).thenReturn(now);

        //SUT
        DirectSaleNoPriceResponseDTOMapper mapper =  new DirectSaleNoPriceResponseDTOMapper();

        //act
        DirectSaleNoPriceResponseDTO dto = mapper.toModel(directSale);

        //assert
        assertEquals("DS-ABC12345", dto.getDirectSaleId());
        assertEquals(List.of("ABCDEF1234"), dto.getItemsId());
        assertEquals(3600L, dto.getTimeLimitSeconds());
        assertEquals(now, dto.getCreationDate());
    }
}