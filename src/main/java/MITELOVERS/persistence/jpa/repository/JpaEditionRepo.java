package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.persistence.jpa.assembler.EditionAssembler;
import MITELOVERS.persistence.jpa.datamodel.EditionDataModel;
import MITELOVERS.persistence.springdata.IEditionSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaEditionRepo implements IEditionRepo {

    @Autowired
    private IEditionSpringDataRepo _springDataRepo;

    @Autowired
    private EditionAssembler _assembler;

    @Override
    public Edition save(Edition edition) {
        EditionDataModel dataModel = _assembler.toDataModel(edition);
        EditionDataModel savedDataModel = _springDataRepo.save(dataModel);
        Edition savedEdition = _assembler.toDomain(savedDataModel);

        return savedEdition;
    }

    @Override
    public Iterable<EditionId> findAllKeys() {
        Iterable<EditionDataModel> dataModels = _springDataRepo.findAll();

        List<EditionId> editionIds = new ArrayList<>();

        for (EditionDataModel dataModel : dataModels) {
            editionIds.add(new EditionId(dataModel.getId()));
        }

        return editionIds;
    }

    @Override
    public Iterable<Edition> findAll() {
        Iterable<EditionDataModel> dataModels = _springDataRepo.findAll();

        List<Edition> editions = new ArrayList<>();

        for (EditionDataModel dataModel : dataModels) {
            editions.add(_assembler.toDomain(dataModel));
        }

        return editions;
    }

    @Override
    public Optional<Edition> ofIdentity(EditionId editionId) {
        EditionDataModel savedDataModel = _springDataRepo.findById(editionId.toString())
                .orElseThrow(() -> new IllegalArgumentException("Edition not found"));

        Edition savedEdition = _assembler.toDomain(savedDataModel);

        return Optional.of(savedEdition);
    }

    @Override
    public boolean containsOfIdentity(EditionId editionId) {

        return _springDataRepo.existsById(editionId.toString());
    }
}
