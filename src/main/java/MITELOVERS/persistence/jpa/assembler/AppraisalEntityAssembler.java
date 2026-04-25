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

    public AppraisalEntityDataModel domain2DM(AppraisalEntity appraisalEntity) {

        List<PublicationTypeId> publicationTypeIds = appraisalEntity.getPublicationTypeIds();
        List<GenreId> GenreIds = appraisalEntity.getGenreIds();
        List<String> publicationTypeStringIds = new ArrayList<>();
        List<String> genreStringIds = new ArrayList<>();

        for (PublicationTypeId publicationTypeId : publicationTypeIds) {
            publicationTypeStringIds.add(publicationTypeId.toString());
        }

        for (GenreId GenreId : GenreIds) {
            genreStringIds.add(GenreId.toString());
        }

        return new AppraisalEntityDataModel(appraisalEntity.identity().toString(),
                appraisalEntity.getName().getName(),
                publicationTypeStringIds,
                genreStringIds
        );

    }

    public AppraisalEntity DM2domain(AppraisalEntityDataModel appraisalEntityDataModel) {

        List<PublicationTypeId> publicationTypeIds = new ArrayList<>();
        List<GenreId> genreIds = new ArrayList<>();

        List <String> publicationTypeStringIds = appraisalEntityDataModel.getPublicationTypeIds();
        List <String> genreStringIds = appraisalEntityDataModel.getGenresIds();

        for (String publicationTypeId : publicationTypeStringIds) {

            publicationTypeIds.add(new PublicationTypeId(publicationTypeId));

        }

        for (String genreId : genreStringIds) {

            genreIds.add(new GenreId(genreId));

        }

        return _appraisalEntityFactory.createAppraisalEntity(new AppraisalEntityId(appraisalEntityDataModel.getId()),
                new Name(appraisalEntityDataModel.getName()),
                publicationTypeIds,
                genreIds);
    }

}
