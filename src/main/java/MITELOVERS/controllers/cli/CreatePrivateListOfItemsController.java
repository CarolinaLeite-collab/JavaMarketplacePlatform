package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;

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

    public ListOfItems createListOfItems(String userId, ListOfItemsRequestDTO dto) {

        UserId recUserId = new UserId(new Email(userId));
        Name name = new Name(dto.getName());
        GenreId genreId = new GenreId(dto.getGenreId());


        return _service.save(recUserId, name, genreId);
    }

}