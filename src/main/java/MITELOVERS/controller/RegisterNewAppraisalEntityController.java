package MITELOVERS.controller;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.appraisalentity.AppraisalEntityFactory;
import MITELOVERS.domain.repository.IAppraisalEntityRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * Controller orchestrating the registration of new {@link AppraisalEntity} instances as part
 * of US004 requirements.
 * <p>
 * Coordinates between {@link IGenreRepo}, {@link IPublicationTypeRepo}, and {@link IAppraisalEntityRepo}
 * to provide data for UI and create appraisal entities with their specialized genres
 * and publication types.
 *
 * @see AppraisalEntity
 */

@Controller
public class RegisterNewAppraisalEntityController {
    private IAppraisalEntityRepo _iAppraisalEntityRepo;
    private AppraisalEntityFactory _appraisalEntityFactory;
    private IPublicationTypeRepo _iPubTypeRepo;
    private IGenreRepo _iGenreRepo;

    public RegisterNewAppraisalEntityController(IAppraisalEntityRepo iAppraisalEntityRepo, IPublicationTypeRepo iPublicationTypeRepo, AppraisalEntityFactory appraisalEntityFactory, IGenreRepo iGenreRepo) {

        _iAppraisalEntityRepo = iAppraisalEntityRepo;
        _iPubTypeRepo = iPublicationTypeRepo;
        _iGenreRepo = iGenreRepo;
        _appraisalEntityFactory = appraisalEntityFactory;

    }

    public Iterable<PublicationTypeId> getPublicationTypesId(){

        return _iPubTypeRepo.findAllKeys();
    }

    public Iterable <GenreId> getGenresId(){

        return _iGenreRepo.findAllKeys();
    }

    public AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationTypeId> publicationTypeIds, List<GenreId> genreIds){

        AppraisalEntity appraisalEntity = _appraisalEntityFactory.createAppraisalEntity(name, publicationTypeIds, genreIds);

        if (_iAppraisalEntityRepo.containsOfIdentity(appraisalEntity.identity())) {

            throw new IllegalStateException("Appraisal entity already exists!");

        }

        return _iAppraisalEntityRepo.save (appraisalEntity);

    }

}