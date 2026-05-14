package MITELOVERS.persistence.mem;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-Memory Repository implementation of {@link IPublishingCompanyRepo} for storing and managing {@link PublishingCompany}
 * aggregates during runtime (they are lost when the application stops).
 * <p>
 * Provides methods to save new publishing companies, list all stored publishing companies,
 * retrieves stored publishing companies by their {@link PublishingCompanyId}, and check
 * if a publishing company already exists for a given identity
 *
 */

@Repository
@Profile("mem")
public class MemPublishingCompanyRepo implements IPublishingCompanyRepo {

    private final Map<PublishingCompanyId, PublishingCompany> DATA = new HashMap<PublishingCompanyId, PublishingCompany>();

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

        if (!containsOfIdentity(publishingCompanyId)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(publishingCompanyId));

        }
    }

    @Override
    public boolean containsOfIdentity(PublishingCompanyId publishingCompanyId) {

        return DATA.containsKey(publishingCompanyId);

    }

    @Override
    public List<PublishingCompanyId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }

}
