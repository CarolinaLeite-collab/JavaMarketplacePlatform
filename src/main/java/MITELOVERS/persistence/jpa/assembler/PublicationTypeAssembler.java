package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link PublicationType} domain objects
 * and {@link PublicationTypeDataModel} persistence objects.
 * <p>
 * Delegates domain object creation to {@link PublicationTypeFactory}.
 * </p>
 */

@Component
@AllArgsConstructor
public class PublicationTypeAssembler {

    private final PublicationTypeFactory _publicationTypeFactory;

    public PublicationTypeDataModel toDataModel(PublicationType publicationType) {

        PublicationTypeDataModel dmToSave = new PublicationTypeDataModel(publicationType.identity().toString());

        return dmToSave;

    }

    public PublicationType toDomain(PublicationTypeDataModel dataModel) {

        PublicationTypeId id = new PublicationTypeId(dataModel.getPublicationTypeId());

        PublicationType reconstructedPublicationType = _publicationTypeFactory.createPublicationType(id);

        return reconstructedPublicationType;

    }

}
