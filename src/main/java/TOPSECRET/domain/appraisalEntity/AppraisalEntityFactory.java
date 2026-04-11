package TOPSECRET.domain.appraisalEntity;

import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import java.util.List;

/**
 * Factory class responsible for creating instances of {@link AppraisalEntity}.
 * <p>
 * @trhows InstantiationException if appraisalEntity is invalid (as defined by {@link AppraisalEntity}'s constructor).
 */

public class AppraisalEntityFactory {


    public AppraisalEntity createAppraisalEntity(Name name, List<PublicationTypeId> publicationTypeIds, List<GenreId> genresId) {

        return new AppraisalEntity(name, publicationTypeIds, genresId);

    }

}
