package TOPSECRET.persistence.mem;

import TOPSECRET.domain.publicationtype.PublicationTypeFactory;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publishingcompany.PublishingCompanyFactory;
import TOPSECRET.domain.repository.IPublishingCompanyRepo;
import TOPSECRET.domain.valueobject.PublishingCompanyId;

import java.util.*;

/**
 * Repository responsible for storing and managing {@link PublishingCompany} entities.
 * <p>
 * Provides methods to check if a publication company already exists, delegates creation to
 * {@link PublishingCompanyFactory}, stores new publishing companies, and retrieves all stored
 * publishing companies as an unmodifiable list.
 */

public class MemoPublishingCompanyRepo implements IPublishingCompanyRepo {

    private final Map<PublishingCompanyId, PublishingCompany> DATA = new HashMap<PublishingCompanyId, PublishingCompany>();
    private final PublishingCompanyFactory _publishingCompanyFactory;

    public MemoPublishingCompanyRepo(PublishingCompanyFactory publishingCompanyFactory) {

        _publishingCompanyFactory = publishingCompanyFactory;

    }

    @Override
    public PublishingCompany save(PublishingCompany publishingCompany) {

        DATA.put(publishingCompany.identity(), publishingCompany);

        return publishingCompany;
    }

    @Override
    public Iterable<PublishingCompany> findAll() {

        return DATA.values();

    }

    @Override
    public Optional<PublishingCompany> ofIdentity(PublishingCompanyId publishingCompanyId) {

        if(!containsOfIdentity(publishingCompanyId)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(publishingCompanyId));

        }
    }

    @Override
    public boolean containsOfIdentity(PublishingCompanyId publishingCompanyId) {

        return DATA.containsKey(publishingCompanyId);

    }

    public List<PublishingCompanyId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }

    @Override
    public PublishingCompany registerPublishingCompany(String publishingCompanyName) {

        PublishingCompany newPublishingCompany = _publishingCompanyFactory.createPublishingCompany(publishingCompanyName);

        if (containsOfIdentity(newPublishingCompany.identity())) {

            throw new IllegalArgumentException("This publishing company is already registered.");

        }

        return save(newPublishingCompany);

    }

}
