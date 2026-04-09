package TOPSECRET.persistence.mem;

import TOPSECRET.domain.repository.IAppraisalEntityRepo;
import TOPSECRET.domain.appraisalEntity.AppraisalEntity;
import TOPSECRET.domain.appraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.*;
import java.util.*;

/**
 * Repository responsible for managing {@link AppraisalEntity} persistence.
 * <p>
 * Provides operations to check existence by {@link Name} and create new appraisal entities
 * with associated {@link PublicationType}s and {@link Genre}s. Ensures uniqueness by name
 * before creation, throwing {@link IllegalArgumentException} if a duplicate is detected.
 *
 * @see AppraisalEntity
 */

public class MemoAppraisalEntityRepo implements IAppraisalEntityRepo {

    private final Map<AppraisalEntityId, AppraisalEntity> DATA = new HashMap<AppraisalEntityId, AppraisalEntity>();
    private AppraisalEntityFactory _factoryAppraisalEntity;


    public MemoAppraisalEntityRepo(AppraisalEntityFactory factoryAppraisalEntity) {

        _factoryAppraisalEntity = factoryAppraisalEntity;

    }

    @Override
    public AppraisalEntity save(AppraisalEntity appraisalEntity) {

        DATA.put(appraisalEntity.identity(), appraisalEntity);

        return appraisalEntity;

    }

    @Override
    public Iterable<AppraisalEntity> findAll() {

        return DATA.values();

    }

    @Override
    public Optional<AppraisalEntity> ofIdentity(AppraisalEntityId id) {

        if(!containsOfIdentity(id)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(id));

        }

    }

    @Override
    public boolean containsOfIdentity(AppraisalEntityId id) {

        return DATA.containsKey(id);

    }

    @Override
    public AppraisalEntity addAppraisalEntity(Name name, List<PublicationTypeId> publicationTypeIds, List<GenreId> genresIds) {

        AppraisalEntity appraisalEntity = _factoryAppraisalEntity.createAppraisalEntity(name, publicationTypeIds, genresIds);

        return save (appraisalEntity);

    }

}
