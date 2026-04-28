package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.assembler.PublicationTypeAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import MITELOVERS.persistence.springdata.IPublicationTypeSpringDataRepo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JpaPublicationTypeRepo implements IPublicationTypeRepo {

    private final IPublicationTypeSpringDataRepo _springDataRepo;
    private final PublicationTypeAssembler _assembler;

    public JpaPublicationTypeRepo(IPublicationTypeSpringDataRepo springDataRepo,
                                  PublicationTypeAssembler assembler) {

        _springDataRepo = springDataRepo;
        _assembler = assembler;

    }

    @Override
    public PublicationType save(PublicationType publicationType) {

        PublicationTypeDataModel dataModel = _assembler.toDataModel(publicationType);

        _springDataRepo.save(dataModel);

        PublicationTypeDataModel savedDataModel = _springDataRepo.save(dataModel);

        return _assembler.toDomain(savedDataModel);

    }

    @Override
    public Iterable<PublicationTypeId> findAllKeys() {

        List<PublicationTypeId> ids = new ArrayList<>();

        _springDataRepo.findAll().forEach(
                dm -> ids.add(new PublicationTypeId(dm.getPublicationTypeId())));

        return ids;

    }

    @Override
    public Iterable<PublicationType> findAll() {

        List<PublicationType> result = new ArrayList<>();

        _springDataRepo.findAll().forEach(
                dm -> result.add(_assembler.toDomain(dm)));

        return result;

    }

    @Override
    public Optional<PublicationType> ofIdentity(PublicationTypeId id) {

        PublicationTypeDataModel dataModel = _springDataRepo.findById(id.toString())
                .orElseThrow(() -> new IllegalArgumentException("PublicationType not found"));

        return Optional.of(_assembler.toDomain(dataModel));

    }

    @Override
    public boolean containsOfIdentity(PublicationTypeId id) {

        return _springDataRepo.existsById(id.toString());

    }

}