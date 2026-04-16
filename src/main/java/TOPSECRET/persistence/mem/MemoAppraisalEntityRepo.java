package TOPSECRET.persistence.mem;

import TOPSECRET.domain.repository.IAppraisalEntityRepo;
import TOPSECRET.domain.appraisalentity.AppraisalEntity;
import TOPSECRET.domain.appraisalentity.AppraisalEntityFactory;
import TOPSECRET.domain.valueobject.*;
import java.util.*;

/**
 * In-memory implementation of {@link IAppraisalEntityRepo}.
 * <p>
 * This repository is responsible for persisting {@link AppraisalEntity} instances
 * during runtime using a {@link HashMap} as storage, where the key is the
 * {@link AppraisalEntityId}.
 * </p>
 *
 * <p>
 * It provides basic CRUD-like operations such as saving, retrieving by identity,
 * checking existence, and listing all stored entities.
 * </p>
 */

public class MemoAppraisalEntityRepo implements IAppraisalEntityRepo {

    private final Map<AppraisalEntityId, AppraisalEntity> DATA = new HashMap<AppraisalEntityId, AppraisalEntity>();
    private AppraisalEntityFactory _appraisalEntityFactory;


    public MemoAppraisalEntityRepo(AppraisalEntityFactory appraisalEntityFactory) {

        _appraisalEntityFactory = appraisalEntityFactory;

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

    public List<AppraisalEntityId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

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

        AppraisalEntity appraisalEntity = _appraisalEntityFactory.createAppraisalEntity(name, publicationTypeIds, genresIds);

        if (containsOfIdentity(appraisalEntity.identity())) {

            throw new IllegalStateException("Appraisal entity already exists!");

        }

        return save (appraisalEntity);

    }

}
