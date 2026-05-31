package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
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

    public DSFilteredItemsResponseDTO getDirectSaleItemsByGenreAsc(String genreId) {
        return _directSaleService.getDirectSaleItemsByGenreAsc(genreId);
    }

}
