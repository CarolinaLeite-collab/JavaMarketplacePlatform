package TOPSECRET.controller;

import TOPSECRET.domain.appraisalEntity.AppraisalEntity;
import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.repository.IAppraisalEntityRepo;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import TOPSECRET.domain.repository.IPublicationTypeRepo;
import TOPSECRET.domain.valueobject.*;

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
    private IPublicationTypeRepo _iPubTypeRepo;
    private IGenreRepo _iGenreRepo;

    public RegisterNewAppraisalEntityController(IAppraisalEntityRepo iAppraisalEntityRepo, IPublicationTypeRepo iPublicationTypeRepo, IGenreRepo iGenreRepo, UserId adminId) {

        _iAppraisalEntityRepo = iAppraisalEntityRepo;
        _iPubTypeRepo = iPublicationTypeRepo;
        _iGenreRepo = iGenreRepo;
    }

    public Iterable<PublicationType> getPublicationTypes(){

        return _iPubTypeRepo.findAll();
    }

    public Iterable <Genre> getGenres(){

        return _iGenreRepo.findAll();
    }

    public AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationTypeId> publicationTypeIds, List<GenreId> genreIds, User admin){

        if (!admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register appraisal entities");
        }
        return _iAppraisalEntityRepo.addAppraisalEntity(name, publicationTypeIds, genreIds);
    }

}

