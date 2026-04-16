package TOPSECRET.controller;

import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.repository.IPublicationTypeRepo;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for retrieving the list of all publication types.
 * <p>
 * This controller interacts with the {@link IPublicationTypeRepo} to fetch
 * all existing {@link PublicationType} instances in the system.
 * </p>
 */

public class GetPublicationTypeListController {

    private final IPublicationTypeRepo _iPublicationTypeRepo;

    public GetPublicationTypeListController(IPublicationTypeRepo iPublicationTypeRepo, UserId userId) {
        _iPublicationTypeRepo = iPublicationTypeRepo;
    }

    public Iterable<PublicationType> getListOfPublicationTypes() {
        return _iPublicationTypeRepo.findAll();
    }
}