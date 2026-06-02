package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class GetDirectSaleItemsByGenreControllerTest {

    @Mock
    private DirectSaleService _directSaleService;

    // SUT
    @InjectMocks
    private GetDirectSaleItemsByGenreController _controller;

    @Test
    void controllerShouldInstantiate() {
        assertNotNull(_controller);
    }

    @Test
    void shouldDelegateCallToService() {
        // Arrange
        String genreId = "FICTION";

        List<DirectSaleId> expected = List.of(
                new DirectSaleId("DS-A1B2C3D4"),
                new DirectSaleId("DS-A4B2C3D4")
        );

        when(_directSaleService.getDirectSaleItemsByGenreAsc(genreId))
                .thenReturn(expected);

        // Act
        List<DirectSaleId> result =
                _controller.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertSame(expected, result);
    }

}
