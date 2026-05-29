package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;

/**
 * Controller responsible for handling the creation of private lists of items for a user.
 * <p>
 * This class delegates the actual creation logic to {@link IListOfItemsRepo}
 * and providing access to official genres from {@link IGenreRepo}.
 * </p>
 */

public class CreatePrivateListOfItemsController {

    private final ListOfItemsService _service;

    public CreatePrivateListOfItemsController(ListOfItemsService service) {
        _service = service;
    }

    public ListOfItemsResponseDTO createListOfItems(String userId, ListOfItemsRequestDTO dto) {
        ListOfItemsResponseDTO result = _service.save(userId, dto);
        return result;
    }

}