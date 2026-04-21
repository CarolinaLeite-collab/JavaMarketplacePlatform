package MITELOVERS.domain.publishingcompany;

/**
 * Factory responsible for creating {@link PublishingCompany} instances.
 * <p>
 * IllegalArgumentException is thrown if publishingCompanyName is invalid (as defined by {@link MITELOVERS.domain.valueobject.PublishingCompanyId}'s constructor)
 * </p>
 */

public class PublishingCompanyFactory {

    public PublishingCompany createPublishingCompany(String publishingCompanyName) {

        return new PublishingCompany(publishingCompanyName);

    }

}
