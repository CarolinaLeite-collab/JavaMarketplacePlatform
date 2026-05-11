package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.persistence.jpa.assembler.PublicationAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublicationDataModel;
import MITELOVERS.persistence.springdata.IPublicationSpringdataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaPublicationRepo implements IPublicationRepo {

    @Autowired
    private final PublicationAssembler _publicationAssembler;
    @Autowired
    private final IPublicationSpringdataRepo _publicationSpringdataRepo;

    public JpaPublicationRepo(PublicationAssembler publicationAssembler, IPublicationSpringdataRepo publicationSpringdataRepo) {
        _publicationAssembler = publicationAssembler;
        _publicationSpringdataRepo = publicationSpringdataRepo;
    }

    @Override
    public Publication save(Publication publication){
       PublicationDataModel publicationDataModel = _publicationAssembler.toDataModel(publication);
       PublicationDataModel savedDM = _publicationSpringdataRepo.save(publicationDataModel);

       return _publicationAssembler.toDomain(savedDM);
    }

    @Override
    public Optional<Publication> ofIdentity(PublicationId publicationId) {
        PublicationDataModel savedPublicationDataModel = _publicationSpringdataRepo.findById(publicationId.toString()).orElseThrow(() -> new IllegalArgumentException("Publication not found"));

        return Optional.of(_publicationAssembler.toDomain(savedPublicationDataModel));
    }

    @Override
    public boolean containsOfIdentity(PublicationId publicationId) {
        boolean result = _publicationSpringdataRepo.existsById(publicationId.toString());

        return result;
    }

    @Override
    public Iterable<Publication> findAll() {
        Iterable<PublicationDataModel> publicationDms = _publicationSpringdataRepo.findAll();

        List<Publication> publications = new  ArrayList<>();

        for (PublicationDataModel publicationDataModel : publicationDms) {
            publications.add(_publicationAssembler.toDomain(publicationDataModel));
        }

        return publications;
    }

    @Override
    public Iterable<PublicationId> findAllKeys() {

        Iterable<PublicationDataModel> publicationDms = _publicationSpringdataRepo.findAll();

        List<PublicationId> publicationIds = new ArrayList<>();

        for (PublicationDataModel pubDm : publicationDms) {
            Title title = new Title(pubDm.getTitle());
            AuthorId authorId = new AuthorId(pubDm.getAuthorId());
            Year releaseYear = Year.parse(pubDm.getReleaseYear());

            publicationIds.add(new PublicationId(title, authorId, releaseYear));
        }
        return publicationIds;
    }

}
