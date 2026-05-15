package MITELOVERS.domain.appraisalentity;

import MITELOVERS.domain.valueobject.AppraisalEntityId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory class responsible for creating instances of {@link AppraisalEntity}.
 * <p>
 * @throws InstantiationException if appraisalEntity is invalid (as defined by {@link AppraisalEntity}'s constructor).
 */

@Component
public class AppraisalEntityFactory {

    public AppraisalEntity createAppraisalEntity(Name name, List<PublicationTypeId> publicationTypeIds, List<GenreId> genresId) {

        return new AppraisalEntity(name, publicationTypeIds, genresId);

    }

    public AppraisalEntity createAppraisalEntity(AppraisalEntityId id, Name name, List<PublicationTypeId> publicationTypeIds, List<GenreId> genresId) {

        return new AppraisalEntity(id, name, publicationTypeIds, genresId);

    }

}
