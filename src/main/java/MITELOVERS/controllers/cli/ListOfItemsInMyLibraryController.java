package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.ItemDetailsDTO;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * CLI controller responsible for retrieving detailed information about the items
 * contained in a user's library.
 *
 * <p>
 * This controller delegates all business logic to {@link LibraryService},
 * acting as a thin interface layer for command-line interaction.
 * It follows the same application flow as the REST layer, ensuring
 * consistency across different access channels.
 * </p>
 *
 */

@Controller
public class ListOfItemsInMyLibraryController {

    private final LibraryService _libraryService;

    public ListOfItemsInMyLibraryController(LibraryService libraryService) {
        _libraryService = libraryService;
    }

    public List<ItemDetailsDTO> getListOfItemInfoInMyLibrary(UserId userId) {
        return _libraryService.getListOfItemInfoInMyLibraryFull(userId);

    }
}
