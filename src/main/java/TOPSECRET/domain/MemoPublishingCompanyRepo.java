package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for storing and managing {@link PublishingCompany} entities.
 * <p>
 * Handles the registration of publishing companies using a {@link PublishingCompanyFactory}
 * and ensures that duplicate entities are not added to the repository.
 */

public class MemoPublishingCompanyRepo implements IPublishingCompanyRepo {

    private final List<PublishingCompany> _publishingCompany = new ArrayList<>();
    private final PublishingCompanyFactory _publishingCompanyFactory;

    public MemoPublishingCompanyRepo(PublishingCompanyFactory publishingCompanyFactory) {

        _publishingCompanyFactory = publishingCompanyFactory;

    }

    @Override
    public PublishingCompany registerPublishingCompany(String publishingCompanyName) {

        if (publishingCompanyExists(publishingCompanyName)) {

            throw new IllegalArgumentException(("Publishing Company with name " + publishingCompanyName + " already exists."));

        }

        PublishingCompany newPublishingCompany = _publishingCompanyFactory.createPublishingCompany(publishingCompanyName);

        _publishingCompany.add(newPublishingCompany);

        return newPublishingCompany;

    }

    private boolean publishingCompanyExists(String publishingCompanyName) {

        for (PublishingCompany publishingCompany : _publishingCompany) {

            if (publishingCompany.isSamePublishingCompany(publishingCompanyName)) {

                return true;

            }

        }

        return false;

    }

}
