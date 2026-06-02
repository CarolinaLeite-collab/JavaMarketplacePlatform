package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.SharedDuration;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;

/**
 * Controller responsible for sharing publicly a list of a {@link UserId}.
 */
public class ShareListPubliclyController {

    private final ListOfItemsService _service;
    private ListOfItemsResponseDTOMapper _mapper;

    public ShareListPubliclyController(ListOfItemsService service,  ListOfItemsResponseDTOMapper mapper) {
        _service = service;
        _mapper = mapper;
    }

    public ListOfItemsResponseDTO shareListPublicly(String listId, MakeListPublicRequestDTO dto) {
        ListOfItemsId listOfItemsId = new ListOfItemsId(listId);
        SharedDuration sharedDuration = new SharedDuration(dto.getSharedUntil());

        ListOfItems list = _service.makePublic(listOfItemsId, sharedDuration);

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

}