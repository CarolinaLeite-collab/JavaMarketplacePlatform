package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import TOPSECRET.domain.valueobject.Name;

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

    public RegisterNewAppraisalEntityController(IAppraisalEntityRepo iAppraisalEntityRepo, IPublicationTypeRepo iPublicationTypeRepo, IGenreRepo iGenreRepo) {

        _iAppraisalEntityRepo = iAppraisalEntityRepo;
        _iPubTypeRepo = iPublicationTypeRepo;
        _iGenreRepo = iGenreRepo;
    }

    public List getPublicationTypes(){

        return List.copyOf(_iPubTypeRepo.getAll());
    }

    public Iterable <Genre> getGenres(){

        return _iGenreRepo.findAll();
    }

    public AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationType> publicationType, List<Genre> genre, User user){

        if (!user.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register appraisal entities");
        }
        return _iAppraisalEntityRepo.registerNewAppraisalEntity(name, publicationType, genre);
    }

}

