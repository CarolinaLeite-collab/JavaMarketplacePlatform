package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;

/**
 * Controller responsible for sharing publicly a list of a {@link UserId}.
 */
public class ShareListPubliclyController {

    private final ListOfItemsService _service;

    public ShareListPubliclyController(ListOfItemsService service) {
        _service = service;
    }

    public ListOfItemsResponseDTO shareListPublicly(String listId, MakeListPublicRequestDTO dto) {
        ListOfItemsResponseDTO result = _service.makePublic(listId, dto);

        return result;
    }

}