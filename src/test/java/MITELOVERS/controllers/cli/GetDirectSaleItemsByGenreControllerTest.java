package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.controllers.cli.GetDirectSaleItemsByGenreController;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
        String genreId = "GEN-123";
        DSFilteredItemsResponseDTO expected =
                new DSFilteredItemsResponseDTO(
                        List.of(new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-1"))
                );

        when(_directSaleService.getDirectSaleItemsByGenreAsc(genreId))
                .thenReturn(expected);

        // Act
        DSFilteredItemsResponseDTO result =
                _controller.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertSame(expected, result);
    }

}
