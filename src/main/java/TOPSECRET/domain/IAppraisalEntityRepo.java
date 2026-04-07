package TOPSECRET.domain;


import TOPSECRET.domain.appraisalEntity.AppraisalEntity;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Name;

import java.util.List;

public interface IAppraisalEntityRepo {

    AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationType> publicationTypes, List<Genre> genres);
}
