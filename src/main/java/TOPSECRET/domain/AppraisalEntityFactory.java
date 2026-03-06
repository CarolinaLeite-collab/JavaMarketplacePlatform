package TOPSECRET.domain;

import java.util.List;

/**
 * Factory class responsible for creating instances of {@link AppraisalEntity}.
 * <p>
 * @trhows InstantiationException if appraisalEntity is invalid (as defined by {@link AppraisalEntity}'s constructor).
 */

public class AppraisalEntityFactory {
    public AppraisalEntity createAppraisalEntity(Name name, List<PublicationType> publicationTypes, List<Genre> genres) {
        return new AppraisalEntity(name, publicationTypes, genres);
    }
}
