package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.persistence.jpa.datamodel.PublishingCompanyDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class PublishingCompanyAssembler {

    private PublishingCompanyFactory _publishingCompanyFactory;

    public PublishingCompanyDataModel toDataModel(PublishingCompany publishingCompany) {

        if (publishingCompany == null) {
            throw new IllegalArgumentException("PublishingCompany cannot be null");
        }

        return new PublishingCompanyDataModel(
                publishingCompany.identity().toString());
    }

    public PublishingCompany toDomain(PublishingCompanyDataModel dataModel) {

        if (dataModel == null) {
            throw new IllegalArgumentException("PublishingCompanyDataModel cannot be null");
        }

        PublishingCompanyId publishingCompanyId =
                new PublishingCompanyId(dataModel.getPublishingCompanyId());

        PublishingCompany publishingCompany = _publishingCompanyFactory.createPublishingCompany(publishingCompanyId);

        return publishingCompany;
    }
}
