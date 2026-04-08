package TOPSECRET.domain.publishingcompany;

/**
 * Factory responsible for creating {@link PublishingCompany} instances.
 */

public class PublishingCompanyFactory {

    public PublishingCompany createPublishingCompany(String publishingCompanyName) {

        return new PublishingCompany(publishingCompanyName);

    }

}
