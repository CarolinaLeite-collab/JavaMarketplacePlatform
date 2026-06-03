package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.assembler.PublicationTypeAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import MITELOVERS.persistence.springdata.IPublicationTypeSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of {@link IPublicationTypeRepo} for storing {@link PublicationType} instances.
 * <p>
 * Active only when the {@code jpa} Spring profile is enabled.
 * Delegates persistence to {@link IPublicationTypeSpringDataRepo} and
 * mapping to {@link PublicationTypeAssembler}.
 * </p>
 */

@Repository
@Profile("jpa")
public class JpaPublicationTypeRepo implements IPublicationTypeRepo {

    @Autowired
    private IPublicationTypeSpringDataRepo _publicationTypeSpringDataRepo;

    @Autowired
    private PublicationTypeAssembler _publicationTyperAssembler;


    @Override
    public PublicationType save(PublicationType publicationType) {

        PublicationTypeDataModel dataModel = _publicationTyperAssembler.toDataModel(publicationType);

        PublicationTypeDataModel savedDataModel = _publicationTypeSpringDataRepo.save(dataModel);

        return _publicationTyperAssembler.toDomain(savedDataModel);

    }

    @Override
    public Iterable<PublicationTypeId> findAllKeys() {

        Iterable<PublicationTypeDataModel> publicationTypeDms = _publicationTypeSpringDataRepo.findAll();

        List<PublicationTypeId> publicationTypeIds = new ArrayList<>();

        for  (PublicationTypeDataModel publicationTypeDataModel : publicationTypeDms) {

            publicationTypeIds.add(new PublicationTypeId(publicationTypeDataModel.getPublicationTypeId()));
        }

        return publicationTypeIds;

    }

    @Override
    public Iterable<PublicationType> findAll() {

        Iterable<PublicationTypeDataModel> publicationTypeDms = _publicationTypeSpringDataRepo.findAll();

        List<PublicationType> publicationTypes = new ArrayList<>();

        for ( PublicationTypeDataModel publicationTypeDataModel : _publicationTypeSpringDataRepo.findAll() ) {

            publicationTypes.add(_publicationTyperAssembler.toDomain(publicationTypeDataModel));
        }

        return publicationTypes;

    }

    @Override
    public Optional<PublicationType> ofIdentity(PublicationTypeId id) {

        Optional<PublicationTypeDataModel> dataModel =
                _publicationTypeSpringDataRepo.findById(id.toString());

        if (dataModel.isPresent()) {
            return Optional.of(_publicationTyperAssembler.toDomain(dataModel.get()));
        }

        return Optional.empty();

    }

    @Override
    public boolean containsOfIdentity(PublicationTypeId id) {

        return _publicationTypeSpringDataRepo.existsById(id.toString());

    }

}