package MITELOVERS.persistence.mem;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.repository.IAppraisalEntityRepo;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
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

public class MemAppraisalEntityRepo implements IAppraisalEntityRepo {

    private final Map<AppraisalEntityId, AppraisalEntity> DATA = new HashMap<AppraisalEntityId, AppraisalEntity>();


    public MemAppraisalEntityRepo() {

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

}
