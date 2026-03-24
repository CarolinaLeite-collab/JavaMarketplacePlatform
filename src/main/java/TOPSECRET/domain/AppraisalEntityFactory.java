package TOPSECRET.domain;

import TOPSECRET.ddd.ValueObject;

import java.util.List;

/**
 * Factory class responsible for creating instances of {@link AppraisalEntity}.
 * <p>
 * @trhows InstantiationException if appraisalEntity is invalid (as defined by {@link AppraisalEntity}'s constructor).
 */

public class AppraisalEntityFactory {
    public AppraisalEntity createAppraisalEntity(ValueObject.Name name, List<PublicationType> publicationTypes, List<Genre> genres) {
        return new AppraisalEntity(name, publicationTypes, genres);
    }
}
