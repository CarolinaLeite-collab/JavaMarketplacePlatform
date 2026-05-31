package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.springframework.stereotype.Controller;


/**
 * Controller responsible for retrieving items available in direct sales filtered by genre.
 */

@Controller
public class GetDirectSaleItemsByGenreController {

    private final DirectSaleService _directSaleService;

    public GetDirectSaleItemsByGenreController(DirectSaleService directSaleService) {
        _directSaleService = directSaleService;
    }

    public DSFilteredItemsResponseDTO getDirectSaleItemsByGenreAsc(String genreId) {
        return _directSaleService.getDirectSaleItemsByGenreAsc(genreId);
    }

}
