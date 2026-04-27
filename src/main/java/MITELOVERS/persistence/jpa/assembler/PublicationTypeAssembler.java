package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import org.springframework.stereotype.Component;

@Component
public class PublicationTypeAssembler {

    private final PublicationTypeFactory _publicationTypeFactory;

    public PublicationTypeAssembler(PublicationTypeFactory publicationTypeFactory) {

        _publicationTypeFactory = publicationTypeFactory;

    }

    public PublicationTypeDataModel domain2DM(PublicationType publicationType) {

        return new PublicationTypeDataModel(publicationType);

    }

    public PublicationType DM2Domain(PublicationTypeDataModel dataModel) {

        PublicationTypeId id = new PublicationTypeId(dataModel.getPublicationTypeId());

        return _publicationTypeFactory.createPublicationType(id);

    }

}
