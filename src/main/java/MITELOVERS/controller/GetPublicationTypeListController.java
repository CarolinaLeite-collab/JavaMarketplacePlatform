package MITELOVERS.controller;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import org.springframework.stereotype.Controller;

/**
 * Controller responsible for retrieving the list of all publication types.
 * <p>
 * This controller interacts with the {@link IPublicationTypeRepo} to fetch
 * all existing {@link PublicationType} instances in the system.
 * </p>
 */

@Controller
public class GetPublicationTypeListController {

    private final IPublicationTypeRepo _iPublicationTypeRepo;

    public GetPublicationTypeListController(IPublicationTypeRepo iPublicationTypeRepo) {
        _iPublicationTypeRepo = iPublicationTypeRepo;
    }

    public Iterable<PublicationType> getListOfPublicationTypes() {
        return _iPublicationTypeRepo.findAll();
    }
}
