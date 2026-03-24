package TOPSECRET.controller;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.*;

import java.util.List;


/**
 * Controller orchestrating the registration of new {@link AppraisalEntity} instances as part
 * of US004 requirements.
 * <p>
 * Coordinates between {@link GenreRepo}, {@link IPublicationTypeRepo}, and {@link AppraisalEntityRepo}
 * to provide data for UI and create appraisal entities with their specialized genres
 * and publication types.
 *
 * @see AppraisalEntity
 */

public class RegisterNewAppraisalEntityController {
    private AppraisalEntityRepo _appraisalEntityRepo;
    private IPublicationTypeRepo _iPubTypeRepo;
    private GenreRepo _genreRepo;

    public RegisterNewAppraisalEntityController(AppraisalEntityRepo appraisalEntityRepo, IPublicationTypeRepo iPublicationTypeRepo, GenreRepo genreRepo){
        _appraisalEntityRepo = appraisalEntityRepo;
        _iPubTypeRepo = iPublicationTypeRepo;
        _genreRepo = genreRepo;
    }

    public List getPublicationTypes(){

        return List.copyOf(_iPubTypeRepo.getAll());
    }

    public List getGenres(){

        return List.copyOf(_genreRepo.getListOfOfficialGenres());
    }

    public AppraisalEntity registerNewAppraisalEntity(ValueObject.Name name, List<PublicationType> publicationType, List<Genre> genre){
        AppraisalEntity appraisalEntity = _appraisalEntityRepo.registerNewAppraisalEntity(name, publicationType, genre);
        return appraisalEntity;
    }

}
