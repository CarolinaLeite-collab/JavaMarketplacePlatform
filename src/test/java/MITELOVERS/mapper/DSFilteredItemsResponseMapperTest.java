package MITELOVERS.mapper;

import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DSFilteredItemsResponseMapperTest {

    private DSFilteredItemsResponseMapper _mapper;

    @BeforeEach
    void setup() {

        _mapper = new DSFilteredItemsResponseMapper();
    }

    @Test
    void toDTO_shouldMapDirectSaleIdsToEntries() {
        // Arrange
        List<String> ids = List.of("DS-A1B2C3D4", "DS-1234ABCD");

        // Act
        DSFilteredItemsResponseDTO dto = _mapper.toDTO(ids);

        // Assert
        assertEquals(2, dto.getDirectSales().size());
        assertEquals("DS-A1B2C3D4", dto.getDirectSales().get(0).getDirectSaleId());
        assertEquals("DS-1234ABCD", dto.getDirectSales().get(1).getDirectSaleId());
    }

    @Test
    void toDTO_shouldHandleEmptyList() {
        // Act
        DSFilteredItemsResponseDTO dto = _mapper.toDTO(List.of());

        // Assert
        assertTrue(dto.getDirectSales().isEmpty());
    }

    @Test
    void toDTO_shouldHandleNullList() {
        // Act
        DSFilteredItemsResponseDTO dto = _mapper.toDTO(null);

        // Assert
        assertNotNull(dto);
        assertTrue(dto.getDirectSales().isEmpty());
    }

    @Test
    void toDTO_shouldCreateNewEntryObjects() {
        // Arrange
        List<String> ids = List.of("DS-A1B2C3D4");

        // Act
        DSFilteredItemsResponseDTO dto = _mapper.toDTO(ids);

        // Assert
        DSFilteredItemsResponseDTO.DirectSaleEntry entry = dto.getDirectSales().get(0);

        assertNotNull(entry);
        assertEquals("DS-A1B2C3D4", entry.getDirectSaleId());
    }

}