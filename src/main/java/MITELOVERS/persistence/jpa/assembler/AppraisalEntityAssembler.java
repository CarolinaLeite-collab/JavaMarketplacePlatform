package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.appraisalentity.AppraisalEntityFactory;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.datamodel.AppraisalEntityDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler responsible for converting between {@link AppraisalEntity}
 * and {@link AppraisalEntityDataModel}.
 * <p>
 * Handles transformation of domain value objects to persistence-friendly
 * formats and reconstruction of domain objects from stored data.
 */

@Component
@AllArgsConstructor
public class AppraisalEntityAssembler {

    private final AppraisalEntityFactory _appraisalEntityFactory;

    public AppraisalEntityDataModel toDataModel(AppraisalEntity appraisalEntity) {

        List<PublicationTypeId> publicationTypeIds = appraisalEntity.getPublicationTypeIds();
        List<GenreId> GenreIds = appraisalEntity.getGenreIds();
        List<String> publicationTypeStringIds = new ArrayList<>();
        List<String> genreStringIds = new ArrayList<>();

        for (PublicationTypeId publicationTypeId : publicationTypeIds) {
            publicationTypeStringIds.add(publicationTypeId.toString());
        }

        for (GenreId genreId : GenreIds) {
            genreStringIds.add(genreId.toString());
        }

        return new AppraisalEntityDataModel(appraisalEntity.identity().toString(),
                appraisalEntity.getName().getName(),
                publicationTypeStringIds,
                genreStringIds
        );

    }

    public AppraisalEntity toDomain(AppraisalEntityDataModel appraisalEntityDataModel) {

        String stringId = appraisalEntityDataModel.getId();
        AppraisalEntityId appraisalEntityId = new AppraisalEntityId(stringId);

        String stringName = appraisalEntityDataModel.getName();
        Name name = new Name(stringName);

        List<PublicationTypeId> publicationTypeIds = new ArrayList<>();
        List <String> publicationTypeStringIds = appraisalEntityDataModel.getPublicationTypeIds();

        for (String publicationTypeId : publicationTypeStringIds) {

            publicationTypeIds.add(new PublicationTypeId(publicationTypeId));

        }

        List<GenreId> genreIds = new ArrayList<>();
        List <String> genreStringIds = appraisalEntityDataModel.getGenresIds();

        for (String genreId : genreStringIds) {

            genreIds.add(new GenreId(genreId));

        }

        AppraisalEntity reconstructedAppraisalEntity = _appraisalEntityFactory.createAppraisalEntity(appraisalEntityId,
                name,
                publicationTypeIds,
                genreIds);

        return reconstructedAppraisalEntity;
    }

}
