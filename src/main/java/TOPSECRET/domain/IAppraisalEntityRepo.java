package TOPSECRET.domain;


import TOPSECRET.domain.valueobject.Name;

import java.util.List;

public interface IAppraisalEntityRepo {

    AppraisalEntity registerNewAppraisalEntity(Name name, List<PublicationType> publicationTypes, List<Genre> genres);
}
