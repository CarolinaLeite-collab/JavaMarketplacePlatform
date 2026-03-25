package TOPSECRET.controller;

import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.*;

import java.util.List;


/**
 * Controller orchestrating the registration of new {@link AppraisalEntity} instances as part
 * of US004 requirements.
 * <p>
 * Coordinates between {@link GenreRepo}, {@link IPublicationTypeRepo}, and {@link IAppraisalEntityRepo}
 * to provide data for UI and create appraisal entities with their specialized genres
 * and publication types.
 *
 * @see AppraisalEntity
 */

public class RegisterNewAppraisalEntityController {
    private IAppraisalEntityRepo _appraisalEntityRepo;
    private IPublicationTypeRepo _iPubTypeRepo;
    private IGenreRepo _iGenreRepo;

<<<<<<< Updated upstream
    public RegisterNewAppraisalEntityController(AppraisalEntityRepo appraisalEntityRepo, IPublicationTypeRepo iPublicationTypeRepo, IGenreRepo genreRepo){
=======
    public RegisterNewAppraisalEntityController(IAppraisalEntityRepo appraisalEntityRepo, IPublicationTypeRepo iPublicationTypeRepo, GenreRepo genreRepo){
>>>>>>> Stashed changes
        _appraisalEntityRepo = appraisalEntityRepo;
        _iPubTypeRepo = iPublicationTypeRepo;
        _iGenreRepo = genreRepo;
    }

    public List getPublicationTypes(){

        return List.copyOf(_iPubTypeRepo.getAll());
    }

    public List getGenres(){

        return List.copyOf(_iGenreRepo.getListOfOfficialGenres());
    }

    public AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationType> publicationType, List<Genre> genre){
        AppraisalEntity appraisalEntity = _appraisalEntityRepo.registerNewAppraisalEntity(name, publicationType, genre);
        return appraisalEntity;
    }

}
