package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link AppraisalEntity} persistence.
 * <p>
 * Provides operations to check existence by {@link Name} and create new appraisal entities
 * with associated {@link PublicationType}s and {@link Genre}s. Ensures uniqueness by name
 * before creation, throwing {@link IllegalArgumentException} if a duplicate is detected.
 *
 * @see AppraisalEntity
 */

public class AppraisalEntityRepo {
    private final List<AppraisalEntity> _appraisalEntities;
    private AppraisalEntityFactory _factoryAppraisalEntity;

    public AppraisalEntityRepo(AppraisalEntityFactory factoryAppraisalEntity) {
        _appraisalEntities = new ArrayList<>();
        _factoryAppraisalEntity = factoryAppraisalEntity;
    }

    private boolean appraisalEntityExists(Name name) {
        for (AppraisalEntity appraisalEntity : _appraisalEntities) {
            if (appraisalEntity.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationType> publicationTypes, List<Genre> genres) throws IllegalArgumentException {

        if (appraisalEntityExists(name)) {
            throw new IllegalArgumentException("This appraisal entity already exists");
        }

        List<PublicationType> typesCopy = new ArrayList<>(publicationTypes);
        List<Genre> genresCopy = new ArrayList<>(genres);

        AppraisalEntity appraisalEntity = _factoryAppraisalEntity.createAppraisalEntity(name, typesCopy, genresCopy);

        _appraisalEntities.add(appraisalEntity);

        return appraisalEntity;
    }
}
