package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.repository.IAppraisalEntityRepo;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
import MITELOVERS.persistence.jpa.assembler.AppraisalEntityAssembler;
import MITELOVERS.persistence.jpa.datamodel.AppraisalEntityDataModel;
import MITELOVERS.persistence.springdata.IAppraisalEntitySpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaAppraisalEntityRepo implements IAppraisalEntityRepo {

    @Autowired
    private  IAppraisalEntitySpringDataRepo _appraisalEntitySpringDataRepo;

    @Autowired
    private  AppraisalEntityAssembler _appraisalEntityAssembler;

    @Override
    public AppraisalEntity save(AppraisalEntity appraisalEntity) {

        AppraisalEntityDataModel dmToSave = _appraisalEntityAssembler.toDataModel(appraisalEntity);

        AppraisalEntityDataModel savedDm =  _appraisalEntitySpringDataRepo.save(dmToSave);

        return _appraisalEntityAssembler.toDomain(savedDm);

    }

    @Override
    public Iterable<AppraisalEntityId> findAllKeys() {

        Iterable<AppraisalEntityDataModel> appraisalEntityDms = _appraisalEntitySpringDataRepo.findAll();

        List<AppraisalEntityId> appraisalEntityIds = new ArrayList<>();

        for (AppraisalEntityDataModel appraisalEntityDM: appraisalEntityDms) {

            appraisalEntityIds.add(new AppraisalEntityId(appraisalEntityDM.getId()));

        }

        return appraisalEntityIds;

    }

    @Override
    public Iterable<AppraisalEntity> findAll() {

        Iterable<AppraisalEntityDataModel> appraisalEntityDms = _appraisalEntitySpringDataRepo.findAll();

        List<AppraisalEntity> appraisalEntities = new ArrayList<>();

        for (AppraisalEntityDataModel appraisalEntityDM: appraisalEntityDms) {

            appraisalEntities.add(_appraisalEntityAssembler.toDomain(appraisalEntityDM));

        }

        return appraisalEntities;

    }

    @Override
    public Optional<AppraisalEntity> ofIdentity(AppraisalEntityId id) {

        AppraisalEntityDataModel savedAppraisalEntityDataModel =
                _appraisalEntitySpringDataRepo.findById(id.toString())
                        .orElseThrow(() -> new IllegalArgumentException("AppraisalEntity not found!"));

        return Optional.of(_appraisalEntityAssembler.toDomain(savedAppraisalEntityDataModel));
    }

    @Override
    public boolean containsOfIdentity(AppraisalEntityId id) {

        return _appraisalEntitySpringDataRepo.existsById(id.toString());

    }
}
