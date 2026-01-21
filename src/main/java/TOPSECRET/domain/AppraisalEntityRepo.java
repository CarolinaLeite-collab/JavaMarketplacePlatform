package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link AppraisalEntity} persistence.
 * <p>
 * Provides operations to check existence by {@link Name} and create new appraisal entities
 * with associated {@link PublicationType}s and {@link Genre}s. Ensures uniqueness by name
 * before creation, throwing {@link IllegalStateException} if a duplicate is detected.
 *
 * @see AppraisalEntity
 */

public class AppraisalEntityRepo {
    private final List<AppraisalEntity> _appraisalEntities;

    public AppraisalEntityRepo() {
        _appraisalEntities = new ArrayList<>();
    }

    private boolean appraisalEntityExists(Name name) {
        for (AppraisalEntity appraisalEntity : _appraisalEntities) {
            if (appraisalEntity.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationType> publicationTypes, List<Genre> genres) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (publicationTypes == null) {
            throw new IllegalArgumentException("Publication type cannot be null");
        }
        if (genres == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        if (appraisalEntityExists(name)) {
            throw new IllegalStateException("This appraisal entity already exists");
        }

        List<PublicationType> typesCopy = new ArrayList<>(publicationTypes);
        List<Genre> genresCopy = new ArrayList<>(genres);

        AppraisalEntity appraisalEntity = new AppraisalEntity(name, typesCopy, genresCopy);

        _appraisalEntities.add(appraisalEntity);

        return appraisalEntity;
    }
}
