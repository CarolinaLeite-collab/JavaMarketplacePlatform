package MITELOVERS.controllers.cli;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import org.springframework.stereotype.Controller;

/**
 * Controller responsible for handling the registration of a new {@link PublishingCompany}.
 * Delegates the creation to {@link PublishingCompanyFactory} and storage to
 * {@link IPublishingCompanyRepo}.
 * Validates uniqueness.
 */

@Controller
public class RegisterPublishingCompanyController {

    private final PublishingCompanyService _publishingCompanyService;

    public RegisterPublishingCompanyController( PublishingCompanyService publishingCompanyFactory) {

        _publishingCompanyService = publishingCompanyFactory;

    }

    public PublishingCompanyResponseDTO registerPublishingCompany(PublishingCompanyRequestDTO dto) {

        return _publishingCompanyService.registerPublishingCompany(dto);

    }

}
