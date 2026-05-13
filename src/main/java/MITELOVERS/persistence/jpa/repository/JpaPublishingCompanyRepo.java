package MITELOVERS.persistence.jpa.repository;


import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.persistence.jpa.assembler.PublishingCompanyAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublishingCompanyDataModel;
import MITELOVERS.persistence.springdata.IPublishingCompanySpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of {@link IPublishingCompanyRepo}.
 *
 * Responsible for persisting and retrieving
 * {@link PublishingCompany} aggregates using Spring Data JPA.
 *
 * Uses {@link PublishingCompanyAssembler} to convert between
 * domain objects and {@link PublishingCompanyDataModel} instances.
 */

@Repository
@Profile("jpa")
public class JpaPublishingCompanyRepo implements IPublishingCompanyRepo {

    @Autowired
    private IPublishingCompanySpringDataRepo _publishingCompanySpringDataRepo;

    @Autowired
    private PublishingCompanyAssembler _publishingCompanyAssembler;


    @Override
    public PublishingCompany save(PublishingCompany publishingCompany) {

        PublishingCompanyDataModel dmToSave = _publishingCompanyAssembler.toDataModel(publishingCompany);

        PublishingCompanyDataModel savedDm = _publishingCompanySpringDataRepo.save(dmToSave);

        return _publishingCompanyAssembler.toDomain(savedDm);
    }

    @Override
    public Iterable<PublishingCompanyId> findAllKeys() {

        Iterable<PublishingCompanyDataModel> publishingCompanyDataModels = _publishingCompanySpringDataRepo.findAll();

        List<PublishingCompanyId> publishingCompanyIds = new ArrayList<>();

        for (PublishingCompanyDataModel  publishingCompanyDataModel : publishingCompanyDataModels) {

            publishingCompanyIds.add(new PublishingCompanyId(publishingCompanyDataModel.getPublishingCompanyId()));
        }

        return publishingCompanyIds;
    }

    @Override
    public Iterable<PublishingCompany> findAll() {

        Iterable<PublishingCompanyDataModel> publishingCompanyDataModels = _publishingCompanySpringDataRepo.findAll();

        List<PublishingCompany> publishingCompanies = new ArrayList<>();

        for (PublishingCompanyDataModel  publishingCompanyDataModel : publishingCompanyDataModels) {

            publishingCompanies.add(_publishingCompanyAssembler.toDomain(publishingCompanyDataModel));

        }

        return publishingCompanies;
    }

    @Override
    public Optional<PublishingCompany> ofIdentity(PublishingCompanyId publishingCompanyId) {

        return _publishingCompanySpringDataRepo.findById(publishingCompanyId.toString())
                .map(_publishingCompanyAssembler::toDomain);
    }

    @Override
    public boolean containsOfIdentity(PublishingCompanyId publishingCompanyId) {

        return _publishingCompanySpringDataRepo.existsById(publishingCompanyId.toString());

    }
}
