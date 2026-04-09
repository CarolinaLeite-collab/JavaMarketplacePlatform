package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.appraisalEntity.AppraisalEntity;
import TOPSECRET.domain.valueobject.AppraisalEntityId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import java.util.List;

public interface IAppraisalEntityRepo extends IRepository<AppraisalEntityId, AppraisalEntity> {

    AppraisalEntity addAppraisalEntity(Name name, List<PublicationTypeId> publicationTypes, List<GenreId> genreIds);

}
