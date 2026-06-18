package MITELOVERS.applicationservices;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Application service responsible for publishing company registration and retrieval.
 * <p>
 * Acts as the entry point between controllers and the publishing company domain,
 * delegating creation to {@link PublishingCompanyFactory} and persistence to
 * {@link IPublishingCompanyRepo}. Returns pure domain objects; mapping to DTOs and
 * adding HATEOAS links is the responsibility of the calling controller.
 * </p>
 */
@Service
public class PublishingCompanyService {

    private final PublishingCompanyFactory _publishingCompanyFactory;
    private final IPublishingCompanyRepo _iPublishingCompanyRepo;

    public PublishingCompanyService(PublishingCompanyFactory publishingCompanyFactory,
                                    IPublishingCompanyRepo iPublishingCompanyRepo) {

        _publishingCompanyFactory = publishingCompanyFactory;
        _iPublishingCompanyRepo = iPublishingCompanyRepo;
    }

    public PublishingCompany registerPublishingCompany(PublishingCompanyRequestDTO publishingCompanyName) {

        String newPubCompName = publishingCompanyName.toString();

        PublishingCompany newPublishingCompany = _publishingCompanyFactory.createPublishingCompany(newPubCompName);

        if (_iPublishingCompanyRepo.containsOfIdentity(newPublishingCompany.identity())) {
            throw new IllegalStateException("Publishing company already exists");
        }

        return _iPublishingCompanyRepo.save(newPublishingCompany);
    }

    public List<PublishingCompany> getAllPublishingCompanies() {

        Iterable<PublishingCompany> publishingCompanies = _iPublishingCompanyRepo.findAll();

        List<PublishingCompany> result = new ArrayList<>();

        for (PublishingCompany publishingCompany : publishingCompanies) {
            result.add(publishingCompany);
        }

        return result;
    }

    public PublishingCompany getPublishingCompanyById(String publishingCompanyId) {

        PublishingCompanyId id = new PublishingCompanyId(publishingCompanyId);

        return _iPublishingCompanyRepo.ofIdentity(id)
                .orElseThrow(() -> new NoSuchElementException("Publishing Company not found"));
    }

}