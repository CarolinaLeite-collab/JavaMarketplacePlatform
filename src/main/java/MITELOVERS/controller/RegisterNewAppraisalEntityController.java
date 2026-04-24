package MITELOVERS.controller;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.appraisalentity.AppraisalEntityFactory;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IAppraisalEntityRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.*;

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

public class RegisterNewAppraisalEntityController {
    private IAppraisalEntityRepo _iAppraisalEntityRepo;
    private AppraisalEntityFactory _appraisalEntityFactory;
    private IPublicationTypeRepo _iPubTypeRepo;
    private IGenreRepo _iGenreRepo;

    public RegisterNewAppraisalEntityController(IAppraisalEntityRepo iAppraisalEntityRepo, IPublicationTypeRepo iPublicationTypeRepo, AppraisalEntityFactory appraisalEntityFactory, IGenreRepo iGenreRepo, UserId adminId) {

        _iAppraisalEntityRepo = iAppraisalEntityRepo;
        _iPubTypeRepo = iPublicationTypeRepo;
        _iGenreRepo = iGenreRepo;
        _appraisalEntityFactory = appraisalEntityFactory;

    }

    public Iterable<PublicationType> getPublicationTypes(){

        return _iPubTypeRepo.findAll();
    }

    public Iterable <Genre> getGenres(){

        return _iGenreRepo.findAll();
    }

    public AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationTypeId> publicationTypeIds, List<GenreId> genreIds){

        AppraisalEntity appraisalEntity = _appraisalEntityFactory.createAppraisalEntity(name, publicationTypeIds, genreIds);

        if (_iAppraisalEntityRepo.containsOfIdentity(appraisalEntity.identity())) {

            throw new IllegalStateException("Appraisal entity already exists!");

        }

        return _iAppraisalEntityRepo.save (appraisalEntity);

    }

}

