package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.domain.valueobject.DirectSaleId;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * Controller responsible for retrieving items available in direct sales filtered by genre.
 */

@Controller
public class GetDirectSaleItemsByGenreController {

    private final DirectSaleService _directSaleService;

    public GetDirectSaleItemsByGenreController(DirectSaleService directSaleService) {
        _directSaleService = directSaleService;
    }

    public List<DirectSaleId> getDirectSaleItemsByGenreAsc(String genreId) {
        return _directSaleService.getDirectSaleItemsByGenreAsc(genreId);
    }

}
