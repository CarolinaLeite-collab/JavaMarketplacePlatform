package TOPSECRET.controller;

import TOPSECRET.domain.IPublicationTypeRepo;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Controller responsible for retrieving the list of all publication types.
 * <p>
 * This controller interacts with the {@link IPublicationTypeRepo} to fetch
 * all existing {@link PublicationType} instances in the system.
 * </p>
 */

public class GetPublicationTypeListController {

    private final IPublicationTypeRepo _iPublicationTypeRepo;

    public GetPublicationTypeListController(IPublicationTypeRepo iPublicationTypeRepo, User user) {
        _iPublicationTypeRepo = iPublicationTypeRepo;
    }

    public List<PublicationType> getListOfPublicationTypes() {
        return _iPublicationTypeRepo.getAll();
    }
}