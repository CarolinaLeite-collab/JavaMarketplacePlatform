package MITELOVERS.mapper;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectSaleResponseDTOMapperTest {

    private DirectSaleResponseDTOMapper _mapper;

    @BeforeEach
    void setup() {
        _mapper = new DirectSaleResponseDTOMapper();
    }

    @Test
    void toResponseDTO_shouldMapFieldsCorrectly() {
        //Arrange
        DirectSale directSale = mock(DirectSale.class);
        Instant now = Instant.now();
        Instant endDate = now.plusSeconds(3600);
        UserId userId = mock(UserId.class);

        when(directSale.identity()).thenReturn(new DirectSaleId("DS-ABC12345"));
        when(directSale.getItemsId()).thenReturn(List.of(new ItemId("ABCDEF1234")));
        when(directSale.getPrice()).thenReturn(new Price(10.0, Currency.EUR));
        when(directSale.getTimeLimit()).thenReturn(Duration.ofSeconds(3600));
        when(directSale.getCreationDate()).thenReturn(now);
        when(directSale.getEndDate()).thenReturn(endDate);
        when(directSale.getDSStatus()).thenReturn(DirectSaleStatus.ACTIVE);
        when(directSale.getSellerId()).thenReturn(userId);

        //Act
        DirectSaleResponseDTO dto = _mapper.toResponseDTO(directSale);

        //Assert
        assertEquals("DS-ABC12345", dto.getDirectSaleId());
        assertEquals(List.of("ABCDEF1234"), dto.getItemsId());
        assertEquals(10.0, dto.getPriceValue());
        assertEquals("EUR", dto.getPriceCurrency());
        assertEquals(3600L, dto.getTimeLimitSeconds());
        assertEquals(now, dto.getCreationDate());
    }

}