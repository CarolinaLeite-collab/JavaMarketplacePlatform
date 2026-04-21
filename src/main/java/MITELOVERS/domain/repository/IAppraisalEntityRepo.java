package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import MITELOVERS.domain.valueobject.AppraisalEntityId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.PublicationTypeId;

import java.util.List;

public interface IAppraisalEntityRepo extends IRepository<AppraisalEntityId, AppraisalEntity> {

    AppraisalEntity addAppraisalEntity(Name name, List<PublicationTypeId> publicationTypes, List<GenreId> genreIds);

}
